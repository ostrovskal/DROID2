#include <jni.h>
#include <string>

#include <stdarg.h>
#include <android/log.h>

static long seed = time(nullptr);

static int x, y, w, h, countMapCells;
static uint8_t t, o;
static uint8_t* level = nullptr;
static int* params = nullptr;
static bool droidMove = false;

#define SEED1   214013L
#define SEED2   2531011L
#define SEED3   0x7fffUL

typedef void (*fun)();

static void log_info(const char* pMessage, ...) {
    va_list varArgs;
    va_start(varArgs, pMessage);
    __android_log_vprint(ANDROID_LOG_INFO, "DROID", pMessage, varArgs);
    __android_log_print(ANDROID_LOG_INFO, "DROID", "\n");
    va_end(varArgs);
}

static long resolve(int begin, int end) {
    seed *= SEED1;
    seed += SEED2;
    auto tmp = (seed >> 16) & SEED3;
    return begin + (tmp % (end - begin));
}

static int nextInt(int begin, int end) { return (int)resolve(begin, end); }
static int nextInt(int end) { return (int)resolve(0, end); }
static long nextLong(int begin, int end) { return resolve(begin, end); }
static bool nextBoolean() { return resolve(0, 2) != 0; }

// индексы звуков
constexpr uint32_t SND_DROID_TAKE_EGG   = 3;
constexpr uint32_t SND_DROID_TAKE_MISC  = 4;

// Флаг кнопки контроллера. Без операции
constexpr uint32_t DIRN 			 = 0x00;
constexpr uint32_t DIR0              = 0x01;
constexpr uint32_t DIRU 			 = 0x02;
constexpr uint32_t DIRD 			 = 0x04;
constexpr uint32_t DIRL 			 = 0x08;
constexpr uint32_t DIRR 			 = 0x10;

// индексы параметров планеты
constexpr uint32_t PARAM_SCORE       = 0;
constexpr uint32_t PARAM_TIME        = 1;
constexpr uint32_t PARAM_FUEL        = 2;
constexpr uint32_t PARAM_LIFE        = 3;
constexpr uint32_t PARAM_BOMB        = 4;

constexpr uint32_t PARAM_EYE         = 9;
constexpr uint32_t PARAM_EGG1        = 10;
constexpr uint32_t PARAM_YELLOW1     = 11;
constexpr uint32_t PARAM_RED1        = 12;
constexpr uint32_t PARAM_GREEN1      = 13;
constexpr uint32_t PARAM_IS_DROID    = 14;
constexpr uint32_t PARAM_DROID_BOMB  = 15;
constexpr uint32_t PARAM_SOUND_EXPL  = 16;
constexpr uint32_t PARAM_SOUND_STONE = 17;
constexpr uint32_t PARAM_SOUND_TAKE  = 18;
constexpr uint32_t PARAM_LIMIT       = 19;
constexpr uint32_t PARAM_DROID_X     = 20;
constexpr uint32_t PARAM_DROID_Y     = 21;
constexpr uint32_t PARAM_DROID_BUT   = 22;
constexpr uint32_t PARAM_DROID_GOD   = 23;
constexpr uint32_t PARAM_DROID_CLASSIC= 24;
constexpr uint32_t PARAM_DROID_MASTER= 25;
constexpr uint32_t PARAM_STAT_GREEN  = 26;
constexpr uint32_t PARAM_STAT_RED    = 27;
constexpr uint32_t PARAM_STAT_YELLOW = 28;
constexpr uint32_t PARAM_STAT_EGG    = 29;
constexpr uint32_t PARAM_STAT_SCORE  = 30;

// константы
constexpr uint32_t DROID_ADD_TIME    = 500;
constexpr uint32_t DROID_ADD_FUEL    = 250;
constexpr uint32_t DROID_ADD_BOMB    = 8;
constexpr uint32_t DROID_ADD_SCORE   = 1000;
constexpr uint32_t DROID_ADD_LIMIT   = 2500;

