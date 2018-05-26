package ru.ostrovskal.droid;

import android.view.Gravity;

import static com.github.ostrovskal.ssh.Constants.DIRU;
import static com.github.ostrovskal.ssh.Constants.SEEK_ANIM_ROTATE;
import static com.github.ostrovskal.ssh.StylesAndAttrs.*;

public final class Constants
{
    // константы для генерации планеты
    public static final int    PLANET_GEN_NULL     = 0;
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
    public final static int    FORM_CHOICE         = 1;
    public final static int    FORM_OPTIONS        = 2;
    public final static int    FORM_RECORDS        = 3;
    public final static int    FORM_EDITOR         = 4;
    public final static int    FORM_GAME_HELP      = 5;
    public final static int    FORM_DLG_EXIT       = 6;
    public final static int    FORM_SPLASH         = 7;
    public final static int    FORM_MENU           = 8;
    public static final int    FORM_DLG_ACTION     = 9;
    public static final int    FORM_DLG_SAVE       = 10;
    public static final int    FORM_PLANET_NEW     = 11;
    public static final int    FORM_PLANET_OPEN    = 12;
    public static final int    FORM_PLANET_PROP    = 13;
    public static final int    FORM_EDITOR_HELP    = 14;
    public static final int    FORM_DLG_DELETE     = 15;
    public static final int    FORM_DLG_GENERATE   = 16;
    public static final int    FORM_SEND           = 17;
    public static final int    FORM_RECV           = 18;
    public static final int    FORM_FINISH         = 19;
    public static final int    FORM_DLG_NEW_PACK   = 20;
    // дествия формы операций в редакторе планет
    public static final int    FORM_CHOICE_NEW     = 0;
    public static final int    FORM_CHOICE_OPEN    = 1;
    public static final int    FORM_CHOICE_PROP    = 2;
    public static final int    FORM_CHOICE_HELP    = 3;
    public static final int    FORM_CHOICE_DEL     = 4;
    public static final int    FORM_CHOICE_GEN     = 5;
    public static final int    FORM_CHOICE_SEND    = 6;
    public static final int    FORM_CHOICE_ALL     = 7;
    public static final int    FORM_CHOICE_TEST    = 8;
    public static final int    FORM_CHOICE_SAVE    = 9;
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
    public final static String KEY_THEME           = "#theme";
    public final static String KEY_PLAYER          = "player";
    public final static String KEY_PACK            = "#pack";
    public final static String KEY_FIRST_START     = "first_start";
    public final static String KEY_TYPE_PLANET     = "#type_planet";
    public final static String KEY_EDIT_PLANET     = "#edit_planet";
    public final static String KEY_EDIT_TILE       = "#edit_tile";
    public final static String KEY_TMP_PACK        = "#tmp_pack";
    // индексы всех тайлов
    public static final byte   T_NULL              = 0;
    public static final byte   T_EARTH             = 1;
    public static final byte   T_BETON             = 2;
    public static final byte   T_BRICK             = 3;
    public static final byte   T_LIFE              = 4;
    public static final byte   T_FUEL              = 5;
    public static final byte   T_BOMBS             = 6;
    public static final byte   T_EXPL0             = 7;
    public static final byte   T_EXPL1             = 8;
    public static final byte   T_EXPL2             = 9;
    public static final byte   T_EXPL3             = 10;
    public static final byte   T_DROIDD            = 11;
    public static final byte   T_DROIDR            = 12;
    public static final byte   T_DROIDU            = 13;
    public static final byte   T_DROIDL            = 14;
    public static final byte   T_REDD              = 15;
    public static final byte   T_REDR              = 16;
    public static final byte   T_REDU              = 17;
    public static final byte   T_REDL              = 18;
    public static final byte   T_YELLOWD           = 19;
    public static final byte   T_YELLOWR           = 20;
    public static final byte   T_YELLOWU           = 21;
    public static final byte   T_YELLOWL           = 22;
    public static final byte   T_EYEG              = 23;
    public static final byte   T_BOMB              = 24;
    public static final byte   T_STONE0            = 25;
    // новые тайлы
    public static final byte   T_STONE1            = 26;
    public static final byte   T_STONE2            = 27;
    public static final byte   T_STONE3            = 28;
    public static final byte   T_EGG0              = 29;
    public static final byte   T_EGG1              = 30;
    public static final byte   T_EGG2              = 31;
    public static final byte   T_EGG3              = 32;
    public static final byte   T_SCORE             = 33;
    public static final byte   T_TIME              = 34;
    public static final byte   T_EYEB              = 35;
    public static final byte   T_GREEND            = 36;
    public static final byte   T_GREENR            = 37;
    public static final byte   T_GREENU            = 38;
    public static final byte   T_GREENL            = 39;
    public static final byte   T_BOMBDROID         = 40;
    public static final byte   T_EXPLEGG           = 41;
    public static final byte   T_EXPLDROID0        = 42;
    // первый номер падающих тайлов
    public static final byte   T_DROP              = (43 - 24);
    // типы объектов
    public static final int    O_STONE             = 0;
    public static final int    O_BOMB              = 1;
    public static final int    O_EXPL              = 2;
    public static final int    O_DROID             = 3;
    public static final int    O_RED               = 4;
    public static final int    O_GREEN             = 5;
    public static final int    O_YELLOW            = 6;
    public static final int    O_EYE               = 7;
    public static final int    O_EGG               = 8;
    public static final int    O_EXPLEGG           = 9;
    public static final int    O_BOMBDROID         = 10;
    public static final int    O_NULL              = 11;
    public static final int    O_BETON             = 12;
    public static final int    O_BRICK             = 13;
    public static final int    O_EARTH             = 14;
    public static final int    O_SCORE             = 15;
    public static final int    O_TIME              = 16;
    public static final int    O_LIFE              = 17;
    public static final int    O_BOMBS             = 18;
    public static final int    O_FUEL              = 19;
    // флаги тайлов
    public static final int    MSKT                = 0x003f; // маска тайла
    public static final int    MSKO                = 0x001f; // маска объекта
    public static final int    MSKU                = 0x0040; // признак обновления
    // флаги характеристик
    public static final int    FK                  = 0x0020; // твари гибнут
    public static final int    FY                  = 0x0040; // желтая гибнет
    public static final int    FB                  = 0x0080; // падающие камень и бомба взрываются
    public static final int    FN                  = 0x0100; // пустота для всех
    public static final int    FG                  = 0x0200; // человек и глаз проходит
    public static final int    FS                  = 0x0400; // соскок
    public static final int    FM                  = 0x0800; // гибель человека
    public static final int    FT                  = 0x1000; // человек берет
    public static final int    FE                  = 0x2000; // взрывается от другого взрыва
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

/*
    public static String getStatus(int arg)
    {
        switch(arg)
        {
            case ACTION_FINISH:
                return "ACTION_FINISH";
            case ACTION_PACK:
                return "ACTION_PACK";
            case ACTION_NAME:
                return "ACTION_NAME";
            case ACTION_LOAD:
                return "ACTION_LOAD";
            case ACTION_SAVE:
                return "ACTION_SAVE";
            case ACTION_DELETE:
                return "ACTION_DELETE";
            case ACTION_NEW:
                return "ACTION_NEW";
            case ACTION_GENERATE:
                return "ACTION_GENERATE";
            case ACTION_EXIT:
                return "ACTION_EXIT";
            case ACTION_THEME:
                return "ACTION_THEME";
            case ACTION_UPDATE:
                return "ACTION_UPDATE";
            case STATUS_INIT:
                return "STATUS_INIT";
            case STATUS_DEAD:
                return "STATUS_DEAD";
            case STATUS_CLEARED:
                return "STATUS_CLEARED";
            case STATUS_WORK:
                return "STATUS_WORK";
            case STATUS_LOOP:
                return "STATUS_LOOP";
            case STATUS_MESSAGE:
                return "STATUS_MESSAGE";
            case STATUS_EXIT:
                return "STATUS_EXIT";
            case STATUS_SUICIDED:
                return "STATUS_SUICIDED";
            case STATUS_UNK:
                return "STATUS_UNK";
            case MSG_SERVICE:
                return "MSG_SERVICE";
            case MSG_FORM:
                return "MSG_FORM";
        }
        return String.valueOf(arg);
    }
*/

