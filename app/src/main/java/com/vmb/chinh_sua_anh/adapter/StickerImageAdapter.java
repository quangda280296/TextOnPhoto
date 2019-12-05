package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.NetworkUtil;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Interface.IOnGetBitmap;
import com.vmb.chinh_sua_anh.adapter.holder.StickerImageViewHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.utils.LoadBitmapFromUrl;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

import java.util.List;

public class StickerImageAdapter extends BaseAdapter implements IOnGetBitmap {

    private String url;
    private boolean isForceDownload;

    public StickerImageAdapter(Context context, List list, String url, boolean isForceDownload) {
        super(context, list);
        this.url = url;
        this.isForceDownload = isForceDownload;
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
        params.setMargins(1, 1, 1, 1);

        img.setLayoutParams(params);
        img.setBackgroundColor(context.getResources().getColor(R.color.gray_lv_2));

        return new StickerImageViewHolder(img);
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        final String TAG = "downloadSticker()";

        if (viewHolder instanceof StickerImageViewHolder) {
            final StickerImageViewHolder holder = (StickerImageViewHolder) viewHolder;

            holder.img_sticker.setOnTouchListener(new OnTouchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetworkUtil.isNetworkAvailable(context)) {
                        ToastUtil.shortToast(context, context.getString(R.string.check_network_and_try_again));
                        return;
                    }

                    new LoadBitmapFromUrl(context, StickerImageAdapter.this).execute(getUri(position));
                }
            }, context));

            if (isForceDownload) {
                Log.i(TAG, "position: " + position + " is forced download()");
                Glide.with(context)
                        .load(getUri(position))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.img_sticker);
                return;
            }

            Glide.with(context)
                    .load(getUri(position))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_sticker);
        }
    }

    protected String getUri(int position) {
        String TAG = "getUri()";
        String uri = String.format(url, new Object[]{Integer.valueOf(position + 1)});
        Log.i(TAG, "uri = " + uri);
        return uri;
    }

    /*protected String getUriThumb(int position) {
        return Config.UrlData.URL_STICKER + tabName + "/image_" + (position + 1) + ".png";
    }

    protected String getUriFullBG(int position) {
        return Config.UrlData.URL_STICKER + tabName + "/image_" + (position + 1) + ".png";
    }*/

    @Override
    public void onGetBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.d("onGetBitmap", "bitmap == null");
            return;
        }

        PhotoHandler.getInstance().setSticker(bitmap);
        if (context instanceof Activity) {
            ((Activity) context).setResult(Activity.RESULT_OK);
            ((Activity) context).finish();
        }
    }
}