package ru.ostrovskal.droid.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.TILE_STATE_HOVER
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.byIdx
import com.github.ostrovskal.ssh.utils.dp
import com.github.ostrovskal.ssh.utils.padding
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Planet

class FormEditorActions : FormDialog() {
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				backgroundSet(style_dlg_actions)
				padding = 2.dp
				root = cellLayout(14, 14, 0) {
					layoutParams = LinearLayout.LayoutParams(Theme.dimen(ctx, R.dimen.widthEditorDlgActions),
					                                         Theme.dimen(ctx, R.dimen.heightEditorDlgActions))
					formHeader(R.string.header_dialog_actions)
					var idx = 0
					var dx = 0
					repeat(3) { row ->
						repeat(4) {
							if(idx < iconsEditorActions.size) {
								if(idx == iconsEditorActions.size - 2) dx = 3
								button(style_icon_actions) {
									isClickable = true
									states = TILE_STATE_HOVER
									numResource = iconsEditorActions[idx++]
									setOnClickListener(this@FormEditorActions)
								}.lps(it * 3 + 1 + dx, row * 3 + 1, 3, 3)
							}
						}
					}
					formFooter(Constants.BTN_NO, R.integer.I_NO)
				}
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
		root.byIdx<Tile>(FORM_CHOICE_SEND).isEnabled = isMap//(Planet.MAP.pack != SYSTEM_DEFAULT && isMap)
	}
	
	override fun onClick(v: View) {
		val form = wnd.findForm<FormEditor>("editor") ?: error("Форма редактора планет не обнаружена!")
		val idx = root?.indexOfChild(v) ?: -1
		val editor = form.editor
		when(idx) {
			-1                  -> {}
			FORM_CHOICE_SAVE    -> sendResult(Constants.MSG_SERVICE, action = ACTION_SAVE, handler = editor.surHandler)
			FORM_CHOICE_PREV    -> editor.apply { preview = !preview; updatePreview(preview, true) }
			FORM_CHOICE_TEST    -> wnd.apply {
				super.footer(Constants.BTN_NO, 0)
				// проверить на модификацию
				if(editor.modify && !Planet.MAP.store(this)) {
					sendResult(STATUS_MESSAGE, STATUS_WORK, R.string.save_planet_failed, handler = editor.surHandler)
				}
				else {
					instanceForm(FORM_GAME, "position", editor.position, "test", 1)
				}
				return
			}
			else                -> {
				wnd.apply {
					super.footer(Constants.BTN_NO, 0)
					instanceForm(idx + FORM_PLANET_NEW - 1)
					return
				}
			}
		}
		super.onClick(v)
	}
}