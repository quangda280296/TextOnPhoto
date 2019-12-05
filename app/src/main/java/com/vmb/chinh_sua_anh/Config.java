package com.vmb.chinh_sua_anh;

import com.quangda280296.photoeditor.BuildConfig;

public class Config {

    public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    public static final String VERSION_APP = BuildConfig.VERSION_NAME;
    public static final String CODE_CONTROL_APP = "00649";

    public static final String[] FORMAT_IMG = new String[]{".PNG", ".JPEG", ".JPG", ".png", ".jpeg", ".jpg"};
    public static final String URL_API_LOG_TIME_LOAD_IMG = "text-on-photo/log_time_load_img.php";
    public static final String PREFERENCE_KEY_SEND_TIME = "key_send_time";

    public class RequestCode {
        public static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
        public static final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;

        public static final int REQUEST_CODE_CAMERA = 10;
        public static final int REQUEST_CODE_DETAIL = 11;

        public static final int REQUEST_CODE_CHANGE_PHOTO = 20;
        public static final int REQUEST_CODE_FILTER = 21;
        public static final int REQUEST_CODE_CROP = 22;

        public static final int REQUEST_CODE_INSERT_TEXT = 30;
        public static final int REQUEST_CODE_INSERT_STICKER = 31;

        public static final int REQUEST_CODE_EDIT_TEXT = 40;
        public static final int REQUEST_CODE_FONT = 41;
    }

    public class FileName {
        public static final String FILE_IMAGES_DATA = "images.txt";
        public static final String FILE_FONTS_NAME = "fonts_name.txt";
        public static final String FILE_FONTS_DATA = "fonts.txt";
        public static final String FILE_NAME_TEMP_PHOTO = "temp";
    }

    public class KeyIntentData {
        public static final String KEY_CHOOSE_IMAGE_PATH_FOLDER = "path_directory";
        public static final String KEY_CHOOSE_IMAGE_DETAIL = "index_directory";

        public static final String KEY_MAIN_ACT_PHOTO_PATH = "photo_path";
        public static final String KEY_MAIN_ACT_URI_PATH = "uri_path";

        public static final String KEY_AFTER_SAVING_ACT_IMAGE_PATH = "path";

        public static final String KEY_TEXT_INPUT_ACT = "text";
        public static final String KEY_LINE_INPUT_ACT = "line";
        public static final String KEY_DETAIL_PHOTO_ACT = "photo_detail";
    }

    public class Directory {
        public static final String SAVE_DIRECTORY = "VMTextOnPhoto";
    }

    public class UrlData {
        /*public static final String URL_BACKGROUND = "http://139.59.241.64/App/Status/";
        public static final String URL_DATA_BACKGROUND = "VietChuLenAnh_VI_BESTPHOTOEDITOR_V2.php";

        public static final String URL_STICKER = "http://139.59.241.64/App/PhotoEditor/Stickers/";
        public static final String URL_DATA_STICKER = "TextOnPhoto_BESTPHOTOEDITOR_Stickers.php";*/

        public static final String URL_IMAGES = "stickers/category.php";
        public static final String URL_FONTS = "stickers/fonts.php";
    }

    public class FragmentTag {
        public static final String Fragment_Tab_1_Detail = "Fragment_Tab_1_Detail";
        public static final String Fragment_Tab_2 = "Fragment_Tab_2";

        public static final String MAIN_BOTTOM_5_BUTTON_FRAGMENT = "MAIN_BOTTOM_5_BUTTON_FRAGMENT";

        public static final String MAIN_DETAIL_BRUSH_MODE_FRAGMENT = "MAIN_DETAIL_BRUSH_MODE_FRAGMENT";
        public static final String MAIN_DETAIL_ERASE_MODE_FRAGMENT = "MAIN_DETAIL_ERASE_MODE_FRAGMENT";

        public static final String MAIN_BOTTOM_MODIFY_TEXT_FRAGMENT = "MAIN_BOTTOM_MODIFY_TEXT_FRAGMENT";

        public static final String MAIN_DETAIL_COLOR_TEXT_FRAGMENT = "MAIN_DETAIL_COLOR_TEXT_FRAGMENT";
        public static final String MAIN_DETAIL_EDIT_IMAGE_FRAGMENT = "MAIN_DETAIL_EDIT_IMAGE_FRAGMENT";
        public static final String MAIN_DETAIL_FONT_TEXT_FRAGMENT = "MAIN_DETAIL_FONT_TEXT_FRAGMENT";
        public static final String MAIN_DETAIL_SIZE_TEXT_FRAGMENT = "MAIN_DETAIL_SIZE_TEXT_FRAGMENT";
        public static final String MAIN_DETAIL_STYLE_TEXT_FRAGMENT = "MAIN_DETAIL_STYLE_TEXT_FRAGMENT";
        public static final String MAIN_DETAIL_BORDER_TEXT_FRAGMENT = "MAIN_DETAIL_BORDER_TEXT_FRAGMENT";
    }

    public class BitmapFilter {
        public static final float EFFECT_AUTOFIX = .5f;
        public static final float EFFECT_BRIGHTNESS = 1.1f;
        public static final float EFFECT_CONTRAST = 1.3f;
        public static final float EFFECT_SHARPEN = .2f;
        public static final float EFFECT_FILLLIGHT = .4f;
        public static final float EFFECT_FISHEYE = .5f;
        public static final float EFFECT_GRAIN = 0.7f;
        public static final float EFFECT_SATURATE = .5f;
        public static final float EFFECT_TEMPERATURE = .9f;
        public static final float EFFECT_VIGNETTE = .5f;

        public class EFFECT_BLACKWHITE {
            public static final float Black = .1f;
            public static final float White = .7f;
        }
    }

    public class Event {
        public static final String SAVED_IMG = "saved_img";
    }
}