package ru.ostrovskal.droid;

import android.graphics.Color;
import android.view.Gravity;

import static android.view.View.GONE;
import static com.github.ostrovskal.ssh.Constants.SEEK_ANIM_ROTATE;
import static com.github.ostrovskal.ssh.Constants.SEEK_ANIM_SCALE;
import static com.github.ostrovskal.ssh.Constants.TILE_GRAVITY_CENTER;
import static com.github.ostrovskal.ssh.Constants.TILE_SCALE_MIN;
import static com.github.ostrovskal.ssh.Constants.TILE_SHAPE_ROUND;
import static com.github.ostrovskal.ssh.Constants.TILE_STATE_HOVER;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_CLICKABLE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_COLOR_DEFAULT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_DRW;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_FOCUSABLE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_FONT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_GRAVITY;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_INT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_PADDING;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SHADOW_COLOR;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SHADOW_DX;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SHADOW_DY;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SHADOW_RADIUS;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SIZE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_ALIGNED;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_ALPHA;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BACKGROUND;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BITMAP_NAME;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_BACKGROUND;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_BUTTONS;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_CHECK;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_EDIT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_HEADER;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_ICONS;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_MENU;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_RADIO;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_SEEK;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_SELECT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_SWITCH;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_TILES;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_BM_TOOLS;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_DIVIDER;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_HEADER;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_HINT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_HTML_HEADER;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_LARGE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_LAYOUT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_LINK;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_MESSAGE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_NORMAL;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_SELECTOR;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_SMALL;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_WINDOW;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_COLOR_WIRED;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_GRAVITY;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_HEIGHT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_HORZ;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_MODE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_NUM;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_RADII;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_SCALE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_SHAPE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_SPACING;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_STATES;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_THEME_NAME;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_VERT;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_WIDTH;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_SSH_WIDTH_SELECTOR;
import static com.github.ostrovskal.ssh.StylesAndAttrs.ATTR_VISIBILITY;
import static com.github.ostrovskal.ssh.StylesAndAttrs.COLOR;
import static com.github.ostrovskal.ssh.StylesAndAttrs.THEME;

public final class Constants
{
    public static final String droidController        = "UUUUUUUUUUUUUUUUUUUU\n" +
                                                        "LLLLUUUUUUUUUUUUUURR\n" +
                                                        "LLLLLLUUUUUUUUUURRRR\n" +
                                                        "LLLLLLFFFFFFFFRRRRRR\n" +
                                                        "LLLLLLFFFFFFFFRRRRRR\n" +
                                                        "LLLLLLFFFFFFFFRRRRRR\n" +
                                                        "LLLLLLFFFFFFFFRRRRRR\n" +
                                                        "LLLLLLDDDDDDDDRRRRRR\n" +
                                                        "LLDDDDDDDDDDDDDDRRRR\n" +
                                                        "DDDDDDDDDDDDDDDDDDDD";
    // режимы сдвига
    public static final int SHIFT_UNDEF            = 0;
    public static final int SHIFT_MAP              = 1;
    public static final int SHIFT_TILE             = 2;
    // действие - 2 бита = 0й(0 - ничего, 1-двигаемся) 1й(0-ничего, 1-бомба)
    public final static int REC_DROID_NONE         = 0x00;
    public final static int REC_DROID_MOVE         = 0x01;
    public final static int REC_DROID_DROP         = 0x02;
    // направление движения 2 бит = 0й(0-вверх,1-вниз,2-вправо,3-влево)
    public final static int REC_DROID_MOVE_UP      = 0x00;
    public final static int REC_DROID_MOVE_DOWN    = 0x04;
    public final static int REC_DROID_MOVE_RIGHT   = 0x08;
    public final static int REC_DROID_MOVE_LEFT    = 0x0c;
    // стандартная скорость игры
    public static final int STD_GAME_SPEED          = 50;
    // задержка между сменой статуса
    public static final long MESSAGE_DELAYED = 2000;
    // задержка сплэш экрана
    public static final long SPLASH_DELAYED  = 5000;
    // установить имя планеты
    public static final int ACTION_NAME     = 10000;
    // загрузка
    public static final int ACTION_LOAD     = 10001;
    // сохранение
    public static final int ACTION_SAVE     = 10002;
    // удаление
    public static final int ACTION_DELETE   = 10003;
    // создание
    public static final int ACTION_NEW      = 10004;
    // генерация
    public static final int ACTION_GENERATE = 10005;
    // новый пакет
    public static final int ACTION_PACK     = 10006;
    // последняя планета
    public static final int ACTION_FINISH   = 10007;
    // инициализация
    public static final int STATUS_INIT     = 1;
    // игрок мертв
    public static final int STATUS_DEAD     = 2;
    // планета зачищена
    public static final int STATUS_CLEARED  = 3;
    // установка значений
    public static final int STATUS_PREPARED = 4;
    // игровой цикл
    public static final int STATUS_LOOP     = 5;
    // сообщение
    public static final int STATUS_MESSAGE  = 6;
    // выход
    public static final int STATUS_EXIT     = 7;
    // неопределено
    public static final int STATUS_UNK      = 8;
    // самоубийство
    public static final int STATUS_SUICIDED = 9;

