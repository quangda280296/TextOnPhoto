package com.xiaopo.flying.sticker;

/**
 * @author wupanjie
 */

public class ResetRotationEvent extends AbstractRotateEvent {

    @Override
    protected boolean isResetRotation() {
        return true;
    }
}