package com.vmb.chinh_sua_anh.adapter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.util.CountryCodeUtil;
import com.vmb.ads_in_app.util.DeviceUtil;
import com.vmb.ads_in_app.util.RetrofitInitiator;
import com.vmb.ads_in_app.util.SharedPreferencesUtil;
import com.vmb.ads_in_app.util.TimeRegUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.Interface.IAPISendTime;
import com.vmb.chinh_sua_anh.adapter.holder.ChoosePackageViewHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.fragment.ChooseImage.Fragment_Tab_1_Detail;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.model.FileFolder;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePackageAdapter extends BaseAdapter {

    private FragmentManager manager;
    private List<FileFolder> list;

    public ChoosePackageAdapter(Context context, List list, FragmentManager manager) {
        super(context, list);
        this.list = list;
        this.manager = manager;
    }

    @Override
    protected int getResLayout() {
        return R.layout.row_choose_package;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ChoosePackageViewHolder(inflater.inflate(getResLayout(), viewGroup, false));
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ChoosePackageViewHolder) {
            ChoosePackageViewHolder holder = (ChoosePackageViewHolder) viewHolder;

            holder.lbl_name.setText(list.get(position).getName());
            //holder.lbl_path.setText(list.get(position).getListPhoto().size() + " " + context.getString(R.string.photo));
            holder.lbl_path.setText(list.get(position).getPathFolder());

            Glide.with(context)
                    .load(new File(list.get(position).getPathFile()))
                    .placeholder(R.drawable.img_empty_photo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (!Data.getInstance().isSendTime()) {
                                Data.getInstance().setSendTime(true);
                                SharedPreferencesUtil.putPrefferBool(context, Config.PREFERENCE_KEY_SEND_TIME, true);
                                long time_now = Calendar.getInstance().getTimeInMillis();
                                long time_load_img = time_now - Data.getInstance().getStart_time_load_img();

                                final String TAG = "ChoosePackageAdapter";

                                int type = Data.getInstance().getTypeSendTime();
                                String deviceID = DeviceUtil.getDeviceId(context);
                                String code = Config.CODE_CONTROL_APP;
                                String version = Config.VERSION_APP;
                                String packg = Config.PACKAGE_NAME;
                                String timeRegister = TimeRegUtil.getTimeRegister(context);
                                String os_version = DeviceUtil.getDeviceOS();
                                String phone_name = DeviceUtil.getDeviceName();
                                String country = CountryCodeUtil.getCountryCode(context);

                                Log.i(TAG, "type = " + type);
                                Log.i(TAG, "time_load_img = " + time_load_img);
                                Log.i(TAG, "deviceID = " + deviceID);
                                Log.i(TAG, "code = " + code);
                                Log.i(TAG, "version = " + version);
                                Log.i(TAG, "package = " + packg);
                                Log.i(TAG, "os_version = " + os_version);
                                Log.i(TAG, "phone_name = " + phone_name);
                                Log.i(TAG, "country = " + country);
                                Log.i(TAG, "timeRegister = " + timeRegister);

                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("deviceID", deviceID)
                                        .addFormDataPart("code", code)
                                        .addFormDataPart("version", version)
                                        .addFormDataPart("package", packg)
                                        .addFormDataPart("os_version", os_version)
                                        .addFormDataPart("phone_name", phone_name)
                                        .addFormDataPart("country", country)
                                        .addFormDataPart("timeRegister", timeRegister)
                                        .addFormDataPart("time_load_img", time_load_img + "")
                                        .addFormDataPart("type", type + "")
                                        .build();

                                IAPISendTime api = RetrofitInitiator.createService(IAPISendTime.class, LibrayData.Url.URL_BASE);
                                Call<ResponseBody> call = api.logTime(requestBody);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Log.i(TAG, "onResponse()");
                                        if (response == null || response.body() == null) {
                                            Log.i(TAG, "response == null || response.body() == null");
                                            return;
                                        }

                                        Log.i(TAG, "response = " + response.toString());
                                        if (response.isSuccessful()) {
                                            Log.i(TAG, "response.isSuccessful()");
                                        } else {
                                            Log.i(TAG, "response.failed");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.i(TAG, "onFailure()");
                                    }
                                });
                            }
                            return false;
                        }
                    })
                    .into(holder.img_thumbnail);

            holder.layout_folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = manager.beginTransaction().
                            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                    Fragment_Tab_1_Detail fragment = new Fragment_Tab_1_Detail();
                    Bundle bundle = new Bundle();
                    bundle.putString(Config.KeyIntentData.KEY_CHOOSE_IMAGE_PATH_FOLDER, list.get(position).getPathFolder());
                    bundle.putInt(Config.KeyIntentData.KEY_CHOOSE_IMAGE_DETAIL, position);
                    fragment.setArguments(bundle);

                    transaction.add(R.id.layout_choose, fragment, Config.FragmentTag.Fragment_Tab_1_Detail);
                    transaction.addToBackStack(Config.FragmentTag.Fragment_Tab_1_Detail);
                    try {
                        transaction.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}