constexpr uint8_t O_BOMB             = 0x1;
constexpr uint8_t O_EXPL             = 0x2;
constexpr uint8_t O_DROID            = 0x3;
constexpr uint8_t O_RED              = 0x4;
constexpr uint8_t O_GREEN            = 0x5;
constexpr uint8_t O_YELLOW           = 0x6;
constexpr uint8_t O_EYE              = 0x7;
constexpr uint8_t O_EGG              = 0x8;
constexpr uint8_t O_EXPLEGG          = 0x9;
constexpr uint8_t O_BOMBDROID        = 0xa;
constexpr uint8_t O_NULL             = 0xb;
constexpr uint8_t O_BETON            = 0xc;
constexpr uint8_t O_BRICK            = 0xd;
constexpr uint8_t O_EARTH            = 0xe;
constexpr uint8_t O_SCORE            = 0xf;
constexpr uint8_t O_TIME             = 0x10;
constexpr uint8_t O_LIFE             = 0x11;
constexpr uint8_t O_BOMBS            = 0x12;
constexpr uint8_t O_FUEL             = 0x13;

// флаги тайлов
constexpr uint8_t MSKT              = 0x003f;
constexpr uint8_t MSKO              = 0x001f; // маска объекта
constexpr uint8_t MSKU              = 0x0040; // признак обновления

// флаги характеристик
constexpr uint16_t FK               = 0x0020; // твари гибнут
constexpr uint16_t FY               = 0x0040; // желтая гибнет
constexpr uint16_t FB               = 0x0080; // падающие камень и бомба взрываются
constexpr uint16_t FN               = 0x0100; // пустота для всех
constexpr uint16_t FG               = 0x0200; // человек и глаз проходит
constexpr uint16_t FS               = 0x0400; // соскок
constexpr uint16_t FM               = 0x0800; // гибель человека
constexpr uint16_t FT               = 0x1000; // человек берет
constexpr uint16_t FE               = 0x2000; // взрывается от другого взрыва

// индексы всех тайлов
constexpr uint8_t T_NULL            = 0;
constexpr uint8_t T_EXPL0           = 7;
constexpr uint8_t T_EXPL1           = 8;
constexpr uint8_t T_EXPL3           = 10;
constexpr uint8_t T_DROIDD          = 11;
constexpr uint8_t T_DROIDR          = 12;
constexpr uint8_t T_DROIDU          = 13;
constexpr uint8_t T_DROIDL          = 14;
constexpr uint8_t T_REDD            = 15;
constexpr uint8_t T_REDR            = 16;
constexpr uint8_t T_REDU            = 17;
constexpr uint8_t T_REDL            = 18;
constexpr uint8_t T_YELLOWD         = 19;
constexpr uint8_t T_YELLOWR         = 20;
constexpr uint8_t T_YELLOWU         = 21;
constexpr uint8_t T_YELLOWL         = 22;
constexpr uint8_t T_BOMB            = 24;
constexpr uint8_t T_STONE0          = 25;
// новые тайлы
constexpr uint8_t T_STONE1          = 26;
constexpr uint8_t T_STONE2          = 27;
constexpr uint8_t T_STONE3          = 28;
constexpr uint8_t T_EGG0            = 29;
constexpr uint8_t T_EGG3            = 32;
constexpr uint8_t T_EYEB            = 35;
constexpr uint8_t T_GREEND          = 36;
constexpr uint8_t T_GREENR          = 37;
constexpr uint8_t T_GREENU          = 38;
constexpr uint8_t T_GREENL          = 39;
constexpr uint8_t T_BOMBDROID       = 40;
constexpr uint8_t T_EXPLEGG         = 41;
constexpr uint8_t T_EXPLDROID0      = 42;
// первый номер падающих тайлов
constexpr uint8_t T_DROP            = (43 - 24);

// смещение бомб дроида
static int offsBombPos[]            = {0, 1, 1, 0, 0, -1, -1, 0};

// смещение координат красных/зеленых тварей
static int offsRG[]                 = {1, 0, T_REDR, 0, 1, T_REDU, -1, 0, T_REDL, 0, -1, T_REDD,
                                       1, 0, T_GREENL, 0, 1, T_GREEND, -1, 0, T_GREENR, 0, -1, T_GREENU};
// смещение координат при формировании взрыва
static int offsExplo[]              = {-1, -1, -1, 0, -1, 1, 0, -1, 0, 0, 0, 1, 1, -1, 1, 0, 1, 1};

// массив смещений для глаза
static int  offsEye[]               = {0, -1, 1};

// массив значений увеличения очков
static int  numScored[]             = {0, 0, 0, -50, 10, 20, 30, 5, 40, 0, 0, 0, 0, 0, 0, -100, 200, 150, 100, 50};