    public static final String[] stringsDroid = {"menu", "icon_tiles", "background_dark", "background_light", "header_dark", "header_light",
                                                 "select_dark", "select_light", "edit_dark", "edit_light", "panel_port_dark", "panel_port_light", "panel_land_dark",
                                                 "panel_land_light", "tool_dark", "tool_light", "button_dark", "button_light", "check_dark", "check_light", "radio_dark",
                                                 "radio_light", "switch_dark", "switch_light", "seek_dark", "seek_light", "classic_sprites", "custom_sprites"};

    public static final float[] dimensDroidDef   = {10000f, 20f, 10f, 44f, 21f, 22f, 140f, 210f, 160f, 170f, 170f, 240f, 310f, 130f, 140f, 130f, 260f};
    public static final float[] dimensDroid600sw = {600f, 36f, 20f, 88f, 42f, 44f, 280f, 420f, 320f, 340f, 340f, 480f, 620f, 260f, 280f, 260f, 520f};
    public static final float[] dimensDroid800sw = {800f, 45f, 25f, 110f, 52f, 55f, 350f, 525f, 400f, 425f, 425f, 600f, 775f, 325f, 350f, 325f, 650f};

    public static final int ATTR_SSH_COLOR_PANEL_COUNTERS = 1000 | ATTR_INT;
    public static final int ATTR_SSH_COLOR_NAME_PLANET    = 1001 | ATTR_INT;
    public static final int ATTR_SSH_COLOR_STAT_COUNTERS  = 1002 | ATTR_INT;
    public static final int ATTR_SSH_BM_PANEL_PORT        = 1003 | ATTR_DRW;
    public static final int ATTR_SSH_BM_PANEL_LAND        = 1004 | ATTR_DRW;

