package com.vmb.chinh_sua_anh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Interface.IOnStylesClickListener;
import com.vmb.chinh_sua_anh.model.Styles;
import com.vmb.chinh_sua_anh.widget.StylesTextView;

import java.util.ArrayList;

public class StylesAdapter extends Adapter<StylesAdapter.ViewHolder> {

    private Context context;
    private IOnStylesClickListener mListener;
    private ArrayList<Styles> stylesArrayList;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public StylesTextView txtStyles;

        public ViewHolder(View view) {
            super(view);
            this.txtStyles = view.findViewById(R.id.txt_styles);
        }
    }

    public long getItemId(int i) {
        return i;
    }

    public StylesAdapter(Context context, ArrayList<Styles> arrayList) {
        this.context = context;
        this.stylesArrayList = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(View.inflate(this.context, R.layout.item_styles, null));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Styles styles = stylesArrayList.get(i);

        viewHolder.txtStyles.setText(String.format(this.context.getResources()
                .getString(R.string.text_name_default_style), new Object[]{Integer.valueOf(i + 1)}));
        viewHolder.txtStyles.setmTextColor(styles.getTextColor());
        viewHolder.txtStyles.setStroke((float) styles.getStrokeWidth(), styles.getStrokeColor());
        if (styles.getShadowDxDy() != 0) {
            viewHolder.txtStyles.setShadowLayer(5.0f, (float) styles.getShadowDxDy(), (float) styles.getShadowDxDy(), styles.getShadowColor());
        }

        viewHolder.txtStyles.setOnTouchListener(new OnTouchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StylesAdapter.this.mListener != null) {
                    StylesAdapter.this.mListener.OnStylesClick(styles);
                }
            }
        }, context));
    }

    public int getItemCount() {
        return this.stylesArrayList.size();
    }

    public IOnStylesClickListener getmListener() {
        return this.mListener;
    }

    public void setmListener(IOnStylesClickListener onStylesClickListener) {
        this.mListener = onStylesClickListener;
    }
}