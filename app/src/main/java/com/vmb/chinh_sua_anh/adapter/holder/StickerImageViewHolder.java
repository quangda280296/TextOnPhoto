package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;

import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

public class StickerImageViewHolder extends BaseViewHolder {

    public ImageCustom img_sticker;

    public StickerImageViewHolder(View itemView) {
        super(itemView);
        if (itemView instanceof ImageCustom)
            img_sticker = (ImageCustom) itemView;
    }
}