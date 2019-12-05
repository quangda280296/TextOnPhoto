package com.vmb.chinh_sua_anh.handler;

import android.graphics.Bitmap;

import photoeditor.PhotoEditor;
import photoeditor.PhotoFilter;

public class PhotoHandler {

    private static PhotoHandler mainData;

    //private boolean isNeedOverrideFilter = false;

    // Photo Editor
    private PhotoEditor photoEditor;
    private PhotoEditor photoEditor_extra;

    // Bitmap Data
    private Bitmap fullScaleBitmap;
    private Bitmap sourceBitmap;
    private Bitmap filterBitmap;

    private Bitmap sticker;

    // Mode
    private boolean eraserMode = false;
    private boolean changePhotoMode;

    private PhotoFilter photoFilter;
    private float param_1;
    private float param_2;

    public static PhotoHandler getInstance() {
        if (mainData == null) {
            synchronized (PhotoHandler.class) {
                mainData = new PhotoHandler();
            }
        }
        return mainData;
    }

    public void destroy() {
        mainData = null;
    }

    /*public boolean isNeedOverrideFilter() {
        return isNeedOverrideFilter;
    }

    public void setNeedOverrideFilter(boolean needOverrideFilter) {
        isNeedOverrideFilter = needOverrideFilter;
    }*/

    public PhotoEditor getPhotoEditor() {
        return photoEditor;
    }

    public void setPhotoEditor(PhotoEditor photoEditor) {
        this.photoEditor = photoEditor;
    }

    public PhotoEditor getPhotoEditor_extra() {
        return photoEditor_extra;
    }

    public void setPhotoEditor_extra(PhotoEditor photoEditor_extra) {
        this.photoEditor_extra = photoEditor_extra;
    }

    public Bitmap getFullScaleBitmap() {
        return fullScaleBitmap;
    }

    public void setFullScaleBitmap(Bitmap fullScaleBitmap) {
        this.fullScaleBitmap = fullScaleBitmap;
    }

    public Bitmap getSourceBitmap() {
        return sourceBitmap;
    }

    public void setSourceBitmap(Bitmap sourceBitmap) {
        this.sourceBitmap = sourceBitmap;
    }

    public Bitmap getFilterBitmap() {
        return filterBitmap;
    }

    public void setFilterBitmap(Bitmap filterBitmap) {
        this.filterBitmap = filterBitmap;
    }

    public Bitmap getSticker() {
        return sticker;
    }

    public void setSticker(Bitmap sticker) {
        this.sticker = sticker;
    }

    public boolean isEraserMode() {
        return eraserMode;
    }

    public void setEraserMode(boolean eraserMode) {
        this.eraserMode = eraserMode;
    }

    public boolean isChangePhotoMode() {
        return changePhotoMode;
    }

    public void setChangePhotoMode(boolean changePhotoMode) {
        this.changePhotoMode = changePhotoMode;
    }

    public PhotoFilter getPhotoFilter() {
        return photoFilter;
    }

    public void setPhotoFilter(PhotoFilter photoFilter) {
        this.photoFilter = photoFilter;
    }

    public float getParam_1() {
        return param_1;
    }

    public void setParam_1(float param_1) {
        this.param_1 = param_1;
    }

    public float getParam_2() {
        return param_2;
    }

    public void setParam_2(float param_2) {
        this.param_2 = param_2;
    }
}