package ru.ostrovskal.droid.forms

import android.os.Message
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.BTN_OK
import com.github.ostrovskal.ssh.Constants.MSG_FORM
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

class FormPlanetNP: FormDialog(), Select.OnSelectItemClickListener {
	private var isNew               = false
	private var pack                = ""
	
	override fun onItemClick(select: Select, view: View, position: Int, id: Long) {
		val nPack = select.selectionString
		if(nPack == "...") {
			wnd.instanceForm(FORM_DLG_NEW_SYSTEM)
		} else pack = nPack
	}
	
	class PlanetNP(val isNew: Boolean) : UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			val port = config.isVert
			cellLayout(30, if(port) 17 else 15) {
				formHeader(if(isNew) R.string.header_planet_new else R.string.header_planet_prop)
				backgroundSet(StylesAndAttrs.style_form)
				val dy = if(port) 2 else 0
				select {
					id = R.id.slType
					isEnabled = isNew
					adapter = SelectAdapter(this, ctx, SelectPopup(), SelectItem(), ctx.arrayStr(R.array.planetTypes))
					selection = KEY_TYPE_PLANET.optInt
				}.lps(0, 0, if(port) 30 else 15, 2)
				select {
					id = R.id.slPack
					adapter = SelectAdapter(this, ctx, SelectPopup(), SelectItem(), Pack.arrayStr(Pack.name, Pack.name) + "...")
					selectionString = KEY_TMP_PACK.optText
				}.lps(if(port) 0 else 15, dy)
				editLayout {
					edit(R.string.hint_number) {
						id = R.id.etNumber
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 0..99
					}
				}.lps(0, 2 + dy, 15, 3)
				editLayout {
					edit(R.string.hint_name) {
						id = R.id.etName
					} }.lps(15, 2 + dy, 15, 3)
				editLayout {
					edit(R.string.hint_fuel) {
						id = R.id.etFuel
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 300..2000
					}
				}.lps(0, 5 + dy, 15, 3)
				editLayout {
					edit(R.string.hint_time) {
						id = R.id.etTime
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 300..5000
					}
				}.lps(15, 5 + dy, 15, 3)
				editLayout {
					edit(R.string.hint_width) {
						id = R.id.etWidth
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 14..40
						isEnabled = isNew
					}
				}.lps(0, 8 + dy, 15, 3)
				editLayout {
					edit(R.string.hint_height) {
						id = R.id.etHeight
						inputType = InputType.TYPE_CLASS_NUMBER
						range = 14..40
						isEnabled = isNew
					}
				}.lps(15, 8 + dy, 15, 3)
				formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
			}
		}
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		isNew = index == FORM_PLANET_NEW
		return UI {
			include(PlanetNP(index == FORM_PLANET_NEW)) {}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		if(!isNew) {
			content.byId<Edit>(R.id.etName).string = Planet.MAP.name
			content.byId<Edit>(R.id.etNumber).int = Planet.MAP.num.toInt()
			content.byId<Edit>(R.id.etTime).int = Planet.MAP.time
			content.byId<Edit>(R.id.etFuel).int = Planet.MAP.fuel
			content.byId<Edit>(R.id.etWidth).int = Planet.MAP.width
			content.byId<Edit>(R.id.etHeight).int = Planet.MAP.height
		}
		content.byId<Select>(R.id.slPack).itemClickListener = this@FormPlanetNP
		pack = KEY_TMP_PACK.optText
	}
	
	private fun updateSystems(sel: String) {
		content.byId<Select>(R.id.slPack).apply {
			adapter = SelectAdapter(this, wnd, SelectPopup(), SelectItem(), Pack.arrayStr(Pack.name, Pack.name) + "...")
			selectionString = sel
		}
		pack = sel
	}

	override fun handleMessage(msg: Message) {
		msg.apply {
			if(arg1 != ACTION_PACK) return@apply
			if(arg2 == BTN_OK) {
				val name = obj.toString()
				// Добавляем новый пакет
				Pack.insert {
					it[Pack.name] = name
					it[Pack.date] = System.currentTimeMillis()
					it[Pack.author] = KEY_PLAYER.optText
					it[Pack.skull] = 5
					it[Pack.planets] = 0
					it[Pack.desc] = "Новая система"
					it[Pack.price] = 0f
				}
				
				updateSystems(name)
			}
			else content.byId<Select>(R.id.slPack).selectionString = pack
		}
	}
	
	override fun footer(btnId: Int, param: Int) {
		val mBuffer = Planet.MAP.buffer
		var change  = false
		var isNum = false
		if(btnId == BTN_OK) {
			try {
				// номер
				val num = content.byId<Edit>(R.id.etNumber).valid.toLong()
				// имя
				val etName = content.byId<Edit>(R.id.etName)
				val name = etName.valid
				// топливо
				val fuel = content.byId<Edit>(R.id.etFuel).valid.toInt()
				// время
				val time = content.byId<Edit>(R.id.etTime).valid.toInt()
				// ширина
				val width = if(!isNew) Planet.MAP.width else content.byId<Edit>(R.id.etWidth).valid.toInt()
				// высота
				val height = if(!isNew) Planet.MAP.height else content.byId<Edit>(R.id.etHeight).valid.toInt()
				// Проверить на существование уже такой планеты с именем
				val isName = Planet.exist { Planet.system.eq(pack) and Planet.title.eq(name) }
				// изменился номер?
				isNum = num != Planet.MAP.num && Planet.MAP.num != -1L
				// изменилось имя или система?
				change = name != Planet.MAP.name || pack != Planet.MAP.pack
				if(!isNew) {
					if(isName && change) throw EditInvalidException(getString(R.string.planet_name_already_exist, name), etName)
					if(change || isNum) Planet.MAP.delete()
				}
				else {
					if(isName) throw EditInvalidException(getString(R.string.planet_name_already_exist, name), etName)
					isNum = true
				}
				if(isNum && Planet.exist { Planet.system.eq(pack) and Planet.position.eq(num) }) {
					Planet.select(Planet.position) {
						where { Planet.system.eq(pack) and Planet.position.greaterEq(num) }
						orderBy(Planet.position, false)
					}.execute()?.release {
						forEach {
							val n = integer(0)
							Planet.update {
								it[Planet.position] = n + 1
								where { Planet.system.eq(pack) and Planet.position.eq(n) }
							}
						}
					}
				}
				if(change || isNum || Planet.MAP.fuel != fuel || Planet.MAP.time != time) {
					mBuffer[0] = width.toByte(); mBuffer[1] = height.toByte()
					Planet.MAP.num = num
					Planet.MAP.name = name
					Planet.MAP.pack = pack
					Planet.MAP.time = time
					Planet.MAP.fuel = fuel
					Planet.MAP.buffer = mBuffer
					if(isNew) Planet.MAP.date = System.currentTimeMillis()
					if(isNew) Planet.MAP.auth = KEY_PLAYER.optText
					change = true
				}
			} catch(e: EditInvalidException) {
				val et = e.et ?: return
				et.startAnimation(shake)
				if(e.msg.isNotEmpty()) wnd.showToast(e.msg, parent = e.et)
				et.requestFocus()
				return
			}
		}
		val sel = content.byId<Select>(R.id.slType).selection
		KEY_TYPE_PLANET.optInt = sel
		if(change || isNum) sendResult(MSG_FORM, if(!isNew) ACTION_SAVE else ACTION_NEW, if(!isNew) 0 else sel)
		super.footer(btnId, 0)
	}
}

