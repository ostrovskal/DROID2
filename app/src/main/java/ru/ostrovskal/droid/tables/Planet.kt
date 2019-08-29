@file:Suppress("NOTHING_TO_INLINE")

package ru.ostrovskal.droid.tables

import android.content.Context
import android.graphics.*
import com.github.ostrovskal.ssh.Constants.FOLDER_FILES
import com.github.ostrovskal.ssh.Rand
import com.github.ostrovskal.ssh.SqlField
import com.github.ostrovskal.ssh.sql.RuleOption
import com.github.ostrovskal.ssh.sql.Table
import com.github.ostrovskal.ssh.utils.*
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread

object Planet: Table() {
	@JvmField val id              = integer("_id").notNull().primaryKey()
	@JvmField val system          = text("system").notNull().references(Pack.name, RuleOption.CASCADE, RuleOption.CASCADE)
	@JvmField val title           = text("title").notNull()
	@JvmField val creator         = text("creator").notNull().default("OSTROV")
	@JvmField val position        = integer("position").notNull().checked { it.between(0, 101) }
	@JvmField val cycles          = integer("cycles").notNull()
	@JvmField val traffic         = integer("traffic").notNull()
	@JvmField val blocked         = integer("blocked").notNull().default(1)
	@JvmField val content         = blob("content").notNull()
	@JvmField val create          = integer("create").notNull()
	
	@SqlField("position") @JvmField var num       = 0L
	@SqlField("traffic")  @JvmField var fuel      = 0
	@SqlField("cycles")   @JvmField var time      = 0
	@SqlField("create")   @JvmField var date      = 0L
	@SqlField("blocked")  @JvmField var block     = true
	@SqlField("content")  @JvmField var buffer    = byteArrayOf(0, 0)
	
	@SqlField("creator")  @JvmField var auth      = ""
	@SqlField("title")    @JvmField var name      = ""
	@SqlField("system")   @JvmField var pack      = ""
	
	@JvmField var egg       = 0
	@JvmField var yellow    = 0
	@JvmField var red       = 0
	@JvmField var green     = 0
	
	// Псевдослучайная последовательность
	@JvmField val rnd       = Rand()
	
	// Признак подготовки карты
	@JvmField var useMap    = false
	
	// Позиция дроида
	@JvmField val pointDroid = Point(-1, -1)
	
	val width               get() = buffer[0].toInt()
	val height              get() = buffer[1].toInt()
	
	// Генерировать монстра
	private fun genMonster(x: Int, y: Int): Byte {
		if(rnd.nextInt(5) == 0) {
			val tile = if(x test 1) T_YELLOWD else if(y test 1) T_REDD else T_GREEND
			return (tile + rnd.nextInt(4)).toByte()
		}
		return T_EARTH
	}
	
	// Сбросить в исходное состояние
	private fun reset() {
		name = ""; auth = ""
		num = -1; time = 0; fuel = 0
		block = true
		droidNull()
		egg = 0; yellow = 0; red = 0; green = 0
		buffer = byteArrayOf(0, 0)
	}
	
	// Найти объекты на карте
	fun searchEntities(): Boolean {
		droidNull()
		yellow 	= 0; red 	= 0; green 	= 0; egg 	= 0
		for(idx in buffer.indices) {
			if(idx > 1) {
				when(remapProp[buffer[idx].toInt() and MSKT] and MSKO) {
					O_DROID		-> droidPos((idx - 2) % width, (idx - 2) / width)
					O_YELLOW	-> yellow++
					O_RED		-> red++
					O_GREEN		-> green++
					O_EGG		-> egg++
				}
			}
		}
		return !droidIsNull()
	}
	
