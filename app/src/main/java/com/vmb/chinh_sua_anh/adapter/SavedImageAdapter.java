package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.DetailPhotoActivity;
import com.vmb.chinh_sua_anh.adapter.holder.ImageViewHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.model.FileFolder;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

import java.io.File;
import java.util.List;

public class SavedImageAdapter extends BaseAdapter {

    private List<File> allFiles;

    public SavedImageAdapter(Context context, List list) {
        super(context, list);
        this.allFiles = list;
    }

    @Override
    protected int getResLayout() {
        return 0;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup) {
        ImageCustom img = new ImageCustom(context);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 2, 2, 2);

        img.setLayoutParams(params);
        img.setScaleType(ImageView.ScaleType.MATRIX);
        img.setBackground(context.getResources().getDrawable(R.drawable.img_empty_photo));

        return new ImageViewHolder(img);
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ImageViewHolder) {
            final ImageViewHolder holder = (ImageViewHolder) viewHolder;

            Glide.with(context)
                    .load(allFiles.get(position).getAbsolutePath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView);

            holder.imageView.setOnTouchListener(new OnTouchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        Bundle bundle = new Bundle();
                        bundle.putString(Config.KeyIntentData.KEY_DETAIL_PHOTO_ACT, allFiles.get(position).getAbsolutePath());
                        Intent intent = new Intent(activity, DetailPhotoActivity.class);
                        intent.putExtras(bundle);
                        activity.startActivityForResult(intent, Config.RequestCode.REQUEST_CODE_DETAIL);
                    }
                }
            }, context));
        }
    }
}