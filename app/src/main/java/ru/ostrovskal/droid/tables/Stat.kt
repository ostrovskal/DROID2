package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.sql.RuleOption
import com.github.ostrovskal.ssh.sql.Table

object Stat: Table() {
	val id = integer("_id").notNull().primaryKey()
	val rec = integer("rec").references(Record.id, RuleOption.CASCADE, RuleOption.CASCADE)
	val auth = text("author").notNull()
	val pack = text("pack").notNull().references(Pack.name, RuleOption.CASCADE, RuleOption.CASCADE)
	val planet = text("planet").notNull()
	val time = integer("time").notNull()
	val fuel = integer("fuel").notNull()
	val score = integer("score").notNull()
	val count = integer("count").notNull()
	val bomb = integer("bomb").notNull()
	val date = integer("date").notNull()
	val yellow = integer("yellow").notNull()
	val red = integer("red").notNull()
	val green = integer("green").notNull()
	val death = integer("death").notNull()
	val egg = integer("egg").notNull()
	val cycles = integer("cycles").notNull()
	val master = integer("master").notNull()
	val god = integer("god").notNull()
}