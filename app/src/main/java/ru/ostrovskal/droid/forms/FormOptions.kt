package ru.ostrovskal.droid.forms

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs.style_menu
import com.github.ostrovskal.ssh.adapters.SelectAdapter
import com.github.ostrovskal.ssh.singleton.Settings
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Check
import com.github.ostrovskal.ssh.widgets.Seek
import com.github.ostrovskal.ssh.widgets.Switch
import com.github.ostrovskal.ssh.widgets.lists.Select
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack

class FormOptions : Form() {
	private var keysToggle 	= arrayOf<String>()
	private var keysSeek 	= arrayOf<String>()
	private var keysCheck 	= arrayOf<String>()
	
	private val coordPort   = intArrayOf(16, 26, 17, 2, 4, 8, 12, 16, 6, 10, 14, 18)
	private val coordLand   = intArrayOf(26, 16, 13, 0, 2, 2, 6, 6, 4, 4, 8, 8)
	
	override fun handleMessage(msg: Message) {
		msg.apply {
			if(arg1 == ACTION_PACK) {
				if(arg2 == BTN_OK) {
				
				} else root.byIdx<Select>(2).selectionString = KEY_TMP_PACK.optText
			}
		}
	}
	
	override fun onItemClick(select: Select, view: View, position: Int, id: Long) {
		val pack = select.selectionString
		if(pack == "...") {
			wnd.instanceForm(FORM_RECV)
		} else KEY_TMP_PACK.optText = pack
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		keysToggle = wnd.arrayStr(R.array.optionsKeysToggle)
		keysSeek = wnd.arrayStr(R.array.optionsKeysSeek)
		keysCheck = wnd.arrayStr(R.array.optionsKeysCheck)
		val vert = config.isVert
		return UI {
			val coord = if(vert) coordPort else coordLand
			val x = if(vert) 0 else 13
			val y = if(vert) 20 else 10
			val dx = if(vert) 6 else 9

			root = cellLayout(coord[0], coord[1], 1.dp, false) {
				formHeader(R.string.header_options)
				backgroundSet(style_menu)
				
				select {
					id = R.id.slTheme
					adapter = SelectAdapter(this, ctx, SelectPopup(), SelectItem(), ctx.arrayStr(R.array.themesNames).toList())
					itemClickListener = object : Select.OnSelectItemClickListener {
						override fun onItemClick(select: Select, view: View, position: Int, id: Long) {
							if(position != KEY_THEME.optInt) {
								KEY_THEME.optInt = position
								wnd.changeTheme()
							}
						}
					}
				}.lps(0, 0, coord[2], 2)
				select {
					id = R.id.slPack
					adapter = SelectAdapter(this, ctx, SelectPopup(), SelectItem(), Pack.listOf(Pack.name, Pack.name) + "...")
					itemClickListener = this@FormOptions
				}.lps(x, coord[3])
				
				seek(1..19, true) { id = R.id.skMus; setOnClickListener(this@FormOptions) }.lps(0, coord[8])
				seek(1..19, true) { id = R.id.skSnd; setOnClickListener(this@FormOptions) }.lps(x, coord[9])
				seek(1..29, true) { id = R.id.skScl }.lps(0, coord[10])
				seek(1..29, true) { id = R.id.skSpd }.lps(x, coord[11])
				
				switch(R.string.options_music) { id = R.id.tgMus; setOnClickListener(this@FormOptions) }.lps(0, coord[4])
				switch(R.string.options_sound) { id = R.id.tgSnd; setOnClickListener(this@FormOptions) }.lps(x, coord[5])
				switch(R.string.options_scale) { id = R.id.tgScl; setOnClickListener(this@FormOptions) }.lps(0, coord[6])
				switch(R.string.options_speed) { id = R.id.tgSpd; setOnClickListener(this@FormOptions) }.lps(x, coord[7])
				
				check(R.string.options_classic) { id = R.id.ckCls }.lps(0, y, dx, 2)
				check(R.string.options_master) { id = R.id.ckMst }.lps(dx, y)
				if(KEY_AUTHOR_COUNT.optInt > 10) check(R.string.options_author) { id = R.id.ckAth }.lps(dx + dx, y)
				
				formFooter(BTN_OK, R.integer.I_SAVE_OPTIONS, BTN_DEF, R.integer.I_DEFAULT_OPTIONS, BTN_NO, R.integer.I_NO)
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		setUI()
	}
	
	override fun onClick(v: View) {
		when(v) {
			is Switch   -> {
				root?.indexOfChild(v)?.apply {
					val s = root.byIdx<Seek>(this - 4); val b = v.isChecked
					s.isEnabled = b
					if(this == 7) { if(b) Sound.playMusic(wnd, 0, true, s.progress / 20f) else Sound.stopMusic() }
				}
			}
			is Seek     -> {
				val vol = v.progress / 20f
				val idx = root?.indexOfChild(v) ?: -1
				when(idx) {
					3       -> Sound.playMusic(wnd, 0, true, vol)
					4       -> Sound.playSound(SND_VOLUME, vol)
				}
			}
			else        -> {
				when(v.id) {
					BTN_OK  -> {
						// пакет
						val tmp = root.byIdx<Select>(2).selectionString
						if(tmp != "...") KEY_PACK.optText = tmp
						// тема
						val updateTheme = (root.byIdx<Check>(11).isChecked != KEY_CLASSIC.optBool)
						// флажки
						for(i in 0..1) keysCheck[i].optBool = root.byIdx<Check>(i + 11).isChecked
						// Seek/Toggle
						for(i in 0..3) { keysToggle[i].optBool = root.byIdx<Switch>(i + 7).isChecked; keysSeek[i].optInt = root.byIdx<Seek>(i + 3).progress }
						if(updateTheme) wnd.changeTheme()
					}
					BTN_DEF -> {
						KEY_TMP_THEME.optInt = KEY_THEME.optInt
						Settings.default()
						// переустановить значения UI
						setUI()
						if(KEY_THEME.optInt != KEY_TMP_THEME.optInt) {
							KEY_TMP_THEME.optInt = KEY_THEME.optInt
							wnd.changeTheme()
						}
						return
					}
					BTN_NO  -> {
						if(KEY_THEME.optInt != KEY_TMP_THEME.optInt) {
							KEY_THEME.optInt = KEY_TMP_THEME.optInt
							wnd.changeTheme()
						}
					}
				}
				footer(v.id, 0)
			}
		}
	}
	
	private fun setUI() {
		for(i in 0..3) {
			root.byIdx<Seek>(i + 3).progress = keysSeek[i].optInt
		}
		for(i in 0..3) {
			root.byIdx<Switch>(i + 7).isChecked = keysToggle[i].optBool
		}
		for(i in 0..1) root.byIdx<Check>(i + 11).isChecked = keysCheck[i].optBool
		root.byIdx<Select>(1).selection = KEY_THEME.optInt
		root.byIdx<Select>(2).selectionString = KEY_TMP_PACK.optText
		// громкость
		DroidWnd.soundVolume()
	}
}