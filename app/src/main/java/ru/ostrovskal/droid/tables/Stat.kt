package ru.ostrovskal.droid.tables

import com.github.ostrovskal.ssh.Constants.*
import com.github.ostrovskal.ssh.STORAGE
import com.github.ostrovskal.ssh.SqlField
import com.github.ostrovskal.ssh.sql.Table
import com.github.ostrovskal.ssh.utils.flags
import com.github.ostrovskal.ssh.utils.info
import com.github.ostrovskal.ssh.utils.optText
import com.github.ostrovskal.ssh.utils.release
import ru.ostrovskal.droid.Constants.*
import java.util.*
import kotlin.experimental.or

object Stat: Table() {
	@JvmField val id = integer("_id").notNull().primaryKey()
	@JvmField val fRec = blob("rec").notNull()
	@JvmField val fMaps = blob("maps").notNull()
	@JvmField val fAuth = text("author").notNull()
	@JvmField val fPack = text("pack").notNull()
	@JvmField val fTime = integer("time").notNull()
	@JvmField val fPlanet = text("planet").notNull()
	@JvmField val fFuel = integer("fuel").notNull()
	@JvmField val fScore = integer("score").notNull()
	@JvmField val fCount = integer("count").notNull()
	@JvmField val fBomb = integer("bomb").notNull()
	@JvmField val fDate = integer("date").notNull()
	@JvmField val fYellow = integer("yellow").notNull()
	@JvmField val fRed = integer("red").notNull()
	@JvmField val fGreen = integer("green").notNull()
	@JvmField val fDeath = integer("death").notNull()
	@JvmField val fEgg = integer("egg").notNull()
	@JvmField val fCycles = integer("cycles").notNull()
	@JvmField val fMaster = integer("master").notNull()
	@JvmField val fGod = integer("god").notNull()
	@JvmField val fSeed = integer("seed").notNull()
	
	// все действия игрока
	@STORAGE @SqlField("") @JvmField var rec        = byteArrayOf()
	// карты на момент запуска
	@STORAGE @SqlField("") @JvmField var maps       = byteArrayOf()
	// начальный для random
	@STORAGE @SqlField("") @JvmField var date       = 0L
	// топливо затраченное
	@STORAGE @SqlField("") @JvmField var fuel       = 0
	// очков заработано
	@STORAGE @SqlField("") @JvmField var score      = 0
	// всего планет зачищено
	@STORAGE @SqlField("") @JvmField var count      = 0
	// всего бомб потрачено
	@STORAGE @SqlField("") @JvmField var bomb       = 0
	// время потраченное на игру
	@STORAGE @SqlField("") @JvmField var time       = 0L
	// всего желтых убито
	@STORAGE @SqlField("") @JvmField var yellow     = 0
	// всего красных убито
	@STORAGE @SqlField("") @JvmField var red        = 0
	// всего зеленых убито
	@STORAGE @SqlField("") @JvmField var green      = 0
	// сколько раз умирал
	@STORAGE @SqlField("") @JvmField var death      = 0
	// всего яиц собрал
	@STORAGE @SqlField("") @JvmField var egg        = 0
	// всего игровых циклов
	@STORAGE @SqlField("") @JvmField var cycles     = 0
	// игра в режиме мастера
	@STORAGE @SqlField("") @JvmField var master     = false
	// игра в режиме бога
	@STORAGE @SqlField("") @JvmField var god        = false
	// seed псевдослучайной величины на момент завершения(для самопроверки состояния)
	@STORAGE @SqlField("") @JvmField var seed       = 0L
	
	@STORAGE @JvmField var name							= ""
	@STORAGE @JvmField var isRecord						= false
	@STORAGE @JvmField var statID						= 0L
	@STORAGE @JvmField var posMap						= 0
	@STORAGE @JvmField var posNum						= 0
	// смещение в битах записи игры
	@STORAGE @JvmField var pos 	 						= 0
	// для подсчета времени игры
	@STORAGE @JvmField var startTime					= 0L
	
