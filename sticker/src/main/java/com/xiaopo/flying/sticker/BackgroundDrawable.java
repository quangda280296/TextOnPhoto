package com.xiaopo.flying.sticker;

import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;

public class BackgroundDrawable extends GradientDrawable {
    private int alpha = 0;
    private int color = 0;
    private float cornerRadius = 0.0f;
    private int height = -1;
    private int width = -1;

    public void setColor(int i) {
        super.setColor(i);
        this.color = i;
    }

    public void setSize(int i, int i2) {
        super.setSize(i, i2);
        this.width = i;
        this.height = i2;
    }

    public void setBounds(@NonNull Rect rect) {
        super.setBounds(rect);
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.alpha = i;
    }

    public void setCornerRadius(float f) {
        super.setCornerRadius(f);
        this.cornerRadius = f;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getBkgColor() {
        return this.color;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }
}