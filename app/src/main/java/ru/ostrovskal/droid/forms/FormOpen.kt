package ru.ostrovskal.droid.forms

import android.content.Context
import android.database.Cursor
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.MSG_FORM
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.adapters.ListAdapter
import com.github.ostrovskal.ssh.adapters.SelectAdapter
import com.github.ostrovskal.ssh.sql.Rowset
import com.github.ostrovskal.ssh.sql.transaction
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Text
import com.github.ostrovskal.ssh.widgets.Tile
import com.github.ostrovskal.ssh.widgets.lists.BaseListView
import com.github.ostrovskal.ssh.widgets.lists.Select
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.Planet

class FormOpen: Form() {
	
	override fun onItemClick(select: Select, view: View, position: Int, id: Long) {
		val nPack = select.selectionString
		if(nPack != result) {
			result = nPack
			connector?.apply { update(queryConnector()); forceLoad() }
		}
		adapter.path = "${wnd.filesDir}/miniatures/$result"
	}
	
	override fun queryConnector() = Planet.select(Planet.title, Planet.title, Planet.create, Planet.position, Planet.id) {
		where { Planet.system eq result }
		orderBy(Planet.position)
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			linearLayout {
				formHeader(R.string.header_planet_open)
				backgroundSet(StylesAndAttrs.style_form)
				select {
					id = R.id.slPack
					val tmp = Planet.MAP.pack
					result = if(tmp.isEmpty()) KEY_PACK.optText else tmp
					adapter = SelectAdapter(this, wnd, SelectPopup(), SelectItem(), Pack.listOf(Pack.name, Pack.name))
					selectionString = result
					itemClickListener = this@FormOpen
				}
				list {
					id = android.R.id.list
					adapter = PlanetAdapter(wnd).apply {
						this@FormOpen.adapter = this
						path = "${wnd.filesDir}/miniatures/$result"
					}
					wnd.findForm<FormEditor>("editor")?.apply { selection = editor.position }
					itemClickListener = object : BaseListView.OnListItemClickListener {
						override fun onItemClick(list: BaseListView, view: View, position: Int, id: Long) {
							KEY_TMP_PACK.optText = result
							sendResult(MSG_FORM, ACTION_LOAD, position)
							footer(Constants.BTN_OK, 0)
						}
					}
				}
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		loaderManager.initLoader(Constants.LOADER_CONNECTOR, null, this).forceLoad()
	}
	
	private inner class PlanetAdapter(context: Context) : ListAdapter(context, OpenItem(), 3), View.OnClickListener {
		override fun bindView(view: View, context: Context, rs: Rowset) {
			val num = rs.integer(Planet.position)
			val pos = rs.position
			view.byIdx<Tile>(3).apply {
				visibility = if(pos == 0) View.INVISIBLE else View.VISIBLE
				tag = num
				setOnClickListener(this@PlanetAdapter)
			}
			view.byIdx<Tile>(4).apply {
				visibility = if(pos >= rs.count - 1) View.INVISIBLE else View.VISIBLE
				tag = num
				setOnClickListener(this@PlanetAdapter)
			}
			super.bindView(view, context, rs)
		}
		
		override fun bindField(view: View?, rs: Rowset, idx: Int) {
			if(view is Text && idx == 2) {
				view.text = rs.integer(idx).datetime
			} else {
				super.bindField(view, rs, idx)
			}
		}
		
		override fun onClick(v: View)
		{
			val numFrom = (v.tag as? Long) ?: return
			Planet.select(Planet.position, Planet.title) {
				where { Planet.system eq result}
				orderBy(Planet.position)
			}.execute()?.release {
				// находим позицию курсорса в соответствии с исходной
				while(numFrom != integer(Planet.position)) { moveToNext() }
				if(v.rotation >= 90f) moveToNext() else moveToPrevious()
				val numTo = integer(Planet.position)
				// обменять номер у планет
				transaction {
					Planet.update {
						it[Planet.position] = 100//numTo
						where { (Planet.system eq result) and (Planet.position eq numFrom ) }
					}
					Planet.update {
						it[Planet.position] = numFrom
						where { (Planet.system eq result) and (Planet.position eq numTo ) }
					}
					Planet.update {
						it[Planet.position] = numTo
						where { (Planet.system eq result) and (Planet.position eq 100L) }
					}
				}
				// проверим, если текущая планета имеет тот же номер и пакет - обновить номер
				Planet.MAP.apply {
					if(result == pack) {
						num = when(num) {
							numFrom	-> numTo
							numTo	-> numFrom
							else    -> num
						}
					}
				}
				loaderManager.getLoader<Cursor>(Constants.LOADER_CONNECTOR).forceLoad()
			}
		}
	}
	
	class OpenItem: UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(25, 10) {
				backgroundSet(StylesAndAttrs.style_item)
				padding = 1.dp
				layoutParams = LinearLayout.LayoutParams(Constants.MATCH, Theme.dimen(ctx, R.dimen.heightDlgItemOpenPlanet))
				button(StylesAndAttrs.style_icon) { numResource = R.integer.I_CANCEL }.lps(1, 1, 8, 8)
				text(R.string.panel_text, style_text_planet).lps(10, 3, 10, 3)
				text(R.string.null_text, StylesAndAttrs.style_text_small) {
					text = System.currentTimeMillis().time
					gravity = Gravity.CENTER
				}.lps(10, 6, 10, 3)
				button(StylesAndAttrs.style_tool_arrow).lps(20, 0, 5, 5)
				button(StylesAndAttrs.style_tool_arrow) { rotation = 180f }.lps(20, 5, 5, 5)
			}
		}
	}
}
