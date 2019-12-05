package com.vmb.chinh_sua_anh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ahmed Adel on 5/8/17.
 */

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<Integer> colorPickerResources;
    private List<Integer> colorPickerColors;

    private OnColorPickerClickListener onColorPickerClickListener;

    public ColorPickerAdapter(@NonNull Context context,
                              @NonNull List<Integer> colorPickerColors,
                              @NonNull List<Integer> colorPickerResources) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = colorPickerColors;
        this.colorPickerResources = colorPickerResources;
    }

    public ColorPickerAdapter(@NonNull Context context) {
        this(context, getDefaultColors(context), getDefaultResources());
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_color_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.colorPickerView.setImageResource(colorPickerResources.get(position));
    }

    @Override
    public int getItemCount() {
        return colorPickerResources.size();
    }

    private void buildColorPickerView(View view, int colorCode) {
        view.setVisibility(View.VISIBLE);

        ShapeDrawable biggerCircle = new ShapeDrawable(new OvalShape());
        biggerCircle.setIntrinsicHeight(20);
        biggerCircle.setIntrinsicWidth(20);
        biggerCircle.setBounds(new Rect(0, 0, 20, 20));
        biggerCircle.getPaint().setColor(colorCode);

        ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
        smallerCircle.setIntrinsicHeight(5);
        smallerCircle.setIntrinsicWidth(5);
        smallerCircle.setBounds(new Rect(0, 0, 5, 5));
        smallerCircle.getPaint().setColor(Color.WHITE);
        smallerCircle.setPadding(10, 10, 10, 10);
        Drawable[] drawables = {smallerCircle, biggerCircle};

        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        view.setBackgroundDrawable(layerDrawable);
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView colorPickerView;

        public ViewHolder(View itemView) {
            super(itemView);
            colorPickerView = itemView.findViewById(R.id.color_picker_view);
            colorPickerView.setOnTouchListener(new OnTouchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onColorPickerClickListener != null)
                        onColorPickerClickListener.onColorPickerClickListener(colorPickerColors.get(getAdapterPosition()));
                }
            }, context));
        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }

    public static List<Integer> getDefaultColors(Context context) {
        List<Integer> colorPickerColors = new ArrayList<>();
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_1));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_2));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_3));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_4));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_5));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_6));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_7));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_8));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_9));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_10));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_11));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_picker_12));
        return colorPickerColors;
    }

    public static List<Integer> getDefaultResources() {
        List<Integer> colorPickerResources = new ArrayList<>();
        colorPickerResources.add(R.drawable.img_color_1);
        colorPickerResources.add(R.drawable.img_color_2);
        colorPickerResources.add(R.drawable.img_color_3);
        colorPickerResources.add(R.drawable.img_color_4);
        colorPickerResources.add(R.drawable.img_color_5);
        colorPickerResources.add(R.drawable.img_color_6);
        colorPickerResources.add(R.drawable.img_color_7);
        colorPickerResources.add(R.drawable.img_color_8);
        colorPickerResources.add(R.drawable.img_color_9);
        colorPickerResources.add(R.drawable.img_color_10);
        colorPickerResources.add(R.drawable.img_color_11);
        colorPickerResources.add(R.drawable.img_color_12);
        return colorPickerResources;
    }
}