    // константы для генерации планеты
    //public static final int    PLANET_GEN_NULL     = 0;
    public static final int    PLANET_GEN_EARTH    = 1;
    public static final int    PLANET_GEN_BETON    = 2;
    public static final int    PLANET_GEN_STONE    = 3;
    public static final int    PLANET_GEN_EGG      = 4;
    public static final int    PLANET_GEN_BOMB     = 5;
    // базовые
    public static final String SYSTEM_DEFAULT      = "classic";
    public final static int    BLOCK_MINIATURE     = 8;
    public final static int    LIMIT_RECORDS       = 50;
    // индексы звуков
    public static final int    SND_EXPLOSIVE       = 0;
    public static final int    SND_DROID_DEATH     = 1;
    public static final int    SND_DROID_DROP_BOMB = 2;
    public static final int    SND_DROID_TAKE_EGG  = 3;
    public static final int    SND_DROID_TAKE_MISC = 4;
    public static final int    SND_STONE_COLISSION = 5;
    public static final int    SND_TIME_ELAPSED    = 6;
    public static final int    SND_VOLUME          = 7;
    // типы форм
    public final static int    FORM_GAME           = 0;
    //public final static int    FORM_CHOICE         = 1;
    //public final static int    FORM_OPTIONS        = 2;
    //public final static int    FORM_RECORDS        = 3;
    //public final static int    FORM_EDITOR         = 4;
    public final static int    FORM_GAME_HELP      = 5;
    public final static int    FORM_DLG_EXIT       = 6;
    public final static int    FORM_SPLASH         = 7;
    public final static int    FORM_MENU           = 8;
    public static final int    FORM_DLG_E_ACTIONS  = 9;
    public static final int    FORM_PLANET_OPEN    = 10;
    public static final int    FORM_PLANET_NEW     = 11;
    //public static final int    FORM_PLANET_PROP    = 12;
    public static final int    FORM_DLG_DELETE     = 13;
    public static final int    FORM_DLG_GENERATE   = 14;
    public static final int    FORM_DLG_SAVE       = 15;
    //public static final int    FORM_SEND           = 16;
    public static final int    FORM_RECV           = 17;
    public static final int    FORM_FINISH         = 18;
    public static final int    FORM_EDITOR_HELP    = 19;
    public static final int    FORM_DLG_NEW_SYSTEM = 20;
    public static final int    FORM_DLG_G_ACTIONS  = 21;
    // дествия формы операций в редакторе планет
    //public static final int    FORM_CHOICE_OPEN    = 1;
    //public static final int    FORM_CHOICE_NEW     = 2;
    public static final int    FORM_CHOICE_PROP    = 3;
    public static final int    FORM_CHOICE_DEL     = 4;
    //public static final int    FORM_CHOICE_GEN     = 5;
    public static final int    FORM_CHOICE_SAVE    = 6;
    public static final int    FORM_CHOICE_SEND    = 7;
    public static final int    FORM_CHOICE_PREV    = 8;
    public static final int    FORM_CHOICE_TEST    = 9;
    //public static final int    FORM_CHOICE_HELP    = 10;
    // ключи установок
    public final static String KEY_SCALE           = "scale";
    public final static String KEY_SPEED           = "speed";
    public final static String KEY_MUSIC           = "music";
    public final static String KEY_SOUND           = "sound";
    public final static String KEY_MASTER          = "master";
    public final static String KEY_CLASSIC         = "classic";
    public final static String KEY_AUTHOR          = "author";
    public final static String KEY_AUTHOR_COUNT    = "#author_count";
    public final static String KEY_SCALE_VOLUME    = "scale_volume";
    public final static String KEY_SPEED_VOLUME    = "speed_volume";
    public final static String KEY_MUSIC_VOLUME    = "music_volume";
    public final static String KEY_SOUND_VOLUME    = "sound_volume";
    public final static String KEY_THEME           = "theme";
    public final static String KEY_TMP_THEME       = "#tmp_theme";
    public final static String KEY_PLAYER          = "player";
    public final static String KEY_PACK            = "#pack";
    public final static String KEY_FIRST_START     = "first_start";
    public final static String KEY_TYPE_PLANET     = "#type_planet";
    public final static String KEY_EDIT_PLANET     = "#edit_planet";
    public final static String KEY_EDIT_TILE       = "#edit_tile";
    public final static String KEY_EDIT_PREVIEW    = "#edit_preview";
    // индексы параметров планеты
    public static final int PARAM_SCORE    = 0;
    public static final int PARAM_TIME     = 1;
    public static final int PARAM_FUEL     = 2;
    public static final int PARAM_LIFE     = 3;
    public static final int PARAM_BOMB     = 4;
    public static final int PARAM_EGG      = 5;
    public static final int PARAM_YELLOW   = 6;
    public static final int PARAM_RED      = 7;
    public static final int PARAM_GREEN    = 8;

