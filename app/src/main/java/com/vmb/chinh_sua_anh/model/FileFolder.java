package com.vmb.chinh_sua_anh.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFolder {

    private String name;
    private String pathFolder;
    private String pathFile;
    private List<File> listPhoto = new ArrayList<>();

    public FileFolder(String name, String pathFolder, String pathFile) {
        this.name = name;
        this.pathFolder = pathFolder;
        this.pathFile = pathFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public List<File> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(List<File> listPhoto) {
        this.listPhoto = listPhoto;
    }

    /*private Builder builder;

    public Builder getBuilder() {
        return builder;
    }

    public FileFolder(Builder builder) {
        this.builder = builder;
    }

    public static class Builder {
        private String name;
        private String pathFile;
        private String pathFolder;
        private List<File> listFile = new ArrayList<>();

        public String getName() {
            return name;
        }

        public String getPathFile() {
            return pathFile;
        }

        public String getPathFolder() {
            return pathFolder;
        }

        public List<File> getListFile() {
            return listFile;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPathFile(String pathFile) {
            this.pathFile = pathFile;
            return this;
        }

        public Builder setPathFolder(String pathFolder) {
            this.pathFolder = pathFolder;
            return this;
        }

        public Builder addFirstFile(File file) {
            this.listFile.add(file);
            return this;
        }

        public FileFolder build() {
            return new FileFolder(this);
        }
    }*/
}