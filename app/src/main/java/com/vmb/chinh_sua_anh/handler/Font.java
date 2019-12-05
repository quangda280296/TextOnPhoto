package com.vmb.chinh_sua_anh.handler;

import java.util.ArrayList;
import java.util.List;

public class Font {

    private static Font fonts;

    public static Font getInstance() {
        if (fonts == null) {
            synchronized (Font.class) {
                fonts = new Font();
            }
        }
        return fonts;
    }

    public void setInstance(Font fonts) {
        this.fonts = fonts;
    }

    public void destroy() {
        this.fonts = null;
    }

    private List<item> data;

    public List<item> getData() {
        return data;
    }

    public class item {
        private String id = "";
        private String uid = "";
        private String name = "";
        private String icon = "";
        private String url = "";
        private List<String> files = new ArrayList<>();

        public String getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getUrl_icon() {
            return icon;
        }

        public List<String> getFiles() {
            return files;
        }
    }
}