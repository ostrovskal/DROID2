@file:Suppress("DEPRECATION")

package ru.ostrovskal.droid

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.AbsoluteLayout
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.Wnd
import com.github.ostrovskal.ssh.singleton.Settings
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.sql.SQL
import com.github.ostrovskal.ssh.ui.UiComponent
import com.github.ostrovskal.ssh.ui.UiCtx
import com.github.ostrovskal.ssh.ui.absoluteLayout
import com.github.ostrovskal.ssh.ui.setContent
import com.github.ostrovskal.ssh.utils.*
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.tables.Record
import ru.ostrovskal.droid.tables.Stat

val Int.msg get() = when(this) {
	ACTION_FINISH   -> "ACTION_FINISH"
	ACTION_PACK     -> "ACTION_PACK"
	ACTION_NAME     -> "ACTION_NAME"
	ACTION_LOAD     -> "ACTION_LOAD"
	ACTION_SAVE     -> "ACTION_SAVE"
	ACTION_DELETE   -> "ACTION_DELETE"
	ACTION_NEW      -> "ACTION_NEW"
	ACTION_GENERATE -> "ACTION_GENERATE"
	ACTION_EXIT     -> "ACTION_EXIT"
	ACTION_THEME    -> "ACTION_THEME"
	ACTION_UPDATE   -> "ACTION_UPDATE"
	STATUS_INIT     -> "STATUS_INIT"
	STATUS_DEAD     -> "STATUS_DEAD"
	STATUS_CLEARED  -> "STATUS_CLEARED"
	STATUS_WORK     -> "STATUS_WORK"
	STATUS_LOOP     -> "STATUS_LOOP"
	STATUS_MESSAGE  -> "STATUS_MESSAGE"
	STATUS_EXIT     -> "STATUS_EXIT"
	STATUS_SUICIDED -> "STATUS_SUICIDED"
	STATUS_UNK      -> "STATUS_UNK"
	MSG_SERVICE     -> "MSG_SERVICE"
	MSG_FORM        -> "MSG_FORM"
	else            -> this.toString()
}

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
	override fun applyTheme() {
		val theme = if(KEY_THEME.optInt == 0) themeDark else themeLight
		theme[theme.size - 1] = if(KEY_CLASSIC.optBool) R.drawable.classic_sprites else R.drawable.custom_sprites
		Theme.setTheme(this, theme)
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
	
	override fun initialize(restart: Boolean) {
		if(wndHandler == null) {
			// Создаем UI хэндлер
			wndHandler = Handler(Looper.getMainLooper(), this)
			// Инициализируем установки
			Settings.initialize(getSharedPreferences(logTag,  Context.MODE_PRIVATE), arrayStr(R.array.settings))
			// Применяем тему и устанавливаем массивы
			applyTheme()
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
/*
				val rnd = Random(System.currentTimeMillis())
				repeat(10) {num ->
					Stat.insert {
						it[Stat.planet] = "Планета $num"
						it[Stat.auth] = "Шаталов С.В."
						it[Stat.pack] = SYSTEM_DEFAULT
						it[Stat.date] = System.currentTimeMillis()
						it[Stat.time] = rnd.nextLong(100000)
						it[Stat.fuel] = rnd.nextLong(10000)
						it[Stat.score] = rnd.nextLong(5000)
						it[Stat.count] = rnd.nextLong(30)
						it[Stat.bomb] = rnd.nextLong(100)
						it[Stat.yellow] = rnd.nextLong(100)
						it[Stat.red] = rnd.nextLong(100)
						it[Stat.green] = rnd.nextLong(100)
						it[Stat.death] = rnd.nextLong(10)
						it[Stat.egg] = rnd.nextLong(100)
						it[Stat.cycles] = rnd.nextLong(10000)
						it[Stat.master] = 0
						it[Stat.god] = 0
					}
				}
*/
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