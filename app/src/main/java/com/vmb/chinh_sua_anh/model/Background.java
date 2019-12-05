package com.vmb.chinh_sua_anh.model;

import java.util.ArrayList;
import java.util.List;

public class Background {

    private data data;

    public Background.data getData() {
        return data;
    }

    public class data {
        private List<bg> list_status_text = new ArrayList<>();
        private bg cate_other;

        public List<bg> getList_status_text() {
            return list_status_text;
        }

        public void setList_status_text(List<bg> list_status_text) {
            this.list_status_text = list_status_text;
        }

        public bg getCate_other() {
            return cate_other;
        }

        public void setCate_other(bg cate_other) {
            this.cate_other = cate_other;
        }

        public class bg {
            private String name = "";
            private String folder = "";
            private int total_bg = 0;

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

            public int getTotal_bg() {
                return total_bg;
            }

            public void setTotal_bg(int total_bg) {
                this.total_bg = total_bg;
            }
        }
    }
}