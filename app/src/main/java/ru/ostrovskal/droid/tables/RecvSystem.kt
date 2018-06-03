package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.sql.Table
import com.github.ostrovskal.ssh.utils.nextLong
import com.github.ostrovskal.ssh.utils.release
import java.util.*

object RecvSystem : Table() {
	@JvmField val id    = integer("_id").notNull().primaryKey()
	@JvmField val pos   = integer("pos").notNull()
	@JvmField val name  = text("name").notNull().unique()
	@JvmField val author= text("author").notNull()
	@JvmField val desc  = text("desc").notNull()
	@JvmField val price = real("price").notNull()
	@JvmField val date  = integer("date").notNull()
	@JvmField val planets = integer("planets").notNull()
	@JvmField val skull = integer("skull").notNull()
	@JvmField val data  = blob("data").notNull()
	
	fun load() {
		deleteAll()
		Planet.select { }.execute()?.release {
			val rnd = Random(System.currentTimeMillis())
			forEach { rs ->
				insert {
					it[pos] = rs.integer("position")
					it[name] = rs.text("title")
					it[author] = rs.text("creator")
					it[desc] = "Загруженная система"
					it[price] = rs.real("traffic")
					it[date] = rs.integer("create")
					it[planets] = rs.integer("cycles")
					it[skull] = rnd.nextLong(10)
					it[data] = rs.blob("content")
				}
			}
		}
	}
	
	fun make(index: Int) {
		select { where { pos eq index.toLong() } }.execute()?.release {
			// создать систему
			
			// создать планеты этой системы
		}
	}
}