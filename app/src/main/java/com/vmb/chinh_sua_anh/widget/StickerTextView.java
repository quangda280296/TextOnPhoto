package com.vmb.chinh_sua_anh.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.vm.TextOnPhoto.PhotoEditor.R;

/**
 * Created by cheungchingai on 6/15/15.
 */
public class StickerTextView extends StickerText {

    private MagicTextView tv_main;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, int lines) {
        super(context, lines);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if (tv_main != null)
            return tv_main;

        tv_main = new MagicTextView(getContext());
        tv_main.setText("");
        tv_main.setTextSize(140);
        tv_main.setTextColor(Color.WHITE);
        tv_main.setSingleLine(false);
        tv_main.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);
        int padding_text = getContext().getResources().getDimensionPixelSize(R.dimen.padding_text);
        tv_main.setPadding(padding_text, padding_text / 2, padding_text, padding_text / 2);
        return tv_main;
    }

    public void setFont(Typeface typeface) {
        if (tv_main != null) {
            tv_main.setTypeface(typeface);
        }
    }

    public void setText(String text) {
        if (tv_main != null) {
            tv_main.setText(text);
        }
    }

    public String getText() {
        if (tv_main != null)
            return tv_main.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }
}