package ru.ostrovskal.droid.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import android.os.Bundle
import android.os.Message
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import com.github.ostrovskal.ssh.AnimFrames
import com.github.ostrovskal.ssh.Constants.MSG_SERVICE
import com.github.ostrovskal.ssh.Constants.fPt
import com.github.ostrovskal.ssh.STORAGE
import com.github.ostrovskal.ssh.Touch
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.utils.*
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.forms.FormEditor
import ru.ostrovskal.droid.msg
import ru.ostrovskal.droid.tables.Planet

const val SHIFT_UNDEF   = 0
const val SHIFT_MAP     = 1
const val SHIFT_TILE    = 2

class ViewEditor(context: Context) : ViewCommon(context), AnimFrames.Callback {
	
	// Анимация, в качестве следящей за режимом сдвига
	private val anim = AnimFrames(this, Int.MAX_VALUE, 100, this)
	
	// Временная позиция карты. Используется при перетаскивании
	private var moffs = Point()
	
	// режим сдвига
	@STORAGE @JvmField var modeShift = SHIFT_UNDEF
	
	// признак модификации содержимого
	@STORAGE @JvmField var modify = false
	
	// содержимое под дроидом
	@STORAGE @JvmField var contentDroid = 0.toByte()
	
	// чуствительность перетаскивания
	private var dragSensitive = Size()
	
	init {
		Sound.playMusic(wnd, 0, true)
		anim.start(false, false)
	}
	
	override fun doFrame(view: View, frame: Int, direction: Int, began: Boolean): Boolean {
		if(status == STATUS_LOOP && wnd.formTag == "editor") {
			// обработка сдвига
			if(modeShift == SHIFT_UNDEF && !preview) {
				Touch.findTouch(0)?.apply {
					val time = System.currentTimeMillis()
					if((time - t0) > 200) {
						modeShift = SHIFT_MAP
						moffs.x = mapOffset.x - (p0.x / tileCanvasSize).toInt()
						moffs.y = mapOffset.y - (p0.y / tileCanvasSize).toInt()
						view.invalidate()
					}
				}
			}
		}
		return false
	}
	
	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(ev: MotionEvent): Boolean {
		Touch.onTouch(ev)
		if(status == STATUS_LOOP) {
			// обработка сдвига
			Touch.drag(0, dragSensitive) { offs, time, t, event ->
				// при первом уведомлении устанавливаем режим
				if(modeShift == SHIFT_UNDEF) {
					modeShift = if(!preview && time > 200) SHIFT_MAP else SHIFT_TILE
				}
				if(event) {
					if(modeShift == SHIFT_MAP) {
						Planet.MAP.apply {
							mapOffset.x = moffs.x + ((t.p0.x - offs.w) / tileCanvasSize).toInt()
							mapOffset.y = moffs.y + ((t.p0.y - offs.h) / tileCanvasSize).toInt()
							if(mapOffset.x < 0) mapOffset.x = 0
							if(mapOffset.y < 0) mapOffset.y = 0
							if((mapOffset.x + canvasSize.w) > width) mapOffset.x = width - canvasSize.w
							if((mapOffset.y + canvasSize.h) > height) mapOffset.y = height - canvasSize.h
							updatePreview(preview, false)
						}
					}
				}
				if(modeShift == SHIFT_TILE) {
					t.resetPosition()
					fPt.set(t.p0.x + offs.w, t.p0.y + offs.h)
					shiftTile(fPt)
				}
				if(!event) modeShift = SHIFT_UNDEF
				invalidate()
			}
			return true
		}
		return super.onTouchEvent(ev)
	}
	
	private fun shiftTile(abs: PointF)
	{
		Planet.MAP.apply {
			// установка элемента
			val xx = previewMO.x + (Math.round(abs.x) - previewCO.x) / previewBlk.w
			val yy = previewMO.y + (Math.round(abs.y) - previewCO.y) / previewBlk.h
			// если по границе карты - пропускаем
			if(xx == 0 || yy == 0 || xx >= width - 1 || yy >= height - 1) return@apply
			// текущий тайл
			var o = wnd.findForm<FormEditor>("editor")?.curTile?.num ?: return@apply
			// содержимое карты по координатам
			val oo = buffer[xx, yy]
			// объект на карте
			val objMap = remapProp[oo] and MSKO
			// текущий объект
			val objTile = remapProp[o] and MSKO
			// они одинаковы?
			if(objMap == objTile) {
				// индекс объекта под пальцем
				var ret = paramsEditElems.search(oo, -1)
				if(ret != -1) {
					// множественная установка
					ret = (ret and -4) + (ret + 1 and 3)
					o = paramsEditElems[ret]
				}
			}
			// если устанавливаем дроида - старого стираем
			// если ставим поверх дроида - стираем его координаты
			else if(objMap == O_DROID) droidNull()
			else if(objTile == O_DROID) {
				// восстанавливаем содержимое под дроидом
				if(!droidIsNull()) buffer[x, y] = contentDroid
				contentDroid = oo.toByte()
				x = xx; y = yy
			}
			buffer[xx, yy] = o
			modify = true
		}
	}

	override fun restoreState(state: Bundle, vararg params: Any?) {
		super.restoreState(state)
		wnd.findForm<FormEditor>("editor")?.setTile(KEY_EDIT_TILE.optInt)
		updatePreview(preview, true)
	}
	
	override fun handleMessage(msg: Message): Boolean {
		"onMessageViewEditor(what: ${msg.what.msg} arg1: ${msg.arg1.msg} arg2: ${msg.arg2} obj: ${msg.obj})".debug()
		if(super.handleMessage(msg)) {
			when(msg.what) {
				STATUS_INIT -> surHandler?.send(MSG_SERVICE, 0, ACTION_LOAD, position)
				STATUS_WORK -> {
					sysMsg = ""; status = STATUS_LOOP
				}
			}
		}
		return true
	}
	
	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		super.surfaceChanged(holder, format, width, height)
		dragSensitive.w = tileCanvasSize / 2
		dragSensitive.h = tileCanvasSize / 2
	}
	
	override fun draw(canvas: Canvas) {
		super.draw(canvas)
		if(modeShift == SHIFT_MAP) {
			// нарисовать признак перетаскивания карты
			canvas.drawColor(0x60404040)
		}
	}
}
