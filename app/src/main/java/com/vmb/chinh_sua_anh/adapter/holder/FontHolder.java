package com.vmb.chinh_sua_anh.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;

public class FontHolder extends BaseViewHolder {

    public View layout_font;
    public TextView lbl_number;
    public TextView lbl_text_font;

    public FontHolder(View itemView) {
        super(itemView);
        layout_font = itemView.findViewById(R.id.layout_font);
        lbl_number = itemView.findViewById(R.id.lbl_number);
        lbl_text_font = itemView.findViewById(R.id.lbl_text_font);
    }
}
