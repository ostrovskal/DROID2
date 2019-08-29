@file:Suppress("DEPRECATION")

package ru.ostrovskal.droid.forms

import android.content.Context
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
import com.github.ostrovskal.ssh.widgets.lists.BaseListView
import ru.ostrovskal.droid.Constants.FORM_GAME
import ru.ostrovskal.droid.Constants.style_tile_droid
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Stat

class FormRecord: Form() {
	private val test 			= booleanArrayOf(false, false, true, true, false, true, true, true, false, true, false)
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
					adapter = StatAdapter(wnd).apply { this@FormRecord.adapter = this }
					itemClickListener = object : BaseListView.OnListItemClickListener {
						override fun onItemClick(list: BaseListView, view: View, position: Int, id: Long) {
							wnd.instanceForm(FORM_GAME, "record", id)
						}
					}
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
		Stat.select(Stat.fTime.max(), Stat.fFuel.max(), Stat.fScore.max(), Stat.fCount.max(), Stat.fBomb.max(), Stat.fYellow.max(),
		            Stat.fRed.max(), Stat.fGreen.max(), Stat.fDeath.max(), Stat.fEgg.max(), Stat.fCycles.max()) {}.execute()?.release {
			// запомнить их
			maxValues = IntArray(columnCount)
			for(i in maxValues.indices) {
				val mx = getInt(i)
				maxValues[i] = mx
				if(test[i]) valMax += mx
			}
		}
		valMin = Float.MIN_VALUE
		// получить минимум по всем записям:
		Stat.select(Stat.fTime.min(), Stat.fFuel.min(), Stat.fBomb.min(), Stat.fCycles.min()) {}.execute()?.release {
			// запомнить их
			val count = columnCount
			for(i in 0 until count) valMin += getInt(i)
		}
		loaderManager.initLoader(Constants.LOADER_CONNECTOR, null, this).forceLoad()
	}
	
	override fun queryConnector() = Stat.select(Stat.fPlanet, Stat.fDate, Stat.fTime, Stat.fFuel, Stat.fScore,
	                                            Stat.fCount, Stat.fBomb, Stat.fYellow, Stat.fRed, Stat.fGreen,
	                                            Stat.fDeath, Stat.fEgg, Stat.fCycles, Stat.id) { orderBy(Stat.fDate, false) }
	
	private inner class StatAdapter(context: Context) : ListAdapter(context, if(config.isVert) RecordVertItem() else RecordHorzItem(), 2) {
		override fun bindField(view: View?, rs: Rowset, idx: Int) {
			if(view is Text && idx == 1) {
				view.text = rs.datetime(Stat.fDate)
			} else {
				super.bindField(view, rs, idx)
			}
		}
		
		override fun bindView(view: View, context: Context, rs: Rowset) {
			super.bindView(view, context, rs)
			// подсчитать текущие показатели
			val curValues = IntArray(maxValues.size)
			var mx = 0f; var mn = 0f
			repeat(11) {
				val v = rs.getInt(it + 2)
				curValues[it] = v
				if(test[it]) mx += v else mn += v
			}
			view.byIdx<Chart>(2).apply {
				max = maxValues
				current = curValues
				startAnim()
			}
			val countMax = Math.round((mx / valMax) * 4)
			val countMin = Math.round((valMin / mn) * 4)
			val count = countMax + countMin
			// отобразить результат
			repeat(8) { view.byIdx<Tile>(it + 3).visibility = if(it < count) View.VISIBLE else View.GONE }
		}
	}
	
	abstract class RecordItem: UiComponent() {
		protected val nums = intArrayOf(R.integer.TILE_TIME, R.integer.TILE_FUEL, R.integer.TILE_SCORE, R.integer.I_GEN_PLANET, R.integer.TILE_BOMB,
		                                R.integer.TILE_YELLOWD, R.integer.TILE_REDD, R.integer.TILE_GREEND, R.integer.TILE_DROIDR, R.integer.TILE_EGG, R.integer.I_CYCLES)
		val colorsItem = intArrayOf(0xc3f556.color, 0x063f5e.color, 0xf5e6e6.color, 0x0e24e9.color, 0xeadada.color, 0x66093a.color,
		                        0xe0f907.color, 0x292807.color, 0x7df442.color, 0x15480a.color, 0xfa11ac.color, 0x260221.color,
		                        0xf24949.color, 0x450303.color, 0x4e16e7.color, 0x460707.color, 0xec550a.color, 0x53261e.color,
		                        0xe12eae7.color, 0x42936.color, 0xe9e6e6.color, 0x0d0c0c.color)
		
	}
	
	class RecordVertItem : RecordItem() {
		
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(28, 18) {
				backgroundSet(StylesAndAttrs.style_item)
				padding = 4.dp
				layoutParams = LinearLayout.LayoutParams(Constants.MATCH, Theme.dimen(ctx, R.dimen.heightDlgItemRecord))
				text(R.string.panel_text) {gravity = Gravity.CENTER}.lps(3, 0, 17, 4)
				text(R.string.null_text, style_text_small) {
					text = System.currentTimeMillis().datetime
				}.lps(20, 0, 8, 4)
				chart(Constants.SSH_MODE_DIAGRAM) {
					direction = Constants.DIRU
					showText = true
					colors = colorsItem
					max = intArrayOf(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000)
					current = intArrayOf(700, 200, 800, 400, 660, 100, 900, 350, 600, 250, 700)
				}.lps(4, 5, 22, 10)
				
				repeat(8) { button(style_icon) { numResource = R.integer.I_STAR2}.lps(1, 15 - it * 2, 2, 2) }
				
				repeat(11) {
					val style = if(it == 3 || it == 10) style_icon else style_tile_droid
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
					gravity = Gravity.CENTER
					text = System.currentTimeMillis().datetime
				}.lps(0, 3, 18, 3)
				chart(Constants.SSH_MODE_DIAGRAM) {
					direction = Constants.DIRR
					showText = true
					colors = colorsItem
				}.lps(3, 7, 13, 22)
				
				repeat(8) { button(style_icon) { numResource = R.integer.I_STAR2}.lps(2 + it * 2, 29, 2, 2) }
				
				repeat(11) {
					val style = if(it == 3 || it == 10) style_icon else style_tile_droid
					button(style) { numResource = nums[it] }.lps(0, 7 + it * 2, 2, 2)
				}
			}
		}
	}
}