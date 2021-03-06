package ru.ostrovskal.droid.views

import android.content.Context
import android.graphics.*
import android.os.Message
import android.view.Gravity
import android.view.SurfaceHolder
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.STORAGE
import com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_MESSAGE
import com.github.ostrovskal.ssh.StylesAndAttrs.THEME
import com.github.ostrovskal.ssh.Surface
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.Touch
import com.github.ostrovskal.ssh.utils.*
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.DroidWnd
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.msg
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.Planet
import ru.ostrovskal.droid.tables.Stat

open class ViewCommon(context: Context) : Surface(context) {
	
	// Размер карты
	@JvmField protected var previewMap	= Size()
	
	// Размер блока
	@JvmField protected var previewBlk	= Size()
	
	// Смещение от начала карты
	@JvmField protected var previewMO	= Point()
	
	// Смещение от начала канвы
	@JvmField protected var previewCO	= Point()
	
	// признак планеты
	val isPlanet                        get() = Planet.buffer.size > 2
	
	// превью режим
	@STORAGE @JvmField var preview		= false
	
	// признак воспроизведения записи
	@STORAGE @JvmField var record		= 0L
	
	// параметры переходного статуса
	@STORAGE @JvmField var newStatus	= 0
	
	@STORAGE @JvmField var nArg 		= 0
	
	@STORAGE @JvmField var idMsg 		= 0
	
	// признак классической игры
	@JvmField protected val classic 	= KEY_CLASSIC.optBool
	
	// размер спрайта
	@JvmField var tileBitmapSize 		= 0
	
	// размер спрайта на экране
	@JvmField var tileCanvasSize 		= 0
	
	// позиция планеты
	@STORAGE @JvmField var position 	= 0
	
	// системное сообщение
	@STORAGE @JvmField var sysMsg 		= ""
	
	// статус исполнения
	@STORAGE @JvmField var status 		= STATUS_UNK
	
	// ссылка на буфер
	val buffer				            get() = Planet.buffer
	
	// количество ячеек в карте
	@JvmField var countMapCells 		= 0
	
	// смещение началы карты в блоках
	@JvmField var mapOffset 			= Point()
	
	// смещение экрана отрисовки карты
	@JvmField var canvasOffset 			= Point()
	
	// экран в фактических блоках
	@JvmField var canvasSize 			= Size()
	
	// экран в блоках максимум
	private var canvasMaxSize 			= Size()
	
	// тайлы
	private val tiles: Bitmap          get() = wnd.bitmapGetCache(if(classic) "classic_sprites" else "custom_sprites") ?: error("")
	
	// ректы
	private var canvasRect 				= Rect()
	
	private var bitmapRect 				= Rect()
	
	private var messageRect 			= RectF()
	
	// кисть для отрисовки сообщения
	private var sys 					= Paint().apply {
		color = Theme.integer(context, ATTR_SSH_COLOR_MESSAGE or THEME)
		textSize = Theme.dimen(context, R.dimen.msg).toFloat()
		textAlign = Paint.Align.CENTER
		typeface = context.makeFont("large")
		setShadowLayer(Theme.dimen(context, R.dimen.shadowTextR) * 2f,
		               Theme.dimen(context, R.dimen.shadowTextX) * 2f,
		               Theme.dimen(context, R.dimen.shadowTextY) * 2f,
		               0x0.color)
		
	}
	
