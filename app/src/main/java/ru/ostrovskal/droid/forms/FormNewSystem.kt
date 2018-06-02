package ru.ostrovskal.droid.forms

import android.view.LayoutInflater
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.byId
import com.github.ostrovskal.ssh.utils.send
import com.github.ostrovskal.ssh.widgets.Edit
import com.github.ostrovskal.ssh.widgets.EditInvalidException
import ru.ostrovskal.droid.Constants.ACTION_PACK
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack

class FormNewSystem: Form() {
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				cellLayout(15, 7) {
					formHeader(R.string.header_dialog_new_system)
					backgroundSet(ru.ostrovskal.droid.Constants.style_dlg_actions)
					editLayout {
						edit(R.string.hint_new_pack) { id = R.id.etPack }
					}.lps(2, 0, 11, 3)
					formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
				}.lps(Constants.MATCH, Theme.dimen(ctx, R.dimen.heightDlgNewSystem))
			}
		}
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == Constants.BTN_OK) try {
			val edit = content.byId<Edit>(R.id.etPack)
			result = edit.valid
			if(Pack.exist({Pack.name eq result }) ) throw EditInvalidException(getString(R.string.pack_already_exist, result), edit)
		} catch(e: EditInvalidException) {
			e.et?.apply {
				startAnimation(shake)
				if(e.msg.isNotEmpty()) wnd.showToast(e.msg, parent = this)
				requestFocus()
			}
			return
		}
		wnd.wndHandler?.send(Constants.MSG_FORM, 0, ACTION_PACK, btnId, result)
		super.footer(btnId, param)
	}
}