    public static final int PARAM_EYE           = 9;
    public static final int PARAM_EGG1          = 10;
    public static final int PARAM_YELLOW1       = 11;
    public static final int PARAM_RED1          = 12;
    public static final int PARAM_GREEN1        = 13;
    public static final int PARAM_IS_DROID      = 14;
    public static final int PARAM_DROID_BOMB    = 15;
    public static final int PARAM_SOUND_EXPL    = 16;
    public static final int PARAM_SOUND_STONE   = 17;
    public static final int PARAM_SOUND_TAKE    = 18;
    public static final int PARAM_LIMIT         = 19;
    public static final int PARAM_DROID_X       = 20;
    public static final int PARAM_DROID_Y       = 21;
    public static final int PARAM_DROID_BUT     = 22;
    public static final int PARAM_DROID_GOD     = 23;
    public static final int PARAM_DROID_CLASSIC = 24;
    public static final int PARAM_DROID_MASTER  = 25;
    public static final int PARAM_STAT_GREEN    = 26;
    public static final int PARAM_STAT_RED      = 27;
    public static final int PARAM_STAT_YELLOW   = 28;
    public static final int PARAM_STAT_EGG      = 29;
    public static final int PARAM_STAT_SCORE    = 30;
    public static final int PARAMS_COUNT        = 31;

    // константы
    public final static int DROID_LIFE          = 5;
    public final static int DROID_ADD_TIME      = 500;
    public final static int DROID_ADD_FUEL      = 250;
    public final static int DROID_ADD_BOMB      = 8;
    public final static int DROID_ADD_SCORE     = 1000;
    public final static int DROID_ADD_LIMIT     = 2500;

