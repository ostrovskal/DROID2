package ru.ostrovskal.droid.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.byIdx
import com.github.ostrovskal.ssh.utils.send
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Planet

class FormEditorActions : FormDialog() {
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				backgroundSet(style_dlg_actions)
				root = cellLayout(14, 20) {
					formHeader(R.string.header_dialog_actions)
					var idx = 0
					var dx = 0
					repeat(4) { row ->
						repeat(3) {
							if(idx < iconsEditorActions.size) {
								if(idx == iconsEditorActions.size - 1) dx = 4
								button(style_button_actions) {
									iconResource = iconsEditorActions[idx++]
									setOnClickListener(this@FormEditorActions)
								}.lps(it * 4 + 1 + dx, row * 4, 4, 4)
							}
						}
					}
					formFooter(Constants.BTN_NO, R.integer.I_NO)
				}.lps(Theme.dimen(ctx, R.dimen.widthEditorDlgActions), Theme.dimen(ctx, R.dimen.heightEditorDlgActions))
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		val editor = wnd.findForm<FormEditor>("editor")?.editor ?: return
		val isMap = editor.isPlanet
		// save, preview
		root.byIdx<Tile>(FORM_CHOICE_SAVE).isEnabled = (editor.modify && isMap)
		root.byIdx<Tile>(FORM_CHOICE_PREV).isEnabled = isMap
		root.byIdx<Tile>(FORM_CHOICE_PREV).isChecked = editor.preview
		// prop, delete
		root.byIdx<Tile>(FORM_CHOICE_PROP).isEnabled = isMap
		root.byIdx<Tile>(FORM_CHOICE_DEL).isEnabled = isMap
		root.byIdx<Tile>(FORM_CHOICE_TEST).isEnabled = isMap
		root.byIdx<Tile>(FORM_CHOICE_SEND).isEnabled = (Planet.pack != SYSTEM_DEFAULT && isMap)
	}
	
	override fun onClick(v: View) {
		val form = wnd.findForm<FormEditor>("editor") ?: error("Форма редактора планет не обнаружена!")
		val idx = root?.indexOfChild(v) ?: -1
		val editor = form.editor
		when(idx) {
			-1                  -> {}
			FORM_CHOICE_SAVE    -> editor.surHandler?.send(Constants.MSG_SERVICE, 0, ACTION_SAVE)
			FORM_CHOICE_PREV    -> editor.apply { preview = !preview; updatePreview(preview, true) }
			FORM_CHOICE_TEST    -> wnd.apply {
				super.footer(Constants.BTN_NO, 0)
				// проверить на модификацию
				if(editor.modify && !Planet.store(this)) {
					editor.surHandler?.send(STATUS_MESSAGE, 0, STATUS_PREPARED, R.string.save_planet_failed)
				}
				else {
					instanceForm(FORM_GAME, "position", editor.position, "isTest", 1)
				}
				return
			}
			else                -> {
				wnd.apply {
					super.footer(Constants.BTN_NO, 0)
					instanceForm(idx + FORM_PLANET_OPEN - 1)
					return
				}
			}
		}
		super.onClick(v)
	}
}