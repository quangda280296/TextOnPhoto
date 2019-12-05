package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
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
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.Interface.IRewardedListener;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.NetworkUtil;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Interface.IOnGetBitmap;
import com.vmb.chinh_sua_anh.activity.MainActivity;
import com.vmb.chinh_sua_anh.adapter.holder.ChooseImage_ContentViewHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.utils.LoadBitmapFromUrl;
import com.vmb.chinh_sua_anh.utils.Utils;
import com.vmb.chinh_sua_anh.widget.ImageCustom;

import java.util.List;

public class ChooseImage_ContentAdapter extends BaseAdapter implements IOnGetBitmap {

    private String url;
    private boolean isForceDownload;

    public ChooseImage_ContentAdapter(Context context, List list, String url, boolean isForceDownload) {
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
        img.setScaleType(ImageView.ScaleType.MATRIX);
        img.setBackground(context.getResources().getDrawable(R.drawable.img_empty_photo));

        return new ChooseImage_ContentViewHolder(img);
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        final String TAG = "downloadImages()";
        if (viewHolder instanceof ChooseImage_ContentViewHolder) {
            final ChooseImage_ContentViewHolder holder = (ChooseImage_ContentViewHolder) viewHolder;

            holder.imageView.setOnTouchListener(new OnTouchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            downloadBitmap(position);
                        }
                    }, 500);
                }
            }, context));

            if (isForceDownload) {
                Log.i(TAG, "position: " + position + " is forced download()");
                Glide.with(context)
                        .load(getUri(position))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.imageView);
                return;
            }

            Glide.with(context)
                    .load(getUri(position))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }
    }

    protected void downloadBitmap(final int position) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            if (context instanceof Activity)
                ToastUtil.customShortSnackbar((Activity) context, context.getString(R.string.check_network_and_try_again),
                        context.getString(R.string.try_again), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadBitmap(position);
                            }
                        });
            return;
        }

        if (!AdsHandler.getInstance().checkRewardedVideo(context))
            startDownload(position);
        else {
            LayoutInflater inflater = LayoutInflater.from(context);
            final View alertLayout = inflater.inflate(R.layout.dialog_confirm_download_photo, null);

            ImageView img_photo = alertLayout.findViewById(R.id.img_photo);
            Glide.with(context)
                    .load(getUri(position))
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

                    // Copy the alert downloadDialog window attributes to new layout parameter instance
                    layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

                    // Set the width and height for the layout parameters
                    // This will bet the width and height of alert downloadDialog
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                    // Apply the newly created layout parameters to the alert downloadDialog window
                    alertDialog.getWindow().setAttributes(layoutParams);
                }
            });

            alertDialog.show();
        }
    }

    public void startDownload(int position) {
        handler = new Handler();
        percent = 0;
        isLoaded = false;
        isDismiss = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(R.layout.dialog_dowloading_photo, null);
        progressBar = alertLayout.findViewById(R.id.number_progress_bar);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        downloadDialog = alert.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isDismiss = true;
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage(R.string.do_u_want_stop_downloading)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isDismiss = false;
                                downloadDialog.show();
                                handler.postDelayed(runnable, 50);
                            }
                        })
                        .create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
        downloadDialog.show();

        handler.postDelayed(runnable, 50);
        new LoadBitmapFromUrl(context, ChooseImage_ContentAdapter.this).execute(getUri(position));
    }

    protected String getUri(int position) {
        String TAG = "getUri()";
        String uri = String.format(url, new Object[]{Integer.valueOf(position + 1)});
        Log.i(TAG, "uri = " + uri);
        return uri;
    }

    /*protected String getUriThumb(int position) {
        return Config.UrlData.URL_BACKGROUND + tabName + "/Background/thumb/bg_" + position + ".jpg";
    }

    protected String getUriFullBG(int position) {
        return Config.UrlData.URL_BACKGROUND + tabName + "/Background/bg_" + position + ".jpg";
    }*/

    @Override
    public void onGetBitmap(Bitmap sourceBitmap) {
        if (sourceBitmap == null) {
            Log.d("onGetBitmap", "sourceBitmap == null");
            return;
        }

        isLoaded = true;
        PhotoHandler.getInstance().setFullScaleBitmap(sourceBitmap);

        Bitmap bitmap = Utils.scaleDownImage(sourceBitmap);
        PhotoHandler.getInstance().setSourceBitmap(bitmap);
        PhotoHandler.getInstance().setFilterBitmap(bitmap);

        //PhotoHandler.getInstance().setNeedOverrideFilter(true);
    }

    private NumberProgressBar progressBar;
    private AlertDialog downloadDialog;

    private boolean isLoaded;
    private boolean isDismiss;
    private int percent;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (percent < 99) {
                if (isDismiss)
                    return;

                percent += 1;
                progressBar.setProgress(percent);
                handler.postDelayed(this, 20);

            } else if (percent == 99) {
                if (isDismiss)
                    return;

                if (isLoaded) {
                    percent += 1;
                    progressBar.setProgress(percent);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            downloadDialog.dismiss();
                            if (context instanceof Activity) {
                                if (PhotoHandler.getInstance().isChangePhotoMode()) {
                                    ((Activity) context).setResult(Activity.RESULT_OK);
                                    ((Activity) context).finish();
                                    return;
                                }

                                context.startActivity(new Intent(context, MainActivity.class));
                                //((Activity) context).finish();
                            }
                        }
                    }, 200);
                } else
                    handler.postDelayed(this, 200);
            }
        }
    };
}