// таблица характиристик тайлов (объкт|флаги)
static uint16_t remapProp[]         = { // T_NULL, T_EARTH
        O_NULL | FN | FG, O_EARTH | FG,
        // T_BETON, T_BRICK,
        O_BETON | FS, O_BRICK,
        // T_LIFE, T_FUEL, T_BOMBS,
        O_LIFE | FK | FT | FB, O_FUEL | FY | FE | FT | FB, O_BOMBS | FY | FE | FT | FB,
        // T_EXPL_0, T_EXPL_1, T_EXPL_2, T_EXPL_3,
        O_EXPL, O_EXPL | FN | FG, O_EXPL | FN | FG, O_EXPL | FN | FG,
        // T_DROID_D, T_DROID_R, T_DROID_U, T_DROID_L,
        O_DROID | FK | FB | FM, O_DROID | FK | FB | FM, O_DROID | FK | FB | FM, O_DROID | FK | FB | FM,
        // T_RED_D, T_RED_R, T_RED_U, T_RED_L,
        O_RED | FM | FB, O_RED | FM | FB, O_RED | FM | FB, O_RED | FM | FB,
        // T_YELLOW_D, T_YELLOW_R, T_YELLOW_U, T_YELLOW_L,
        O_YELLOW | FM | FB, O_YELLOW | FM | FB, O_YELLOW | FM | FB, O_YELLOW | FM | FB,
        // T_EYE_G,
        O_EYE | FY | FB,
        // T_BOMB,
        O_BOMB | FE | FB | FS | FY,
        // T_STONE_0, T_STONE_1, T_STONE_2, T_STONE_3,
        FS, FS, FS, FS,
        // T_EGG_0, T_EGG_1, T_EGG_2, T_EGG_3,
        O_EGG | FT | FS | FB, O_EGG | FT | FS | FB, O_EGG | FT | FS | FB, O_EGG | FT | FS | FB,
        // T_SCORE, T_TIME,
        O_SCORE | FB | FT | FK, O_TIME | FB | FT | FK,
        // T_EYE_B,
        O_EYE | FK | FB,
        // T_GREEN_D, T_GREEN_R, T_GREEN_U, T_GREEN_L,
        O_GREEN | FM | FB, O_GREEN | FM | FB, O_GREEN | FM | FB, O_GREEN | FM | FB,
        // T_BOMB_DROID
        O_BOMBDROID | FS | FB | FY | FE, O_EXPLEGG,
        // T_EXPLDROID0
        O_EXPL,
        // падающие объекты
        // T_BOMB,
        O_BOMB | FE | FB | FS | FY,
        // T_STONE_0, T_STONE_1, T_STONE_2, T_STONE_3,
        FS, FS, FS, FS,
        // T_EGG_0, T_EGG_1, T_EGG_2, T_EGG_3,
        O_EGG | FT | FS | FB, O_EGG | FT | FS | FB, O_EGG | FT | FS | FB, O_EGG | FT | FS | FB,
        // T_NULL, T_NULL,
        O_NULL | FN | FG, O_NULL | FN | FG,
        // T_NULL,
        O_NULL | FN | FG,
        // T_NULL, T_NULL, T_NULL, T_NULL,
        O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG,
        // T_BOMB_DROID
        O_BOMBDROID | FS | FB | FY | FE,
        // T_EXPLDROID0
        O_NULL | FN | FG,
        // T_NULL, T_NULL, T_NULL, T_NULL, T_NULL, T_NULL
        O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG
};

static __inline uint8_t getBuf(int xx, int yy) {
    return *(level + yy * w + xx);
}

static __inline void setBuf(int xx, int yy, uint8_t tile) {
    *(level + yy * w + xx) = tile;
}

static bool isCaps(int xx, int yy, uint16_t msk) {
    return (remapProp[getBuf(xx, yy) & MSKT] & msk) != 0;
}

static void setToMap(int xx, int yy, uint8_t n) {
    if(yy > y || (yy == y && xx > x)) n |= MSKU;
    setBuf(xx, yy, n);
}

