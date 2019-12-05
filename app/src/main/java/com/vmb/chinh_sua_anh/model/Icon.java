package com.vmb.chinh_sua_anh.model;

import java.util.List;

public class Icon {

    private data data;

    public class data {
        private List<sticker> list_stickers;

        public List<sticker> getList_stickers() {
            return list_stickers;
        }

        public void setList_stickers(List<sticker> list_stickers) {
            this.list_stickers = list_stickers;
        }

        public class sticker {
            private String name = "";
            private String folder = "";
            private String icon = "";
            private int total_image = 0;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFolder() {
                return folder;
            }

            public void setFolder(String folder) {
                this.folder = folder;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getTotal_image() {
                return total_image;
            }

            public void setTotal_image(int total_image) {
                this.total_image = total_image;
            }
        }
    }

    public Icon.data getData() {
        return data;
    }
}