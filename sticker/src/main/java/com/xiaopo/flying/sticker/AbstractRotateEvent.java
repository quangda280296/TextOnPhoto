package com.xiaopo.flying.sticker;

import android.view.MotionEvent;

public abstract class AbstractRotateEvent implements StickerIconEvent {

    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        if (isResetRotation())
            stickerView.resetRotaion();
    }

    protected abstract boolean isResetRotation();
}