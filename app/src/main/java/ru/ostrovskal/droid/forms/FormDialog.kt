package ru.ostrovskal.droid.forms

import android.view.LayoutInflater
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.ui.*
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R

open class FormDialog: Form() {
	override fun inflateContent(container: LayoutInflater): UiCtx {
		val header: Int
		val txt: Int
		val height: Int
		val width: Int
		when(index) {
			FORM_DLG_DELETE     -> {
				txt = R.string.dialog_delete
				header = R.string.header_dialog_delete
				height = R.dimen.heightDlgDelete
				width = R.dimen.widthDlgDelete
			}
			FORM_DLG_SAVE       -> {
				txt = R.string.dialog_save
				header = R.string.header_dialog_save
				height = R.dimen.heightDlgSave
				width = R.dimen.widthDlgSave
			}
			FORM_DLG_GENERATE   -> {
				txt = R.string.dialog_generate
				header = R.string.header_dialog_generate
				height = R.dimen.heightDlgGenerate
				width = R.dimen.widthDlgGenerate
			}
			FORM_DLG_EXIT       -> {
				txt = R.string.dialog_exit
				header = R.string.header_dialog_exit
				height = R.dimen.heightDlgExit
				width = R.dimen.widthDlgExit
			}
			else                -> error("Неизвестный тип диалога!")
		}
		return UI {
			linearLayout {
				cellLayout(10, 9) {
					formHeader(header)
					text(txt, StylesAndAttrs.style_text_dlg).lps(0, 0, -1, 5)
					formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
				}.lps(Theme.dimen(ctx, width), Theme.dimen(ctx, height))
			}
		}
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == BTN_OK) {
			when(index) {
				FORM_DLG_EXIT       -> sendResult(MSG_SERVICE, action = ACTION_EXIT)
				FORM_DLG_GENERATE   -> sendResult(MSG_FORM, ACTION_GENERATE)
				FORM_DLG_DELETE     -> sendResult(MSG_FORM, ACTION_DELETE)
			}
		}
		when(index) {
			FORM_DLG_SAVE       -> sendResult(MSG_FORM, if(btnId == BTN_OK) ACTION_SAVE else ACTION_EXIT, 1)
		}
		super.footer(btnId, param)
	}
}