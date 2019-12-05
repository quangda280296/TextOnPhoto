package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;

public class ChoosePackageViewHolder extends BaseViewHolder {

    public View layout_folder;
    public ImageView img_thumbnail;
    public TextView lbl_name;
    public TextView lbl_path;

    public ChoosePackageViewHolder(View itemView) {
        super(itemView);
        layout_folder = itemView.findViewById(R.id.layout_folder);
        img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        lbl_name = itemView.findViewById(R.id.lbl_name);
        lbl_path = itemView.findViewById(R.id.lbl_path);
    }
}