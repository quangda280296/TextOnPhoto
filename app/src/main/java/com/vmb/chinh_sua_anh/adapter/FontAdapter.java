package com.vmb.chinh_sua_anh.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.adapter.holder.FontHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;

import java.util.List;

public class FontAdapter extends BaseAdapter {

    private List<Typeface> list;

    public FontAdapter(Context context, List list) {
        super(context, list);
        this.list = list;
    }

    @Override
    protected int getResLayout() {
        return R.layout.row_font_text;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new FontHolder(inflater.inflate(getResLayout(), viewGroup, false));
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof FontHolder) {
            FontHolder holder = (FontHolder) viewHolder;

            holder.lbl_number.setText((position + 1) + "");
            holder.lbl_text_font.setText(TextOnPhotoHandler.getInstance().getItem().getText().replaceAll("\\n", " "));
            holder.lbl_text_font.setTypeface(list.get(position));
            holder.layout_font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoHandler.getInstance().getPhotoEditor().changeTextFont(list.get(position));
                }
            });
        }
    }
}