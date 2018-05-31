package ru.ostrovskal.droid;

import android.graphics.Color;
import android.view.Gravity;

import static com.github.ostrovskal.ssh.Constants.DIRU;
import static com.github.ostrovskal.ssh.Constants.SEEK_ANIM_ROTATE;
import static com.github.ostrovskal.ssh.Constants.SEEK_ANIM_SCALE;
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
    public final static String KEY_THEME           = "theme";
    public final static String KEY_TMP_THEME       = "#tmp_theme";
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

    public static final int[] tilesEditorPanel  = {R.integer.TILE_NULL, R.integer.TILE_EARTH, R.integer.TILE_STONE, R.integer.TILE_BOMB, R.integer.TILE_BRICK,
                                                   R.integer.TILE_BETON, R.integer.TILE_BOMBS, R.integer.TILE_FUEL, R.integer.TILE_EXPL, R.integer.TILE_SCORE,
                                                   R.integer.TILE_TIME, R.integer.TILE_LIFE, R.integer.TILE_EYEG, R.integer.TILE_DROIDR, R.integer.TILE_EGG,
                                                   R.integer.TILE_YELLOWD, R.integer.TILE_REDD, R.integer.TILE_GREEND};

    public static final int[] tilesGamePanel    = {R.integer.TILE_SCORE, R.integer.TILE_TIME, R.integer.TILE_FUEL, R.integer.TILE_LIFE, R.integer.TILE_BOMB,
                                                   R.integer.TILE_EGG, R.integer.TILE_YELLOWD, R.integer.TILE_REDD, R.integer.TILE_GREEND};

    public static final int[] iconsEditorActions= {R.integer.I_NEW_PLANET, R.integer.I_OPEN_PLANET, R.integer.I_PROP_PLANET, R.integer.I_HELP,
                                                   R.integer.I_DELETE_PLANET, R.integer.I_GEN_PLANET, R.integer.I_SEND_PACK, R.integer.I_ALL_PLANET,
                                                   R.integer.I_TEST_PLANET, R.integer.I_SAVE_PLANET};
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

    public static final int ATTR_SSH_COLOR_PANEL_COUNTERS = 1000 | ATTR_INT;
    public static final int ATTR_SSH_COLOR_NAME_PLANET    = 1001 | ATTR_INT;
    public static final int ATTR_SSH_COLOR_STAT_COUNTERS  = 1002 | ATTR_INT;
    public static final int ATTR_SSH_BM_PANEL_PORT        = 1003 | ATTR_DRW;
    public static final int ATTR_SSH_BM_PANEL_LAND        = 1004 | ATTR_DRW;

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
                                                  ATTR_CLICKABLE, 0,
                                                  ATTR_FOCUSABLE, 0,
                                                  ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_TILES | THEME};

    public static final int[] style_record      = {ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_STAT_COUNTERS | THEME,
                                                   ATTR_SSH_DIR, DIRU,
                                                   ATTR_SSH_SHOW, 1,
                                                   ATTR_SIZE, R.dimen.record,
                                                   ATTR_FONT, R.string.font_small};

    public static final int[] style_text_finish = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                   ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                   ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                   ATTR_SHADOW_COLOR, 0 | COLOR,
                                                   ATTR_GRAVITY, Gravity.CENTER_VERTICAL,
                                                   ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NORMAL | THEME,
                                                   ATTR_SIZE, R.dimen.finish,
                                                   ATTR_FONT, R.string.font_large};

    public static final int[] style_text_counters = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                     ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                     ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                     ATTR_SHADOW_COLOR, 0 | COLOR,
                                                     ATTR_GRAVITY, Gravity.CENTER_VERTICAL,
                                                     ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_PANEL_COUNTERS | THEME,
                                                     ATTR_SIZE, R.dimen.panel,
                                                     ATTR_FONT, R.string.font_normal};

    public static final int[] style_text_planet = {ATTR_SHADOW_DX, R.dimen.shadowTextX,
                                                   ATTR_SHADOW_DY, R.dimen.shadowTextY,
                                                   ATTR_SHADOW_RADIUS, R.dimen.shadowTextR,
                                                   ATTR_SHADOW_COLOR, 0 | COLOR,
                                                   ATTR_GRAVITY, Gravity.CENTER,
                                                   ATTR_COLOR_DEFAULT, ATTR_SSH_COLOR_NAME_PLANET | THEME,
                                                   ATTR_SIZE, R.dimen.planet,
                                                   ATTR_FONT, R.string.font_large};

    public static final int[] style_panel_port = {ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_PANEL_PORT | THEME};
    public static final int[] style_panel_land = {ATTR_SSH_BITMAP_NAME, ATTR_SSH_BM_PANEL_LAND | THEME};
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
