package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.optText
import com.github.ostrovskal.ssh.release
import com.github.ostrovskal.ssh.releaseRun
import com.github.ostrovskal.ssh.sql.Table
import ru.ostrovskal.droid.Constants
import ru.ostrovskal.droid.Constants.SYSTEM_DEFAULT

object Pack : Table() {
	val id      = integer("_id").notNull().primaryKey()
	val name    = text("name").notNull().unique()
	val author  = text("author").notNull()
	val desc    = text("desc")
	val price   = real("price")
	val date    = integer("date").notNull()
	val planets = integer("planets").notNull()
	val skull   = integer("skull").notNull()

	// Возвращает количество планет
	fun countPlanets(nm: String) = select(planets) { where {name eq nm } }.execute()?.releaseRun { integer(planets) } ?: 0
	
	// Изменить количество планет
	fun changeCountPlanets(nm: String, isAdd: Boolean) {
		val countPlanets = countPlanets(nm) + if(isAdd) 1 else -1
		if(countPlanets > 0) {
			update { it[planets] = countPlanets }
		} else {
			delete { where { name eq nm } }
		}
	}

	// Удалить пустые пакеты, если есть
	// Проверить, что количество планет в классической системе >= 30
	fun check(): Boolean {
		select(name).execute()?.release {
			forEach {
				val nm = it.text(name)
				if(countPlanets(nm) == 0L) {
					delete { where { name eq nm } }
					if(nm == Constants.KEY_PACK.optText) Constants.KEY_PACK.optText = SYSTEM_DEFAULT
				}
			}
		}
		return countPlanets(SYSTEM_DEFAULT) >= 30L
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