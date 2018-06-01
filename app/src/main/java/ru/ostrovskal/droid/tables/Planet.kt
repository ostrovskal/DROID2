@file:Suppress("NOTHING_TO_INLINE")

package ru.ostrovskal.droid.tables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.github.ostrovskal.ssh.Constants.FOLDER_FILES
import com.github.ostrovskal.ssh.SqlField
import com.github.ostrovskal.ssh.sql.Rowset
import com.github.ostrovskal.ssh.sql.RuleOption
import com.github.ostrovskal.ssh.sql.Table
import com.github.ostrovskal.ssh.utils.*
import ru.ostrovskal.droid.Constants.*
import ru.ostrovskal.droid.R
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

object Planet: Table() {
	@JvmField val id              = integer("_id").notNull().primaryKey()
	@JvmField val system          = text("system").notNull().references(Pack.name, RuleOption.CASCADE, RuleOption.CASCADE)
	@JvmField val title           = text("title").notNull().unique()
	@JvmField val creator         = text("creator").notNull().default("OSTROV")
	@JvmField val position        = integer("position").notNull().unique().checked { it.between(0, 100) }
	@JvmField val cycles          = integer("cycles").notNull()
	@JvmField val traffic         = integer("traffic").notNull()
	@JvmField val blocked         = integer("blocked").notNull().default(1)
	@JvmField val content         = blob("content").notNull()
	@JvmField val create          = integer("create").notNull()
	
	@JvmField val rnd   = Random(System.currentTimeMillis())

	object MAP {
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
		
		@JvmField var x         = 0
		@JvmField var y         = 0

		val width               get() = buffer[0].toInt()
		val height              get() = buffer[1].toInt()
		
		// Получить планету по позиции
		private fun ofPosition(pos: Int, pack: String): Rowset? {
			select(position) {
				where { system eq pack }
				orderBy(position)
			}.execute()?.apply {
				// берем уровень по позиции
				if(move(pos)) {
					val num = integer(Planet.position)
					close()
					return select { where { system.eq(pack) and Planet.position.eq(num) } }.execute()
				}
			}
			return null
		}
		
		// Генерировать монстра
		private fun genMonster(x: Int, y: Int): Byte {
			if(rnd.nextInt(5) == 0) {
				val tile = if(x flags 1) T_YELLOWD else if(y flags 1) T_REDD else T_GREEND
				return (tile + rnd.nextInt(4)).toByte()
			}
			return T_EARTH
		}
		
		// Сбросить в исходное состояние
		private fun reset()
		{
			name = ""; auth = ""; pack = ""
			num = -1; time = 0; fuel = 0; date = -1
			block = true
			x = -1; y = -1
			egg = 0; yellow = 0; red = 0; green = 0
			buffer = byteArrayOf(0, 0)
		}
		
		// Найти объекты на карте
		private fun searchEntities(): Boolean
		{
			x = -1; y = -1
			yellow 	= 0; red 	= 0; green 	= 0; egg 	= 0
			for(idx in buffer.indices) {
				if(idx > 1) {
					when(remapProp[buffer[idx].toInt() and MSKT] and MSKO) {
						O_DROID		-> { x = (idx - 2) % width; y = (idx - 2) / width }
						O_YELLOW	-> yellow++
						O_RED		-> red++
						O_GREEN		-> green++
						O_EGG		-> egg++
					}
				}
			}
			return (x != -1 && y != -1)
		}
		
		// Сохранить в папку программы
		private fun save()
		{
			val sb = StringBuilder(width * height + name.length + 10 * 6)
			// запомнить название, автор, габариты, время, топливо
			sb.append("$num\n").append("$auth\n").append("$width\n").append("$height\n").
					append("$time\n").append("$fuel\n")
			// перекодировка
			for(y in 0 until height) {
				for(x in 0 until width) sb.append(charsOfPlanetMap[buffer[x, y] and MSKT])
				sb.append('\n')
			}
			// запись
			makeDirectories("planets/$pack", FOLDER_FILES, "$name.pl").writeText(sb.toString())
		}
		
