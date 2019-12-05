package com.vmb.chinh_sua_anh.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.Interface.IFilterListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import photoeditor.PhotoFilter;

/**
 * @author
 * @version 0.1.2
 * @since 5/23/2018
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private int marked = 0;

    public int getMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }

    private Context context;
    private IFilterListener mFilterListener;
    private List<Pair<String, PhotoFilter>> mPairList = new ArrayList<>();

    public FilterAdapter(IFilterListener filterListener, Context context) {
        mFilterListener = filterListener;
        this.context = context;
        setupFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Pair<String, PhotoFilter> filterPair = mPairList.get(position);
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), filterPair.first);
        holder.mImageFilterView.setImageBitmap(fromAsset);

        holder.mTxtFilterName.setText(filterPair.second.name().replace("_", " "));

        int marked = getMarked();
        if (position != marked){
            holder.mImageFilterView.setBorderColor(context.getResources().getColor(android.R.color.transparent));
            holder.mTxtFilterName.setTextColor(context.getResources().getColor(R.color.gray_lv_4));
        } else {
            holder.mImageFilterView.setBorderColor(context.getResources().getColor(R.color.green_store));
            holder.mTxtFilterName.setTextColor(context.getResources().getColor(R.color.green_store));
        }

        holder.layout_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarked(position);
                notifyDataSetChanged();

                mFilterListener.onFilterSelected(mPairList.get(position).second);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPairList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View layout_filter;
        RoundedImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            layout_filter = itemView.findViewById(R.id.layout_filter);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
        }
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupFilters() {
        mPairList.add(new Pair<>("filters/original.webp", PhotoFilter.NONE));
        mPairList.add(new Pair<>("filters/contrast.webp", PhotoFilter.CONTRAST));
        mPairList.add(new Pair<>("filters/brightness.webp", PhotoFilter.BRIGHTNESS));
        mPairList.add(new Pair<>("filters/b_n_w.webp", PhotoFilter.GAMMA));
        mPairList.add(new Pair<>("filters/saturate.webp", PhotoFilter.SATURATE));
        mPairList.add(new Pair<>("filters/sepia.webp", PhotoFilter.SEPIA));
        mPairList.add(new Pair<>("filters/fill_light.webp", PhotoFilter.FILL_LIGHT));
        mPairList.add(new Pair<>("filters/temprature.webp", PhotoFilter.TEMPERATURE));
        mPairList.add(new Pair<>("filters/auto_fix.webp", PhotoFilter.AUTO_FIX));
        mPairList.add(new Pair<>("filters/sharpen.webp", PhotoFilter.SHARPEN));
        mPairList.add(new Pair<>("filters/vignette.webp", PhotoFilter.VIGNETTE));
        mPairList.add(new Pair<>("filters/cross_process.webp", PhotoFilter.CROSS_PROCESS));
        mPairList.add(new Pair<>("filters/dual_tone.webp", PhotoFilter.DUE_TONE));
        mPairList.add(new Pair<>("filters/tint.webp", PhotoFilter.TINT));
        mPairList.add(new Pair<>("filters/gray_scale.webp", PhotoFilter.GRAY_SCALE));
        mPairList.add(new Pair<>("filters/negative.webp", PhotoFilter.NEGATIVE));
        mPairList.add(new Pair<>("filters/documentary.webp", PhotoFilter.DOCUMENTARY));
        mPairList.add(new Pair<>("filters/lomish.webp", PhotoFilter.LOMISH));
        mPairList.add(new Pair<>("filters/posterize.webp", PhotoFilter.POSTERIZE));
        mPairList.add(new Pair<>("filters/fish_eye.webp", PhotoFilter.FISH_EYE));
        mPairList.add(new Pair<>("filters/grain.webp", PhotoFilter.GRAIN));
    }
}