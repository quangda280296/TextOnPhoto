package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;

public class StickerTabHolder extends BaseViewHolder {

    public View layout_tab;
    public ImageView img_icon;
    public TextView lbl_name_tab;

    public StickerTabHolder(View itemView) {
        super(itemView);
        layout_tab = itemView.findViewById(R.id.layout_tab);
        img_icon = itemView.findViewById(R.id.img_icon);
        lbl_name_tab = itemView.findViewById(R.id.lbl_name_tab);
    }
}