static void addScore(int obj, bool isBomb) {
    int score = numScored[obj];
    if(isBomb) score *= 2;
    params[PARAM_SCORE] += score;
    if(params[PARAM_SCORE] < 0) params[PARAM_SCORE] = 0;
    if(params[PARAM_SCORE] >= params[PARAM_LIMIT]) {
        params[PARAM_LIMIT] += params[PARAM_DROID_MASTER] != 0 ? DROID_ADD_LIMIT * 2 : DROID_ADD_LIMIT;
        params[PARAM_LIFE]++;
    }
    // подсчитываем сколько тварей убил
    if(isBomb) {
        switch (obj) {
            case O_RED: params[PARAM_STAT_RED]++; break;
            case O_YELLOW: params[PARAM_STAT_YELLOW]++; break;
            case O_GREEN: params[PARAM_STAT_GREEN]++; break;
        }
    }
    // подсчитываем очки
    params[PARAM_STAT_SCORE] += score;
}

static bool checkKill(int xx, int yy, uint16_t msk) {
    uint8_t oo = (uint8_t)remapProp[getBuf(xx, yy) & MSKT];
    if(!(oo & msk)) return false;
    if(!(oo & msk)) return false;
    oo &= MSKO;
    // проверка признака бога на дроида
    if(oo == O_DROID) {
        if(params[PARAM_DROID_GOD]) return false;
        params[PARAM_IS_DROID] = 0;
    }
    // добавить очки с проверкой на бомбу дроида
    bool bd = oo == O_BOMBDROID;
    addScore(bd ? o : oo, (o == O_BOMBDROID) | bd);
    // поставить взрыв/яичный взрыв
    uint8_t bt = bd ? T_EXPLDROID0 : T_EXPL0;
    if(oo == O_YELLOW || oo == O_RED || oo == O_GREEN) {
        // Гибель существ -> выпадать яйцам или нет?
        if(params[PARAM_DROID_CLASSIC] && nextInt(countMapCells - (params[PARAM_YELLOW1] + params[PARAM_RED1]
        + params[PARAM_GREEN1] + 1)) < 3) bt = T_EXPLEGG;
    }
    setToMap(xx, yy, bt);
    return true;
}

static bool moveDrop() {
    if(t > T_BOMBDROID) {
        // объект уже падает
        setBuf(x, y, T_NULL);
        // падать дальше?
        if(isCaps(x, y + 1, FN)) setToMap(x, y + 1, t);
        else if(o == O_EGG || !checkKill(x, y + 1, FB)) {
            setBuf(x, y, t - T_DROP);
            params[PARAM_SOUND_STONE]++;
            return true;
        }
    } else {
        uint8_t tt = t + T_DROP;
        // падать?
        if (isCaps(x, y + 1, FN)) {
            setBuf(x, y, T_NULL);
            setToMap(x, y + 1, tt);
        } else {
            // соскок?
            if (isCaps(x, y + 1, FS)) {
                int dx = 0;
                if (isCaps(x - 1, y + 1, FN) && isCaps(x - 1, y, FN)) dx = -1;
                else if (isCaps(x + 1, y + 1, FN) && isCaps(x + 1, y, FN)) dx = 1;
                else return false;
                // падаем
                setBuf(x, y, T_NULL);
                setToMap(x + dx, y, tt);
            }
        }
    }
    return false;
}

static void procDrop() {
    if(moveDrop() && (o == O_BOMB || o == O_BOMBDROID)) setToMap(x, y, o == O_BOMBDROID ? T_EXPLDROID0 : T_EXPL0);
}

static void procExpl() {
    bool isExplDroid = t == T_EXPLDROID0;
    if(t == T_EXPL0 || isExplDroid) {
        for(int i = 0 ; i < 8 ; i++) {
            int yy = y + offsExplo[i * 2];
            int xx = x + offsExplo[i * 2 + 1];
            o = (uint8_t)remapProp[getBuf(xx, yy) & MSKT];
            uint8_t oo = o & MSKO;
            if(oo == O_BETON) continue;
            else if(oo == O_DROID) {
                if(params[PARAM_DROID_GOD]) continue;
                params[PARAM_IS_DROID] = 0;
            }
            if(isExplDroid) addScore(oo, true);
            setToMap(xx, yy, (o & FE) ? t : T_EXPL1);
        }
        params[PARAM_SOUND_EXPL]++;
    }
    else setBuf(x, y, (t == T_EXPL3) ? T_NULL : t + (uint8_t)1);
}

