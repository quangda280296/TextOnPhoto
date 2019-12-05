package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;

import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

public class ImageViewHolder extends BaseViewHolder {

    public ImageCustom imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        if (itemView instanceof ImageCustom)
            imageView = (ImageCustom) itemView;
    }
}