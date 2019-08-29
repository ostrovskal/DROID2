@file:Suppress("DEPRECATION")

package ru.ostrovskal.droid.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Message
import android.view.SurfaceHolder
import android.view.View
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.STORAGE
import com.github.ostrovskal.ssh.StylesAndAttrs.style_seek
import com.github.ostrovskal.ssh.Touch
import com.github.ostrovskal.ssh.layouts.AbsLayout
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Controller
import com.github.ostrovskal.ssh.widgets.Seek
import com.github.ostrovskal.ssh.widgets.Text
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.forms.FormGame
import ru.ostrovskal.droid.msg
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.tables.Planet.rnd
import ru.ostrovskal.droid.tables.Stat
import ru.ostrovskal.droid.tables.Stat.god
import java.util.*

class ViewGame(context: Context) : ViewCommon(context) {
	
	// Задержка обработки всех объектов, кроме дроида
	private var delayEntity         = 0
	
	// признак запуска в режиме теста
	@STORAGE @JvmField var isTest	= false

	// Главная разметка
	private val main                = wnd.main
	
	// курсор
	private val cursor				= Controller(context, wnd.main, R.id.controller, false, style_droidController).apply {
		setControllerMap(droidController)
		wnd.main.addView(this)
	}
	
	// слайдер скорости
	private val speed				= Seek(context, style_seek)
	
	// координаты дроида на экране
	private val droid 				= PointF()
	
	// параметры планеты
	@STORAGE @JvmField var params 	= IntArray(PARAMS_COUNT)
	
	// Кэш параметров планеты
	private var paramsCache 		= IntArray(PARAMS_COUNT)
	
	// буфер для форматирования счетчиков
	private val strBuffer 			= StringBuilder()
	
	init {
		main.setOnTouchListener { _, event ->
			Touch.onTouch(event)
			Touch.findTouch(0)?.apply {
				cursor.visibility = View.VISIBLE
				cursor.position = p1.toInt(iPt)
			}
			true
		}
	}

	external fun processBuffer(delay: Boolean): Boolean
	external fun initBuffer(buffer: ByteArray, params: IntArray)
	external fun releaseBuffer()

