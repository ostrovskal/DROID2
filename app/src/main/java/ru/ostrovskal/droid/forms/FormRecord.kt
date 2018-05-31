package ru.ostrovskal.droid.forms

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs
import com.github.ostrovskal.ssh.StylesAndAttrs.style_icon
import com.github.ostrovskal.ssh.StylesAndAttrs.style_text_small
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.adapters.ListAdapter
import com.github.ostrovskal.ssh.singleton.Sound
import com.github.ostrovskal.ssh.sql.Rowset
import com.github.ostrovskal.ssh.sql.max
import com.github.ostrovskal.ssh.sql.min
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Chart
import com.github.ostrovskal.ssh.widgets.Text
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.FORM_GAME
import ru.ostrovskal.droid.Constants.style_tile_droid
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Stat

class FormRecord: Form() {
	private val flags 			= booleanArrayOf(false, false, true, true, false, true, true, true, false, true, false)
	private var maxValues 		= intArrayOf()
	private var valMin	  		= 0f
	private var valMax			= 0f
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				backgroundSet(StylesAndAttrs.style_form)
				formHeader(R.string.header_record)
				list(config.isVert) {
					id = android.R.id.list
					selector = ColorDrawable(0)
					adapter = StatAdapter(wnd).apply { this@FormRecord.adapter = this }
				}
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		// лучше меньше - time, fuel, bomb, death, cycles
		// лучше больше - score, count, yellow, red, green, egg
		// звезды - (сумма текущих / сумма больших) * число звезд / 2
		// (сумма минимальных / сумма текущих) * число звезд /2
		// перезапускаем музыку(если вышли из записи игры)
		Sound.playMusic(wnd, 0, true)
		// получить максимум по всем записям:
		valMax = Float.MIN_VALUE
		Stat.select(Stat.time.max(), Stat.fuel.max(), Stat.score.max(), Stat.count.max(), Stat.bomb.max(), Stat.yellow.max(),
		            Stat.red.max(), Stat.green.max(), Stat.death.max(), Stat.egg.max(), Stat.cycles.max()) {}.execute()?.release {
			// запомнить их
			maxValues = IntArray(columnCount)
			for(i in maxValues.indices) {
				val mx = getInt(i)
				maxValues[i] = mx
				if(flags[i]) valMax += mx
			}
		}
		valMin = Float.MIN_VALUE
		// получить минимум по всем записям:
		Stat.select(Stat.time.min(), Stat.fuel.min(), Stat.bomb.min(), Stat.cycles.min()) {}.execute()?.release {
			// запомнить их
			val count = columnCount
			for(i in 0 until count) valMin += getInt(i)
		}
		loaderManager.initLoader(Constants.LOADER_CONNECTOR, null, this).forceLoad()
	}
	
	override fun queryConnector() = Stat.select(Stat.planet, Stat.date, Stat.time, Stat.fuel, Stat.score,
	                                            Stat.count, Stat.bomb, Stat.yellow, Stat.red, Stat.green,
	                                            Stat.death, Stat.egg, Stat.cycles, Stat.id) { orderBy(Stat.date, false) }
	
	private inner class StatAdapter(context: Context) : ListAdapter(context, if(config.isVert) RecordVertItem() else RecordHorzItem(), 4) {
		override fun bindField(view: View?, rs: Rowset, idx: Int) {
			if(view is Text && idx == 1) {
				view.text = rs.datetime(Stat.date)
			} else {
				super.bindField(view, rs, idx)
			}
		}
		
		override fun bindView(view: View, context: Context, rs: Rowset) {
			super.bindView(view, context, rs)
			view.byIdx<View>(2).apply {
				setOnClickListener {
					wnd.instanceForm(FORM_GAME, "record", rs.getLong(mRowIDColumn))
				}
			}
			// подсчитать текущие показатели
			val curValues = IntArray(maxValues.size)
			var mx = 0f; var mn = 0f
			repeat(11) {
				val v = rs.getInt(it + 2)
				curValues[it] = v
				if(flags[it]) mx += v else mn += v
			}
			view.byIdx<Chart>(3).apply {
				max = maxValues
				current = curValues
				startAnim()
			}
			val countMax = Math.round((mx / valMax) * 4)
			val countMin = Math.round((valMin / mn) * 4)
			val count = countMax + countMin
			// отобразить результат
			repeat(8) { view.byIdx<Tile>(it + 4).visibility = if(it < count) View.VISIBLE else View.GONE }
		}
	}
	
	abstract class RecordItem: UiComponent() {
		protected val nums = intArrayOf(R.integer.TILE_YELLOWD, R.integer.TILE_REDD, R.integer.TILE_GREEND, R.integer.TILE_EGG, R.integer.TILE_DROIDR,
		                                R.integer.TILE_TIME, R.integer.TILE_BOMB, R.integer.TILE_FUEL, R.integer.TILE_SCORE, R.integer.I_GEN_PLANET, R.integer.I_CYCLES)
		
		val colorsItem = intArrayOf(0xc3f556.color, 0x063f5e.color, 0xf5e6e6.color, 0x0e24e9.color, 0xeadada.color, 0x66093a.color,
		                        0xe0f907.color, 0x292807.color, 0x7df442.color, 0x15480a.color, 0xfa11ac.color, 0x260221.color,
		                        0xf24949.color, 0x450303.color, 0x4e16e7.color, 0x460707.color, 0xec550a.color, 0x53261e.color,
		                        0xe12eae7.color, 0x42936.color, 0xe9e6e6.color, 0x0d0c0c.color)
		
	}
	
	class RecordVertItem : RecordItem() {
		
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(32, 18) {
				backgroundSet(StylesAndAttrs.style_item)
				padding = 4.dp
				layoutParams = LinearLayout.LayoutParams(Constants.MATCH, Theme.dimen(ctx, R.dimen.heightDlgItemRecord))
				text(R.string.panel_text) {gravity = Gravity.CENTER}.lps(4, 0, 16, 4)
				text(R.string.null_text, style_text_small) {
					text = fmtTime(System.currentTimeMillis(), "%d.%m.%Y %H:%M:%S")
				}.lps(21, 0, 11, 4)
				button(style_icon) {
					numResource = R.integer.I_PLAY_RECORD
					states = Constants.TILE_STATE_HOVER
				}.lps(27, 8, 4, 5)
				chart(Constants.SSH_MODE_DIAGRAM) {
					direction = Constants.DIRU
					showText = false
					colors = colorsItem
				}.lps(4, 5, 22, 10)
				
				repeat(8) { button(style_icon) { numResource = R.integer.I_STAR2}.lps(1, 15 - it * 2, 2, 2) }
				
				repeat(11) {
					val style = if(it > 8) style_icon else style_tile_droid
					button(style) { numResource = nums[it] }.lps(4 + it * 2, 16, 2, 2)
				}
			}
		}
	}

	class RecordHorzItem : RecordItem() {
		
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(18, 32) {
				backgroundSet(StylesAndAttrs.style_item)
				padding = 4.dp
				layoutParams = LinearLayout.LayoutParams(Theme.dimen(ctx, R.dimen.heightDlgItemRecord), Constants.MATCH)
				text(R.string.panel_text) {gravity = Gravity.CENTER}.lps(0, 0, 18, 3)
				text(R.string.null_text, style_text_small) {
					text = fmtTime(System.currentTimeMillis(), "%d.%m.%Y %H:%M:%S")
				}.lps(0, 3, 13, 3)
				button(style_icon) {
					numResource = R.integer.I_PLAY_RECORD
					states = Constants.TILE_STATE_HOVER
				}.lps(13, 3, 4, 4)
				chart(Constants.SSH_MODE_DIAGRAM) {
					direction = Constants.DIRR
					showText = false
					colors = colorsItem
				}.lps(3, 8, 13, 22)
				
				repeat(8) { button(style_icon) { numResource = R.integer.I_STAR2}.lps(2 + it * 2, 30, 2, 2) }
				
				repeat(11) {
					val style = if(it > 8) style_icon else style_tile_droid
					button(style) { numResource = nums[it] }.lps(0, 8 + it * 2, 2, 2)
				}
			}
		}
	}
}