package com.vmb.chinh_sua_anh.model;

public class Styles {

    private int shadowColor;
    private int shadowDxDy;
    private int strokeColor;
    private int strokeWidth;
    private int textColor;

    public Styles() {
    }

    public Styles(int i, int i2, int i3, int i4, int i5) {
        this.shadowColor = i;
        this.shadowDxDy = i2;
        this.strokeColor = i3;
        this.strokeWidth = i4;
        this.textColor = i5;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(int i) {
        this.shadowColor = i;
    }

    public int getShadowDxDy() {
        return this.shadowDxDy;
    }

    public void setShadowDxDy(int i) {
        this.shadowDxDy = i;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(int i) {
        this.strokeColor = i;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(int i) {
        this.strokeWidth = i;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }
}