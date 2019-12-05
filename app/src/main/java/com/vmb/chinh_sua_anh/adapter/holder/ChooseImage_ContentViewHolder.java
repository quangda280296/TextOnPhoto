package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;
import android.widget.ImageView;

import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

public class ChooseImage_ContentViewHolder extends BaseViewHolder {

    public ImageCustom imageView;

    public ChooseImage_ContentViewHolder(View itemView) {
        super(itemView);
        if (itemView instanceof ImageCustom)
            imageView = (ImageCustom) itemView;
    }
}