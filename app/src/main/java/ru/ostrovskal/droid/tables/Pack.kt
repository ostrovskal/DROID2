package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.sql.Table
import com.github.ostrovskal.ssh.utils.optText
import com.github.ostrovskal.ssh.utils.release
import com.github.ostrovskal.ssh.utils.releaseRun
import ru.ostrovskal.droid.Constants
import ru.ostrovskal.droid.Constants.SYSTEM_DEFAULT

object Pack : Table() {
	@JvmField val id      = integer("_id").notNull().primaryKey()
	@JvmField val name    = text("name").notNull().unique()
	@JvmField val author  = text("author").notNull()
	@JvmField val desc    = text("desc")
	@JvmField val price   = real("price")
	@JvmField val date    = integer("date").notNull()
	@JvmField val planets = integer("planets").notNull()
	@JvmField val skull   = integer("skull").notNull()

	// Возвращает количество планет
	fun countPlanets(nm: String) = select(planets) { where {name eq nm } }.execute()?.releaseRun { integer(0) } ?: 0
	
	// Изменить количество планет
	fun changeCountPlanets(nm: String, isAdd: Boolean) {
		val countPlanets = countPlanets(nm) + if(isAdd) 1 else -1
		update { it[planets] = countPlanets; where { name eq nm } }
	}

	// Удалить пустые пакеты, если есть
	// Проверить, что количество планет в классической системе > 0
	fun check(): Boolean {
		val curDate = System.currentTimeMillis()
		select(name, date).execute()?.release {
			forEach {
				val nm = it.text(name)
				val dt = it.integer(date)
				val count = countPlanets(nm)
				// удалить пакет, если в нем нет планет и дата создания отличается от текущей на 100 минут
				if(count <= 0L && (curDate - dt) > 360000) {
					delete { where { name eq nm } }
					if(nm == Constants.KEY_PACK.optText) Constants.KEY_PACK.optText = SYSTEM_DEFAULT
				}
			}
		}
		return countPlanets(SYSTEM_DEFAULT) == 30L
	}
	
	// Создание пустой классической системы
	fun default() {
		delete { where { name eq SYSTEM_DEFAULT } }
		insert {
			it[name] = SYSTEM_DEFAULT
			it[date] = System.currentTimeMillis()
			it[author] = "OSTROV"
			it[planets] = 0L
			it[skull] = 8L
			it[desc] = "Основная система планет. После зачистки которой, открывается доступ к другим системам."
			it[price] = 0f
		}
	}
}