	// "пустая" кисть
	private var nil 					= Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
		textSize = 20f.dp
		color = Color.WHITE
		textAlign = Paint.Align.RIGHT
		typeface = context.makeFont("small")
	}
	
	// вернуть окно
	val wnd: DroidWnd                   get() = context as DroidWnd
	
	init {
		delay = DroidWnd.applySpeed(STD_GAME_SPEED)
		tileBitmapSize = tiles.height / 4
		Planet.useMap = false
	}
	
	protected fun prepareMap(calcCanvas: Boolean) {
		Planet.apply {
			if(isPlanet) {
				if(calcCanvas) {
					val mapOffs 	= Point(this@ViewCommon.width - width * tileCanvasSize,
					                       this@ViewCommon.height - height * tileCanvasSize)
					val canvasOffs 	= Point((this@ViewCommon.width - canvasMaxSize.w * tileCanvasSize) / 2,
											  (this@ViewCommon.height - canvasMaxSize.h * tileCanvasSize) / 2)
					canvasOffset.set(if(mapOffs.x < 0) canvasOffs.x else mapOffs.x / 2, if(mapOffs.y < 0) canvasOffs.y else mapOffs.y / 2)
					canvasSize.set(if(canvasMaxSize.w > width) width else canvasMaxSize.w, if(canvasMaxSize.h > height) height else canvasMaxSize.h)
					countMapCells = width * height
					Touch.reset()
					useMap = true
				}
				val pt = droidPos()
				val droidOffs = Point(pt.x - canvasMaxSize.w / 2 , pt.y - canvasMaxSize.h / 2)
				mapOffset.set(if(droidOffs.x < 0) 0 else droidOffs.x, if(droidOffs.y < 0) 0 else droidOffs.y)
				if(mapOffset.x + canvasSize.w > width) mapOffset.x = width - canvasSize.w
				if(mapOffset.y + canvasSize.h > height) mapOffset.y = height - canvasSize.h
				updatePreview(preview, calcCanvas)
			}
		}
	}
	
	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		super.surfaceChanged(holder, format, width, height)
		// применить масштаб
		tileCanvasSize = DroidWnd.applyScale((if(width > height) height else width) / (12 * config.multiplySW).toInt())
		canvasMaxSize = Size(width / tileCanvasSize, height / tileCanvasSize)
		messageRect.right = width.toFloat(); messageRect.bottom = height.toFloat()
	}
	
	override fun handleMessage(msg: Message): Boolean {
		"onMessageCommonView(what: ${msg.what.msg} arg1: ${msg.arg1.msg} arg2: ${msg.arg2} obj: ${msg.obj})".debug()
		val h = wnd.wndHandler
		val s = surHandler
		val editor = (this@ViewCommon as? ViewEditor)
		if(h != null && s != null) {
			if(wnd.restart) {
				wnd.restart = false
				if(newStatus != 0) s.send(STATUS_MESSAGE, 0, newStatus, nArg, idMsg)
				else when(status) {
					STATUS_CLEARED	-> s.send(STATUS_CLEARED, a1 = position)
					STATUS_UNK		-> s.send(STATUS_INIT)
					else			-> s.send(status)
				}
				return false
			} else {
				msg.apply {
					when(what) {
						STATUS_MESSAGE -> {
							// изменение статуса с задержкой
							sysMsg = resources.getString(obj as? Int ?: 0)
							s.send(arg1, MESSAGE_DELAYED, arg2)
						}
						MSG_SERVICE    -> when(arg1) {
							ACTION_LOAD     -> {
								// загружаем карту
								position = msg.arg2
								val success = if(record != 0L) Stat.load(position) else Planet.load(position)
								h.send(MSG_FORM, a1 = ACTION_NAME)
								if(success) {
									if(editor != null) editor.modify = false else sysMsg = Planet.name
									s.send(STATUS_PREPARED, MESSAGE_DELAYED)
								}
								else {
									h.send(MSG_FORM, a1 = ACTION_NAME)
									if(editor == null) {
										// НЕ УДАЛОСЬ ЗАГРУЗИТЬ ПЛАНЕТУ - ВОЗМОЖНО ОНА БЫЛА ПОСЛЕДНЕЙ
										sysMsg = ""
										h.send(MSG_FORM, a1 = ACTION_FINISH)
									}
								}
							}
							ACTION_SAVE     -> {
								val success = Planet.store(context)
								if(arg2 == 1) h.send(MSG_FORM, a1 = ACTION_EXIT)
								else {
									editor?.modify = false
									s.send(STATUS_MESSAGE, a1 = STATUS_PREPARED, o = if(success) R.string.save_planet_success else R.string.save_planet_failed)
									// для случая свойства планеты сменили имя
									h.send(MSG_FORM, a1 = ACTION_NAME)
								}
							}
							ACTION_DELETE   -> {
								val count = Pack.countPlanets(Planet.pack) - 1
								Planet.delete(true)
								h.send(MSG_FORM, a1 = ACTION_LOAD, a2 = if(position >= count) position - 1 else position)
								editor?.modify = false
							}
							ACTION_NEW      -> {
								Planet.generator(context, arg2)
								editor?.modify = false
							}
							ACTION_GENERATE -> {
								Planet.default(context)
								h.send(MSG_FORM, a1 = ACTION_LOAD, a2 = 0)
								editor?.modify = false
							}
						}
						STATUS_EXIT	        -> h.send(MSG_FORM, a1 = ACTION_EXIT)
					}
				}
			}
		}
		return super.handleMessage(msg)
	}
	
	override fun draw(canvas: Canvas) {
		super.draw(canvas)
		if(isPlanet) {
			if(!Planet.useMap) prepareMap(true)

			repeat(previewMap.h) { y ->
				canvasRect.top = previewCO.y + y * previewBlk.h
				canvasRect.bottom = canvasRect.top + previewBlk.h
				repeat(previewMap.w) { x ->
					val v = remapTiles[buffer[previewMO.x + x, previewMO.y + y] and MSKT]
					if(v != T_NULL) {
						bitmapRect.left = v % 10 * tileBitmapSize
						bitmapRect.top = v / 10 * tileBitmapSize
						bitmapRect.right = bitmapRect.left + tileBitmapSize
						bitmapRect.bottom = bitmapRect.top + tileBitmapSize
						canvasRect.left = previewCO.x + x * previewBlk.w
						canvasRect.right = canvasRect.left + previewBlk.w
						canvas.drawBitmap(tiles, bitmapRect, canvasRect, nil)
					}
				}
			}
			sys.drawTextInBounds(canvas, sysMsg, messageRect, Gravity.CENTER)
		} else if(this is ViewEditor) {
			sys.drawTextInBounds(canvas, context.getString(R.string.no_planet), messageRect, Gravity.CENTER)
		}
		nil.drawTextInBounds(canvas, "fps: $fps", messageRect, Gravity.END or Gravity.BOTTOM)
	}
	
	fun updatePreview(v: Boolean, full: Boolean) {
		previewMO.x = if(v) 0 else mapOffset.x
		previewMO.y = if(v) 0 else mapOffset.y
		if(full) {
			val h = Planet.height.toFloat()
			val w = Planet.width.toFloat()
			previewMap.h = if(v) h.toInt() else canvasSize.h
			previewMap.w = if(v) w.toInt() else canvasSize.w
			previewBlk.h = if(v) (height / h).toInt() else tileCanvasSize
			previewBlk.w = if(v) (width / w).toInt() else tileCanvasSize
			previewCO.x = if(v) ((width - (w * previewBlk.w)) / 2f).toInt() else canvasOffset.x
			previewCO.y = if(v) ((height - (h * previewBlk.h)) / 2f).toInt() else canvasOffset.y
		}
		if(this is ViewEditor) KEY_EDIT_PREVIEW.optBool = v
	}
}
