package photoeditor;

import android.view.MotionEvent;

import com.vmb.chinh_sua_anh.utils.FragmentUtil;
import com.xiaopo.flying.sticker.StickerIconEvent;
import com.xiaopo.flying.sticker.StickerView;

/**
 * @author wupanjie
 */

public class DeleteTextEvent implements StickerIconEvent {
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        stickerView.removeCurrentSticker();
        while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 0)
            FragmentUtil.getInstance().getManager().popBackStackImmediate();
    }
}