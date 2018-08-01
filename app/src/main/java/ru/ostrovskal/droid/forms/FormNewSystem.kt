package ru.ostrovskal.droid.forms

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.byId
import com.github.ostrovskal.ssh.utils.maxLength
import com.github.ostrovskal.ssh.utils.optText
import com.github.ostrovskal.ssh.utils.send
import com.github.ostrovskal.ssh.widgets.Edit
import com.github.ostrovskal.ssh.widgets.EditInvalidException
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack

class FormNewSystem: Form() {
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				cellLayout(15, 12) {
					formHeader(R.string.header_dialog_new_system)
					backgroundSet(style_dlg_actions)
					editLayout {
						edit(R.id.etPack, R.string.hint_new_pack)
					}.lps(1, 0, 13, 3)
					editLayout {
						edit(R.id.etDesc, R.string.hint_desc) {
							maxLength = 100
							inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
						}
					}.lps(1, 3, 13, 5)
					formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
				}.lps(Constants.MATCH, Theme.dimen(ctx, R.dimen.heightDlgNewSystem))
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		content.byId<Edit>(R.id.etDesc).string = getString(R.string.new_system)
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == Constants.BTN_OK) {
			val desc: String
			var edit: Edit
			try {
				edit = content.byId(R.id.etPack)
				result = edit.valid
				if(Pack.exist {Pack.name eq result } ) throw EditInvalidException(getString(R.string.pack_already_exist, result), edit)
				edit = content.byId(R.id.etDesc)
				desc = edit.valid
			} catch(e: EditInvalidException) {
				e.et?.apply {
					startAnimation(shake)
					if(e.msg.isNotEmpty()) wnd.showToast(e.msg, parent = this)
					requestFocus()
				}
				return
			}
				// Добавляем новый пакет
				Pack.insert {
					it[Pack.name] = result
					it[Pack.date] = System.currentTimeMillis()
					it[Pack.author] = KEY_PLAYER.optText
					it[Pack.skull] = 5
					it[Pack.planets] = 0
					it[Pack.desc] = desc
					it[Pack.price] = 0f
				}
		}
		wnd.wndHandler?.send(Constants.MSG_FORM, 0, ACTION_PACK, btnId, result)
		super.footer(btnId, param)
	}
}