static void procRG() {
    int tmp = (t - (o == O_RED ? T_REDD : T_GREEND)) * 3;
    int xx = x + offsRG[tmp + 1];
    int yy = y + offsRG[tmp];
    uint8_t tt = T_NULL;
    if(isCaps(xx, yy, FN)) setToMap(xx, yy, t);
    else if(!checkKill(xx, yy, FK)) tt = (uint8_t)offsRG[tmp + 2];
    setBuf(x, y, tt);
	params[PARAM_RED1 + (o - O_RED)]++;
}

static void procYellow() {
    // смещение координат желтых тварей
    static int offsYellow[]             = {0, 1, T_YELLOWR, 1, 0, T_YELLOWL, -1, 0, T_YELLOWU, 0, 1, T_YELLOWD,
                                           0, -1, T_YELLOWL, -1, 0, T_YELLOWR, 1, 0, T_YELLOWD, 0, -1, T_YELLOWU};
    int s = (t - T_YELLOWD) * 6;
    int xx = x + offsYellow[s + 1];
    int yy = y + offsYellow[s];
    uint8_t tt = T_NULL;
    // смотрим вбок - пусто - поворачиваем
    if(isCaps(xx, yy, FN)) setToMap(xx, yy, (uint8_t)offsYellow[s + 2]);
    // проверяем вбок на гибель
    else if(!checkKill(xx, yy, FY | FK)) {
        xx = x + offsYellow[s + 4];
        yy = y + offsYellow[s + 3];
        // смотрим по направлению движения
        if(isCaps(xx, yy, FN)) setToMap(xx, yy, t);
        // проверяем по направлению движения на гибель
        else if(!checkKill(xx, yy, FY | FK)) tt = (uint8_t)offsYellow[s + 5];
    }
    setBuf(x, y, tt);
    params[PARAM_YELLOW1]++;
}

static void procEgg() {
    bool make = false;
    // запускать ли процесс дрожания яйца?
    if((t == T_EGG0 || t == T_EGG0 + T_DROP) && nextInt(countMapCells - params[PARAM_EGG1]) != 1) t--;
    // вылупиться ? только для 3 яйца
    if(t == T_EGG3 || t == T_EGG3 + T_DROP) {
        t -= 3;
        make = nextInt(countMapCells - params[PARAM_EGG1]) < 5;
        if(make) {
            t = T_REDD + (uint8_t)nextInt(11);
            if(t >= T_YELLOWU) t += 14;
        }
    }
    t++;
    setBuf(x, y, t);
    if(!make) {
        params[PARAM_EGG1]++;
        moveDrop();
    }
}

static void procEye() {
    int limit = (t == T_EYEB ? 5 : 3);
    int rnd = nextInt(countMapCells - params[PARAM_EYE]);
    if(rnd >= 1 && rnd < limit) {
        int xx = x + offsEye[nextInt(3)];
        int yy = y + offsEye[nextInt(3)];
        if(isCaps(xx, yy, FN | FG)) setToMap(xx, yy, isCaps(xx, yy, FT) ? T_EXPL0 : t);
    }
    params[PARAM_EYE]++;
}

static void procExplEgg() {
    for(int i = 0 ; i < 8 ; i++) {
        int xx = x + offsExplo[i * 2 + 1];
        int yy = y + offsExplo[i * 2];
        if(nextBoolean()) {
            o = (uint8_t)remapProp[getBuf(xx, yy) & MSKT];
            if(o & (FN | FG) || o == O_EXPLEGG) setToMap(xx, yy, T_EGG0);
        }
    }
    params[PARAM_SOUND_EXPL]++;
}

