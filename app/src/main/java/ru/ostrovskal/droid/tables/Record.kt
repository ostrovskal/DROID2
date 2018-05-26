package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.sql.Table

object Record: Table() {
	val id = integer("_id").notNull().primaryKey()
	val map = blob("map").notNull()
	val rec = blob("rec").notNull()
}