    // индексы всех тайлов
    public static final  byte T_NULL            = 0;
    public static final  byte T_EARTH           = 1;
    public static final  byte T_BETON           = 2;
    public static final  byte T_BRICK           = 3;
    private static final byte T_LIFE            = 4;
    private static final byte T_FUEL            = 5;
    private static final byte T_BOMBS           = 6;
    public static final  byte T_EXPL0           = 7;
    public static final  byte T_EXPL1           = 8;
    private static final byte T_EXPL2           = 9;
    public static final  byte T_EXPL3           = 10;
    public static final  byte T_DROIDD          = 11;
    public static final  byte T_DROIDR          = 12;
    public static final  byte T_DROIDU          = 13;
    public static final  byte T_DROIDL          = 14;
    public static final  byte T_REDD            = 15;
    private static final byte T_REDR            = 16;
    private static final byte T_REDU            = 17;
    private static final byte T_REDL            = 18;
    public static final  byte T_YELLOWD         = 19;
    private static final byte T_YELLOWR         = 20;
    public static final  byte T_YELLOWU         = 21;
    private static final byte T_YELLOWL         = 22;
    private static final byte T_EYEG            = 23;
    public static final  byte T_BOMB            = 24;
    public static final  byte T_STONE0          = 25;
    // новые тайлы
    public static final  byte T_STONE1          = 26;
    public static final  byte T_STONE2          = 27;
    public static final  byte T_STONE3          = 28;
    public static final  byte T_EGG0            = 29;
    private static final byte T_EGG1            = 30;
    private static final byte T_EGG2            = 31;
    public static final  byte T_EGG3            = 32;
    private static final byte T_SCORE           = 33;
    private static final byte T_TIME            = 34;
    public static final  byte T_EYEB            = 35;
    public static final  byte T_GREEND          = 36;
    private static final byte T_GREENR          = 37;
    private static final byte T_GREENU          = 38;
    private static final byte T_GREENL          = 39;
    public static final  byte T_BOMBDROID       = 40;
    public static final  byte T_EXPLEGG         = 41;
    public static final  byte T_EXPLDROID0      = 42;
    // первый номер падающих тайлов
    public static final  byte T_DROP            = (43 - 24);
    // типы объектов
    public static final  int O_STONE            = 0;
    public static final  int O_BOMB             = 1;
    private static final int O_EXPL             = 2;
    public static final  int O_DROID            = 3;
    public static final  int O_RED              = 4;
    public static final  int O_GREEN            = 5;
    public static final  int O_YELLOW           = 6;
    private static final int O_EYE              = 7;
    public static final  int O_EGG              = 8;
    public static final  int O_EXPLEGG          = 9;
    public static final  int O_BOMBDROID        = 10;
    private static final int O_NULL             = 11;
    public static final  int O_BETON            = 12;
    private static final int O_BRICK            = 13;
    private static final int O_EARTH            = 14;
    public static final  int O_SCORE            = 15;
    public static final  int O_TIME             = 16;
    public static final  int O_LIFE             = 17;
    public static final  int O_BOMBS            = 18;
    public static final  int O_FUEL             = 19;
    // флаги тайлов
    public static final  int MSKT               = 0x003f; // маска тайла
    public static final  int MSKO               = 0x001f; // маска объекта
    public static final  int MSKU               = 0x0040; // признак обновления
    // флаги характеристик
    public static final int    FK               = 0x0020; // твари гибнут
    public static final int    FY               = 0x0040; // желтая гибнет
    public static final int    FB               = 0x0080; // падающие камень и бомба взрываются
    public static final int    FN               = 0x0100; // пустота для всех
    public static final int    FG               = 0x0200; // человек и глаз проходит
    public static final int    FS               = 0x0400; // соскок
    public static final int    FM               = 0x0800; // гибель человека
    public static final int    FT               = 0x1000; // человек берет
    public static final int    FE               = 0x2000; // взрывается от другого взрыва

    // смещение бомб дроида
    public final static int[] offsBombPos   = {0, 1, 1, 0, 0, -1, -1, 0};

    // смещение координат красных/зеленых тварей
    public final static byte[] offsRG       = {1, 0, T_REDR, 0, 1, T_REDU, -1, 0, T_REDL, 0, -1, T_REDD,
                                               1, 0, T_GREENL, 0, 1, T_GREEND, -1, 0, T_GREENR, 0, -1, T_GREENU};

    // смещение координат желтых тварей
    public final static byte[] offsYellow   = {0, -1, T_YELLOWL, 1, 0, T_YELLOWR, 1, 0, T_YELLOWD, 0, 1, T_YELLOWU,
                                               0, 1, T_YELLOWR, -1, 0, T_YELLOWL, -1, 0, T_YELLOWU, 0, -1, T_YELLOWD};

