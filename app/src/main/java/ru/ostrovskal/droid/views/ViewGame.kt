@file:Suppress("DEPRECATION")

package ru.ostrovskal.droid.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import android.os.Message
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.widget.AbsoluteLayout
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
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.forms.FormGame
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.tables.Stat
import ru.ostrovskal.droid.tables.Stat.god
import java.util.*

class ViewGame(context: Context) : ViewCommon(context) {
	// признак запуска в режиме теста
	@STORAGE @JvmField var test		= false

	// Главная разметка
	private val main                = wnd.main
	
	// курсор
	private val cursor				= Controller(context, wnd.main)
	
	// слайдер скорости
	private val speed				= Seek(context, style_seek)
	
	// признак автора
	private var isGod				= DroidWnd.isAuthor()
	
	// признак опытного игрока
	private var isMaster			= KEY_MASTER.optBool
	
	// координаты дроида на экране
	private val droid 				= PointF()
	
	// тип текущего тайла
	private var o 					= 0
	
	// текущий тайл
	private var t 					= 0
	
	// текущие координаты обработки
	private var x 					= 0
	private var y 					= 0
	
	// признак броска бомбы
	private var isDropBomb 			= false
	
	// признак дроида
	private var isDroid 			= false
	
	// режим паузы
	@STORAGE @JvmField var isPause	= false
	
	// параметры планеты
	@STORAGE @JvmField var params 	= IntArray(PARAMS_COUNT)
	
	// Кэш параметров планеты
	private var paramsCache 		= IntArray(PARAMS_COUNT)
	
	// буфер для форматирования счетчиков
	private val strBuffer 			= StringBuilder()
	
	init {
		main.layoutTouchListener = object : AbsLayout.OnAbsLayoutTouched {
			override fun onTouch(event: MotionEvent) = if(record == 0L) {
				Touch.onTouch(event)
				Touch.findTouch(0)?.apply { cursor.position = p1.toInt(iPt) }
				true
			} else false
		}
	}

	// обновление счетчиков в основном потоке
	private val updatePanel = Runnable {
		var idx = 0
		wnd.findForm<FormGame>("game")?.root?.loopChildren {
			if(it !is Tile) {
				val value = params[idx]
				if(value != paramsCache[idx]) {
					paramsCache[idx] = value
					strBuffer.zero(value, formatLengths[idx])
					(it as? Text)?.text = strBuffer
				}
				idx++
			}
		}
	}
	
	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
	{
		super.surfaceChanged(holder, format, width, height)
		if(record == 0L) {
		} else {
			// добавить слайдер скорости
			speed.apply {
				main.addView(this, AbsoluteLayout.LayoutParams(width - 40.dp, 40.dp, 20.dp, height - 40.dp))
				alpha = 0.65f
				range = 0..30
				progress = KEY_SPEED_VOLUME.optInt * 10
				isEnabled = true
				setOnClickListener {
					var v = 3f - progress / 10f
					if(v < 0.1f) v = 0.1f
					delay = (70 * v).toLong()
				}
			}
		}
		// счетчики
		val form = wnd.findForm<FormGame>("game")
		if(form != null) {
			form.namePlanet?.setOnClickListener {
				isPause = !isPause
				sysMsg = if(isPause) {
					Sound.pauseAll()
					resources.getString(R.string.msg_paused)
				} else {
					Sound.resumeAll()
					""
				}
			}
		}
		Arrays.fill(paramsCache, -1)
	}
	
