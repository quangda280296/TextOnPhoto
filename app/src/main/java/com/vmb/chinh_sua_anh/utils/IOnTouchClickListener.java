package com.vmb.chinh_sua_anh.utils;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.activity.MainActivity;

public class IOnTouchClickListener implements View.OnTouchListener {
    /**
     * min movement to detect as a click action.
     */
    private int minMove = 50;
    private float startX;
    private float startY;

    private Context context;
    private int type = 0;
    private boolean fillAfter = false;
    private View.OnClickListener mListener;

    public IOnTouchClickListener(Context context, View.OnClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public IOnTouchClickListener(Context context, int type, View.OnClickListener mListener) {
        this.context = context;
        this.type = type;
        this.mListener = mListener;
    }

    public IOnTouchClickListener(Context context, boolean fillAfter, View.OnClickListener mListener) {
        this.context = context;
        this.fillAfter = fillAfter;
        this.mListener = mListener;
    }

    public IOnTouchClickListener(Context context, int type, boolean fillAfter, View.OnClickListener mListener) {
        this.context = context;
        this.type = type;
        this.fillAfter = fillAfter;
        this.mListener = mListener;
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > minMove || differenceY > minMove) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        Animation zoom_in = null;
        Animation zoom_out = null;

        if (type == 0 || type == 1 || type == 4 || type == 5) {
            zoom_in = AnimationUtils.loadAnimation(context, com.vmb.ads_in_app.R.anim.zoom_in);
            zoom_out = AnimationUtils.loadAnimation(context, com.vmb.ads_in_app.R.anim.zoom_out);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                if (type == 2)
                    v.setBackgroundColor(context.getResources().getColor(R.color.purple_lv_4));
                else if (type == 3)
                    v.setBackgroundColor(context.getResources().getColor(R.color.white_lv_2));
                else if (type == 0 || type == 1 || type == 4 || type == 5)
                    v.startAnimation(zoom_out);
                return true;

            case MotionEvent.ACTION_UP:
                if (type == 0 || type == 1 || type == 4 || type == 5)
                    v.startAnimation(zoom_in);

                float endX = event.getX();
                float endY = event.getY();

                if (isAClick(startX, endX, startY, endY)) {
                    /*if (type == 0) {
                        if (context instanceof MainActivity)
                            Utils.setBackground((MainActivity) context, R.id.bottom,
                                    context.getResources().getColor(R.color.white_lv_1));
                    } else if (type == 1) {
                        if (context instanceof MainActivity)
                            Utils.setBackground((MainActivity) context, R.id.bottom,
                                    context.getResources().getColor(R.color.black));
                    } else */if (type == 4) {
                        if (context instanceof MainActivity)
                            Utils.setBackgroundBlack((MainActivity) context, R.id.align,
                                    context.getResources().getColor(android.R.color.transparent));
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onClick(v);
                        }
                    }, 100);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if (type == 2)
                    v.setBackgroundColor(context.getResources().getColor(R.color.gray_lv_6));
                else if (type == 3)
                    v.setBackgroundColor(context.getResources().getColor(R.color.gray_lv_3));
                else if (type == 0 || type == 1 || type == 4 || type == 5)
                    v.startAnimation(zoom_in);
                break;
        }

        return false;
    }
}