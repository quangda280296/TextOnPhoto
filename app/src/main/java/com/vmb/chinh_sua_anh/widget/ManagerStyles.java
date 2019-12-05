package com.vmb.chinh_sua_anh.widget;

import android.graphics.Color;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.vmb.chinh_sua_anh.model.Styles;

import java.util.ArrayList;

public class ManagerStyles {

    private Styles styles;
    private String FONT_NAME = "";
    ArrayList<Styles> stylesArrayList = new ArrayList();

    public ManagerStyles() {
        styles = new Styles();
        styles.setTextColor(-1);
        styles.setStrokeWidth(2);
        styles.setStrokeColor(-16776961);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK);
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(SupportMenu.CATEGORY_MASK);
        styles.setStrokeWidth(2);
        styles.setStrokeColor(-16776961);
        styles.setShadowColor(InputDeviceCompat.SOURCE_ANY);
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(SupportMenu.CATEGORY_MASK);
        styles.setStrokeWidth(2);
        styles.setStrokeColor(SupportMenu.CATEGORY_MASK);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK);
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(InputDeviceCompat.SOURCE_ANY);
        styles.setStrokeWidth(2);
        styles.setStrokeColor(-16776961);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(SupportMenu.CATEGORY_MASK);
        styles.setStrokeWidth(2);
        styles.setStrokeColor(-16711681);
        styles.setShadowColor(InputDeviceCompat.SOURCE_ANY);
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(SupportMenu.CATEGORY_MASK);
        styles.setStrokeWidth(2);
        styles.setStrokeColor(-16776961);
        styles.setShadowColor(InputDeviceCompat.SOURCE_ANY);
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(229, 229, 229));
        styles.setStrokeWidth(0);
        styles.setStrokeColor(1);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(255, 108, 0));
        styles.setStrokeWidth(1);
        styles.setStrokeColor(Color.rgb(51, 0, 0));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(255, 246, 0));
        styles.setStrokeWidth(1);
        styles.setStrokeColor(Color.rgb(51, 0, 0));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(228, 64, 0));
        styles.setStrokeWidth(1);
        styles.setStrokeColor(Color.rgb(51, 0, 0));
        styles.setShadowColor(-1);
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(-1);
        styles.setStrokeWidth(3);
        styles.setStrokeColor(Color.rgb(255, 0, 0));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(7);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(SupportMenu.CATEGORY_MASK);
        styles.setStrokeWidth(3);
        styles.setStrokeColor(Color.rgb(255, 255, 255));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(7);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(157, 241, 161));
        styles.setStrokeWidth(2);
        styles.setStrokeColor(Color.rgb(43, 194, 82));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(2);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(255, TsExtractor.TS_STREAM_TYPE_E_AC3, 26));
        styles.setStrokeWidth(2);
        styles.setStrokeColor(Color.rgb(PsExtractor.PRIVATE_STREAM_1, 91, 1));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(2);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(122, 193, 255));
        styles.setStrokeWidth(2);
        styles.setStrokeColor(Color.rgb(0, 78, 149));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(3);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(228, 103, 21));
        styles.setStrokeWidth(2);
        styles.setStrokeColor(-1);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(0);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(255, 255, 255));
        styles.setStrokeWidth(4);
        styles.setStrokeColor(ViewCompat.MEASURED_STATE_MASK);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(0);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(218, 29, 131));
        styles.setStrokeWidth(3);
        styles.setStrokeColor(-1);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK); 
        styles.setShadowDxDy(0);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(219, 71, 25));
        styles.setStrokeWidth(3);
        styles.setStrokeColor(-1);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK);
        styles.setShadowDxDy(0);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(Color.rgb(251, 0, 0));
        styles.setStrokeWidth(0);
        styles.setStrokeColor(-1);
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK);
        styles.setShadowDxDy(10);
        this.stylesArrayList.add(styles);

        styles = new Styles();
        styles.setTextColor(-1);
        styles.setStrokeWidth(3);
        styles.setStrokeColor(Color.rgb(168, 0, 255));
        styles.setShadowColor(ViewCompat.MEASURED_STATE_MASK);
        styles.setShadowDxDy(8);
        this.stylesArrayList.add(styles);
    }

    public static CharSequence ClearStyles(String str) {
        CharSequence spannableString = new SpannableString(str);
        int i = 0;
        ForegroundColorSpan[] foregroundColorSpanArr = ((SpannableString) spannableString).
                getSpans(0, spannableString.length(), ForegroundColorSpan.class);
        int length = foregroundColorSpanArr.length;
        while (i < length) {
            ((SpannableString) spannableString).removeSpan(foregroundColorSpanArr[i]);
            i++;
        }
        return spannableString;
    }

    public ArrayList<Styles> getStylesArrayList() {
        return this.stylesArrayList;
    }

    public void setStylesArrayList(ArrayList<Styles> arrayList) {
        this.stylesArrayList = arrayList;
    }
}
