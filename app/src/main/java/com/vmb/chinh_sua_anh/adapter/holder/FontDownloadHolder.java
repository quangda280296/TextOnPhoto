package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;
import android.widget.ImageView;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;

public class FontDownloadHolder extends BaseViewHolder {

    public ImageView img_thumbnail;
    public View layout_watch;

    public FontDownloadHolder(View itemView) {
        super(itemView);
        img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        layout_watch = itemView.findViewById(R.id.layout_watch);
    }
}