    // смещение координат при формировании взрыва
    public final static int[] offsExplo     = {-1, -1, -1, 0, -1, 1, 0, -1, 0, 0, 0, 1, 1, -1, 1, 0, 1, 1};

    // массив смещений для глаза
    public static final int[]  offsEye             = {0, -1, 1};
    // длины счетчиков
    public static final int[]  formatLengths       = {5, 5, 5, 3, 3, 3, 3, 3, 3};
    // массив значений увеличения очков
    public final static int[]  numScored           = {0, 0, 0, -50, 10, 20, 30, 5, 40, 0, 0, 0, 0, 0, 0, -100, 200, 150, 100, 50};
    // таблицы перекодировок тайлов для отображения
    public static final char[] charsOfPlanetMap    =
            {' ', '\u2591', '\u2588', '\u2593', '\u263b', '\u25d9', '\u2302', '\u263c', ' ', ' ', ' ', '\u25bc', '\u25ba', '\u25b2', '\u25c4', '\u0466', '\u0467', '\u0466',
             '\u0467', '\u046a', '\u046b', '\u046a', '\u046b', '\u0470', '\u25ca', '\u25cc', '\u25cc', '\u25cc', '\u25cc', '\u0472', '\u0473', '\u0473', '\u0473', '\u06de',
             '\u06e9', '\u0471', '\u046c', '\u046d', '\u046d', '\u046d', ' ', ' ', ' '};
    public static final byte[] remapTiles          =
            {T_NULL, T_EARTH, T_BETON, T_BRICK, T_LIFE, T_FUEL, T_BOMBS, T_EXPL0, T_EXPL1, T_EXPL2, T_EXPL3, T_DROIDD, T_DROIDR, T_DROIDU, T_DROIDL, T_REDD, T_REDR, T_REDU, T_REDL,
             T_YELLOWD, T_YELLOWR, T_YELLOWU, T_YELLOWL, T_EYEG, T_BOMB, T_STONE0, T_STONE1, T_STONE2, T_STONE3, T_EGG0, T_EGG1, T_EGG2, T_EGG3, T_SCORE, T_TIME, T_EYEB, T_GREEND,
             T_GREENR, T_GREENU, T_GREENL, T_BOMB, T_EXPL0, T_EXPL0,
             // падающие тайлы
             T_BOMB, T_STONE0, T_STONE1, T_STONE2, T_STONE3, T_EGG0, T_EGG1, T_EGG2, T_EGG3, T_NULL, T_NULL, T_NULL, T_NULL, T_NULL, T_NULL, T_NULL, T_BOMB, T_NULL, T_NULL, T_NULL,
             T_NULL, T_NULL};
    // таблица характиристик тайлов (объкт|флаги)
    public static final int[]  remapProp           = { // T_NULL, T_EARTH
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
                                                       O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG, O_NULL | FN | FG};

    // Таблица перекодировок тайлов для отображения
    public static final int[] paramsEditElems =   {T_YELLOWD, T_YELLOWR, T_YELLOWU, T_YELLOWL, T_REDD, T_REDR, T_REDU, T_REDL,
                                                   T_GREEND, T_GREENR, T_GREENU, T_GREENL, T_EGG0, T_EGG1, T_EGG2, T_EGG3,
                                                   T_DROIDD, T_DROIDR, T_DROIDU, T_DROIDL, T_STONE0, T_STONE1, T_STONE2,
                                                   T_STONE3, T_EYEG, T_EYEB, T_EYEG, T_EYEB, T_EXPL0, T_EXPL1, T_EXPL2, T_EXPL3};

    public static final int[] tilesEditorPanel  = {R.integer.TILE_NULL, R.integer.TILE_EARTH, R.integer.TILE_STONE, R.integer.TILE_BOMB, R.integer.TILE_BRICK,
                                                   R.integer.TILE_BETON, R.integer.TILE_BOMBS, R.integer.TILE_FUEL, R.integer.TILE_EXPL, R.integer.TILE_SCORE,
                                                   R.integer.TILE_TIME, R.integer.TILE_LIFE, R.integer.TILE_EYEG, R.integer.TILE_DROIDR, R.integer.TILE_EGG,
                                                   R.integer.TILE_YELLOWD, R.integer.TILE_REDD, R.integer.TILE_GREEND};

