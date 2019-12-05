package com.vmb.chinh_sua_anh.fragment.ChooseImage;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.NetworkUtil;
import com.vmb.ads_in_app.util.RetrofitInitiator;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.Interface.IGetData;
import com.vmb.chinh_sua_anh.adapter.ChooseImage_ContentAdapter;
import com.vmb.chinh_sua_anh.adapter.ChooseImage_TitleAdapter;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment_v4;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.Image;
import com.vmb.chinh_sua_anh.utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_2 extends BaseFragment_v4 {

    private RecyclerView recycler_title;
    private RecyclerView recycler_content;
    private View root;

    @Override
    protected int getResLayout() {
        return R.layout.fragment_choose_image_tab_2;
    }

    @Override
    protected void initView(View view) {
        recycler_title = view.findViewById(R.id.recycler_title);
        recycler_content = view.findViewById(R.id.recycler_content);
        root = view.findViewById(R.id.root);
    }

    @Override
    protected void initData() {
        // If the size of views will not change as the data changes.
        recycler_title.setHasFixedSize(true);
        // Setting the LayoutManager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_title.setLayoutManager(layoutManager);

        // If the size of views will not change as the data changes.
        recycler_content.setHasFixedSize(true);
        // Setting the LayoutManager.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recycler_content.setLayoutManager(gridLayoutManager);
    }

    public void initTitle() {
        String TAG = "initTitle()";

        if (Image.getInstance().getData() != null) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
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
        final String TAG = "initTitle()";

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

                    if (response.body() == null || getActivity() == null || getActivity().isFinishing()) {
                        Log.i(TAG, "response.body() == null || getActivity() == null || getActivity().isFinishing()");
                        return;
                    }

                    Image.getInstance().setData(response.body().getData());

                    Image image = Utils.handleVersionImages(getActivity());
                    if (image != null) {
                        List<Image.data.item> old_list = image.getData().getPhotos();
                        Image.data data = Image.getInstance().getData();
                        if (data == null)
                            return;
                        List<Image.data.item> new_list = data.getPhotos();

                        for (int i = 0; i < old_list.size(); i++) {
                            String id = old_list.get(i).getId();
                            for (int k = 0; k < new_list.size(); k++) {
                                if (id.equals(new_list.get(k).getId())) {
                                    if (old_list.get(i).getVersion() != new_list.get(k).getVersion())
                                        data.getPhotos().get(k).setChangeVersion(true);
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
                        FileOutputStream fout = getActivity().openFileOutput(Config.FileName.FILE_IMAGES_DATA, Activity.MODE_PRIVATE);
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
                    if (getActivity() == null || getActivity().isFinishing()) {
                        Log.i(TAG, "getActivity() == null || getActivity().isFinishing()");
                        return;
                    }

                    if (Utils.initImagesWhileOffline(getContext()))
                        handleData();
                    else
                        root.setBackgroundResource(R.drawable.bg_no_internet);
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                Log.i(TAG, "onFailure()");
                if (getActivity() == null || getActivity().isFinishing()) {
                    Log.i(TAG, "getActivity() == null || getActivity().isFinishing()");
                    return;
                }

                if (Utils.initImagesWhileOffline(getContext()))
                    handleData();
                else
                    root.setBackgroundResource(R.drawable.bg_no_internet);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void handleData() {
        String TAG = "handleData()";

        Image.data data = Image.getInstance().getData();
        if (data == null)
            return;

        List<Image.data.item> photos = data.getPhotos();
        if (photos.size() <= 0) {
            Log.i(TAG, "size() <= 0");
            root.setBackgroundResource(R.drawable.bg_no_internet);
            return;
        }

        // Setting the adapter.
        ChooseImage_TitleAdapter titleAdapter = new ChooseImage_TitleAdapter(getActivity(), photos);
        recycler_title.setAdapter(titleAdapter);

        List ls = new ArrayList();
        for (int i = 0; i < photos.get(0).getCount(); i++)
            ls.add("");

        ChooseImage_ContentAdapter contentAdapter = new ChooseImage_ContentAdapter(getActivity(), ls,
                photos.get(0).getUrl(), photos.get(0).isChangeVersion());
        recycler_content.setAdapter(contentAdapter);

        AdsHandler.getInstance().initRewardedVideo(getContext());
    }

    @Override
    public void onPause() {
        Log.i("Fragment_Tab_2", "onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("Fragment_Tab_2", "onResume()");
        super.onResume();
    }
}