    public static final int TEXT_BITMAP_MENU             = 20 | TEXT;
    public static final int TEXT_BITMAP_ICONS            = 21 | TEXT;
    public static final int TEXT_BITMAP_BACKGROUND_DARK  = 22 | TEXT;
    public static final int TEXT_BITMAP_BACKGROUND_LIGHT = 23 | TEXT;
    public static final int TEXT_BITMAP_HEADER_DARK      = 24 | TEXT;
    public static final int TEXT_BITMAP_HEADER_LIGHT     = 25 | TEXT;
    public static final int TEXT_BITMAP_SELECT_DARK      = 26 | TEXT;
    public static final int TEXT_BITMAP_SELECT_LIGHT     = 27 | TEXT;
    public static final int TEXT_BITMAP_EDIT_DARK        = 28 | TEXT;
    public static final int TEXT_BITMAP_EDIT_LIGHT       = 29 | TEXT;
    public static final int TEXT_BITMAP_PANEL_PORT_DARK  = 30 | TEXT;
    public static final int TEXT_BITMAP_PANEL_PORT_LIGHT = 31 | TEXT;
    public static final int TEXT_BITMAP_PANEL_LAND_DARK  = 32 | TEXT;
    public static final int TEXT_BITMAP_PANEL_LAND_LIGHT = 33 | TEXT;
    public static final int TEXT_BITMAP_TOOL_DARK        = 34 | TEXT;
    public static final int TEXT_BITMAP_TOOL_LIGHT       = 35 | TEXT;
    public static final int TEXT_BITMAP_BUTTON_DARK      = 36 | TEXT;
    public static final int TEXT_BITMAP_BUTTON_LIGHT     = 37 | TEXT;
    public static final int TEXT_BITMAP_CHECK_DARK       = 38 | TEXT;
    public static final int TEXT_BITMAP_CHECK_LIGHT      = 39 | TEXT;
    public static final int TEXT_BITMAP_RADIO_DARK       = 40 | TEXT;
    public static final int TEXT_BITMAP_RADIO_LIGHT      = 41 | TEXT;
    public static final int TEXT_BITMAP_SWITCH_DARK      = 42 | TEXT;
    public static final int TEXT_BITMAP_SWITCH_LIGHT     = 43 | TEXT;
    public static final int TEXT_BITMAP_SEEK_DARK        = 44 | TEXT;
    public static final int TEXT_BITMAP_SEEK_LIGHT       = 45 | TEXT;
    public static final int TEXT_BITMAP_CLASSIC_SPRITES  = 46 | TEXT;
    public static final int TEXT_BITMAP_CUSTOM_SPRITES   = 47 | TEXT;

