package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.Interface.IRewardedListener;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.NetworkUtil;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Interface.IOnGetFont;
import com.vmb.chinh_sua_anh.Interface.IOnRemoveRowFontDownload;
import com.vmb.chinh_sua_anh.adapter.holder.FontDownloadHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.Font;
import com.vmb.chinh_sua_anh.handler.FontSQLHelper;
import com.vmb.chinh_sua_anh.utils.DownloadFileFromURL;

import java.util.ArrayList;
import java.util.List;

public class FontDownloadAdapter extends BaseAdapter implements IOnGetFont {

    private List<Font.item> listFont;
    private IOnRemoveRowFontDownload listener;
    private int tag = -1;

    public FontDownloadAdapter(Context context, List list) {
        super(context, list);
        this.listFont = list;
    }

    public FontDownloadAdapter(Context context, List list, IOnRemoveRowFontDownload listener, int tag) {
        super(context, list);
        this.listFont = list;
        this.listener = listener;
        this.tag = tag;
    }

    @Override
    protected int getResLayout() {
        return R.layout.row_download_font;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new FontDownloadHolder(inflater.inflate(getResLayout(), viewGroup, false));
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        final String TAG = "downloadFont()";

        if (viewHolder instanceof FontDownloadHolder) {
            final FontDownloadHolder holder = (FontDownloadHolder) viewHolder;

            holder.layout_watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetworkUtil.isNetworkAvailable(context)) {
                        ToastUtil.shortToast(context, context.getString(R.string.check_network_and_try_again));
                        return;
                    }

                    if (!AdsHandler.getInstance().checkRewardedVideo(context))
                        startDownload(position);
                    else {
                        LayoutInflater inflater = LayoutInflater.from(context);
                        final View alertLayout = inflater.inflate(R.layout.dialog_confirm_download_font, null);

                        ImageView img_photo = alertLayout.findViewById(R.id.img_photo);
                        Glide.with(context)
                                .load(listFont.get(position).getUrl_icon())
                                .into(img_photo);

                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setView(alertLayout);
                        final AlertDialog alertDialog = alert.create();

                        Button btn_yes = alertLayout.findViewById(R.id.btn_yes);
                        btn_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AdsHandler.getInstance().displayRewardedVideo(context, new IRewardedListener() {
                                    @Override
                                    public void onRewarded() {
                                        startDownload(position);
                                    }

                                    @Override
                                    public void onFailed() {
                                        startDownload(position);
                                    }
                                });
                                alertDialog.dismiss();
                            }
                        });

                        Button btn_no = alertLayout.findViewById(R.id.btn_no);
                        btn_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                // Initialize a new window manager layout parameters
                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                                // Copy the alert dialog window attributes to new layout parameter instance
                                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

                                // Set the width and height for the layout parameters
                                // This will bet the width and height of alert dialog
                                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                                // Apply the newly created layout parameters to the alert dialog window
                                alertDialog.getWindow().setAttributes(layoutParams);
                            }
                        });

                        alertDialog.show();
                    }
                }
            });

            Glide.with(context)
                    .load(listFont.get(position).getUrl_icon())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_thumbnail);
        }
    }

    public void startDownload(int position) {
        String TAG = "downloadFont()";
        List<String> files = listFont.get(position).getFiles();
        Log.i(TAG, "size = " + files.size());

        List<String> listFontNames = Data.getInstance().getListFontNames();
        List<String> list = new ArrayList<>();

        FontSQLHelper helper = new FontSQLHelper(context);
        for (String s : files)
            if (!listFontNames.contains(s)) {
                list.add(s);
                helper.insert(s);
            }
        new DownloadFileFromURL(context, FontDownloadAdapter.this,
                listFont.get(position).getUrl(), list).execute();
        removeItem(position);
        if (listener != null)
            listener.onRemove(tag);
    }

    @Override
    public void onGetFont(List<String> fontNames) {
        Log.d("downloadFont()", "onGetFont()");
        if (context == null)
            return;

        Data.getInstance().addFontJustDownloaded(context, fontNames);
        if (context instanceof Activity)
            ((Activity) context).setResult(Activity.RESULT_OK);
    }
}