	override fun handleMessage(msg: Message): Boolean
	{
		if(super.handleMessage(msg)) {
			val s = surHandler
			if(s != null) {
				msg.apply {
					when(what) {
						STATUS_INIT 	-> {
							Stat.init(true, isMaster, isGod, record)
							params[PARAM_LIFE]	= if(test) 1 else DROID_LIFE
							params[PARAM_SCORE]	= 0
							params[PARAM_LIMIT]	= if(isMaster) DROID_ADD_LIMIT * 2 else DROID_ADD_LIMIT
							status = STATUS_DEAD
							s.send(STATUS_DEAD, a2 = 1)
						}
						STATUS_SUICIDED	-> {
							if(status == STATUS_LOOP) {
								if(test) {
									idMsg = R.string.msg_test_complete
									newStatus = STATUS_EXIT
								}
								else {
									if(record != 0L && arg1 != 0) {
										wnd.wndHandler?.send(MSG_FORM, a1 = ACTION_EXIT)
										return true
									}
									else {
										if(record == 0L) {
											// добавить суицид в запись
											Stat.add(1, 0, 0, false, true)
											Stat.death++
										}
										newStatus = restart()
										status = STATUS_DEAD
										idMsg = if(newStatus == STATUS_EXIT) R.string.msg_game_over else R.string.msg_droid_suicyded
									}
								}
								surHandler?.send(STATUS_MESSAGE, 0, newStatus, nArg, idMsg)
							}
						}
						STATUS_DEAD		-> {
							params[PARAM_BOMB]	= if(isMaster) DROID_ADD_BOMB / 2 else DROID_ADD_BOMB
							params[PARAM_FUEL]	= 0
							params[PARAM_TIME]	= 0
							status = STATUS_CLEARED
							s.send(STATUS_CLEARED, a1 = position, a2 = arg2)
						}
						STATUS_CLEARED	-> {
							status = STATUS_WORK
							s.send(MSG_SERVICE, 0, ACTION_LOAD, arg1)
							if(params[PARAM_TIME] != 0 || arg2 == 1) Sound.playRandomMusic(wnd, true)
						}
						STATUS_WORK		-> {
							params[PARAM_FUEL]	+= Planet.MAP.fuel
							params[PARAM_TIME]	+= Planet.MAP.time
							if(record != 0L) {
								isMaster= Stat.master
								isGod 	= Stat.god
							}
							newStatus = 0
							idMsg = 0
							sysMsg = ""
							Stat.init(false, isMaster, isGod, record)
							rnd = Random(Stat.random)
							status = STATUS_LOOP
							cursor.reset()
						}
					}
				}
			}
		}
		return true
	}
	