	// обновление счетчиков в основном потоке
	private val updatePanel = Runnable {
		var idx = 0
		wnd.findForm<FormGame>("game")?.root?.loopChildren {
			if(idx test 1) {
				val index = idx / 2
				val value = params[index]
				if(value != paramsCache[index]) {
					paramsCache[index] = value
					(it as? Text)?.text = strBuffer.padZero(value, formatLengths[index], false)
				}
			}
			idx++
		}
	}
	
	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		super.surfaceChanged(holder, format, width, height)
		if(record == 0L) {
			// Добавить контроллер
			main.removeView(cursor)
			main.addView(cursor)
		} else {
			// добавить слайдер скорости
			speed.apply {
				main.addView(this, AbsLayout.LayoutParams(width - 10.dp, 40.dp, 10.dp, height - 45.dp))
				isEnabled = true
				alpha = 0.65f
				range = 0..30
				progress = KEY_SPEED_VOLUME.optInt
				setOnClickListener {
					var v = 3f - progress / 10f
					if(v < 0.1f) v = 0.1f
					delay = (70 * v).toLong()
				}
			}
		}
		// счетчики
		Arrays.fill(paramsCache, -1)
	}
	
	override fun handleMessage(msg: Message): Boolean {
		"onMessageViewGame(what: ${msg.what.msg} arg1: ${msg.arg1.msg} arg2: ${msg.arg2} obj: ${msg.obj})".debug()
		if(super.handleMessage(msg)) {
			val s = surHandler
			if(s != null) {
				msg.apply {
					when(what) {
						MSG_SERVICE      -> {
							if(arg1 == ACTION_LOAD) {
								params[PARAM_FUEL]	+= Planet.fuel
								params[PARAM_TIME]	+= Planet.time
								if(record == 0L) {
									params[PARAM_YELLOW] = Planet.yellow
									params[PARAM_RED] = Planet.red
									params[PARAM_GREEN] = Planet.green
									params[PARAM_EGG] = Planet.egg
									post(updatePanel)
								}
							}
						}
						STATUS_INIT 	-> {
							Stat.init(true, params[PARAM_DROID_MASTER] != 0, params[PARAM_DROID_GOD] != 0, record)
							Arrays.fill(params, 0)
							params[PARAM_LIFE]	= if(isTest) 1 else DROID_LIFE
							params[PARAM_DROID_CLASSIC]	= KEY_CLASSIC.optBool.toInt()
							params[PARAM_LIMIT]	= if(params[PARAM_DROID_MASTER] != 0) DROID_ADD_LIMIT * 2 else DROID_ADD_LIMIT
							status = STATUS_DEAD
							s.send(STATUS_DEAD, a2 = 1)
							testDroid()
						}
						STATUS_DEAD     -> {
							params[PARAM_BOMB]	= if(params[PARAM_DROID_MASTER] != 0) DROID_ADD_BOMB / 2 else DROID_ADD_BOMB
							params[PARAM_FUEL]	= 0
							params[PARAM_TIME]	= 0
							status = STATUS_CLEARED
							s.send(STATUS_CLEARED, a1 = position, a2 = arg2)
						}
						STATUS_CLEARED  -> {
							status = STATUS_PREPARED
							s.send(MSG_SERVICE, 0, ACTION_LOAD, arg1)
							if(params[PARAM_TIME] != 0 || arg2 == 1) Sound.playRandomMusic(wnd, true)
						}
						STATUS_PREPARED -> {
							initBuffer(buffer, params)
							if(record != 0L) {
								params[PARAM_DROID_MASTER] = Stat.master.toInt()
								params[PARAM_DROID_GOD] = god.toInt()
							} else {
								params[PARAM_DROID_MASTER] = KEY_MASTER.optBool.toInt()
								params[PARAM_DROID_GOD] = DroidWnd.isAuthor().toInt()
							}
							newStatus = 0
							idMsg = 0
							sysMsg = ""
							delayEntity = 0
							Stat.init(false, params[PARAM_DROID_MASTER] != 0, params[PARAM_DROID_GOD] != 0, record)
							rnd.startSeed = Stat.date
							status = STATUS_LOOP
							cursor.reset()
						}
						STATUS_SUICIDED -> {
							if(status == STATUS_LOOP) {
								// добавить суицид в запись
								Stat.add(1, 0, 0, false, suicide = true)
								Stat.death++
								newStatus = restart()
								status = STATUS_DEAD
								idMsg = if(newStatus == STATUS_EXIT) R.string.msg_game_over else R.string.msg_droid_suicyded
								surHandler?.send(STATUS_MESSAGE, 0, newStatus, nArg, idMsg)
							}
						}
					}
				}
			}
		}
		return true
	}
	
	fun checkStates() {
		if(newStatus == 0) {
			var dead = params[PARAM_IS_DROID] == 0
			if(dead) {
				idMsg = if(params[PARAM_LIFE] == 1) R.string.msg_game_over else R.string.msg_droid_destroyed
				Sound.playSound(SND_DROID_DEATH)
				// подсчитываем сколько раз умирал
				Stat.death++
			}
			else if(params[PARAM_FUEL] == 0) { idMsg = R.string.msg_out_of_fuel; dead = true }
			else if(params[PARAM_TIME] == 0) { idMsg = R.string.msg_out_of_time; dead = true }
			else if(params[PARAM_RED1] == 0 && params[PARAM_EGG1] == 0 && params[PARAM_YELLOW1] == 0 && params[PARAM_GREEN1] == 0) {
				idMsg = R.string.msg_planet_cleaned
				nArg = ++position
				newStatus = STATUS_CLEARED
				// подсчитываем сколько уровней прошел
				Stat.count++
				if(record == 0L && !isTest) {
					// разблокировать планету
					Planet.update {
						it[Planet.blocked] = 0L
						where { Planet.system.eq(Planet.pack) and Planet.position.eq(Planet.num) }
					}
				}
			}
			else {
				params[PARAM_TIME]--
				// подсчитываем сколько игровых циклов потратил
				Stat.cycles++
				if(params[PARAM_TIME] < 200 && params[PARAM_TIME] % 10 == 0) Sound.playSound(SND_TIME_ELAPSED)
			}
			if(idMsg != 0) {
				if(isTest) {
					idMsg = R.string.msg_test_complete
					newStatus = STATUS_EXIT
				} else {
					if(dead) newStatus = restart()
				}
				surHandler?.send(STATUS_MESSAGE, MESSAGE_DELAYED, newStatus, nArg, idMsg)
			}
		}
	}
	
	private fun restart(): Int {
		return if(params[PARAM_LIFE] <= 1) {
			Stat.egg = params[PARAM_STAT_EGG]
			Stat.score = params[PARAM_STAT_SCORE]
			Stat.red = params[PARAM_STAT_RED]
			Stat.green = params[PARAM_STAT_GREEN]
			Stat.yellow = params[PARAM_STAT_YELLOW]
			Stat.save(rnd.startSeed); STATUS_EXIT
		} else {
			params[PARAM_LIFE]--
			STATUS_DEAD
		}
	}

	fun testDroid() {
	}

	override fun draw(canvas: Canvas) {
		super.draw(canvas)
		// обновляем счетчики
		post(updatePanel)
		// обработка карты
		if(status == STATUS_LOOP) {
			delayEntity++
			// подсчитываем время затраченное на игру
			val tm = System.currentTimeMillis()
			Stat.time += tm - Stat.startTime
			Stat.startTime = tm
			// сбрасываем временные счетчики
			val procAll = delayEntity ntest 1
			val pt = Planet.droidPos()
			val xd = pt.x
			val yd = pt.y
			droid.set(canvasOffset.x + (xd - mapOffset.x) * tileCanvasSize + tileCanvasSize / 2f,
					canvasOffset.y + (yd - mapOffset.y) * tileCanvasSize + tileCanvasSize / 2f)
			params[PARAM_DROID_BUT] = if(record != 0L && newStatus == 0) {
				val dir = Stat.control(params[PARAM_TIME])
				if(dir == DIRS) {
					surHandler?.send(STATUS_SUICIDED)
					DIRN
				} else dir
			} else cursor.buttonStates()
			if(processBuffer(procAll)) {
				Planet.droidPos(params[PARAM_DROID_X], params[PARAM_DROID_Y])
				prepareMap(false)
				// подсчитываем сколько топлива потратил
				Stat.fuel++
			}
			val dp = Planet.droidPos()
			repeat(params[PARAM_SOUND_STONE]) {
				Sound.playSound(SND_STONE_COLISSION, dp, dp)
			}
			repeat(params[PARAM_SOUND_EXPL]) {
				Sound.playSound(SND_EXPLOSIVE, dp, dp)
			}
			if(params[PARAM_SOUND_TAKE] != 0) Sound.playSound(params[PARAM_SOUND_TAKE], dp, dp)
			if(newStatus == 0) Stat.add(params[PARAM_TIME], xd, yd, params[PARAM_DROID_BOMB] != 0)
			if(params[PARAM_DROID_BOMB] != 0) {
				Sound.playSound(SND_DROID_DROP_BOMB, dp, dp)
				// подсчитываем сколько бомб потратил
                Stat.bomb++
			}
			if(procAll) {
				checkStates()
				params[PARAM_RED] = params[PARAM_RED1]
				params[PARAM_GREEN] = params[PARAM_GREEN1]
				params[PARAM_YELLOW] = params[PARAM_YELLOW1]
				params[PARAM_EGG] = params[PARAM_EGG1]
			}
		}
	}

	override fun onDetachedFromWindow() {
		releaseBuffer()
		main.removeView(cursor)
		main.removeView(speed)
		super.onDetachedFromWindow()
	}
	
	/** При активации формы финиша игры - убрать лишние элементы */
	fun finishForm() {
		Stat.egg = params[PARAM_STAT_EGG]
		Stat.score = params[PARAM_STAT_SCORE]
		Stat.red = params[PARAM_STAT_RED]
		Stat.green = params[PARAM_STAT_GREEN]
		Stat.yellow = params[PARAM_STAT_YELLOW]
		Stat.save(rnd.startSeed)
		main.removeView(speed)
		main.removeView(cursor)
		wnd.instanceForm(FORM_FINISH)
	}
}
