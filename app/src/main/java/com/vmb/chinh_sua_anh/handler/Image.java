package com.vmb.chinh_sua_anh.handler;

import java.util.ArrayList;
import java.util.List;

public class Image {

    private static Image images;

    public static Image getInstance() {
        if (images == null) {
            synchronized (Image.class) {
                images = new Image();
            }
        }
        return images;
    }

    public void destroy() {
        this.images = null;
    }

    private data data;

    public Image.data getData() {
        return data;
    }

    public void setData(Image.data data) {
        this.data = data;
    }

    public class data {
        private List<item> stickers = new ArrayList<>();
        private List<item> photos = new ArrayList<>();

        public List<item> getStickers() {
            return stickers;
        }

        public List<item> getPhotos() {
            return photos;
        }

        public class item {
            private String id = "";
            private String uid = "";
            private String name = "";
            private String summary = "";
            private int count = 0;
            private int version = 0;
            private String url = "";
            private String icon = "";

            private boolean isChangeVersion = false;

            public String getId() {
                return id;
            }

            public String getUid() {
                return uid;
            }

            public String getName() {
                return name;
            }

            public String getSummary() {
                return summary;
            }

            public int getCount() {
                return count;
            }

            public int getVersion() {
                return version;
            }

            public String getUrl() {
                return url;
            }

            public String getUrl_icon() {
                return icon;
            }

            public boolean isChangeVersion() {
                return isChangeVersion;
            }

            public void setChangeVersion(boolean changeVersion) {
                isChangeVersion = changeVersion;
            }
        }
    }
}