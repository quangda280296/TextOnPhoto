package com.vmb.chinh_sua_anh.fragment.ChooseImage;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.adapter.ChoosePackageDetailAdapter;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment_v4;
import com.vmb.chinh_sua_anh.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Fragment_Tab_1_Detail extends BaseFragment_v4 {

    private List<File> allFiles;
    private RecyclerView recycler;

    @Override
    protected int getResLayout() {
        return R.layout.fragment_grid_layout;
    }

    @Override
    protected void initView(View view) {
        recycler = view.findViewById(R.id.recycler);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String pathFolder = bundle.getString(Config.KeyIntentData.KEY_CHOOSE_IMAGE_PATH_FOLDER);

        int index = bundle.getInt(Config.KeyIntentData.KEY_CHOOSE_IMAGE_DETAIL);
        /*if (index >= Data.getInstance().getAlFolders().size())
            allFiles = Utils.getImageInDirectory(pathFolder);
        else
            allFiles = Data.getInstance().getAlFolders().get(index).getListPhoto();

        if (allFiles != null && allFiles.size() <= 0)*/
        allFiles = Utils.getImageInDirectory(pathFolder);

        if (allFiles == null) {
            recycler.setBackgroundResource(R.drawable.bg_no_result);
        } else if (allFiles.size() > 0) {
            // sort DATE newest -> oldest
            Comparator comparator = new Compare();
            sortList(allFiles, comparator);
            // If the size of views will not change as the data changes.
            recycler.setHasFixedSize(true);
            // Setting the LayoutManager.
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            recycler.setLayoutManager(gridLayoutManager);
            ChoosePackageDetailAdapter adapter = new ChoosePackageDetailAdapter(getActivity(), allFiles, index);
            recycler.setAdapter(adapter);
        } else {
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
    public void onClick(View v) {

    }
}