    public static final int[] tilesGamePanel    = {R.integer.TILE_SCORE, R.integer.TILE_TIME, R.integer.TILE_FUEL, R.integer.TILE_LIFE, R.integer.TILE_BOMB,
                                                   R.integer.TILE_EGG, R.integer.TILE_YELLOWD, R.integer.TILE_REDD, R.integer.TILE_GREEND};

    public static final int[] iconsEditorActions= {R.integer.I_OPEN_PLANET, R.integer.I_NEW_PLANET, R.integer.I_PROP_PLANET,
                                                   R.integer.I_DELETE_PLANET, R.integer.I_GEN_PLANET, R.integer.I_SAVE_PLANET,
                                                   R.integer.I_SEND_PACK, R.integer.I_PREVIEW_PLANET, R.integer.I_TEST_PLANET,
                                                   R.integer.I_HELP};

    // Координаты элементов сплэша по вертикали
    public static final int[] coordV            = {22, 44, 0, 0, 22, 4, 2, 30, 15, 5, 17, 31, 4, 4, 0, 36, 22, 4, 1, 4, 20, 30};

    // Координаты элементов сплэша по горизонтали
    public static final int[] coordH            = {44, 33, 0, 0, 44, 4, 8, 17, 26, 6, 33, 18, 5, 5, 0, 24, 44, 3, 6, 4, 32, 17};

    private static final int ATTR_SSH_COLOR_PANEL_COUNTERS = 1000 | ATTR_INT;
    private static final int ATTR_SSH_COLOR_NAME_PLANET    = 1001 | ATTR_INT;
    private static final int ATTR_SSH_COLOR_STAT_COUNTERS  = 1002 | ATTR_INT;
    private static final int ATTR_SSH_BM_PANEL_PORT        = 1003 | ATTR_DRW;
    private static final int ATTR_SSH_BM_PANEL_LAND        = 1004 | ATTR_DRW;

    public static final int[] themeDark = {ATTR_SSH_SPACING, R.dimen.cellSpacing,
                                           ATTR_SSH_THEME_NAME, R.string.themeDark,
                                           ATTR_SSH_BM_MENU, R.drawable.menu,
                                           ATTR_SSH_BM_ICONS, R.drawable.icon_tiles,
                                           ATTR_SSH_MODE, SEEK_ANIM_ROTATE,
                                           ATTR_SSH_COLOR_DIVIDER, 0x7a7a7a | COLOR,
                                           ATTR_SSH_COLOR_LAYOUT, 0x2d2929 | COLOR,
                                           ATTR_SSH_COLOR_NORMAL, 0x9599f7 | COLOR,
                                           ATTR_SSH_COLOR_LARGE, 0xbc5a1d | COLOR,
                                           ATTR_SSH_COLOR_SMALL, 0x2ea362 | COLOR,
                                           ATTR_SSH_COLOR_HINT, 0xf77499 | COLOR,
                                           ATTR_SSH_COLOR_SELECTOR, Color.MAGENTA | COLOR,
                                           ATTR_SSH_COLOR_HEADER, 0xcfba41 | COLOR,
                                           ATTR_SSH_COLOR_HTML_HEADER, 0xf22782 | COLOR,
                                           ATTR_SSH_COLOR_NAME_PLANET, 0xe7e114 | COLOR,
                                           ATTR_SSH_COLOR_PANEL_COUNTERS, 0xeac8c8 | COLOR,
                                           ATTR_SSH_COLOR_STAT_COUNTERS, 0xaafdfd | COLOR,
                                           ATTR_SSH_COLOR_LINK, 0xb8fa01 | COLOR,
                                           ATTR_SSH_COLOR_MESSAGE, 0xd2fa64 | COLOR,
                                           ATTR_SSH_COLOR_WINDOW, 0x030303 | COLOR,
                                           ATTR_SSH_COLOR_WIRED, 0x808080 | COLOR,
                                           ATTR_SSH_BM_BACKGROUND, R.drawable.theme_background_dark,
                                           ATTR_SSH_BM_HEADER, R.drawable.theme_header_dark,
                                           ATTR_SSH_BM_SELECT, R.drawable.theme_select_dark,
                                           ATTR_SSH_BM_EDIT, R.drawable.theme_edit_dark,
                                           ATTR_SSH_BM_PANEL_PORT, R.drawable.theme_panel_port_dark,
                                           ATTR_SSH_BM_PANEL_LAND, R.drawable.theme_panel_land_dark,
                                           ATTR_SSH_BM_TOOLS, R.drawable.theme_tool_dark,
                                           ATTR_SSH_BM_BUTTONS, R.drawable.theme_button_dark,
                                           ATTR_SSH_BM_RADIO, R.drawable.theme_radio_dark,
                                           ATTR_SSH_BM_CHECK, R.drawable.theme_check_dark,
                                           ATTR_SSH_BM_SEEK, R.drawable.theme_seek_dark,
                                           ATTR_SSH_BM_SWITCH, R.drawable.theme_switch_dark,
                                           ATTR_SSH_BM_TILES, R.drawable.classic_sprites};

