@file:Suppress("DEPRECATION")

package ru.ostrovskal.droid

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.AbsoluteLayout
import com.github.ostrovskal.ssh.*
import com.github.ostrovskal.ssh.Constants.APP_GAME
import com.github.ostrovskal.ssh.Constants.logTag
import com.github.ostrovskal.ssh.singleton.Settings
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.sql.SQL
import com.github.ostrovskal.ssh.ui.UiComponent
import com.github.ostrovskal.ssh.ui.UiCtx
import com.github.ostrovskal.ssh.ui.setContent
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.tables.Record
import ru.ostrovskal.droid.tables.Stat

class DroidWnd: Wnd() {
	lateinit var main: AbsoluteLayout

	companion object {
		/**
		 * Установка уровня звука и музыки
		 */
		fun soundVolume()
		{
			val snd = if(KEY_SOUND.optBool) KEY_SOUND_VOLUME.optInt else 0
			val mus = if(KEY_MUSIC.optBool) KEY_MUSIC_VOLUME.optInt else 0
			Sound.setVolume(snd / 20f, mus / 20f)
		}
		
		/**
		 * Проверить на режим автора
		 */
		fun isAuthor() = KEY_AUTHOR_COUNT.optInt > 10 && KEY_AUTHOR.optBool
		
		/**
		 * Применить масштаб
		 */
		fun applyScale(value: Int): Int {
			val scl = if(KEY_SCALE.optBool) KEY_SCALE_VOLUME.optInt else 15
			return (value * (scl / 15f)).toInt()
		}
		
		/**
		 * Применить скорость
		 */
		fun applySpeed(value: Int): Long {
			val spd = if(KEY_SPEED.optBool) KEY_SPEED_VOLUME.optInt else 15
			return (value * (2f - (spd / 15f))).toLong()
		}
	}
	
	// Применение темы
	private fun applyTheme() {
		val theme = if(KEY_THEME.optInt == 0) themeDark else themeLight
		theme[theme.size - 1] = if(KEY_CLASSIC.optBool) TEXT_BITMAP_CLASSIC_SPRITES else TEXT_BITMAP_CUSTOM_SPRITES
		Theme.setTheme(theme)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		startLog(this, "DROID", true, BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, BuildConfig.DEBUG)
		super.onCreate(savedInstanceState)
		Main().setContent(this, APP_GAME)
		// загружаем фрагмент
		if(savedInstanceState == null) {
			val idx = if(intent.getIntExtra("changeTheme", 0) == 0) FORM_SPLASH else FORM_MENU
			instanceForm(FORM_MENU)
		}
	}
	
	override fun initialize() {
		if(wndHandler == null) {
			// Создаем UI хэндлер
			wndHandler = Handler(Looper.getMainLooper(), this)
			// Инициализируем установки
			Settings.initialize(getSharedPreferences(logTag,  Context.MODE_PRIVATE), arrayStr(R.array.settings))
			// Применяем тему и устанавливаем массивы
			applyTheme()
			Theme.setStrings(stringsDroid)
			Theme.setDimens(dimensDroidDef, dimensDroid600sw, dimensDroid800sw)
			// Запускаем звуки
			Sound.initialize(this, 5, arrayStr(R.array.sound), arrayIDs(R.array.music))
			// Запускаем БД
			SQL.connection(this, false, Record, Pack, Stat, Planet) {
				var res = it
				if(res) res = Pack.check()
				if(!res) {
					Pack.default()
					Planet.default(this@DroidWnd)
				}
			}
		}
	}

	inner class Main : UiComponent() {
		override fun createView(ui: UiCtx) = with(ui){
			main = absoluteLayout { id = R.id.mainContainer }
			main
		}
	}
}