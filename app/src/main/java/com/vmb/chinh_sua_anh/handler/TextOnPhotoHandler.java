package com.vmb.chinh_sua_anh.handler;

import com.vmb.chinh_sua_anh.model.TextOnPhoto;
import com.xiaopo.flying.sticker.Sticker;

import java.util.ArrayList;
import java.util.List;

public class TextOnPhotoHandler {

    private static TextOnPhotoHandler textOnPhoto;

    private List<TextOnPhoto> list = new ArrayList<>();
    private Sticker sticker;
    private int iD = 0;

    public static TextOnPhotoHandler getInstance() {
        if(textOnPhoto == null) {
            synchronized (TextOnPhotoHandler.class) {
                textOnPhoto = new TextOnPhotoHandler();
            }
        }
        return textOnPhoto;
    }

    public void destroy() {
        textOnPhoto = null;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public void addToList(TextOnPhoto item) {
        this.list.add(item);
    }

    public TextOnPhoto getItem() {
        if(iD >= this.list.size())
            return null;
        return this.list.get(iD);
    }

    public void setItem(TextOnPhoto item) {
        this.list.set(iD, item);
    }

    public List<TextOnPhoto> getList() {
        return list;
    }

    public void setList(List<TextOnPhoto> list) {
        this.list = list;
    }
}