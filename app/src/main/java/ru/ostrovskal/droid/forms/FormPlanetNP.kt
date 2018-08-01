package ru.ostrovskal.droid.forms

import android.os.Message
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs
import com.github.ostrovskal.ssh.adapters.SelectAdapter
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Edit
import com.github.ostrovskal.ssh.widgets.EditInvalidException
import com.github.ostrovskal.ssh.widgets.lists.Select
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.Planet

class FormPlanetNP: Form() {
	private var isNew               = false
	private var nPack                = ""
	private var oldPack             = ""
	private var oldName             = ""
	private var oldNum              = -1L
	
	override fun onItemClick(select: Select, view: View, position: Int, id: Long) {
		val sel = select.selectionString
		if(sel == "...") {
			wnd.instanceForm(FORM_DLG_NEW_SYSTEM)
			//select.selectionString = nPack
		}
		nPack = sel
	}
	
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		isNew = index == FORM_PLANET_NEW
		nPack = Planet.MAP.pack
		if(nPack.isEmpty()) nPack = KEY_PACK.optText
		return UI {
			include(PlanetNP(index == FORM_PLANET_NEW, nPack)) {}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		if(!isNew) {
			with(Planet.MAP) {
				oldNum = num
				oldPack = nPack
				oldName = name
				content.byId<Edit>(R.id.etName).string = oldName
				content.byId<Edit>(R.id.etNumber).int = oldNum.toInt()
				content.byId<Edit>(R.id.etTime).int = time
				content.byId<Edit>(R.id.etFuel).int = fuel
				content.byId<Edit>(R.id.etWidth).int = width
				content.byId<Edit>(R.id.etHeight).int = height
			}
		}
		content.byId<Select>(R.id.slPack).itemClickListener = this@FormPlanetNP
	}
	
	override fun handleMessage(msg: Message) {
		msg.apply {
			if(arg1 != ACTION_PACK) return@apply
			if(arg2 == BTN_OK) {
				val name = obj.toString()
				content.byId<Select>(R.id.slPack).apply {
					adapter = SelectAdapter(wnd, SelectPopup(), SelectItem(), Pack.listOf(Pack.name, Pack.name) + "...")
					selectionString = name
				}
				nPack = name
			}
			else content.byId<Select>(R.id.slPack).selectionString = nPack
		}
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == BTN_OK) {
			try {
				// номер
				var nNum = content.byId<Edit>(R.id.etNumber).valid.toLong()
				// имя
				val etName = content.byId<Edit>(R.id.etName)
				val nName = etName.valid
				// топливо
				val nFuel = content.byId<Edit>(R.id.etFuel).valid.toInt()
				// время
				val nTime = content.byId<Edit>(R.id.etTime).valid.toInt()
				// ширина
				val nWidth = if(!isNew) Planet.MAP.width else content.byId<Edit>(R.id.etWidth).valid.toInt()
				// высота
				val nHeight = if(!isNew) Planet.MAP.height else content.byId<Edit>(R.id.etHeight).valid.toInt()
				// изменилось имя или система?
				val isPack = nPack != oldPack && oldPack != ""
				val isName = nName != oldName && oldName != ""
				// изменился номер?
				val isNum = nNum != oldNum && oldNum != -1L
				// Проверить на существование уже такой планеты с именем, если оно изменилось
				if(isName) {
					if(Planet.exist { Planet.system.eq(nPack) and Planet.title.eq(nName) } )
						throw EditInvalidException(getString(R.string.planet_name_already_exist, nName), etName)
				}
				// если изменилась позиция планеты, удаляем планету по старому номеру и сжимаем позиции, после ее номера
				if(isNum || isPack) Planet.MAP.delete(false)
				// если планета новая или изменился пакет или изменилась позиция - вставляем позицию планеты
				if(isNum || isPack || isNew) {
					if(Planet.exist { Planet.system.eq(nPack) and Planet.position.eq(nNum) }) {
						Planet.select(Planet.position) {
							where { Planet.system.eq(nPack) and Planet.position.greaterEq(nNum) }
							orderBy(Planet.position, false)
						}.execute()?.release {
							forEach {
								val n = integer(0)
								Planet.update {
									it[Planet.position] = n + 1
									where { Planet.system.eq(nPack) and Planet.position.eq(n) }
								}
							}
						}
					}
					else {
						// Если такой позиции нет - вставляем в конец
						nNum = Planet.count { Planet.system eq nPack }
					}
				}
				// Изменяем характеристики планеты, на новые:
				with(Planet.MAP) {
					num = nNum
					name = nName
					pack = nPack
					time = nTime
					fuel = nFuel
					auth = KEY_AUTHOR.optText
					buffer[0] = nWidth.toByte()
					buffer[1] = nHeight.toByte()
				}
			}
			catch(e: EditInvalidException) {
				val et = e.et ?: return
				et.startAnimation(shake)
				if(e.msg.isNotEmpty()) wnd.showToast(e.msg, parent = e.et)
				et.requestFocus()
				return
			}
			val sel = content.byId<Select>(R.id.slType).selection
			KEY_TYPE_PLANET.optInt = sel
			sendResult(MSG_FORM, if(!isNew) ACTION_SAVE else ACTION_NEW, if(!isNew) 0 else sel)
		}
		super.footer(btnId, 0)
	}
	
	class PlanetNP(val isNew: Boolean, val sys: String) : UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			val port = config.isVert
			cellLayout(30, if(port) 17 else 15) {
				formHeader(if(isNew) R.string.header_planet_new else R.string.header_planet_prop)
				backgroundSet(StylesAndAttrs.style_form)
				val dy = if(port) 2 else 0
				select {
					id = R.id.slType
					isEnabled = isNew
					adapter = SelectAdapter(ctx, SelectPopup(), SelectItem(), ctx.arrayStr(R.array.planetTypes).toList())
					selection = KEY_TYPE_PLANET.optInt
				}.lps(0, 0, if(port) 30 else 15, 2)
				select {
					id = R.id.slPack
					adapter = SelectAdapter(ctx, SelectPopup(), SelectItem(), Pack.listOf(Pack.name, Pack.name) + "...")
					selectionString = sys
				}.lps(if(port) 0 else 15, dy)
				editLayout {
					edit(R.id.etNumber, R.string.hint_number) {
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 0..99
					}
				}.lps(0, 2 + dy, 15, 3)
				editLayout {
					edit(R.id.etName, R.string.hint_name)
				}.lps(15, 2 + dy, 15, 3)
				editLayout {
					edit(R.id.etFuel, R.string.hint_fuel) {
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 300..2000
					}
				}.lps(0, 5 + dy, 15, 3)
				editLayout {
					edit(R.id.etTime, R.string.hint_time) {
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 300..5000
					}
				}.lps(15, 5 + dy, 15, 3)
				editLayout {
					edit(R.id.etWidth, R.string.hint_width) {
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 14..40
						isEnabled = isNew
					}
				}.lps(0, 8 + dy, 15, 3)
				editLayout {
					edit(R.id.etHeight, R.string.hint_height) {
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 14..40
						isEnabled = isNew
					}
				}.lps(15, 8 + dy, 15, 3)
				formFooter(BTN_OK, R.integer.I_YES, BTN_NO, R.integer.I_NO)
			}
		}
	}
}