	private fun checkStates()
	{
		if(newStatus == 0) {
			var dead = !isDroid
			if(!isDroid) {
				idMsg = if(params[PARAM_LIFE] == 1) R.string.msg_game_over else R.string.msg_droid_destroyed
				Sound.playSound(SND_DROID_DEATH)
				// подсчитываем сколько раз умирал
				Stat.death++
			}
			else if(params[PARAM_FUEL] == 0) { idMsg = R.string.msg_out_of_fuel; dead = true }
			else if(params[PARAM_TIME] == 0) { idMsg = R.string.msg_out_of_time; dead = true }
			else if(params[PARAM_RED1] == 0 && params[PARAM_EGG1] == 0 && params[PARAM_YELLOW1] == 0 && params[PARAM_GREEN1] == 0) {
				idMsg = R.string.msg_planet_cleaned
				"fuel:${Planet.MAP.fuel - params[PARAM_FUEL]} time: ${Planet.MAP.time - params[PARAM_TIME]}".info()
				nArg = ++position
				newStatus = STATUS_CLEARED
				// подсчитываем сколько уровней прошел
				Stat.count++
				if(record == 0L && !test) {
					// разблокировать планету
					Planet.update {
						it[Planet.blocked] = 0L
						where { Planet.system.eq(Planet.MAP.pack) and Planet.position.eq(Planet.MAP.num) }
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
				if(test) {
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
			Stat.save(); STATUS_EXIT
		} else {
			params[PARAM_LIFE]--
			STATUS_DEAD
		}
	}
	
	override fun draw(canvas: Canvas)
	{
		// обработчики объектов
		fun procEye()
		{
			val limit = if(t == T_EYEB.toInt()) 4 else 2
			val r = rnd.nextInt(countMapCells - params[PARAM_EYE])
			if(r in 1..limit) {
				val xx = x + offsEye[rnd.nextInt(3)]
				val yy = y + offsEye[rnd.nextInt(3)]
				if(isProp(xx, yy, FN or FG)) setToMap(xx, yy, if(isProp(xx, yy, FT)) T_EXPL0.toInt() else t)
			}
			params[PARAM_EYE]++
		}
		
		fun procYellow()
		{
			val s = (t - T_YELLOWD) * 6
			var xx = x + offsYellow[s + 1]
			var yy = y + offsYellow[s]
			var tt = T_NULL
			// смотрим вбок - пусто - поворачиваем
			if(isProp(xx, yy, FN)) setToMap(xx, yy, offsYellow[s + 2].toInt())
			// проверяем вбок на гибель
			else if(!checkKill(xx, yy, FY or FK)) {
				xx = x + offsYellow[s + 4]
				yy = y + offsYellow[s + 3]
				// смотрим по направлению движения
				if(isProp(xx, yy, FN)) setToMap(xx, yy, t)
				// проверяем по направлению движения на гибель
				else if(!checkKill(xx, yy, FY or FK)) tt = offsYellow[s + 5]
			}
			buffer[x, y] = tt
			params[PARAM_YELLOW1]++
		}
		
		fun procRG()
		{
			val tmp = (t - if(o == O_RED) T_REDD else T_GREEND) * 3
			val xx = x + offsRG[tmp + 1]
			val yy = y + offsRG[tmp]
			var tt = T_NULL
			if(isProp(xx, yy, FN)) setToMap(xx, yy, t)
			else if(!checkKill(xx, yy, FK)) tt = offsRG[tmp + 2]
			buffer[x, y] = tt
			params[PARAM_RED1 + (o - O_RED)]++
		}
		
		fun procExplEgg()
		{
			for(i in 0..8) {
				val xx = x + offsExplo[i * 2 + 1]
				val yy = y + offsExplo[i * 2]
				o = remapProp[buffer[xx, yy] and MSKT]
				if(o flags (FN or FG) || o == O_EXPLEGG) setToMap(xx, yy, T_EGG0.toInt())
			}
			Sound.playSound(SND_EXPLOSIVE, Point(x, y), Planet.MAP.droidPos())
		}
		
		fun procExpl()
		{
			val isExplDroid = t == T_EXPLDROID0.toInt()
			if(t == T_EXPL0.toInt() || isExplDroid) {
				for(i in 0..8) {
					val yy = y + offsExplo[i * 2]
					val xx = x + offsExplo[i * 2 + 1]
					o = remapProp[buffer[xx, yy] and MSKT]
					val oo = o and MSKO
					if(oo == O_BETON) continue
					else if(oo == O_DROID) {
						if(god) continue
						isDroid = false
					}
					if(isExplDroid) addScore(oo, true)
					setToMap(xx, yy, if(o flags FE) t else T_EXPL1.toInt())
				}
				Sound.playSound(SND_EXPLOSIVE, Point(x, y), Planet.MAP.droidPos())
			}
			else buffer[x, y] = if(t == T_EXPL3.toInt()) T_NULL.toInt() else t + 1
		}
		
		fun procEgg()
		{
			var make = false
			// запускать ли процесс дрожания яйца?
			if((t == T_EGG0.toInt() || t == T_EGG0 + T_DROP) && rnd.nextInt(countMapCells - params[PARAM_EGG1]) != 1) t--
			// вылупиться ? только для 3 яйца
			if(t == T_EGG3.toInt() || t == T_EGG3 + T_DROP) {
				t -= 3
				make = rnd.nextInt(countMapCells - params[PARAM_EGG1]) < 5
				if(make) {
					t = T_REDD + rnd.nextInt(11)
					if(t >= T_YELLOWU) t += 14
				}
			}
			t++
			buffer[x, y] = t
			if(!make) {
				params[PARAM_EGG1]++
				moveDrop()
			}
		}
		
		fun procDrop() {
			val expl = if(o == O_BOMBDROID) T_EXPLDROID0 else T_EXPL0
			if(moveDrop() && (o == O_BOMB || o == O_BOMBDROID)) setToMap(x, y, expl.toInt())
		}
		
		fun droidControl(dir: Int) {
			if(dir == DIR0) {
				if(params[PARAM_BOMB] > 0) {
					// кидаем бомбу
					val xx = x + offsBombPos[(t - T_DROIDD) * 2]
					val yy = y + offsBombPos[(t - T_DROIDD) * 2 + 1]
					if(isProp(xx, yy, FG)) {
						isDropBomb = true
						setToMap(xx, yy, T_BOMBDROID.toInt())
						params[PARAM_BOMB]--
						Sound.playSound(SND_DROID_DROP_BOMB, Point(x, y), Planet.MAP.droidPos())
						// подсчитываем сколько бомб потратил
						Stat.bomb++
					}
				}
			}
			else if(dir != DIRN) {
				val xx = x + offsDroid[dir * 3]
				val yy = y + offsDroid[dir * 3 + 1]
				val tt = offsDroid[dir * 3 + 2]
				// движемся
				val pe = remapProp[buffer[xx, yy] and MSKT]
				o = pe and MSKO
				// гибель
				if(pe and FM == FM && !god) setToMap(xx, yy, T_EXPL0.toInt())
				else {
					var isTmp = (pe and (FN or FG or FT)) != 0
					if(isTmp) {
						// особые случаи
						when(o) {
							O_SCORE -> params[PARAM_SCORE] += DROID_ADD_SCORE
							O_TIME  -> params[PARAM_TIME] += DROID_ADD_TIME
							O_LIFE  -> params[PARAM_LIFE]++
							O_FUEL  -> params[PARAM_FUEL] += DROID_ADD_FUEL
							O_BOMBS -> params[PARAM_BOMB] += if(isMaster) DROID_ADD_BOMB / 2 else DROID_ADD_BOMB
						}
						if(pe and FT != 0) {
							Sound.playSound(if(o == O_EGG) SND_DROID_TAKE_EGG else SND_DROID_TAKE_MISC, Point(x, y), Planet.MAP.droidPos())
							addScore(o, false)
							// подсчитываем сколько яиц собрал
							if(o == O_EGG) Stat.egg++
						}
					}
					else {
						val nn = buffer[xx, yy].toByte()
						when(nn) {
							T_STONE0, T_STONE1, T_STONE2, T_STONE3,
							T_BOMB, T_BOMBDROID -> {
								// сдвигаем камень/бомбу
								val dx = when(tt) {
									T_DROIDL -> -1
									T_DROIDR -> 1
									else     -> 0
								}
								if(isProp(xx + dx, yy, FN)) {
									buffer[xx + dx, yy] = nn
									isTmp = true
								}
							}
						}
					}
					if(isTmp) {
						buffer[x, y] = T_NULL
						setToMap(xx, yy, tt.toInt())
						Planet.MAP.droidPos(xx, yy)
						params[PARAM_FUEL]--
						initMap(false)
						// подсчитываем сколько топлива потратил
						Stat.fuel++
					}
				}
			}
		}
		
		fun procDroid() {
			isDroid = true
			isDropBomb = false
			val oldX = x
			val oldY = y
			if(params[PARAM_FUEL] > 0) {
				val pt = Planet.MAP.droidPos()
				droid.set(canvasOffset.x + (pt.x - mapOffset.x) * tileCanvasSize + tileCanvasSize / 2f,
						  canvasOffset.y + (pt.y - mapOffset.y) * tileCanvasSize + tileCanvasSize / 2f)
				if(record != 0L && newStatus == 0) {
					Stat.control(params[PARAM_TIME]) {dir, suicide -> if(suicide) surHandler?.send(STATUS_SUICIDED) else droidControl(dir) } }
				else {
					droidControl(cursor.buttonStates())
				}
			}
			if(newStatus == 0) Stat.add(params[PARAM_TIME], oldX, oldY, isDropBomb)
		}
		
		val funHandlers = arrayOf(::procDrop, ::procDrop, ::procExpl, ::procDroid, ::procRG, ::procRG,
								  ::procYellow, ::procEye, ::procEgg, ::procExplEgg, ::procDrop, ::procExpl)
		super.draw(canvas)
		// обновляем счетчики
		post(updatePanel)
		// обработка карты
		if(status == STATUS_LOOP) {
			// проверка на паузу
			if(isPause) return
			// подсчитываем время затраченное на игру
			val tm = System.currentTimeMillis()
			Stat.time += tm - Stat.startTime
			Stat.startTime = tm
			// сбрасываем временные счетчики
			isDroid = false
			params[PARAM_RED1] 		= 0
			params[PARAM_GREEN1] 	= 0
			params[PARAM_YELLOW1]	= 0
			params[PARAM_EGG1] 		= 0
			params[PARAM_EYE] 		= 0
			y = 0
			repeat(Planet.MAP.height) {
				x = 0
				repeat(Planet.MAP.width) {
					t = buffer[x, y]
					if(t flags MSKU) buffer[x, y] = t and MSKT
					else {
						o = remapProp[t] and MSKO
						if(o <= O_BOMBDROID) funHandlers[o].invoke()
					}
					x++
				}
				y++
			}
			checkStates()
			params[PARAM_RED] 	= params[PARAM_RED1]
			params[PARAM_GREEN] = params[PARAM_GREEN1]
			params[PARAM_YELLOW]= params[PARAM_YELLOW1]
			params[PARAM_EGG] 	= params[PARAM_EGG1]
		}
	}
	
	private fun addScore(obj: Int, isBomb: Boolean)
	{
		var score = numScored[obj]
		if(isBomb) score *= 2
		params[PARAM_SCORE] += score
		if(params[PARAM_SCORE] < 0) params[PARAM_SCORE] = 0
		if(params[PARAM_SCORE] >= params[PARAM_LIMIT]) {
			params[PARAM_LIMIT] += if(isMaster) DROID_ADD_LIMIT * 2 else DROID_ADD_LIMIT
			params[PARAM_LIFE]++
		}
		// подсчитываем сколько тварей убил
		if(isBomb) {
			when(obj) {
				O_RED    -> Stat.red++
				O_YELLOW -> Stat.yellow++
				O_GREEN  -> Stat.green++
			}
		}
		// подсчитываем очки
		Stat.score += score
	}
	
	private fun isProp(xx: Int, yy: Int, msk: Int): Boolean = remapProp[buffer[xx, yy] and MSKT] flags msk

	private fun setToMap(xx: Int, yy: Int, n: Int) { buffer[xx, yy] = (n or if(yy > y || yy == y && xx > x) MSKU else 0) }
	
	private fun moveDrop(): Boolean {
		if(t > T_BOMBDROID) {
			// объект уже падает
			buffer[x, y] = T_NULL
			// падать дальше?
			if(isProp(x, y + 1, FN)) setToMap(x, y + 1, t)
			else if(o == O_EGG || !checkKill(x, y + 1, FB)) {
				buffer[x, y] = t - T_DROP
				if(o == O_STONE) Sound.playSound(SND_STONE_COLISSION, Point(x, y), Planet.MAP.droidPos())
				return true
			}
		}
		else {
			val tt = t + T_DROP
			// падать?
			if(isProp(x, y + 1, FN)) {
				buffer[x, y] = T_NULL
				setToMap(x, y + 1, tt)
			}
			else {
				// соскок?
				if(isProp(x, y + 1, FS)) {
					val dx = if(isProp(x - 1, y + 1, FN) && isProp(x - 1, y, FN)) -1
					else if(isProp(x + 1, y + 1, FN) && isProp(x + 1, y, FN)) 1
					else return false
					// падаем
					buffer[x, y] = T_NULL
					setToMap(x + dx, y, tt)
				}
			}
		}
		return false
	}
	
	private fun checkKill(xx: Int, yy: Int, msk: Int): Boolean
	{
		var oo = remapProp[buffer[xx, yy] and MSKT]
		if(oo nflags msk) return false
		oo = oo and MSKO
		// проверка при признаке бога на дроида
		if(god && oo == O_DROID) return false
		// добавить очки с проверкой на бомбу дроида
		val bd = oo == O_BOMBDROID
		addScore(if(bd) o else oo, (o == O_BOMBDROID) or bd)
		// поставить взрыв/яичный взрыв
		var bt = if(bd) T_EXPLDROID0 else T_EXPL0
		when(oo) {
			O_YELLOW, O_RED, O_GREEN -> {
				if(!classic && rnd.nextInt(countMapCells - (params[PARAM_YELLOW1] + params[PARAM_RED1] +
				                                            params[PARAM_GREEN1] + 1)) < 3) bt = T_EXPLEGG
			}
		}
		setToMap(xx, yy, bt.toInt())
		return true
	}
}