    public static final int[] themeLight = {ATTR_SSH_SPACING, R.dimen.cellSpacing,
                                            ATTR_SSH_THEME_NAME, R.string.themeLight,
                                            ATTR_SSH_BM_MENU, R.drawable.menu,
                                            ATTR_SSH_BM_ICONS, R.drawable.icon_tiles,
                                            ATTR_SSH_MODE, SEEK_ANIM_SCALE,
                                            ATTR_SSH_COLOR_DIVIDER, 0x7f7f7f | COLOR,
                                            ATTR_SSH_COLOR_LAYOUT, 0x9e5e1e | COLOR,
                                            ATTR_SSH_COLOR_NORMAL, 0xea5191 | COLOR,
                                            ATTR_SSH_COLOR_LARGE, 0x5670f1 | COLOR,
                                            ATTR_SSH_COLOR_SMALL, 0x9841df | COLOR,
                                            ATTR_SSH_COLOR_HINT, 0xf77499 | COLOR,
                                            ATTR_SSH_COLOR_SELECTOR, 0xf23346 | COLOR,
                                            ATTR_SSH_COLOR_HEADER, 0xa9f145 | COLOR,
                                            ATTR_SSH_COLOR_HTML_HEADER, 0x9a4dfc | COLOR,
                                            ATTR_SSH_COLOR_NAME_PLANET, 0xf9f681 | COLOR,
                                            ATTR_SSH_COLOR_PANEL_COUNTERS, 0xb7a0a0 | COLOR,
                                            ATTR_SSH_COLOR_STAT_COUNTERS, 0xe36efa | COLOR,
                                            ATTR_SSH_COLOR_LINK, 0xb8fa01 | COLOR,
                                            ATTR_SSH_COLOR_MESSAGE, 0xd2fa64 | COLOR,
                                            ATTR_SSH_COLOR_WINDOW, 0x020202 | COLOR,
                                            ATTR_SSH_COLOR_WIRED, 0x404040 | COLOR,
                                            ATTR_SSH_BM_BACKGROUND, R.drawable.theme_background_light,
                                            ATTR_SSH_BM_HEADER, R.drawable.theme_header_light,
                                            ATTR_SSH_BM_SELECT, R.drawable.theme_select_light,
                                            ATTR_SSH_BM_EDIT, R.drawable.theme_edit_light,
                                            ATTR_SSH_BM_PANEL_PORT, R.drawable.theme_panel_port_light,
                                            ATTR_SSH_BM_PANEL_LAND, R.drawable.theme_panel_land_light,
                                            ATTR_SSH_BM_TOOLS, R.drawable.theme_tool_light,
                                            ATTR_SSH_BM_BUTTONS, R.drawable.theme_button_light,
                                            ATTR_SSH_BM_RADIO, R.drawable.theme_radio_light,
                                            ATTR_SSH_BM_CHECK, R.drawable.theme_check_light,
                                            ATTR_SSH_BM_SEEK, R.drawable.theme_seek_light,
                                            ATTR_SSH_BM_SWITCH, R.drawable.theme_switch_light,
                                            ATTR_SSH_BM_TILES, R.drawable.classic_sprites};

