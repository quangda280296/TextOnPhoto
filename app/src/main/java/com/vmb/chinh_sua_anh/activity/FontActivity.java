package com.vmb.chinh_sua_anh.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.Interface.BannerLoaderListener;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.NetworkUtil;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.RetrofitInitiator;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.Interface.IGetData;
import com.vmb.chinh_sua_anh.Interface.IOnRemoveRowFontDownload;
import com.vmb.chinh_sua_anh.adapter.FontDownloadAdapter;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.Font;
import com.vmb.chinh_sua_anh.utils.Utils;
import com.vmb.chinh_sua_anh.widget.FocusAwareScrollView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FontActivity extends BaseActivity implements IOnRemoveRowFontDownload {

    private FocusAwareScrollView nestedScrollView;
    private View img_back;
    private View parent;
    //private RecyclerView recycler;
    private ViewGroup layout_data;

    @Override
    protected int getResLayout() {
        return R.layout.activity_font;
    }

    @Override
    protected void initView() {
        nestedScrollView = findViewById(R.id.scrollView);
        parent = findViewById(R.id.parent);
        //recycler = findViewById(R.id.recycler);
        img_back = findViewById(R.id.img_back);
        layout_data = findViewById(R.id.layout_data);
    }

    @Override
    protected void initData() {
        img_back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        AdsHandler.getInstance().initRewardedVideo(getApplicationContext());

        /*// If the size of views will not change as the data changes.
        recycler.setHasFixedSize(true);
        // Setting the LayoutManager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);*/

        initFont();
    }

    public void initFont() {
        String TAG = "initFont()";

        if (Font.getInstance().getData() != null) {
            if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                if (Data.getInstance().isOfflineMode()) {
                    Log.i(TAG, "offlineMode == true");
                    Data.getInstance().setOfflineMode(false);
                    callAPI();
                    return;
                }
            }
            handleData();
            return;
        }

        callAPI();
    }

    public void callAPI() {
        final String TAG = "initFont()";

        IGetData api = RetrofitInitiator.createService(IGetData.class, LibrayData.Url.URL_ROOT);
        Call<Font> call = api.getListFont();
        call.enqueue(new Callback<Font>() {
            @Override
            public void onResponse(Call<Font> call, Response<Font> response) {
                Log.i(TAG, "onResponse()");
                if (response == null) {
                    Log.i(TAG, "response == null");
                    return;
                }

                if (response.isSuccessful()) {
                    Log.i(TAG, "response.isSuccessful()");

                    if (response.body() == null || isFinishing()) {
                        Log.i(TAG, "response.body() == null || isFinishing()");
                        return;
                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.i(TAG, "json = " + json);

                    // Write to a file
                    try {
                        FileOutputStream fout = openFileOutput(Config.FileName.FILE_FONTS_DATA, Activity.MODE_PRIVATE);
                        OutputStreamWriter osw = new OutputStreamWriter(fout);
                        osw.write(json);
                        osw.flush();
                        osw.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.i(TAG, e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i(TAG, e.getMessage());
                    }

                    Font.getInstance().setInstance(response.body());
                    handleData();

                } else {
                    Log.i(TAG, "response.isSuccessful() == false");
                    if (isFinishing()) {
                        Log.i(TAG, "isFinishing()");
                        return;
                    }

                    if (Utils.initFontsWhileOffline(getApplicationContext()))
                        handleData();
                    else
                        parent.setBackgroundResource(R.drawable.bg_no_internet);
                }
            }

            @Override
            public void onFailure(Call<Font> call, Throwable t) {
                Log.i(TAG, "onFailure()");
                if (isFinishing()) {
                    Log.i(TAG, "isFinishing()");
                    return;
                }

                if (Utils.initFontsWhileOffline(getApplicationContext()))
                    handleData();
                else
                    parent.setBackgroundResource(R.drawable.bg_no_internet);
            }
        });
    }

    public void handleData() {
        final String TAG = "handleData()";

        if (Font.getInstance().getData() != null && Font.getInstance().getData().size() <= 0) {
            Log.i(TAG, "size() <= 0");
            parent.setBackgroundResource(R.drawable.bg_no_internet);
            return;
        }
        List<Font.item> listFont = Font.getInstance().getData();
        List<String> listFontNames = Data.getInstance().getListFontNames();

        if (listFont == null)
            return;

        Iterator<Font.item> iterator = listFont.iterator();
        while (iterator.hasNext()) {
            List<String> files = iterator.next().getFiles();
            Iterator<String> file = files.iterator();
            while (file.hasNext()) {
                String f = file.next();
                if (listFontNames.contains(f)) {
                    file.remove();
                    if (files.size() <= 0) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        int size = listFont.size();
        if (size > 0) {
            List<Font.item> list = new ArrayList<>();
            list.add(listFont.get(0));
            FontDownloadAdapter adapter = new FontDownloadAdapter(FontActivity.this, list);
            RecyclerView recyclerView = new RecyclerView(getApplicationContext());
            // If the size of views will change as the data changes.
            recyclerView.setHasFixedSize(false);
            // Setting the LayoutManager.
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);
            layout_data.addView(recyclerView, new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            recyclerView.setAdapter(adapter);
        }

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.layout_adview, null, false);
        layout_data.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AdsHandler.getInstance().initBanner(getApplicationContext(),
                LibrayData.AdsSize.NATIVE_ADS, (ViewGroup) view);

        for (int i = 1; i < size; i += 5) {
            if (i + 4 < size) {
                List<Font.item> list = new ArrayList<>();
                list.add(listFont.get(i));
                list.add(listFont.get(i + 1));
                list.add(listFont.get(i + 2));
                list.add(listFont.get(i + 3));
                list.add(listFont.get(i + 4));
                FontDownloadAdapter adapter = new FontDownloadAdapter(FontActivity.this, list,
                        FontActivity.this, views.size());
                RecyclerView recyclerView = new RecyclerView(getApplicationContext());
                // If the size of views will change as the data changes.
                recyclerView.setHasFixedSize(false);
                // Setting the LayoutManager.
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);
                layout_data.addView(recyclerView, new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                recyclerView.setAdapter(adapter);

                LayoutInflater in = LayoutInflater.from(getApplicationContext());
                View vi = in.inflate(R.layout.layout_adview, null, false);
                layout_data.addView(vi, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                views.add(vi);

            } else {
                List<Font.item> list = new ArrayList<>();
                for (int k = i; k < size; k++)
                    list.add(listFont.get(k));

                FontDownloadAdapter adapter = new FontDownloadAdapter(FontActivity.this, list);
                RecyclerView recyclerView = new RecyclerView(getApplicationContext());
                // If the size of views will change as the data changes.
                recyclerView.setHasFixedSize(false);
                // Setting the LayoutManager.
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);
                layout_data.addView(recyclerView, new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                recyclerView.setAdapter(adapter);
            }
        }

        nestedScrollView.registerViewSeenCallBack(views, new FocusAwareScrollView.OnViewSeenListener() {
            @Override
            public void onViewSeen(View v, int percentageScrolled) {
                if (isNativeLoaded)
                    loadNative((ViewGroup) v);
                else
                    listPending.add(v);
            }
        });

        /*// Setting the adapter.
        FontDownloadAdapter adapter = new FontDownloadAdapter(FontActivity.this, listFont);
        recycler.setAdapter(adapter);*/
    }

    ArrayList<View> views = new ArrayList<>();
    boolean isNativeLoaded = true;
    List<View> listPending = new ArrayList<>();

    public void loadNative(ViewGroup viewGroup) {
        isNativeLoaded = false;
        AdsHandler.getInstance().initBanner(getApplicationContext(), LibrayData.AdsSize.NATIVE_ADS,
                viewGroup, new BannerLoaderListener() {
                    @Override
                    public void onLoaded() {
                        if (listPending.size() > 0) {
                            loadNative((ViewGroup) listPending.get(0));
                            listPending.remove(0);
                        } else
                            isNativeLoaded = true;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onRemove(int position) {
        layout_data.removeView(views.get(position));
    }
}