    public static final int DIMEN_SIZE_PANEL                = 51 | RES;
    public static final int DIMEN_SIZE_STAT                 = 52 | RES;
    public static final int DIMEN_SIZE_MESSAGE              = 53 | RES;
    public static final int DIMEN_SIZE_PLANET               = 54 | RES;
    public static final int DIMEN_SIZE_FINISH               = 55 | RES;
    public static final int DIMEN_HEIGHT_NEW_PACK           = 56 | DIMEN;
    public static final int DIMEN_HEIGHT_ACTIONS            = 57 | DIMEN;
    public static final int DIMEN_HEIGHT_DLG_EXIT           = 58 | DIMEN;
    public static final int DIMEN_HEIGHT_DLG_SAVE           = 59 | DIMEN;
    public static final int DIMEN_HEIGHT_DLG_DELETE         = 60 | DIMEN;
    public static final int DIMEN_HEIGHT_DLG_GENERATE       = 61 | DIMEN;
    public static final int DIMEN_HEIGHT_DLG_SEND_PLANET    = 62 | DIMEN;
    public static final int DIMEN_HEIGHT_ITEM_OPEN_PLANET   = 63 | DIMEN;
    public static final int DIMEN_HEIGHT_ITEM_RECORD        = 64 | DIMEN;
    public static final int DIMEN_HEIGHT_ITEM_CHOISE_PLANET = 65 | DIMEN;
    public static final int DIMEN_HEIGHT_ITEM_RECV_PLANET   = 66 | DIMEN;

    public static final int[] themeDark = {ATTR_SSH_SPACING, DIMEN_CELL_SPACING,
                                           ATTR_SSH_BM_MENU, TEXT_BITMAP_MENU,
                                           ATTR_SSH_BM_ICONS, TEXT_BITMAP_ICONS,
                                           ATTR_SSH_MODE, SEEK_ANIM_ROTATE,
                                           ATTR_SSH_COLOR_LAYOUT, 0x2d2929 | COLOR,
                                           ATTR_SSH_COLOR_NORMAL, 0x9599f7 | COLOR,
                                           ATTR_SSH_COLOR_LARGE, 0xbc5a1d | COLOR,
                                           ATTR_SSH_COLOR_SMALL, 0x2ea362 | COLOR,
                                           ATTR_SSH_COLOR_HINT, 0xf77499 | COLOR,
                                           ATTR_SSH_COLOR_SELECTOR, 0x5af184 | COLOR,
                                           ATTR_SSH_COLOR_HEADER, 0xcfba41 | COLOR,
                                           ATTR_SSH_COLOR_HTML_HEADER, 0xf22782 | COLOR,
                                           ATTR_SSH_COLOR_NAME_PLANET, 0xe7e114 | COLOR,
                                           ATTR_SSH_COLOR_PANEL_COUNTERS, 0xeac8c8 | COLOR,
                                           ATTR_SSH_COLOR_STAT_COUNTERS, 0xaafdfd | COLOR,
                                           ATTR_SSH_COLOR_LINK, 0xb8fa01 | COLOR,
                                           ATTR_SSH_COLOR_MESSAGE, 0xd2fa64 | COLOR,
                                           ATTR_SSH_COLOR_WINDOW, 0x030303 | COLOR,
                                           ATTR_SSH_COLOR_WIRED, 0x808080 | COLOR,
                                           ATTR_SSH_BM_BACKGROUND, TEXT_BITMAP_BACKGROUND_DARK,
                                           ATTR_SSH_BM_HEADER, TEXT_BITMAP_HEADER_DARK,
                                           ATTR_SSH_BM_SELECT, TEXT_BITMAP_SELECT_DARK,
                                           ATTR_SSH_BM_EDIT, TEXT_BITMAP_EDIT_DARK,
                                           ATTR_SSH_BM_PANEL_PORT, TEXT_BITMAP_PANEL_PORT_DARK,
                                           ATTR_SSH_BM_PANEL_LAND, TEXT_BITMAP_PANEL_LAND_DARK,
                                           ATTR_SSH_BM_TOOLS, TEXT_BITMAP_TOOL_DARK,
                                           ATTR_SSH_BM_BUTTONS, TEXT_BITMAP_BUTTON_DARK,
                                           ATTR_SSH_BM_RADIO, TEXT_BITMAP_RADIO_DARK,
                                           ATTR_SSH_BM_CHECK, TEXT_BITMAP_CHECK_DARK,
                                           ATTR_SSH_BM_SEEK, TEXT_BITMAP_SEEK_DARK,
                                           ATTR_SSH_BM_SWITCH, TEXT_BITMAP_SWITCH_DARK,
                                           ATTR_SSH_BM_TILES, TEXT_BITMAP_CLASSIC_SPRITES};

