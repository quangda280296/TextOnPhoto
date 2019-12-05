package com.vmb.chinh_sua_anh.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class StylesTextView extends TextView {

    private int mStrokeColor;
    private float mStrokeWidth;
    private int mTextColor;

    public StylesTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StylesTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public StylesTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.mStrokeColor = -1;
        this.mStrokeWidth = -1;
        this.mTextColor = -1;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TextPaint paint = getPaint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeMiter(0.0f);
        setTextColor(this.mStrokeColor);
        paint.setStrokeWidth(this.mStrokeWidth);
        super.onDraw(canvas);
        paint.setStyle(Style.FILL);
        setTextColor(this.mTextColor);
        super.onDraw(canvas);
    }

    public float getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void setStroke(float f, int i) {
        this.mStrokeWidth = f;
        this.mStrokeColor = i;
    }

    public void setmTextColor(int i) {
        this.mTextColor = i;
    }
}
