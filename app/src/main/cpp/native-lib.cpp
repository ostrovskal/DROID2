#include <jni.h>
#include <string>

#include <stdarg.h>
#include <android/log.h>

static long seed = time(nullptr);

static int w, h, countMapCells;
static uint8_t t, o;
static uint8_t* level = nullptr;
static uint8_t* buf = nullptr;
static int* params = nullptr;
static bool droidMove = false;
static jobject mBuffer = nullptr;
static jobject mParams = nullptr;
static uint8_t* ret_addr = nullptr;

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

constexpr uint8_t O_STONE            = 0x0;
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
constexpr uint8_t T_BETON           = 2;
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

// смещения вокруг исходной точки
static int offsets[]                = {0, 0, 0, 1, 0, 0, 0, -1, 0};

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

static __inline bool isCaps(uint16_t msk) {
    return (remapProp[*ret_addr & MSKT] & msk) != 0;
}

static bool isCapsIdx(int idx, uint16_t msk) {
    ret_addr = buf + offsets[idx];
    return isCaps(msk);
}

static __inline void setToMap(uint8_t n) {
    *ret_addr = (uint8_t)(n | (ret_addr > buf ? MSKU : 0));
}

static void setToMapIdx(int idx, uint8_t n) {
    ret_addr = buf + offsets[idx];
    setToMap(n);
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

static bool checkKill(uint16_t msk) {
    uint8_t oo = (uint8_t)remapProp[*ret_addr & MSKT];
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
        if(params[PARAM_DROID_CLASSIC] == 0 && nextInt(countMapCells - (params[PARAM_YELLOW1] + params[PARAM_RED1]
                                                                        + params[PARAM_GREEN1] + 1)) < 3) bt = T_EXPLEGG;
    }
    setToMap(bt);
    return true;
}

static bool moveDrop() {
    auto is = isCapsIdx(1, FN);
    auto tc = T_NULL;
    auto tt = t;
    // объект уже падает?
    if(t > T_BOMBDROID) {
        // падать дальше?
        if(!is) {
            *buf = T_NULL;
            if(o != O_EGG) { if (checkKill(FB)) return false; }
            *buf = t - T_DROP;
            if (o == O_STONE) params[PARAM_SOUND_STONE]++;
            return true;
        }
    } else {
        // падать?
        if (is) tt = t + T_DROP;
        else {
            // соскок?
            if (!isCapsIdx(1, FS)) return false;
            if (isCapsIdx(8, FN) && isCapsIdx(7, FN)) {}
            else if (!isCapsIdx(2, FN) || !isCapsIdx(3, FN)) return false;
        }
    }
    *buf = tc;
    setToMap(tt);
    return false;
}

static void procDrop() {
    if(moveDrop() && (o == O_BOMB || o == O_BOMBDROID)) *buf = (o == O_BOMBDROID ? T_EXPLDROID0 : T_EXPL0);
}

static void procExpl() {
    bool isExplDroid = t == T_EXPLDROID0;
    if(t == T_EXPL0 || isExplDroid) {
        for (int idx = 0; idx < 9; idx++) {
            ret_addr = buf + offsets[idx];
            o = (uint8_t)remapProp[*ret_addr & MSKT];
            uint8_t oo = o & MSKO;
            if(oo == O_BETON) continue;
            else if(oo == O_DROID) {
                if(params[PARAM_DROID_GOD]) continue;
                params[PARAM_IS_DROID] = 0;
            }
            if(isExplDroid) addScore(oo, true);
            setToMap(o & FE ? t : T_EXPL1);
        }
        params[PARAM_SOUND_EXPL]++;
    }
    else *buf = (t == T_EXPL3 ? T_NULL : t + (uint8_t)1);
}