		// Создать превью планеты
		private fun preview(context: Context, blockSize: Int): Bitmap?
		{
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
			for(y in 0 until height) {
				dst.top = offsY + y * blockSize
				dst.bottom = dst.top + blockSize
				for(x in 0 until width) {
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
		private fun miniature(context: Context)
		{
			val bmp = preview(context, BLOCK_MINIATURE) ?: return
			FileOutputStream(makeDirectories("miniatures/$pack", FOLDER_FILES, "$name.png")).release {
				bmp.compress(Bitmap.CompressFormat.PNG, 100, this)
			}
			bmp.recycle()
		}
		
		inline fun droidNull() { x = -1; y = -1 }
		
		inline fun droidIsNull() = ( x == -1 || y == -1 )
		
		// Создать классическую планету из папки активов
		fun make(context: Context, pck: String, nm: String) = try {
			var width = 0
			var idx = 0
			// загрузка
			context.assets.open("$pck/$nm.pl").reader().release {
				forEachLine {
					val v = it.ival(0, 10)
					when(idx++) {
						0    -> num = v.toLong()
						1    -> auth = it
						2    -> width = v
						3    -> { buffer = ByteArray(width * v + 2); buffer[0] = width.toByte(); buffer[1] = v.toByte() }
						4    -> time = v
						5    -> fuel = v
						else -> {
							val row = idx - 7
							for(col in 0 until width) {
								var tile = charsOfPlanetMap.search(it[col], T_NULL.toInt())
								val obj = remapProp[tile] and MSKO
								when(obj) {
									O_DROID -> { x = col; y = row }
									O_STONE -> tile = T_STONE0 + rnd.nextInt(4)
								}
								buffer[col, row] = tile
							}
						}
					}
				}
			}
			name = nm
			pack = pck
			true
		} catch(e: IOException) {
			reset()
			false
		}

		// Генерировать планету
		fun generator(context: Context, type: Int): Int
		{
			buffer = ByteArray(width * height + 2)
			buffer[0] = width.toByte(); buffer[1] = height.toByte()
			
			for(y in 0 until height) {
				for(x in 0 until width) {
					buffer[x, y] = if(x == 0 || y == 0 || x == width - 1 || y == height - 1) T_BETON
					else when(type) {
						PLANET_GEN_EARTH -> when(rnd.nextInt(4)) {
							0, 1 -> T_EARTH
							2    -> T_BOMB
							else -> (T_STONE0 + rnd.nextInt(4)).toByte()
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
							0, 1, 2 -> T_EARTH
							3       -> (T_STONE0 + rnd.nextInt(4)).toByte()
							4       -> genMonster(x, y)
							5       -> T_BOMB
							else    -> T_NULL
						}
						PLANET_GEN_EGG   -> when(rnd.nextInt(7)) {
							0, 1, 2 -> T_EARTH
							3       -> (T_EGG0 + rnd.nextInt(4)).toByte()
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
						else             -> T_NULL
					}
				}
			}
			buffer[1, 1] = T_DROIDR
			block = true
			store(context)
			return 0
		}

		// Добавить или обновить планету в БД
		fun store(context: Context): Boolean
		{
			if(!searchEntities()) {
				context.resources.getString(R.string.droid_not_found, name).debug()
				return false
			}
			miniature(context)
			save()
			if(exist( { system.eq(pack) and position.eq(num) } )) {
				update { autoValues(this@MAP) }
			} else {
				insert { it.autoValues(this@MAP) }
				Pack.changeCountPlanets(SYSTEM_DEFAULT, true)
			}
			return true
		}

		// Удалить планету
		fun delete()
		{
			// удалить текстовое представление карты
			makeDirectories("planets/$pack", FOLDER_FILES, "$name.pl").delete()
			// удалить миниатюру
			makeDirectories("miniatures/$pack", FOLDER_FILES, "$name.png").delete()
			// удалить из БД
			delete { where { system.eq(pack) and position.eq(num) and title.eq(name) } }
			// Изменить количество планет в системе
			Pack.changeCountPlanets(pack, false)
			// сброс
			reset()
		}
		
		// Загрузить из БД
		fun load(pos: Int): Boolean
		{
			val pck = KEY_TMP_PACK.optText
			ofPosition(pos, pck)?.release {
				autoValues(this@MAP)
				searchEntities()
				return true
			}
			reset()
			return false
		}
	}
	
	// Создать планеты классической системы
	fun default(context: Context) {
		// запустить в отдельном потоке
		thread {
			delete { where { system eq SYSTEM_DEFAULT } }
			// проверить содержимое папки assets/classic
			val classic = context.assets.list(SYSTEM_DEFAULT)
			if(classic.size < 30) error("System of assets\\$SYSTEM_DEFAULT not found!")
			classic.forEach {
				var res = MAP.make(context, SYSTEM_DEFAULT, it.substring(0, it.lastIndexOf('.')))
				if(res) res = MAP.store(context)
				if(!res) context.resources.getString(R.string.error_make_or_save, MAP.name).debug()
			}
		}
	}
}