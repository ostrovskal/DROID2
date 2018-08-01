package ru.ostrovskal.droid.forms

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.github.ostrovskal.ssh.AnimFrames
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.BTN_OK
import com.github.ostrovskal.ssh.Constants.TILE_STATE_NONE
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs.style_text_large
import com.github.ostrovskal.ssh.StylesAndAttrs.style_tool_arrow
import com.github.ostrovskal.ssh.layouts.CellLayout
import com.github.ostrovskal.ssh.layouts.EditLayout
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.EditInvalidException
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R

class FormSplash: Form(), AnimFrames.Callback {
	override fun doFrame(view: View, frame: Int, direction: Int, began: Boolean): Boolean {
		(content as? CellLayout)?.shiftCells(0, (if(config.isVert) 30 else 17) + frame, 0, 1)
		return frame > 4
	}
	
	private var alphaAnm: Animation?    = null
	private val anim                    by lazy { AnimFrames(lyt, 4, 50, this) }
	private var ref					    = 0
	private lateinit var lyt: EditLayout
	
	private val post = Runnable {
		if(KEY_FIRST_START.optBool) {
			content.byIdx<EditLayout>(1).apply { visible = true; startAnimation(alphaAnm) }
			content.byIdx<Tile>(2).apply { visibility = View.VISIBLE; startAnimation(alphaAnm) }
			anim.start(false, false)
		} else footer(Constants.BTN_NO, 0)
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			val coords = if(config.isVert) coordV else coordH
			cellLayout(coords[0], coords[1]) {
				backgroundSet { solid = 0.color }
				text(R.string.splash_adaptation, style_text_large) {
					gravity = Gravity.CENTER
					textSize = 30 * config.multiplySW
				}.lps(coords[2], coords[3], coords[4], coords[5])
				lyt = editLayout {
					visibility = View.GONE
					edit(R.id.etName, R.string.hint_splash_name)
				}.lps(coords[6], coords[7], coords[8], coords[9])
				button(style_tool_arrow) {
					id = BTN_OK
					visibility = View.GONE
					rotation = 90f
				}.lps(coords[10], coords[11], coords[12], coords[13])
				text(R.string.copyright, style_text_large) { gravity = Gravity.CENTER }.lps(coords[14], coords[15], coords[16], coords[17])
				button {
					states = TILE_STATE_NONE
					setBitmap("droid")
					setOnClickListener { ref++ }
				}.lps(coords[18], coords[19], coords[20], coords[21])
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		alphaAnm = AnimationUtils.loadAnimation(wnd, R.anim.alpha)
		wnd.wndHandler?.postDelayed(post, SPLASH_DELAYED)
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == Constants.BTN_OK) {
			try {
				KEY_PLAYER.optText = lyt.edit.valid
			} catch(e: EditInvalidException) {
				e.et?.startAnimation(shake)
				return
			}
		}
		if(ref > 2) KEY_AUTHOR_COUNT.optInt = if(DroidWnd.isAuthor()) 0 else 15
		val first = KEY_FIRST_START.optBool
		KEY_FIRST_START.optBool = false
		wnd.instanceForm(FORM_MENU, "first", first)
		super.footer(btnId, param)
	}
	
	override fun backPressed() { wnd.finish() }
	
	override fun saveState(state: Bundle) {
		state.put("ref", ref)
		super.saveState(state)
	}
	
	override fun restoreState(state: Bundle) {
		wnd.wndHandler?.apply { removeCallbacksAndMessages(null); lyt.postDelayed(post, 1200) }
		ref - state.getInt("ref")
		super.restoreState(state)
	}
}
