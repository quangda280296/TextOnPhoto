package com.vmb.chinh_sua_anh.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;

import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.xiaopo.flying.sticker.BackgroundDrawable;

public class TextOnPhoto {

    private String text;
    private int textColor;
    private float textSize;
    private int textLine;

    private Typeface typeface;
    private AlignText alignText;

    private boolean isSetBackground;
    private BackgroundDrawable shapeDrawable;

    private int shadowColor;
    private int shadowDxDy;
    private int shadowRadius;

    private int strokeColor;
    private int strokeWidth;

    private boolean styleTypeFaceBold;
    private boolean styleTypeFaceItalic;
    private boolean styleTypeFaceNormal;
    private boolean styleTypeFaceStriker;
    private boolean styleTypeFaceUnderline;

    public TextOnPhoto(Context context) {
        this.textColor = Color.WHITE;
        this.alignText = AlignText.CENTER;
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Original Fonts/Fonts/SVN-Steady.otf");

        this.textLine = 1;
        this.textSize = 35.0f;

        this.strokeWidth = 0;
        this.strokeColor = 0;

        this.shadowDxDy = 0;
        this.shadowColor = 0;
        this.shadowRadius = 0;

        this.styleTypeFaceNormal = true;
        this.styleTypeFaceBold = false;
        this.styleTypeFaceItalic = false;
        this.styleTypeFaceUnderline = false;
        this.styleTypeFaceStriker = false;

        TextOnPhotoHandler.getInstance().addToList(this);
        TextOnPhotoHandler.getInstance().setiD(TextOnPhotoHandler.getInstance().getList().size() - 1);
    }

    public void setStyleTypeFaceNormal(boolean z) {
        this.styleTypeFaceNormal = z;
        if (z) {
            setStyleTypeFaceBold(false);
            setStyleTypeFaceItalic(false);
            setStyleTypeFaceUnderline(false);
            setStyleTypeFaceStriker(false);
        }
    }

    public void clearStrokeAndShadow() {
        this.strokeWidth = 0;
        this.strokeColor = textColor;
        this.shadowDxDy = 0;
        this.shadowColor = ViewCompat.MEASURED_STATE_MASK;
        this.shadowRadius = 0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextLine() {
        return textLine;
    }

    public void setTextLine(int textLine) {
        this.textLine = textLine;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public AlignText getAlignText() {
        return alignText;
    }

    public void setAlignText(AlignText alignText) {
        this.alignText = alignText;
    }

    public boolean isSetBackground() {
        return isSetBackground;
    }

    public void setSetBackground(boolean setBackground) {
        isSetBackground = setBackground;
    }

    public BackgroundDrawable getShapeDrawable() {
        return shapeDrawable;
    }

    public void setShapeDrawable(BackgroundDrawable shapeDrawable) {
        this.shapeDrawable = shapeDrawable;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowDxDy() {
        return shadowDxDy;
    }

    public void setShadowDxDy(int shadowDxDy) {
        this.shadowDxDy = shadowDxDy;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public boolean isStyleTypeFaceBold() {
        return styleTypeFaceBold;
    }

    public void setStyleTypeFaceBold(boolean styleTypeFaceBold) {
        this.styleTypeFaceBold = styleTypeFaceBold;
        if (styleTypeFaceBold)
            setStyleTypeFaceNormal(false);
    }

    public boolean isStyleTypeFaceItalic() {
        return styleTypeFaceItalic;
    }

    public void setStyleTypeFaceItalic(boolean styleTypeFaceItalic) {
        this.styleTypeFaceItalic = styleTypeFaceItalic;
        if (styleTypeFaceItalic)
            setStyleTypeFaceNormal(false);
    }

    public boolean isStyleTypeFaceNormal() {
        return styleTypeFaceNormal;
    }

    public boolean isStyleTypeFaceStriker() {
        return styleTypeFaceStriker;
    }

    public void setStyleTypeFaceStriker(boolean styleTypeFaceStriker) {
        this.styleTypeFaceStriker = styleTypeFaceStriker;
        if (styleTypeFaceStriker)
            setStyleTypeFaceNormal(false);
    }

    public boolean isStyleTypeFaceUnderline() {
        return styleTypeFaceUnderline;
    }

    public void setStyleTypeFaceUnderline(boolean styleTypeFaceUnderline) {
        this.styleTypeFaceUnderline = styleTypeFaceUnderline;
        if (styleTypeFaceUnderline)
            setStyleTypeFaceNormal(false);
    }
}