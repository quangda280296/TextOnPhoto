package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.vmb.chinh_sua_anh.activity.MainActivity;
import com.vmb.chinh_sua_anh.adapter.holder.ImageViewHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

import java.io.File;
import java.util.List;

public class ChoosePackageDetailAdapter extends BaseAdapter {

    private List<File> allFiles;
    private int index;

    public ChoosePackageDetailAdapter(Context context, List list, int index) {
        super(context, list);
        this.allFiles = list;
        this.index = index;
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;

                                if (PhotoHandler.getInstance().isChangePhotoMode()) {
                                    Intent intent = activity.getIntent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Config.KeyIntentData.KEY_MAIN_ACT_PHOTO_PATH, allFiles.get(position).getAbsolutePath());
                                    intent.putExtras(bundle);
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.finish();
                                    return;
                                }

                                Intent intent = new Intent(activity, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Config.KeyIntentData.KEY_MAIN_ACT_PHOTO_PATH, allFiles.get(position).getAbsolutePath());
                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                                //activity.finish();
                            }
                        }
                    }, 200);
                }
            }, context));
        }
    }
}