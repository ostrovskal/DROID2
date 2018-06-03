package ru.ostrovskal.droid.forms

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.MATCH
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs
import com.github.ostrovskal.ssh.StylesAndAttrs.style_text_html
import com.github.ostrovskal.ssh.adapters.SelectAdapter
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Html
import com.github.ostrovskal.ssh.widgets.Text
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack
import kotlin.properties.Delegates

class FormHelp: Form() {
	private var html            by Delegates.notNull<Html>()
	private var path: String?   = null
	private var scroll          = 0
	
	override fun backPressed()  { if(!html.back()) super.backPressed() }
	
	override fun onResume() {
		super.onResume()
		if(!html.valid) html.setArticle(path, scroll)
	}
	
	override fun saveState(state: Bundle) {
		super.saveState(state)
		html.save(state)
	}
	
	override fun restoreState(state: Bundle) {
		super.restoreState(state)
		html.restore(state)
		path = state.getString("path")
		scroll = state.getInt("scroll")
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				backgroundSet(StylesAndAttrs.style_form)
				formHeader(R.string.header_help)
				html = html(if(index == FORM_FINISH) style_text_finish else style_text_html) {
					id = R.id.html
					root = when(index) {
						FORM_GAME_HELP   -> "html/game/"
						FORM_EDITOR_HELP -> "html/editor/"
						FORM_FINISH      -> {
							// проверить какую систему прошел и подставить соответствующий корень
							if(KEY_PACK.optText != "CLASSIC") "html/finish2/"
							else {
								KEY_AUTHOR_COUNT.optInt = 20
								"html/finish1/"
							}
						}
						else             -> error("Unknown help source!")
					}
					langResource = R.string.lang
					if(index != FORM_FINISH) {
						val classic = KEY_CLASSIC.optBool
						
						var key = "theme_tool"
						aliases["tool"] = Html.Alias(key, wnd.bitmapGetCache(key), 3, 1, 0, 0, 30f)
						key = if(classic) "classic_sprites" else "custom_sprites"
						aliases["main"] = Html.Alias(key, wnd.bitmapGetCache(key), 10, 4, 0, 0, 23f)
						key = "icon_tiles"
						aliases["icons"] = Html.Alias(key, wnd.bitmapGetCache(key), 10, 3, 0, 0, 30f)
						key = "controller_tiles"
						aliases["cursor"] = Html.Alias(key, wnd.bitmapGetCache(key), 6, 1, 0, 0, 45f)
						
						val width = 330.dp
						
						key = "game_panel$theme$classic"
						aliases["game_panel"] = Html.Alias(key, GamePanel().makeBitmap(wnd, key, width, 165.dp))
						key = "game_record$theme$classic"
						aliases["game_record"] = Html.Alias(key, FormRecord.RecordVertItem().makeBitmap(wnd, key, width, 165.dp))
						key = "game_recv$theme$classic"
						aliases["game_recv"] = Html.Alias(key, FormRecv.RecvItem().makeBitmap(wnd, key, width, 165.dp))
						if(index == FORM_EDITOR_HELP) {
							key = "help_editor_panel$theme$classic"
							aliases["editor_panel"] = Html.Alias(key, EditorPanel().makeBitmap(wnd, key, width, 165.dp))
							key = "help_editor_new$theme"
							aliases["editor_new"] = Html.Alias(key, FormPlanetNP.PlanetNP(true, SYSTEM_DEFAULT).makeBitmap(wnd, key, width, 270.dp))
							key = "help_editor_prop$theme"
							aliases["editor_prop"] = Html.Alias(key, FormPlanetNP.PlanetNP(false, SYSTEM_DEFAULT).makeBitmap(wnd, key, width, 270.dp))
							key = "help_editor_open$theme"
							aliases["editor_open"] = Html.Alias(key, PlanetOpen().makeBitmap(wnd, key, width, 270.dp))
							key = "help_editor_actions$theme"
							aliases["editor_actions"] = Html.Alias(key, EditorActions().makeBitmap(wnd, key, width, 187.dp))
							key = "help_editor_send$theme"
							aliases["editor_send"] = Html.Alias(key, EditorSend().makeBitmap(wnd, key, width, 187.dp))
						}
					}
				}.lps(MATCH, MATCH)
			}
		}
	}
	
	private class PlanetOpen: UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			linearLayout {
				formHeader(R.string.header_planet_open)
				backgroundSet(StylesAndAttrs.style_form)
				select { adapter = SelectAdapter(this, ctx, SelectPopup(), SelectItem(), Pack.listOf(Pack.name, Pack.name)) }
				include(FormOpen.OpenItem()) {}
				include(FormOpen.OpenItem()) {
					it.byIdx<Text>(1).setText(R.string.panel_text2)
				}
			}
		}
	}
	
	private class EditorActions : UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(10, 10) {
				formHeader(R.string.header_dialog_actions)
				backgroundSet(style_dlg_actions)
				padding = 2.dp
				var idx = 0
				var dx = 0
				repeat(3) {row ->
					repeat(4) {
						if(idx < iconsEditorActions.size) {
							if(idx == iconsEditorActions.size - 2) dx = 2
							button(StylesAndAttrs.style_icon) { numResource = iconsEditorActions[idx++] }.lps(it * 2 + 1 + dx, row * 2, 2, 2) }
					}
				}
				formFooter(Constants.BTN_OK, R.integer.I_NO)
			}
		}
	}
	
	private class EditorPanel : UiComponent() {
		
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(28, 18) {
				backgroundSet(style_panel_port)
				repeat(3) {row ->
					repeat(6) { button(style_panel_tile) { numResource = tilesEditorPanel[row * 6 + it] }.lps(2 + it * 4, 5 + row * 4, 4, 4) }
				}
				text(R.string.panel_text, style_text_planet).lps(1, 1, 26, 3)
			}
		}
	}
	
	private class GamePanel: UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(28, 19) {
				backgroundSet(style_panel_port)
				repeat(3) {
					button(style_panel_tile) { numResource = tilesGamePanel[it] }.lps(2, 6 + it * 4, 4, 4)
					text(R.string._00000, style_text_counters).lps(6, 6 + it * 4, 5, 4)
				}
				repeat(2) { col ->
					repeat(3) {
						button(style_panel_tile) { numResource = tilesGamePanel[3 + col * 3 + it] }.lps(11 + col * 8, 6 + it * 4, 4, 4)
						text(R.string._000, style_text_counters).lps(15 + col * 8, 6 + it * 4, 4, 4)
					}
				}
				text(R.string.panel_text, style_text_planet).lps(1, 1, 26, 3)
			}
		}
	}
	
	private class EditorSend: UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(10, 19, 2.dp) {
				formHeader(R.string.header_send_system)
				backgroundSet(StylesAndAttrs.style_form)
				repeat(10) {
					button(StylesAndAttrs.style_icon) { numResource = if(it > 4) R.integer.I_SKULL1 else R.integer.I_SKULL2 }.lps(it, 13, 1, 2)
				}
				text(R.string.null_text, style_text_planet) { id = R.id.etName; gravity = Gravity.CENTER }.lps(0, 0, 10, 2)
				text(R.string.panel_text, StylesAndAttrs.style_text_small) { id = R.id.etNumber; gravity = Gravity.CENTER }.lps(0, 2, 10, 1)
				editLayout {
					edit(R.string.hint_price) { id = R.id.etPrice; inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL; range = 0..500 }
				}.lps(0, 3, 10, 3)
				editLayout {
					edit(R.string.hint_desc) {
						id = R.id.etDesc
						maxLength = 100
						inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
					}
				}.lps(0, 6, 10, 7)
				formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
			}
		}
	}
}