static void procRG() {
    static int offsRG[] = {1, T_REDR, 3, T_REDU, 5, T_REDL, 7, T_REDD, 1, T_GREENL, 3, T_GREEND, 5, T_GREENR, 7, T_GREENU};
    int tmp = (t - (o == O_RED ? T_REDD : T_GREEND)) << 1;
    uint8_t tt = T_NULL;
    if(isCapsIdx(offsRG[tmp], FN)) setToMap(t);
    else if(!checkKill(FK)) tt = (uint8_t)offsRG[tmp + 1];
    *buf = tt;
	params[PARAM_RED1 + (o - O_RED)]++;
}

static void procYellow() {
    static uint8_t offsYellow[] = {3, T_YELLOWR, 1, T_YELLOWL, 5, T_YELLOWU, 3, T_YELLOWD, 7, T_YELLOWL, 5, T_YELLOWR, 1, T_YELLOWD, 7, T_YELLOWU};
    int s = (t - T_YELLOWD) * 4;
    uint8_t tt = T_NULL;
    // смотрим вбок - пусто - поворачиваем
    if(isCapsIdx(offsYellow[s], FN)) setToMap(offsYellow[s + 1]);
    // проверяем вбок на гибель
    else if(!checkKill(FY | FK)) {
        // смотрим по направлению движения
        if(isCapsIdx(offsYellow[s + 2], FN)) setToMap(t);
        // проверяем по направлению движения на гибель
        else if(!checkKill(FY | FK)) tt = offsYellow[s + 3];
    }
    *buf = tt;
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
    *buf = t;
    if(!make) {
        params[PARAM_EGG1]++;
        moveDrop();
    }
}

static void procEye() {
    int rnd = nextInt(countMapCells - params[PARAM_EYE]);
    if(rnd > 0 && rnd < (t == T_EYEB ? 5 : 3)) {
        if(isCapsIdx(nextInt(7) + 1, FN | FG)) setToMap(isCaps(FT) ? T_EXPL0 : t);
    }
    params[PARAM_EYE]++;
}

static void procExplEgg() {
    for(int idx = 1 ; idx < 9 ; idx++) {
        if(nextBoolean()) {
            ret_addr = buf + offsets[idx];
            o = (uint8_t)remapProp[*ret_addr & MSKT];
            if(o & (FN | FG) || o == O_EXPLEGG) setToMap(T_EGG0);
        }
    }
    params[PARAM_SOUND_EXPL]++;
}

static void procDroid() {
    if(params[PARAM_FUEL] <= 0) return;
    uint8_t dir = (uint8_t)params[PARAM_DROID_BUT];
    int idx = 0;
    droidMove = false;
    params[PARAM_IS_DROID] = 1;
    if (dir == DIR0) {
        if (params[PARAM_BOMB] > 0) {
            // кидаем бомбу
            if (isCapsIdx((t - T_DROIDD) * 2 + 1, FG)) {
                params[PARAM_DROID_BOMB] = 1;
                setToMap(T_BOMBDROID);
                params[PARAM_BOMB]--;
            }
        }
    } else if (dir != DIRN) {
        if (dir & DIRR)  idx = 3; // 16
        else if (dir & DIRL) idx = 7; // 8
        else {
            if (dir & DIRD) idx = 1; // 4
            else if (dir & DIRU) idx = 5;// 2
        }
        // движемся
        ret_addr = buf + offsets[idx];
        auto pe = remapProp[*ret_addr & MSKT];
        o = (uint8_t)(pe & MSKO);
        // гибель
        if (pe & FM) {
            setToMap(T_EXPL0);
            params[PARAM_IS_DROID] = 0;
        } else {
            droidMove = (pe & (FN | FG | FT)) != 0;
            if (droidMove) {
                // особые случаи
                switch (o) {
                    case O_SCORE: params[PARAM_SCORE] += DROID_ADD_SCORE; break;
                    case O_TIME: params[PARAM_TIME] += DROID_ADD_TIME; break;
                    case O_LIFE: params[PARAM_LIFE]++; break;
                    case O_FUEL: params[PARAM_FUEL] += DROID_ADD_FUEL; break;
                    case O_BOMBS: params[PARAM_BOMB] += params[PARAM_DROID_MASTER] != 0 ? DROID_ADD_BOMB / 2 : DROID_ADD_BOMB; break;
                }
                if (pe & FT) {
                    params[PARAM_SOUND_TAKE] = o == O_EGG ? SND_DROID_TAKE_EGG : SND_DROID_TAKE_MISC;
                    addScore(o, false);
                    // подсчитываем сколько яиц собрал
                  if(o == O_EGG) params[PARAM_STAT_EGG]++;
                }
            } else {
                auto nn = *ret_addr;
                switch (nn) {
                    case T_STONE0: case T_STONE1: case T_STONE2: case T_STONE3:
                    case T_BOMB: case T_BOMBDROID:
                        // сдвигаем камень/бомбу
                        if(idx & 2) {
                            auto addr = ret_addr;
                            ret_addr += offsets[idx];
                            if(isCaps(FN)) { setToMap(nn); droidMove = true; }
                            ret_addr = addr;
                        }
                }
            }
            if(droidMove) {
                *buf = T_NULL;
                setToMap((uint8_t)(idx / 2 + T_DROIDD));
                params[PARAM_FUEL]--;
                auto addr = (int)(ret_addr - level);
                params[PARAM_DROID_X] = (addr % w) - 1;
                params[PARAM_DROID_Y] = (addr / w) - 1;
            }
        }
    }
}