	fun init(begin: Boolean, master: Boolean, god: Boolean, id: Long)
	{
		isRecord = id != 0L
		if(isRecord) {
			if(begin) {
				statID = id
				pos = 0
				posMap = 0
				posNum = -1
			}
		}
		else {
			if(begin) {
				date = System.currentTimeMillis()
				startTime = date
				// сбрасываем значения
				time = 0
				fuel = 0; score = 0; count = 0; bomb = 0
				yellow = 0; red = 0; green = 0; egg = 0
				death = 0; cycles = 0
				name = ""
				pos = 0
				rec = byteArrayOf()
				maps = byteArrayOf()
				this.master = master
				this.god = god
			} else {
				if(name != Planet.MAP.name) {
					Planet.MAP.apply {
						val nameBytes = name.toByteArray()
						val tmp = ByteArray(maps.size + buffer.size + nameBytes.size + 5)
						// копируем старый во временный
						System.arraycopy(maps, 0, tmp, 0, maps.size)
						tmp[maps.size] = nameBytes.size.toByte()
						// копируем имя планеты
						System.arraycopy(nameBytes, 0, tmp, maps.size + 1, nameBytes.size)
						// копируем время и топливо
						val dstPos = maps.size + 1 + nameBytes.size
						tmp[dstPos + 0] = (time / 100).toByte()
						tmp[dstPos + 1] = (time % 100).toByte()
						tmp[dstPos + 2] = (fuel / 100).toByte()
						tmp[dstPos + 3] = (fuel % 100).toByte()
						// копируем новый во временный
						System.arraycopy(buffer, 0, tmp, maps.size + nameBytes.size + 5, buffer.size)
						Stat.name = name
						maps = tmp
					}
				}
			}
		}
	}
	
	fun save(seed: Long) {
		if(!isRecord) {
			// поместить в БД
			// обрезать массив записи в соответствии с реальным объемом
			val size = pos / 8 + if(pos flags 4) 1 else 0
			rec = Arrays.copyOf(rec, size)
			// поместить в БД
			insert {
				it.autoValues(this@Stat)
				it[fAuth] = KEY_PLAYER.optText
				it[fPack] = Planet.MAP.pack
				it[fPlanet] = Planet.MAP.name
				it[fSeed] = seed
			}
		} else {
			if(this.seed != seed) "seed записи <${this.seed}> не соответствует текущему seed <$seed>!".info()
		}
	}
	
	fun add(time: Int, x: Int, y: Int, dropBomb: Boolean, suicide: Boolean = false) {
		if(!isRecord) {
			if(time > 0) {
				var action = DIRS
				val idx = pos / 8
				if(idx >= rec.size) {
					// увеличиваем массив записи
					val tmp = ByteArray(idx + time / 2 + (time and 1))
					// копируем старый во временный
					System.arraycopy(rec, 0, tmp, 0, rec.size)
					rec = tmp
				}
				if(!suicide) {
					val pt = Planet.MAP.droidPos()
					action = if(dropBomb) REC_DROID_DROP else REC_DROID_NONE
					if(x != pt.x) {
						action = REC_DROID_MOVE or when {
							x < pt.x -> REC_DROID_MOVE_RIGHT
							x > pt.x -> REC_DROID_MOVE_LEFT
							else     -> 0
						}
					}
					if(y != pt.y) {
						action = REC_DROID_MOVE or when {
							y < pt.y -> REC_DROID_MOVE_DOWN
							y > pt.y -> REC_DROID_MOVE_UP
							else     -> 0
						}
					}
				}
				// устанавливаем
				val v = rec[idx]
				val s = pos and 4
				val r = (action shl s).toByte()
				rec[idx] = v or r
				pos += 4
			}
		}
	}
	
	fun load(position: Int): Boolean
	{
		if(posMap == 0) {
			// грузим себя из БД
			select(fMaps, fRec, fMaster, fGod, fSeed) { where { id eq statID } }.execute()?.release {
				autoValues(this@Stat)
			}
		}
		if(maps.isNotEmpty() && rec.isNotEmpty()) {
			if(posNum == -1) posNum = position
			if(position != posNum) {
				var l = maps[posMap].toInt() + 5
				l += (maps[posMap + l].toInt() * maps[posMap + l + 1].toInt()) + 2
				posMap += l
				posNum = position
			}
			// инициализируем данные
			if(posMap >= maps.size) return false
			val l 			= maps[posMap].toInt() + 1
			Planet.MAP.name 	= String(maps.copyOfRange(posMap + 1, posMap + l))
			Planet.MAP.time		= maps[posMap + l + 0].toInt() * 100 + maps[posMap + l + 1].toInt()
			Planet.MAP.fuel		= maps[posMap + l + 2].toInt() * 100 + maps[posMap + l + 3].toInt()
			val szBuffer	= maps[posMap + l + 4].toInt() * maps[posMap + l + 5].toInt() + 2
			Planet.MAP.buffer	= maps.copyOfRange(posMap + l + 4, posMap + l + 4 + szBuffer)
			return Planet.MAP.searchEntities()
		}
		return false
	}
	
	fun control(time: Int): Int {
		if(time <= 0 && pos >= (rec.size * 8)) return DIRN
		val sh = pos and 4
		val v = (rec[pos / 8].toInt() shr sh) and DIRS
		val dir = when {
			v flags REC_DROID_DROP -> DIR0// кидаем бомбу ?
			v flags REC_DROID_MOVE -> 2 shl (v shr 2)// движемся ?
			else                   -> DIRN
		}
		pos += 4
		return if(v == DIRS) DIRS else dir
	}
	
	override fun toString() = "Stat"
}