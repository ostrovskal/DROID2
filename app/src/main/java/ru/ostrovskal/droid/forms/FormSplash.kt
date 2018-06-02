package ru.ostrovskal.droid.forms

import android.os.Bundle
import android.text.util.Linkify.WEB_URLS
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.AnimFrames
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.BTN_OK
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs.*
import com.github.ostrovskal.ssh.layouts.CellLayout
import com.github.ostrovskal.ssh.layouts.EditLayout
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.optBool
import com.github.ostrovskal.ssh.utils.optInt
import com.github.ostrovskal.ssh.utils.optText
import com.github.ostrovskal.ssh.widgets.Edit
import com.github.ostrovskal.ssh.widgets.EditInvalidException
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R

class FormSplash: Form(), AnimFrames.Callback {
	private lateinit var lyt: EditLayout
	private lateinit var name: Edit
	private lateinit var ok: Tile
	private val anim                    by lazy { AnimFrames(lyt, 5, 50, this) }
	private var ref					    = 0
	
	private val post = Runnable {
		if(KEY_FIRST_START.optBool) {
			lyt.apply { visible = true; startAnimation(shake) }
			ok.apply { visibility = View.VISIBLE; startAnimation(shake) }
			anim.start(true, true)
		} else footer(Constants.BTN_NO, 0)
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			cellLayout(20, 44) {
				text(R.string.splash_adaptation, style_text_large) { gravity = Gravity.CENTER }.lps(0, 0, 20, 4)
				button { setBitmap("droid"); setOnClickListener { ref++ } }.lps(2, 4, 16, 21)
				text(R.string.copyright, style_text_large) { gravity = Gravity.CENTER }.lps(0, 28, 20, 4)
				text(R.string.rights, style_text_normal) { gravity = Gravity.CENTER }.lps(0, 32, 20, 3)
				text(R.string.splash_web, style_text_small) { gravity = Gravity.CENTER; autoLinkMask = WEB_URLS }.lps(0, 35, 20, 3)
				lyt = editLayout { visibility = View.GONE; name = edit(R.string.hint_splash_name) { id = android.R.id.edit } }.lps(2, 26, 14, 9)
				ok = button(style_tool_arrow) { id = BTN_OK; visibility = View.GONE; rotation = 90f }.lps(17, 26, 3, 9)
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		wnd.wndHandler?.postDelayed(post, 5000)
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == Constants.BTN_OK) {
			try {
				KEY_PLAYER.optText = name.valid
			} catch(e: EditInvalidException) {
				e.et?.startAnimation(shake)
				return
			}
		}
		if(ref > 3) KEY_AUTHOR_COUNT.optInt = if(DroidWnd.isAuthor()) 0 else 20
		val first = KEY_FIRST_START.optBool
		KEY_FIRST_START.optBool = false
		wnd.instanceForm(FORM_MENU, "first", first)
		super.footer(btnId, param)
	}
	
	override fun backPressed() { wnd.finish() }
	
	override fun saveState(state: Bundle) { anim.stop() }
	
	override fun restoreState(state: Bundle) {
		wnd.wndHandler?.apply { removeCallbacksAndMessages(null); lyt.postDelayed(post, 1200) }
	}
	
	override fun doFrame(view: View, frame: Int, direction: Int, began: Boolean): Boolean {
		(content as? CellLayout)?.shiftCells(0, 28 + frame, 0, 1)
		return frame >= anim.frames
	}
}