static fun objHundlers[] = { &procDrop, &procDrop, &procExpl, &procDroid, &procRG, &procRG,
                             &procYellow, &procEye, &procEgg, &procExplEgg, &procDrop, &procExpl };

extern "C" {
    JNIEXPORT void Java_ru_ostrovskal_droid_views_ViewGame_releaseBuffer(JNIEnv *env, jclass) {
        env->DeleteGlobalRef(mBuffer);
        env->DeleteGlobalRef(mParams);
        mBuffer = nullptr; mParams = nullptr;
    }

    JNIEXPORT void Java_ru_ostrovskal_droid_views_ViewGame_initBuffer(JNIEnv *env, jclass, jbyteArray buffer, jintArray pars) {
        if (mBuffer || mParams) Java_ru_ostrovskal_droid_views_ViewGame_releaseBuffer(env, nullptr);
        mBuffer = env->NewGlobalRef(buffer);
        mParams = env->NewGlobalRef(pars);
        auto buf = (uint8_t *) env->GetPrimitiveArrayCritical(buffer, nullptr);
        params = (jint *) env->GetPrimitiveArrayCritical(pars, nullptr);
        w = buf[0];
        h = buf[1];
        level = buf + 2;
        offsets[1] = w;     // down
        offsets[2] = w + 1; // down + right
        offsets[4] = -w + 1;// up + right
        offsets[5] = -w;    // up
        offsets[6] = -w - 1;// up + left
        offsets[8] = w - 1; // down + left
        env->ReleasePrimitiveArrayCritical(buffer, buf, JNI_ABORT);
        env->ReleasePrimitiveArrayCritical(pars, params, JNI_ABORT);
    }

    JNIEXPORT bool Java_ru_ostrovskal_droid_views_ViewGame_processBuffer(JNIEnv, jobject, jboolean delay) {
        droidMove = false;
        memset(&params[PARAM_EYE], 0, 10 * sizeof(jint));
        countMapCells = w * h;
        buf = level + 2;
        auto bufEnd = buf + countMapCells;
        while(buf++ < bufEnd) {
            t = *buf;
            if (t & MSKU) *buf &= MSKT;
            else {
                o = (uint8_t) remapProp[t] & MSKO;
                if (o <= O_BOMBDROID && (o == O_DROID || delay)) objHundlers[o]();
            }
        }
        return droidMove;
    }
}