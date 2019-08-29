@file:Suppress("DEPRECATION")

package ru.ostrovskal.droid.forms

import android.app.FragmentTransaction
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Text
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.editorView
import ru.ostrovskal.droid.msg
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.views.ViewEditor

class FormEditor : Form() {
	
	private var namePlanet: Text?   = null
	
	// текущий тайл
	var curTile: Tile? 				= null
	
	// поверхность
	lateinit var editor: ViewEditor
	
	override fun backPressed() {
		if(wnd.fragmentManager.backStackEntryCount != 0) footer(BTN_NO, 0)
	}
	
	override fun onResume() {
		editor.preview = KEY_EDIT_PREVIEW.optBool
		super.onResume()
	}
	
	override fun handleMessage(msg: Message) {
		val s = editor.surHandler
		if(s != null) {
			msg.apply {
				"onMessageFormEditor(what: ${what.msg} arg1: ${arg1.msg} arg2: $arg2 obj: $obj)".debug()
				when(arg1) {
					ACTION_LOAD     -> s.send(a1 = ACTION_LOAD, a2 = arg2)
					ACTION_GENERATE -> s.send(a1 = ACTION_GENERATE)
					ACTION_DELETE   -> s.send(a1 = ACTION_DELETE)
					ACTION_NEW      -> s.send(a1 = ACTION_NEW, a2 = arg2)
					ACTION_SAVE     -> s.send(a1 = ACTION_SAVE, a2 = arg2)
					ACTION_NAME     -> {
						namePlanet?.text = Planet.name
						KEY_EDIT_PLANET.optText = "${Planet.num}#${Planet.pack}"
					}
					ACTION_EXIT     -> footer(BTN_NO, 0)
				}
			}
		}
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx = UI {
		val port = config.isVert
		linearLayout(port) {
			containerLayout(if(port) 100 else 70, if(port) 70 else 100, true) {
				id = R.id.editorContainer
				editor = editorView { id = R.id.editor }
			}.lps(WRAP, WRAP)
			root = cellLayout(if(port) 28 else 14, if(port) 19 else 32) {
				backgroundSet(style = if(port) style_panel_port else style_panel_land)
				var x = if(port) 2 else 1
				var y = if(port) 5 else 6
				val dx = if(port) 0 else 4
				val dy = if(port) 4 else 0
				repeat(3) { row ->
					repeat(6) {
						button(style_tile_droid) {
							setOnClickListener(this@FormEditor)
							isClickable = true
							numResource = tilesEditorPanel[row * 6 + it]
						}.lps(x + dy * it, y + dx * it, 4, 4)
					}
					x += dx
					y += dy
				}
				namePlanet = text(R.string.null_text, style_text_planet) {
					setOnClickListener {
						if(fragmentManager.backStackEntryCount == 1) wnd.instanceForm(FORM_DLG_E_ACTIONS)
					}
					if(port) lps(1, 1, 26, 3) else lps(0, 0, 14, 6)
				}
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		setTile(KEY_EDIT_TILE.optInt)
		val lst = KEY_EDIT_PLANET.optText.split('#')
		if(lst.size == 2) {
			editor.position = lst[0].ival(0, 10)
			Planet.pack = lst[1]
		}
		else {
			editor.position = 0
			Planet.pack = SYSTEM_DEFAULT
		}
		Sound.playMusic(wnd, 0, true)
	}
	
	override fun onClick(v: View) {
		setTile(root?.indexOfChild(v) ?: 0)
	}
	
	override fun footer(btnId: Int, param: Int) {
		// вызывать только, если других фрагментов нет
		if(editor.modify && fragmentManager.backStackEntryCount == 1) {
			editor.modify = false
			wnd.instanceForm(FORM_DLG_SAVE)
		}
		else super.footer(btnId, param)
	}
	
	override fun saveState(state: Bundle) {
		editor.saveState(state)
		super.saveState(state)
		namePlanet?.apply { state.put("name_planet", text) }
	}
	
	override fun restoreState(state: Bundle) {
		super.restoreState(state)
		editor.restoreState(state)
		namePlanet?.apply { text = state.getString("name_planet") }
	}
	
	override fun setAnimation(trans: FragmentTransaction) { trans.setTransition(FragmentTransaction.TRANSIT_NONE) }
	
	override fun onDestroy() {
		editor.holder.removeCallback(editor)
		super.onDestroy()
	}
	
	fun setTile(idx: Int) {
		curTile?.isChecked = false
		curTile = root?.byIdx(idx)
		curTile?.isChecked = true
		KEY_EDIT_TILE.optInt = idx
	}
}