	// Сохранить в папку программы
	private fun save() {
		val sb = StringBuilder(width * height + name.length + 10 * 6)
		// запомнить номер, автора, время, топливо
		sb.append("$num\n").append("$auth\n").append("$time\n").append("$fuel\n")
		// перекодировка
		repeat(height) { y ->
			repeat(width) { sb.append(charsOfPlanetMap[buffer[it, y] and MSKT]) }
			sb.append('\n')
		}
		// запись
		makeDirectories("planets/$pack", FOLDER_FILES, "$name.pl").writeText(sb.toString())
	}
	
	// Создать превью планеты
	private fun preview(context: Context, blockSize: Int): Bitmap? {
		val dst = Rect()
		val src = Rect()
		val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
		// определяем максимальный габарит
		val maxWidth = if(width > height) width else height
		val sz = blockSize * maxWidth
		// выравнивание по центру
		val offsX = (sz - blockSize * width) / 2
		val offsY = (sz - blockSize * height) / 2
		// создаем битмапы
		val tiles = context.createBitmap(R.drawable.custom_sprites) ?: return null
		val min = Bitmap.createBitmap(sz, sz, Bitmap.Config.ARGB_8888) ?: return null
		val canvas = Canvas(min)
		val cols = 10
		val wh = tiles.width / cols
		// стираем превью
		canvas.drawColor(0)
		// нарисовать в битмап всю планету
		repeat(height) {y ->
			dst.top = offsY + y * blockSize
			dst.bottom = dst.top + blockSize
			repeat(width) { x ->
				val v = remapTiles[buffer[x, y] and MSKT]
				src.left = v % cols * wh
				src.top = v / cols * wh
				src.right = src.left + wh
				src.bottom = src.top + wh
				dst.left = offsX + x * blockSize
				dst.right = dst.left + blockSize
				canvas.drawBitmap(tiles, src, dst, mPaint)
			}
		}
		tiles.recycle()
		return min
	}
	
	// Создать миниатюру
	private fun miniature(context: Context) {
		val bmp = preview(context, BLOCK_MINIATURE) ?: return
		FileOutputStream(makeDirectories("miniatures/$pack", FOLDER_FILES, "$name.png")).release {
			bmp.compress(Bitmap.CompressFormat.PNG, 100, this)
		}
		bmp.recycle()
	}
	
	inline fun droidNull() { pointDroid.set(-1, -1) }
	
	inline fun droidIsNull() = ( pointDroid.x == -1 || pointDroid.y == -1 )
	
	inline fun droidPos() = pointDroid
	
	inline fun droidPos(x: Int, y: Int) { pointDroid.set(x, y) }
	
	// Генерировать планету
	fun generator(context: Context, type: Int) {
		val w = width
		val h = height
		buffer = byteArrayOf2D(w, h)

		repeat(h) { y ->
			repeat(w) {x ->
				buffer[x, y] = if(x == 0 || y == 0 || x == w - 1 || y == h - 1) T_BETON
				else when(type) {
					PLANET_GEN_EARTH -> when(rnd.nextInt(7)) {
						0, 1, 2  -> T_EARTH
						3        -> T_BOMB
						4        -> genMonster(x, y)
						5        -> T_NULL
						else     -> (T_STONE0 + rnd.nextInt(4)).toByte()
					}
					PLANET_GEN_BETON -> when(rnd.nextInt(6)) {
						0    -> T_NULL
						1    -> T_EARTH
						2    -> T_BRICK
						3    -> genMonster(x, y)
						4    -> T_BOMB
						else -> T_BETON
					}
					PLANET_GEN_STONE -> when(rnd.nextInt(7)) {
						0, 1    -> T_EARTH
						2, 3    -> (T_STONE0 + rnd.nextInt(4)).toByte()
						4       -> genMonster(x, y)
						5       -> T_BOMB
						else    -> T_NULL
					}
					PLANET_GEN_EGG   -> when(rnd.nextInt(7)) {
						0, 1    -> T_EARTH
						2, 3    -> (T_EGG0 + rnd.nextInt(4)).toByte()
						4       -> genMonster(x, y)
						5       -> T_BOMB
						else    -> T_NULL
					}
					PLANET_GEN_BOMB  -> when(rnd.nextInt(8)) {
						0, 1, 2 -> T_EARTH
						3       -> genMonster(x, y)
						4, 5    -> T_BOMB
						6       -> (T_STONE0 + rnd.nextInt(4)).toByte()
						else    -> T_NULL
					}
					else            -> T_NULL
				}
			}
		}
		buffer[1, 1] = T_DROIDR
		block = true
		useMap = false
		store(context)
	}
	
