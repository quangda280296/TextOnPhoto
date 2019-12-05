package com.vmb.chinh_sua_anh.fragment.ChooseImage;

import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.victor.loading.rotate.RotateLoading;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.util.PermissionUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.adapter.ChoosePackageAdapter;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment_v4;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.model.FileFolder;
import com.vmb.chinh_sua_anh.utils.Utils;

import java.util.Calendar;

public class Fragment_Tab_1_Choose_Package extends BaseFragment_v4 implements View.OnClickListener {

    private RecyclerView recycler;
    private RotateLoading rotateLoading;

    private FloatingActionButton btn_camera;

    @Override
    protected int getResLayout() {
        return R.layout.fragment_choose_image_tab_1;
    }

    @Override
    protected void initView(View view) {
        recycler = view.findViewById(R.id.recycler);
        rotateLoading = view.findViewById(R.id.rotateLoading);
        btn_camera = view.findViewById(R.id.btn_camera);
    }

    @Override
    protected void initData() {
        btn_camera.setOnClickListener(this);
        // If the size of views will change as the data changes.
        recycler.setHasFixedSize(false);
        // Setting the LayoutManager.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        getImageInStorage();

        if (PermissionUtil.requestPermission(getActivity(), Config.RequestCode.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Data.getInstance().setStart_time_load_img(Calendar.getInstance().getTimeInMillis());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                Utils.startCamera(getActivity());
                break;

            default:
                break;
        }
    }

    public void getImageInStorage() {
        if (!Data.getInstance().isScanDone())
            loading();

        recycler.setAdapter(new ChoosePackageAdapter(getActivity(), Data.getInstance().getAlFolders(), getFragmentManager()));

        Data.getInstance().setListener(new Data.ScanPhotoListener() {
            @Override
            public void onInsert(final int position, final FileFolder fileFolder) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 0)
                            recycler.setAdapter(new ChoosePackageAdapter(getActivity(), Data.getInstance().getAlFolders(),
                                    getFragmentManager()));
                        /*else
                            ((BaseAdapter) recycler.getAdapter()).addItem(position, fileFolder);*/
                    }
                });
            }

            @Override
            public void onChange(final int position, final FileFolder fileFolder) {
                /*new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseAdapter) recycler.getAdapter()).changeItem(position, fileFolder);
                    }
                });*/
            }

            /*@Override
            public void onRemove(int position, int index) {
                FileFolder fileFolder = Data.getInstance().getAlFolders().get(index);
                List<File> list = fileFolder.getListPhoto();
                String path = list.get(position).getAbsolutePath();
                list.remove(position);
                if (list.size() <= 0)
                    Data.getInstance().getAlFolders().remove(index);
                else if (path.equals(fileFolder.getPathFile()))
                    fileFolder.setPathFile(list.get(0).getAbsolutePath());
                ((BaseAdapter) recycler.getAdapter()).updateList(Data.getInstance().getAlFolders());
            }*/

            @Override
            public void onDone() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        rotateLoading.stop();
                        if (recycler.getAdapter().getItemCount() == 0)
                            rotateLoading.setBackgroundResource(R.drawable.bg_no_result);
                    }
                });
            }
        });
    }

    public void loading() {
        rotateLoading.start();
    }
}