    public static final int[] style_tile_droid = {ATTR_SSH_VERT, 4,
                                                  ATTR_SSH_HORZ, 10,
                                                  ATTR_FOCUSABLE, 0,
                                                  ATTR_SSH_WIDTH_SELECTOR, R.dimen.widthSelector,
                                                  ATTR_SSH_BACKGROUND, ATTR_SSH_COLOR_SELECTOR | THEME,
                                                  ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_TILES | THEME};

    public static final int[] style_button_actions = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                       ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                       ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                       ATTR_SHADOW_COLOR, 0x1 | COLOR,
                                                       ATTR_SIZE, R.dimen.large,
                                                       ATTR_FONT, R.string.font_large,
                                                       ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_MESSAGE | THEME,
                                                       ATTR_CLICKABLE, 1,
                                                       ATTR_PADDING, 2,
                                                       ATTR_SSH_ALIGNED, 1,
                                                       ATTR_SSH_HORZ, 2,
                                                       ATTR_SSH_NUM, 0,
                                                       ATTR_SSH_STATES, TILE_STATE_HOVER,
                                                       ATTR_GRAVITY, Gravity.CENTER,
                                                       ATTR_SSH_GRAVITY, TILE_GRAVITY_CENTER,
                                                       ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_BUTTONS | THEME };

    public static final int[] style_text_finish = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                   ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                   ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                   ATTR_SHADOW_COLOR, 0x1 | COLOR,
                                                   ATTR_GRAVITY, Gravity.CENTER_VERTICAL,
                                                   ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NORMAL | THEME,
                                                   ATTR_SIZE, R.dimen.finish,
                                                   ATTR_FONT, R.string.font_large};

    public static final int[] style_text_counters = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                     ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                     ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                     ATTR_SHADOW_COLOR, 0x1 | COLOR,
                                                     ATTR_GRAVITY, Gravity.CENTER_VERTICAL,
                                                     ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_PANEL_COUNTERS | THEME,
                                                     ATTR_SIZE, R.dimen.panel,
                                                     ATTR_FONT, R.string.font_normal};

    public static final int[] style_text_planet = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                   ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                   ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                   ATTR_SHADOW_COLOR, 0x1 | COLOR,
                                                   ATTR_GRAVITY, Gravity.CENTER,
                                                   ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NAME_PLANET | THEME,
                                                   ATTR_SIZE, R.dimen.planet,
                                                   ATTR_FONT, R.string.font_large};

    public static final int[] style_panel_tile =  {ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_TILES | THEME,
                                                   ATTR_SSH_HORZ, 10,
                                                   ATTR_SSH_VERT, 4,
                                                   ATTR_CLICKABLE, 0,
                                                   ATTR_FOCUSABLE, 0,
                                                   ATTR_SSH_ALIGNED, 1,
                                                   ATTR_SSH_SCALE, TILE_SCALE_MIN,
                                                   ATTR_SSH_GRAVITY, Gravity.CENTER};

    public static final int[] style_dlg_actions = {ATTR_SSH_SHAPE, TILE_SHAPE_ROUND,
                                                   ATTR_SSH_RADII, R.string.radii_dlg};

    public static final int[] style_panel_port = {ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_PANEL_PORT | THEME, ATTR_SSH_NUM, 0 };

    public static final int[] style_panel_land = {ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_PANEL_LAND | THEME, ATTR_SSH_NUM, 0 };

    public static final int[] style_droidController=   {ATTR_VISIBILITY, GONE,
                                                        ATTR_SSH_WIDTH, 180,
                                                        ATTR_SSH_HEIGHT, 180,
                                                        ATTR_SSH_ALPHA, 184,
                                                        ATTR_SSH_BITMAP_NAME, R.drawable.controller_tiles,
                                                        ATTR_SSH_HORZ, 6,
                                                        ATTR_SSH_VERT, 1,
                                                        ATTR_SSH_NUM, 0};
}