    public static final int[] themeLight = {ATTR_SSH_SPACING, DIMEN_CELL_SPACING,
                                            ATTR_SSH_BM_MENU, TEXT_BITMAP_MENU,
                                            ATTR_SSH_BM_ICONS, TEXT_BITMAP_ICONS,
                                            ATTR_SSH_MODE, SEEK_ANIM_ROTATE,
                                            ATTR_SSH_COLOR_LAYOUT, 0x9e5e1e | COLOR,
                                            ATTR_SSH_COLOR_NORMAL, 0xea5191 | COLOR,
                                            ATTR_SSH_COLOR_LARGE, 0x5670f1 | COLOR,
                                            ATTR_SSH_COLOR_SMALL, 0x9841df | COLOR,
                                            ATTR_SSH_COLOR_HINT, 0 | COLOR,
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
                                            ATTR_SSH_BM_BACKGROUND, TEXT_BITMAP_BACKGROUND_LIGHT,
                                            ATTR_SSH_BM_HEADER, TEXT_BITMAP_HEADER_LIGHT,
                                            ATTR_SSH_BM_SELECT, TEXT_BITMAP_SELECT_LIGHT,
                                            ATTR_SSH_BM_EDIT, TEXT_BITMAP_EDIT_LIGHT,
                                            ATTR_SSH_BM_PANEL_PORT, TEXT_BITMAP_PANEL_PORT_LIGHT,
                                            ATTR_SSH_BM_PANEL_LAND, TEXT_BITMAP_PANEL_LAND_LIGHT,
                                            ATTR_SSH_BM_TOOLS, TEXT_BITMAP_TOOL_LIGHT,
                                            ATTR_SSH_BM_BUTTONS, TEXT_BITMAP_BUTTON_LIGHT,
                                            ATTR_SSH_BM_RADIO, TEXT_BITMAP_RADIO_LIGHT,
                                            ATTR_SSH_BM_CHECK, TEXT_BITMAP_CHECK_LIGHT,
                                            ATTR_SSH_BM_SEEK, TEXT_BITMAP_SEEK_LIGHT,
                                            ATTR_SSH_BM_SWITCH, TEXT_BITMAP_SWITCH_LIGHT,
                                            ATTR_SSH_BM_TILES, TEXT_BITMAP_CLASSIC_SPRITES};

