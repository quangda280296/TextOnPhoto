package com.vmb.chinh_sua_anh.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.Interface.IGetData;
import com.vmb.chinh_sua_anh.adapter.StickerImageAdapter;
import com.vmb.chinh_sua_anh.adapter.StickerTabAdapter;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.Image;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.ads_in_app.util.NetworkUtil;
import com.vmb.ads_in_app.util.RetrofitInitiator;
import com.vmb.chinh_sua_anh.utils.Utils;
import com.vm.TextOnPhoto.PhotoEditor.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StickerActivity extends BaseActivity {

    private View img_back;
    private RecyclerView recycler_content;
    private RecyclerView recycler_tab;
    private View root;

    @Override
    protected int getResLayout() {
        return R.layout.activity_sticker;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.img_back);
        recycler_content = findViewById(R.id.recycler_content);
        recycler_tab = findViewById(R.id.recycler_tab);
        root = findViewById(R.id.root);
    }

    @Override
    protected void initData() {
        img_back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        // If the size of views will not change as the data changes.
        recycler_tab.setHasFixedSize(true);
        // Setting the LayoutManager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recycler_tab.setLayoutManager(layoutManager);

        // If the size of views will not change as the data changes.
        recycler_content.setHasFixedSize(true);
        // Setting the LayoutManager.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler_content.setLayoutManager(gridLayoutManager);

        initTab();

        AdsHandler.getInstance().initBanner(StickerActivity.this);
    }

    /*public void initTab() {
        final String TAG = "initTab()";

        IGetData api = RetrofitInitiator.createService(IGetData.class, Config.UrlData.URL_STICKER);
        Call<Icon> call = api.getListSticker();
        call.enqueue(new Callback<Icon>() {
            @Override
            public void onResponse(Call<Icon> call, Response<Icon> response) {
                Log.i(TAG, "onResponse()");
                if (response == null) {
                    Log.i(TAG, "response == null");
                    return;
                }

                if (response.isSuccessful()) {
                    Log.i(TAG, "response.isSuccessful()");

                    if (response.body() == null || isFinishing()) {
                        Log.i(TAG, "response.body() null || activity.isFinishing()");
                        return;
                    }

                    List<Icon.data.sticker> list = response.body().getData().getList_stickers();

                    // Setting the adapter.
                    StickerTabAdapter titleAdapter = new StickerTabAdapter(StickerActivity.this, list);
                    recycler_tab.setAdapter(titleAdapter);

                    List ls = new ArrayList();
                    for(int i = 0; i < list.get(0).getTotal_image(); i++)
                        ls.add("");

                    StickerImageAdapter contentAdapter =
                            new StickerImageAdapter(StickerActivity.this, ls, list.get(0).getFolder());
                    recycler_content.setAdapter(contentAdapter);
                }
            }

            @Override
            public void onFailure(Call<Icon> call, Throwable t) {

            }
        });
    }*/

    public void initTab() {
        String TAG = "initTab()";

        if (Image.getInstance().getData() != null) {
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
        final String TAG = "initTab()";

        IGetData api = RetrofitInitiator.createService(IGetData.class, LibrayData.Url.URL_ROOT);
        Call<Image> call = api.getListImage();
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
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

                    Image.getInstance().setData(response.body().getData());

                    Image image = Utils.handleVersionImages(StickerActivity.this);
                    if (image != null) {
                        List<Image.data.item> old_list = image.getData().getStickers();
                        Image.data data = Image.getInstance().getData();
                        if(data == null)
                            return;
                        List<Image.data.item> new_list = data.getStickers();

                        for (int i = 0; i < old_list.size(); i++) {
                            String id = old_list.get(i).getId();
                            for (int k = 0; k < new_list.size(); k++) {
                                if (id.equals(new_list.get(k).getId())) {
                                    if (old_list.get(i).getVersion() != new_list.get(k).getVersion())
                                        data.getStickers().get(k).setChangeVersion(true);
                                }
                            }
                        }
                    }

                    handleData();

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.i(TAG, "json = " + json);

                    // Write to a file
                    try {
                        FileOutputStream fout = openFileOutput(Config.FileName.FILE_IMAGES_DATA, Activity.MODE_PRIVATE);
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

                } else {
                    Log.i(TAG, "response.isSuccessful() == false");
                    if (isFinishing()) {
                        Log.i(TAG, "isFinishing()");
                        return;
                    }

                    if (Utils.initImagesWhileOffline(getApplicationContext()))
                        handleData();
                    else
                        root.setBackgroundResource(R.drawable.bg_no_internet);
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                Log.i(TAG, "onFailure()");
                if (isFinishing()) {
                    Log.i(TAG, "isFinishing()");
                    return;
                }

                if (Utils.initImagesWhileOffline(getApplicationContext()))
                    handleData();
                else
                    root.setBackgroundResource(R.drawable.bg_no_internet);
            }
        });
    }

    public void handleData() {
        String TAG = "handleData()";

        Image.data data = Image.getInstance().getData();
        if(data == null)
            return;

        List<Image.data.item> stickers = data.getStickers();
        if (stickers.size() <= 0) {
            Log.i(TAG, "size() <= 0");
            root.setBackgroundResource(R.drawable.bg_no_internet);
            return;
        }

        // Setting the adapter.
        StickerTabAdapter titleAdapter = new StickerTabAdapter(StickerActivity.this, stickers);
        recycler_tab.setAdapter(titleAdapter);

        List ls = new ArrayList();
        for (int i = 0; i < stickers.get(0).getCount(); i++)
            ls.add("");

        StickerImageAdapter contentAdapter =
                new StickerImageAdapter(StickerActivity.this, ls,
                        stickers.get(0).getUrl(), stickers.get(0).isChangeVersion());
        recycler_content.setAdapter(contentAdapter);
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}