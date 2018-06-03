package ru.ostrovskal.droid.forms

import android.content.Context
import android.content.Loader
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.ostrovskal.ssh.Constants
import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.Form
import com.github.ostrovskal.ssh.StylesAndAttrs.*
import com.github.ostrovskal.ssh.Theme
import com.github.ostrovskal.ssh.Wnd
import com.github.ostrovskal.ssh.adapters.ListAdapter
import com.github.ostrovskal.ssh.sql.Rowset
import com.github.ostrovskal.ssh.ui.*
import com.github.ostrovskal.ssh.utils.*
import com.github.ostrovskal.ssh.widgets.Check
import com.github.ostrovskal.ssh.widgets.Text
import com.github.ostrovskal.ssh.widgets.Tile
import ru.ostrovskal.droid.Constants.ACTION_PACK
import ru.ostrovskal.droid.Constants.style_text_planet
import ru.ostrovskal.droid.R
import ru.ostrovskal.droid.tables.Pack
import ru.ostrovskal.droid.tables.RecvSystem

class FormRecv: Form() {
	var checked = booleanArrayOf()
	var systems = listOf<String>()
	var skull1 = 0
	var skull2 = 0
	
	override fun queryConnector() = RecvSystem.select(RecvSystem.name, RecvSystem.planets, RecvSystem.desc, RecvSystem.price,
	                                                 RecvSystem.author, RecvSystem.date, RecvSystem.skull, RecvSystem.id) {
		where { RecvSystem.name.notIN(systems) }
		orderBy(RecvSystem.price)
	}
	
	override fun onLoadFinished(loader: Loader<Rowset>, data: Rowset?) {
		super.onLoadFinished(loader, data)
		checked = BooleanArray(data?.count ?: 0) { false }
	}
	
	override fun inflateContent(container: LayoutInflater): UiCtx {
		return UI {
			skull1 = resources.getInteger(R.integer.I_SKULL1)
			skull2 = resources.getInteger(R.integer.I_SKULL2)
			val port = config.isVert
			cellLayout(10, if(port) 20 else 15) {
				backgroundSet(style_form)
				formHeader(R.string.header_recv_system)
				list(port) {
					selector = ColorDrawable(0)
					id = android.R.id.list
					adapter = RecvAdapter(ctx).apply { this@FormRecv.adapter = this }
				}.lps(0, 0, 10, if(port) 16 else 11)
				formFooter(Constants.BTN_OK, R.integer.I_YES, Constants.BTN_NO, R.integer.I_NO)
			}
		}
	}
	
	override fun initContent(content: ViewGroup) {
		RecvSystem.load()
		systems = Pack.listOf(Pack.name, Pack.name)
		loaderManager.initLoader(Constants.LOADER_CONNECTOR, null, this).forceLoad()
	}
	
	override fun footer(btnId: Int, param: Int) {
		if(btnId == BTN_OK) {
			checked.forEachIndexed { index, b ->
				if(b) RecvSystem.make(index)
			}
		}
		sendResult(MSG_FORM, ACTION_PACK, btnId)
		super.footer(btnId, param)
	}
	
	inner class RecvAdapter(context: Context): ListAdapter(context, RecvItem(), 7) {
		val sb      = StringBuilder(16)
		
		override fun bindView(view: View, context: Context, rs: Rowset) {
			super.bindView(view, context, rs)
			val skull = rs.integer(RecvSystem.skull)
			repeat(10) {
				view.byIdx<Tile>(it + 7).num = if(it < skull) skull2 else skull1
			}
		}
		
		override fun bindField(view: View?, rs: Rowset, idx: Int) {
			val pos = rs.position
			if(view is Check) {
				view.isChecked = checked[pos]
				view.setTag(pos)
			}
			else if(view is Text && idx == 1) {
				view.text = sb.zero(rs.integer(idx), 3)
			}
			else if(view is Text && idx == 5) {
				view.text = rs.integer(idx).datetime
			}
			else super.bindField(view, rs, idx)
		}
	}
	
	class RecvItem: UiComponent() {
		override fun createView(ui: UiCtx): View = with(ui) {
			cellLayout(20, 15, 2.dp) {
				padding = 3.dp
				backgroundSet(style_item)
				layoutParams = if(config.isVert) {
					LinearLayout.LayoutParams(MATCH, Theme.dimen(ctx, R.dimen.heightDlgItemRecvPlanet))
				} else {
					LinearLayout.LayoutParams(Theme.dimen(ctx, R.dimen.heightDlgItemRecvPlanet), MATCH)
				}
				// name system
				text(R.string.panel_text, style_text_planet) { gravity = Gravity.CENTER }.lps(3, 0, 13, 3)
				// count planets
				text(R.string.no_planet, style_text_large) { gravity = Gravity.CENTER }.lps(16, 0, 6, 3)
				// desc
				text(R.string.splash_desc, style_text_normal) {
					gravity = Gravity.CENTER
					backgroundSet {
						selectorWidth = 2f.dp
						selectorColor = 0.color
						radii = floatArrayOf(10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f)
						shape = TILE_SHAPE_ROUND
						solid = 0x6f000000
						isShowBackground = true
					}
				}.lps(1, 3, 15, 10)
				// price
				text(R.string.no_planet, style_text_large) { gravity = Gravity.CENTER }.lps(16, 4, 4, 8)
				// author
				text(R.string.splash_adaptation, style_text_normal) { text = "Шаталов С.В."; gravity = Gravity.CENTER }.lps(0, 13, 12, 2)
				// date
				text(R.string.null_text, style_text_small) { text = System.currentTimeMillis().datetime; gravity = Gravity.CENTER }.lps(12, 13, 8, 2)
				// select
				check(R.string.null_text) {
					setOnClickListener {
						val idx = it.tag as? Int ?: return@setOnClickListener
						val checked = (context as? Wnd)?.findForm<FormRecv>("recv_system")?.checked ?: return@setOnClickListener
						checked[idx] = isChecked
					}
				}.lps(0, 0, 3, 3)
				repeat(10) {
					button(style_icon) { numResource = R.integer.I_SKULL1 }.lps(0, 12 - it, 1, 1)
				}
			}
		}
	}
}