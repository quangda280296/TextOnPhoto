package com.vmb.chinh_sua_anh.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.ChooseImageActivity;
import com.vmb.chinh_sua_anh.activity.CropActivity;
import com.vmb.chinh_sua_anh.activity.FilterActivity;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;

import photoeditor.PhotoEditor;

public class Main_Detail_Edit_Image_Fragment extends BaseFragment {

    private LinearLayout layout_filter;
    private LinearLayout layout_exchange;
    private LinearLayout layout_crop;
    private LinearLayout flip_h;
    private LinearLayout flip_v;
    private LinearLayout rotate;

    @Override
    protected int getResLayout() {
        return R.layout.layout_main_bottom_edit_image;
    }

    @Override
    protected void initView(View view) {
        layout_filter = view.findViewById(R.id.layout_filter);
        layout_exchange = view.findViewById(R.id.layout_exchange);
        layout_crop = view.findViewById(R.id.layout_crop);
        flip_h = view.findViewById(R.id.flip_h);
        flip_v = view.findViewById(R.id.flip_v);
        rotate = view.findViewById(R.id.rotate);
    }

    @Override
    protected void initData() {
        layout_filter.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        layout_exchange.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        layout_crop.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        flip_h.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        flip_v.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        rotate.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_filter:
                getActivity().startActivityForResult(new Intent(getActivity(), FilterActivity.class),
                        Config.RequestCode.REQUEST_CODE_FILTER);
                break;

            case R.id.layout_exchange:
                PhotoHandler.getInstance().setChangePhotoMode(true);
                getActivity().startActivityForResult(new Intent(getActivity(), ChooseImageActivity.class),
                        Config.RequestCode.REQUEST_CODE_CHANGE_PHOTO);
                //PhotoHandler.getInstance().setNeedOverrideFilter(false);
                break;

            case R.id.layout_crop:
                getActivity().startActivityForResult(new Intent(getActivity(), CropActivity.class),
                        Config.RequestCode.REQUEST_CODE_CROP);
                break;

            case R.id.flip_h:
                PhotoHandler.getInstance().getPhotoEditor().flipHorizontally();
                break;

            case R.id.flip_v:
                PhotoHandler.getInstance().getPhotoEditor().flipVertically();
                break;

            case R.id.rotate:
                PhotoEditor photoEditor = PhotoHandler.getInstance().getPhotoEditor();
                if (photoEditor != null)
                    photoEditor.rotate();
                break;

            default:
                break;
        }
    }
}