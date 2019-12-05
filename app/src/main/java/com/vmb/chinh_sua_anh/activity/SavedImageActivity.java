package com.vmb.chinh_sua_anh.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.PermissionUtil;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.adapter.SavedImageAdapter;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SavedImageActivity extends BaseActivity {

    private RecyclerView recycler;
    private View img_back;
    private List<File> allFiles;

    @Override
    protected int getResLayout() {
        return R.layout.activity_saved_image;
    }

    @Override
    protected void initView() {
        recycler = findViewById(R.id.recycler);
        img_back = findViewById(R.id.img_back);
        img_back.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdsHandler.getInstance().displayInterstitial(SavedImageActivity.this, false);
                AdsHandler.getInstance().initBanner(SavedImageActivity.this);
            }
        }, 500);
    }

    @Override
    protected void initData() {
        if (PermissionUtil.requestPermission(SavedImageActivity.this,
                Config.RequestCode.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
            init();
    }

    public void init() {
        allFiles = Utils.getImageInDirectory
                (Environment.getExternalStorageDirectory().toString() + "/" + Config.Directory.SAVE_DIRECTORY + "/");
        if (allFiles == null) {
            recycler.setBackgroundResource(R.drawable.bg_no_result);
        } else if (allFiles.size() > 0) {
            // sort DATE newest -> oldest
            Comparator comparator = new Compare();
            sortList(allFiles, comparator);
            // If the size of views will not change as the data changes.
            recycler.setHasFixedSize(true);
            // Setting the LayoutManager.
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            recycler.setLayoutManager(gridLayoutManager);
            SavedImageAdapter adapter = new SavedImageAdapter(SavedImageActivity.this, allFiles);
            recycler.setAdapter(adapter);
        } else {
            recycler.setAdapter(new SavedImageAdapter(SavedImageActivity.this, allFiles));
            recycler.setBackgroundResource(R.drawable.bg_no_result);
        }
    }

    public <T> void sortList(List<T> list, Comparator<? super T> comparator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(comparator);
        } else {
            Collections.sort(list, comparator);
        }
    }

    public class Compare implements Comparator<File> {
        @Override
        public int compare(File item_1, File item_2) {
            Date lastModDate_1 = new Date(item_1.lastModified());
            Date lastModDate_2 = new Date(item_2.lastModified());

            long a = lastModDate_1.getTime();
            long b = lastModDate_2.getTime();

            if (a > b)
                return -1;
            else if (a < b)
                return 1;
            else
                return 0;
        }
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.RequestCode.REQUEST_CODE_DETAIL:
                initData();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.RequestCode.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    init();
                    Data.getInstance().setTypeSendTime(1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Data.getInstance().scanPhoto(getApplicationContext());
                        }
                    }).start();
                } else
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.accept_to_get_images));
                break;

            default:
                break;
        }
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