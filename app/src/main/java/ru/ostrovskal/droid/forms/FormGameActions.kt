package ru.ostrovskal.droid.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants.ACTION_EXIT
import com.github.ostrovskal.ssh.Constants.MSG_FORM
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.dp
import com.github.ostrovskal.ssh.utils.send
import ru.ostrovskal.droid.Constants
import ru.ostrovskal.droid.Constants.style_button_actions
import ru.ostrovskal.droid.Constants.style_dlg_actions
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.views.ViewGame

class FormGameActions: Form() {
	var game: ViewGame? = null
	
	override fun initContent(content: ViewGroup) {
		game = wnd.findForm<FormGame>("game")?.game
		game?.sleepThread = true
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				backgroundSet(style_dlg_actions)
				root = cellLayout(14, 11, 1.dp) {
					formHeader (R.string.header_dialog_actions)
					button(style_button_actions) {
						setOnClickListener(this@FormGameActions)
						text = getString(R.string.game_actions_exit)
					}.lps(0, 0, 14, 3)
					button(style_button_actions) {
						setOnClickListener(this@FormGameActions)
						text = getString(R.string.game_actions_restart)
					}.lps(0, 3, 14, 3)
					button(style_button_actions) {
						setOnClickListener(this@FormGameActions)
						text = getString(R.string.game_actions_continue)
					}.lps(0, 6, 14, 3)
				}.lps(Theme.dimen(ctx, R.dimen.widthGameDlgActions), Theme.dimen(ctx, R.dimen.heightGameDlgActions))
			}
		}
	}
	
	override fun onClick(v: View) {
		when(root?.indexOfChild(v) ?: -1) {
			1   -> sendResult(MSG_FORM, ACTION_EXIT)
			2   -> {
				game?.surHandler?.removeMessages(Constants.STATUS_SUICIDED)
				game?.surHandler?.send(Constants.STATUS_SUICIDED, a1 = 1)
			}
		}
		super.onClick(v)
	}
	
	override fun footer(btnId: Int, param: Int) {
		game?.sleepThread = false
		super.footer(btnId, param)
	}
}