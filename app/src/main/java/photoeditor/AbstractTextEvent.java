package photoeditor;

import android.view.MotionEvent;

import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.xiaopo.flying.sticker.StickerIconEvent;
import com.xiaopo.flying.sticker.StickerView;

public abstract class AbstractTextEvent implements StickerIconEvent {

    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        if (isText())
            PhotoHandler.getInstance().getPhotoEditor().requestEditText();
    }

    protected abstract boolean isText();
}