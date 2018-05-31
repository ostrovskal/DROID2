package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.sql.RuleOption
import com.github.ostrovskal.ssh.sql.Table

object Stat: Table() {
	@JvmField val id = integer("_id").notNull().primaryKey()
	@JvmField val rec = integer("rec").references(Record.id, RuleOption.CASCADE, RuleOption.CASCADE)
	@JvmField val auth = text("author").notNull()
	@JvmField val pack = text("pack").notNull().references(Pack.name, RuleOption.CASCADE, RuleOption.CASCADE)
	@JvmField val planet = text("planet").notNull()
	@JvmField val time = integer("time").notNull()
	@JvmField val fuel = integer("fuel").notNull()
	@JvmField val score = integer("score").notNull()
	@JvmField val count = integer("count").notNull()
	@JvmField val bomb = integer("bomb").notNull()
	@JvmField val date = integer("date").notNull()
	@JvmField val yellow = integer("yellow").notNull()
	@JvmField val red = integer("red").notNull()
	@JvmField val green = integer("green").notNull()
	@JvmField val death = integer("death").notNull()
	@JvmField val egg = integer("egg").notNull()
	@JvmField val cycles = integer("cycles").notNull()
	@JvmField val master = integer("master").notNull()
	@JvmField val god = integer("god").notNull()
}