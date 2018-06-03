package ru.ostrovskal.droid.forms

import android.app.FragmentTransaction
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.config
import com.github.ostrovskal.ssh.utils.debug
import com.github.ostrovskal.ssh.utils.send
import com.github.ostrovskal.ssh.widgets.Text
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.msg
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.views.ViewGame

class FormGame : Form() {
	// поверхность
	lateinit var game: ViewGame

	@JvmField var namePlanet: Text? = null
	
	override fun backPressed() {
		val tm = if(twicePressed()) tmBACK + 2000 else System.currentTimeMillis() + 10000
		if(game.status == STATUS_LOOP) {
			if(tm > System.currentTimeMillis()) {
				footer(BTN_NO, 0)//, ACTION_BACKPRESSED)
			} else {
				wnd.showToast(getString(R.string.press_restart), parent = content)
			}
		}
		tmBACK = System.currentTimeMillis()
	}
	
	override fun twicePressed() = game.record == 0L
	
	override fun handleMessage(msg: Message) {
		val s = game.surHandler
		if(s != null) {
			msg.apply {
				"onMessageFormGame(what: ${what.msg} arg1: ${arg1.msg} arg2: $arg2 obj: $obj)".debug()
				when(arg1) {
					ACTION_LOAD     -> s.send(a1 = ACTION_LOAD, a2 = arg2)
					ACTION_NAME     -> namePlanet?.text = Planet.MAP.name
					ACTION_EXIT     -> footer(BTN_NO, 0)
					ACTION_FINISH   -> wnd.instanceForm(FORM_FINISH)
				}
			}
		}
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		val port = config.isVert
		return UI {
			linearLayout(port) {
				containerLayout(if(port) 100 else 70, if(port) 70 else 100, true) {
					id = R.id.gameContainer
					game = custom { id = R.id.game }
				}.lps(WRAP, WRAP)
				cellLayout(if(port) 28 else 17, if(port) 19 else 32) {
					backgroundSet(if(port) style_panel_port else style_panel_land)
					repeat(3) {
						button(style_tile_droid) { numResource = tilesGamePanel[it] }.lps(2, 6 + it * 4, 4, 4)
						text(R.string._00000, style_text_counters).lps(6, 6 + it * 4, 5, 4)
					}
					val x = if(port) 11 else 1
					val y = if(port) 6 else 19
					repeat(2) { col ->
						repeat(3) {
							button(style_tile_droid) { numResource = tilesGamePanel[3 + col * 3 + it] }.lps(x + col * 8, y + it * 4, 4, 4)
							text(R.string._000, style_text_counters).lps(x + 4 + col * 8, y + it * 4, 4, 4)
						}
					}
					namePlanet = text(R.string.null_text, style_text_planet) {
						if(port) lps(1, 1, 26, 3) else lps(0, 0, 17, 6)
					}
				}
			}
		}
	}

	override fun initContent(content: ViewGroup) {
		game.position = arguments.getInt("position")
		game.record = arguments.getLong("record")
		game.test = arguments.containsKey("test")
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(param == ACTION_BACKPRESSED) {
			game.surHandler?.removeMessages(STATUS_SUICIDED)
			game.surHandler?.send(STATUS_SUICIDED, a1 = 1)
		}
		else super.footer(id, 0)
	}
	
	override fun saveState(state: Bundle) {
		game.saveState(state)
		super.saveState(state)
	}
	
	override fun restoreState(state: Bundle) {
		super.restoreState(state)
		game.restoreState(state)
	}
	
	override fun setAnimation(trans: FragmentTransaction) { trans.setTransition(FragmentTransaction.TRANSIT_NONE) }
	
	override fun onDestroy() {
		game.holder.removeCallback(game)
		super.onDestroy()
	}
}
