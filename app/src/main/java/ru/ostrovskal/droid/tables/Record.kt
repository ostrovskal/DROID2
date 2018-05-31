package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.sql.Table

object Record: Table() {
	@JvmField val id = integer("_id").notNull().primaryKey()
	@JvmField val map = blob("map").notNull()
	@JvmField val rec = blob("rec").notNull()
}