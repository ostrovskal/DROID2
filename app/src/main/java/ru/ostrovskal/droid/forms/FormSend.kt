package ru.ostrovskal.droid.forms

import android.text.InputType.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.BTN_OK
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs.*
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Edit
import com.github.ostrovskal.ssh.widgets.EditInvalidException
import com.github.ostrovskal.ssh.widgets.Text
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.style_text_planet
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.Planet

class FormSend: Form() {
	private var skull1          = 0
	private var skull2          = 0
	private var skull           = 0
	
	override fun footer(btnId: Int, param: Int) {
		val p: Int
		val d: String
		if(btnId == BTN_OK) {
			try {
				// цена
				p = content.byId<Edit>(R.id.etPrice).valid.toInt()
				// описание
				d = content.byId<Edit>(R.id.etDesc).string
			}
			catch(e: EditInvalidException) {
				val et = e.et ?: return
				et.startAnimation(shake)
				et.requestFocus()
				return
			}
			// формируем бинарный пакет
			makePacketForSend(p, d)
		}
		super.footer(btnId, param)
	}
	
	private fun makePacketForSend(price: Int, desc: String) {
		// запомнить в БД
		Pack.update {
			it[Pack.desc] = desc
			it[Pack.price] = price.toFloat()
			it[Pack.skull] = skull.toLong()
			where { Pack.name eq Planet.pack }
		}
		// сформировать пакет
		// отправить на сервер
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			skull1 = resources.getInteger(R.integer.I_SKULL1)
			skull2 = resources.getInteger(R.integer.I_SKULL2)
			val port = config.isVert
			root = cellLayout(10, if(port) 19 else 17, 2.dp) {
				formHeader(R.string.header_send_system)
				backgroundSet(style_form)
				val ySkull = if(port) 13 else 11
				val wText = if(port) 10 else 6
				repeat(10) {
					button(style_icon) {
						num = if(it > 4) skull1 else skull2
						setOnClickListener {
							setSkulls(root?.indexOfChild(it) ?: 0)
						}
					}.lps(it, ySkull, 1, 2)
				}
				text(R.string.null_text, style_text_planet) { id = R.id.etName; gravity = Gravity.CENTER }.lps(0, 0, wText, 2)
				text(R.string.panel_text, style_text_small) { id = R.id.etNumber; gravity = Gravity.CENTER }.lps(0, 2, wText, 1)
				editLayout {
					edit(R.id.etPrice, R.string.hint_price) { inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL; range = 0..500 }
					if(port) lps(0, 3, 10, 3) else lps(6, 0, 4, 3)
				}
				editLayout {
					edit(R.id.etDesc, R.string.hint_desc) {
						maxLength = 100
						inputType = TYPE_TEXT_FLAG_MULTI_LINE or TYPE_CLASS_TEXT
					}
					if(port) lps(0, 6, 10, 7) else lps(0, 3, 11, 8)
				}
				formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		Pack.select(Pack.name, Pack.date, Pack.price, Pack.desc, Pack.skull, Pack.author, Pack.planets) {
			where { Pack.name eq Planet.pack }
		}.execute()?.release {
			root.byId<Text>(R.id.etName).text = "${text("name")}: ${text("planets")}"
			root.byId<Text>(R.id.etNumber).text = datetime(Pack.date)
			root.byId<Edit>(R.id.etPrice).string = text("price")
			root.byId<Edit>(R.id.etDesc).string = text("desc")
			setSkulls(integer(Pack.skull).toInt())
		}
	}
	
	private fun setSkulls(skulls: Int) {
		skull = skulls
		repeat(10) {
			root.byIdx<Tile>(it + 1).num = if(it < skull) skull2 else skull1
		}
	}
}