	// Добавить или обновить планету в БД
	fun store(context: Context): Boolean {
		if(!searchEntities()) {
			context.resources.getString(R.string.droid_not_found, name).debug()
			return false
		}
		miniature(context)
		save()
		val result: Boolean
		if(exist { system.eq(pack) and position.eq(num) } ) {
			result = update {
				autoValues(Planet)
				where { position.eq(num) and system.eq(pack) }
			} > 0L
		} else {
			date = System.currentTimeMillis()
			if(auth.isEmpty()) auth = KEY_PLAYER.optText
			result = insert { it.autoValues(Planet) } > 0L
			if(result) Pack.changeCountPlanets(pack, true)
		}
		return result
	}
	
	// Удалить планету
	fun delete(isReset: Boolean): Boolean {
		// удалить из БД
		if(delete { where { system.eq(pack) and position.eq(num) and title.eq(name) } } > 0L) {
			// Изменить количество планет в системе
			Pack.changeCountPlanets(pack, false)
			// Сжать номера
			Planet.select(position) {
				where { system.eq(pack) and position.greater(num) }
				orderBy(position, true)
			}.execute()?.release {
				forEach { _ ->
					val n = integer(0)
					update {
						it[Planet.position] = n - 1
						where { system.eq(pack) and Planet.position.eq(n) }
					}
				}
			}
			if(isReset) {
				// удалить текстовое представление карты
				makeDirectories("planets/$pack", FOLDER_FILES, "$name.pl").delete()
				// удалить миниатюру
				makeDirectories("miniatures/$pack", FOLDER_FILES, "$name.png").delete()
				// сброс
				reset()
			}
			return true
		}
		return false
	}
	
	// Загрузить из БД
	fun load(pos: Int): Boolean {
		useMap = false
		select { where { position.eq(pos.toLong()) and system.eq(pack) } }.execute()?.release {
			autoValues(Planet)
			if( searchEntities() ) return true
		}
		reset()
		return false
	}
	
	// Создать планеты классической системы
	fun default(context: Context) {
		// запустить в отдельном потоке
		thread {
			delete { where { system eq SYSTEM_DEFAULT } }
			// проверить содержимое папки assets/classic
			val classic = context.assets.list(SYSTEM_DEFAULT) ?: error("System of assets\\$SYSTEM_DEFAULT not found!")
			// создать классические планеты
			pack = SYSTEM_DEFAULT
			classic.forEach {
				name = it.substringBeforeLast('.')
				var res = true
				try {
					val map = context.assets.open("$SYSTEM_DEFAULT/$it").reader().releaseRun { readLines() }
					num = map[0].toLong()
					auth = map[1]
					time = map[2].toInt()
					fuel = map[3].toInt()
					buffer = byteArrayOf2D(map[4].length, map.size - 4)
					
					repeat(height) {y ->
						repeat(width) {x ->
							var tile = charsOfPlanetMap.search(map[y + 4][x], T_NULL.toInt())
							if((remapProp[tile] and MSKO) == O_STONE) tile = T_STONE0 + rnd.nextInt(4)
							buffer[x, y] = tile
						}
					}
				} catch(e: IOException) {
					reset()
					res = false
				}
				if(res) res = store(context)
				if(!res) context.resources.getString(R.string.error_make_or_save, name).debug()
			}
		}
	}
}