    public static final int[] style_tile_droid = {ATTR_SHADOW_DX, DIMEN_SHADOW_DX,
                                                  ATTR_SHADOW_DY, DIMEN_SHADOW_DY,
                                                  ATTR_SHADOW_RADIUS, DIMEN_SHADOW_RADIUS,
                                                  ATTR_SHADOW_COLOR, 0 | COLOR,
                                                  ATTR_GRAVITY, Gravity.CENTER,
                                                  ATTR_SSH_VERT, 10,
                                                  ATTR_SSH_HORZ, 4,
                                                  ATTR_SSH_BORDER_WIDTH, DIMEN_BORDER_WIDTH,
                                                  ATTR_SSH_PRESSED_OFFS, DIMEN_PRESSED_OFFSET,
                                                  ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NORMAL | THEME,
                                                  ATTR_SIZE, DIMEN_SIZE_NORMAL,
                                                  ATTR_FONT, TEXT_FONT_NORMAL,
                                                  ATTR_CLICKABLE, 0,
                                                  ATTR_FOCUSABLE, 0,
                                                  ATTR_SSH_BITMAP, ATTR_SSH_BM_TILES | THEME};

    public static final int[] style_chart_stat = {ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_STAT_COUNTERS | THEME,
                                                  ATTR_SSH_DIR, DIRU,
                                                  ATTR_SSH_SHOW, 1,
                                                  ATTR_SIZE, DIMEN_SIZE_STAT,
                                                  ATTR_FONT, TEXT_FONT_SMALL};

    public static final int[] style_text_finish = {ATTR_SHADOW_DX, DIMEN_SHADOW_DX,
                                                   ATTR_SHADOW_DY, DIMEN_SHADOW_DY,
                                                   ATTR_SHADOW_RADIUS, DIMEN_SHADOW_RADIUS,
                                                   ATTR_SHADOW_COLOR, 0 | COLOR,
                                                   ATTR_GRAVITY, Gravity.CENTER_VERTICAL,
                                                   ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NORMAL | THEME,
                                                   ATTR_SIZE, DIMEN_SIZE_FINISH,
                                                   ATTR_FONT, TEXT_FONT_LARGE};

    public static final int[] style_text_counters = {ATTR_SHADOW_DX, DIMEN_SHADOW_DX,
                                                     ATTR_SHADOW_DY, DIMEN_SHADOW_DY,
                                                     ATTR_SHADOW_RADIUS, DIMEN_SHADOW_RADIUS,
                                                     ATTR_SHADOW_COLOR, 0 | COLOR,
                                                     ATTR_GRAVITY, Gravity.CENTER_VERTICAL,
                                                     ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_PANEL_COUNTERS | THEME,
                                                     ATTR_SIZE, DIMEN_SIZE_PANEL,
                                                     ATTR_FONT, TEXT_FONT_NORMAL};

    public static final int[] style_text_planet = {ATTR_SHADOW_DX, DIMEN_SHADOW_DX,
                                                   ATTR_SHADOW_DY, DIMEN_SHADOW_DY,
                                                   ATTR_SHADOW_RADIUS, DIMEN_SHADOW_RADIUS,
                                                   ATTR_SHADOW_COLOR, 0 | COLOR,
                                                   ATTR_GRAVITY, Gravity.CENTER,
                                                   ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NAME_PLANET | THEME,
                                                   ATTR_SIZE, DIMEN_SIZE_PLANET,
                                                   ATTR_FONT, TEXT_FONT_LARGE};
}
/*
    <!-- ************************************************************ -->
    <!-- все слои панелей -->
    <style name="wnd.panelPort">
        <item name="sshCols">28</item>
        <item name="sshRows">18</item>
        <item name="sshBackground">?bmPanelPort</item>
    </style>
    <style name="wnd.panelLand">
        <item name="sshCols">14</item>
        <item name="sshRows">32</item>
        <item name="sshBackground">?bmPanelLand</item>
    </style>

    <!-- ************************************************************ -->
    <!-- все поверхности -->

    <style name="wnd.game">
        <item name="android:id">@id/game</item>
        <item name="sshValue">100</item>
    </style>
    <style name="wnd.editor">
        <item name="android:id">@id/editor</item>
        <item name="sshValue">30</item>
    </style>
 */