static void procDroid() {
    if(params[PARAM_FUEL] <= 0) return;
    uint8_t dir = (uint8_t)params[PARAM_DROID_BUT];
    uint8_t tt = T_NULL;
    droidMove = false;
    int xx = x;
    int yy = y;
    params[PARAM_DROID_BOMB] = 0;
    params[PARAM_IS_DROID] = 1;
    if (dir == DIR0) {
        if (params[PARAM_BOMB] > 0) {
            // кидаем бомбу
            xx = x + offsBombPos[(t - T_DROIDD) * 2];
            yy = y + offsBombPos[(t - T_DROIDD) * 2 + 1];
            if (isCaps(xx, yy, FG)) {
                params[PARAM_DROID_BOMB] = 1;
                setToMap(xx, yy, T_BOMBDROID);
                params[PARAM_BOMB]--;
            }
        }
    } else if (dir != DIRN) {
        if (dir & DIRR) { xx++; tt = T_DROIDR; }
        else if (dir & DIRL) { xx--; tt = T_DROIDL; }
        else {
            if (dir & DIRD) { yy++; tt = T_DROIDD; }
            else if (dir & DIRU) { yy--; tt = T_DROIDU; }
        }
        // движемся
        auto pe = remapProp[getBuf(xx, yy) & MSKT];
        o = (uint8_t)(pe & MSKO);
        // гибель
        if (pe & FM) {
            setToMap(xx, yy, T_EXPL0);
            params[PARAM_IS_DROID] = 0;
        } else {
            droidMove = (pe & (FN | FG | FT)) != 0;
            if (droidMove) {
                // особые случаи
                switch (o) {
                    case O_SCORE:
                        params[PARAM_SCORE] += DROID_ADD_SCORE;
                        break;
                    case O_TIME:
                        params[PARAM_TIME] += DROID_ADD_TIME;
                        break;
                    case O_LIFE:
                        params[PARAM_LIFE]++;
                        break;
                    case O_FUEL:
                        params[PARAM_FUEL] += DROID_ADD_FUEL;
                        break;
                    case O_BOMBS:
                        params[PARAM_BOMB] += params[PARAM_DROID_MASTER] != 0 ? DROID_ADD_BOMB / 2 : DROID_ADD_BOMB;
                        break;
                }
                if (pe & FT) {
                    params[PARAM_SOUND_TAKE] = o == O_EGG ? SND_DROID_TAKE_EGG : SND_DROID_TAKE_MISC;
                    addScore(o, false);
                    // подсчитываем сколько яиц собрал
                  if(o == O_EGG) params[PARAM_STAT_EGG]++;
                }
            } else {
                auto nn = getBuf(xx, yy);
                switch (nn) {
                    case T_STONE0:
                    case T_STONE1:
                    case T_STONE2:
                    case T_STONE3:
                    case T_BOMB:
                    case T_BOMBDROID: {
                        // сдвигаем камень/бомбу
                        int dx = 0;
                        if (tt == T_DROIDL) dx = -1;
                        else if (tt == T_DROIDR) dx = 1;
                        if (isCaps(xx + dx, yy, FN)) {
                            setBuf(xx + dx, yy, nn);
                            droidMove = true;
                        }
                    }
                }
            }
            if(droidMove) {
                setBuf(x, y, T_NULL);
                setToMap(xx, yy, tt);
                params[PARAM_FUEL]--;
                params[PARAM_DROID_X] = xx;
                params[PARAM_DROID_Y] = yy;
            }
        }
    }
}

static fun objHundlers[] = { &procDrop, &procDrop, &procExpl, &procDroid, &procRG, &procRG,
                             &procYellow, &procEye, &procEgg, &procExplEgg, &procDrop, &procExpl };

extern "C" {
    JNIEXPORT bool Java_ru_ostrovskal_droid_views_ViewGame_processBuffer(JNIEnv *env, jclass, jbyteArray buffer,
                                                                         jintArray pars, jboolean delay) {
        jboolean isCopy;
        droidMove = false;
        auto b = (jbyte *) env->GetPrimitiveArrayCritical(buffer, &isCopy);
        params = (jint *) env->GetPrimitiveArrayCritical(pars, &isCopy);
        memset(&params[PARAM_EYE], 0, 10 * sizeof(jint));
        auto buf = (uint8_t *) b;
        w = *buf++;
        h = *buf++;
        countMapCells = w * h;
        level = buf;
        for (int yy = 0; yy < h; yy++) {
            y = yy;
            for (int xx = 0; xx < w; xx++) {
                x = xx;
                t = *buf;
                if (t & MSKU) *buf &= MSKT;
                else {
                    o = (uint8_t) remapProp[t] & MSKO;
                    if (o <= O_BOMBDROID && (o == O_DROID || delay)) objHundlers[o]();
                }
                buf++;
            }
        }
        env->ReleasePrimitiveArrayCritical(buffer, b, JNI_ABORT);
        env->ReleasePrimitiveArrayCritical(pars, params, JNI_ABORT);
        return droidMove;
    }
}
