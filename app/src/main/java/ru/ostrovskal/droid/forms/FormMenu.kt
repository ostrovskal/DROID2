package ru.ostrovskal.droid.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ostrovskal.ssh.*
import com.github.ostrovskal.ssh.StylesAndAttrs.style_menu
import com.github.ostrovskal.ssh.StylesAndAttrs.style_tool
import com.github.ostrovskal.ssh.singleton.Settings
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.ui.UI
import com.github.ostrovskal.ssh.ui.UiCtx
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R.integer.*
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.tables.Stat

class FormMenu: Form() {
	private val coordPort   = intArrayOf(6, 3, 0, 8, 0, 12, 12, 8, 0, 24, 12, 12, 12, 24)
	private val coordLand   = intArrayOf(12, 1, 3, 5, 3, 9, 21, 5, 0, 14, 21, 9, 24, 14)
	private val btnIcons    = intArrayOf(I_GAME, I_SELECT_PLANET, I_OPTIONS, I_RECORD, I_EDITOR, I_HELP, I_EXIT)
	private val buttons     = mutableListOf<Tile>()
	
	override fun inflateContent(container: LayoutInflater) = UI {
		if(content != null) UiCtx.addView(this, content as View)
		else {
			val coord = if(config.isPortrait) coordPort else coordLand
			cellLayout(if(config.isPortrait) 16 else 28, if(config.isPortrait) 28 else 18) {
				backgroundSet(style_menu)
				repeat(7) {
					buttons.add(button(style_tool) {
						setOnClickListener(this@FormMenu)
						iconResource = btnIcons[it]
					}.lps(coord[it * 2], coord[it * 2 + 1], 4, 4))
				}
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		// при первом запуске - запускать анимацию справки
		if(arguments.getBoolean("first")) {
			arguments.putBoolean("first", false)
			shake?.repeatCount = 5
			buttons[5].startAnimation(shake)
		}
		// кнопка редактора
		//buttons[4].visibility = if(MainWnd.isAuthor()) View.VISIBLE else View.GONE
		// кнопка выбор планеты
		buttons[1].isEnabled = Planet.exist { Planet.blocked eq 1 }
		// определить кол-во статистики и если необходимо удалить старую
		val count = Stat.count()
		if(count > LIMIT_RECORDS) {
			Stat.select(Planet.create) {
				orderBy(Planet.create, false)
				limit(1, 50)
			}.execute()?.release { Stat.delete { where { Stat.date less this@release[Stat.date] } } }
		}
		// разблокировать статистику
		buttons[3].isEnabled = count > 0
		// устанавливаем громкость
		DroidWnd.soundVolume()
		// запускаем музыку в заставке
		Sound.playMusic(wnd, 0, true)
	}
	
	override fun onClick(v: View) {
		val idx = buttons.indexOf(v)
		var position = 0L
		if(idx == FORM_GAME) {
			val pack = KEY_PACK.optText
			Settings[KEY_TMP_PACK] = pack
			sql {
				Planet.select(Planet.position) {
					where { (Planet.system eq pack) and (Planet.blocked eq 1) }
					orderBy(Planet.position)
					limit(1)
				}.execute()?.apply { position = this[Planet.position] }
				if(position >= Planet.count { Planet.system eq pack }) position = 0
			}
		}
		wnd.instanceForm(idx, "position", position)
	}
}
