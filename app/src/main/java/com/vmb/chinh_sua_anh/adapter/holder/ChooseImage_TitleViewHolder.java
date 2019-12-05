package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;

public class ChooseImage_TitleViewHolder extends BaseViewHolder {

    public FrameLayout layout_root;
    public TextView lbl_title;
    public View view_1;
    public View view_2;
    public View view_3;
    public View view_4;

    public ChooseImage_TitleViewHolder(View itemView) {
        super(itemView);

        layout_root = itemView.findViewById(R.id.layout_sticker);
        lbl_title = itemView.findViewById(R.id.lbl_title);

        view_1 = itemView.findViewById(R.id.view_1);
        view_2 = itemView.findViewById(R.id.view_2);
        view_3 = itemView.findViewById(R.id.view_3);
        view_4 = itemView.findViewById(R.id.view_4);
    }
}
