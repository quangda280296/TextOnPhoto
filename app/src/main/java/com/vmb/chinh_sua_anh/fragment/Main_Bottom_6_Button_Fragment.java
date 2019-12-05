package com.vmb.chinh_sua_anh.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.view.View;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.FontActivity;
import com.vmb.chinh_sua_anh.activity.StickerActivity;
import com.vmb.chinh_sua_anh.activity.TextInputActivity;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.utils.FragmentUtil;
import com.vmb.chinh_sua_anh.utils.IOnTouchClickListener;
import com.vmb.chinh_sua_anh.utils.Utils;

import photoeditor.PhotoEditor;

public class Main_Bottom_6_Button_Fragment extends BaseFragment {
    private boolean isModifyAnything = false;

    private View layout_edit_image;
    private View layout_insert_text;
    private View layout_more_fonts;
    private View layout_insert_sticker;
    private View layout_brush_mode;
    private View layout_eraser_mode;

    private View ic_arrow_edit_image;

    public boolean isModifyAnything() {
        return isModifyAnything;
    }

    @Override
    protected int getResLayout() {
        return R.layout.layout_main_bottom_6_btn;
    }

    @Override
    protected void initView(View view) {
        layout_edit_image = view.findViewById(R.id.layout_edit_image);
        layout_insert_text = view.findViewById(R.id.layout_insert_text);
        layout_more_fonts = view.findViewById(R.id.layout_more_fonts);
        layout_insert_sticker = view.findViewById(R.id.layout_insert_sticker);
        layout_brush_mode = view.findViewById(R.id.layout_brush_mode);
        layout_eraser_mode = view.findViewById(R.id.layout_eraser_mode);

        ic_arrow_edit_image = view.findViewById(R.id.ic_arrow_edit_image);
    }

    @Override
    protected void initData() {
        layout_edit_image.setOnTouchListener(new IOnTouchClickListener(getActivity(), this));
        layout_insert_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), this));
        layout_more_fonts.setOnTouchListener(new IOnTouchClickListener(getActivity(), this));
        layout_insert_sticker.setOnTouchListener(new IOnTouchClickListener(getActivity(), this));
        layout_brush_mode.setOnTouchListener(new IOnTouchClickListener(getActivity(), this));
        layout_eraser_mode.setOnTouchListener(new IOnTouchClickListener(getActivity(), this));
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = FragmentUtil.getInstance().getManager();
        isModifyAnything = true;
        switch (v.getId()) {
            case R.id.layout_edit_image:
                Utils.turnOffBrushMode();
                if (manager != null)
                    if (manager.findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_EDIT_IMAGE_FRAGMENT) == null) {
                        while (manager.getBackStackEntryCount() > 0)
                            manager.popBackStackImmediate();

                        FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Edit_Image_Fragment(),
                                Config.FragmentTag.MAIN_DETAIL_EDIT_IMAGE_FRAGMENT);
                        ic_arrow_edit_image.setVisibility(View.VISIBLE);
                    } else {
                        FragmentUtil.getInstance().popBackstack();
                        setBackgroundWhite();
                        hideArrow();
                    }
                break;

            case R.id.layout_insert_text:
                if (manager != null)
                    while (manager.getBackStackEntryCount() > 0)
                        manager.popBackStackImmediate();
                Utils.turnOffBrushMode();
                getActivity().startActivityForResult(new Intent(getActivity(), TextInputActivity.class),
                        Config.RequestCode.REQUEST_CODE_INSERT_TEXT);
                break;

            case R.id.layout_more_fonts:
                if (manager != null)
                    while (manager.getBackStackEntryCount() > 0)
                        manager.popBackStackImmediate();
                Utils.turnOffBrushMode();
                getActivity().startActivityForResult(new Intent(getActivity(), FontActivity.class),
                        Config.RequestCode.REQUEST_CODE_FONT);
                break;

            case R.id.layout_insert_sticker:
                if (manager != null)
                    while (manager.getBackStackEntryCount() > 0)
                        manager.popBackStackImmediate();
                Utils.turnOffBrushMode();
                getActivity().startActivityForResult(new Intent(getActivity(), StickerActivity.class),
                        Config.RequestCode.REQUEST_CODE_INSERT_STICKER);
                break;

            case R.id.layout_brush_mode:
                Utils.turnOffBrushMode();
                if (manager != null)
                    if (manager.findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_BRUSH_MODE_FRAGMENT) == null) {
                            while (manager.getBackStackEntryCount() > 0)
                                manager.popBackStackImmediate();

                        PhotoEditor photoEditor = PhotoHandler.getInstance().getPhotoEditor();
                        if (photoEditor != null)
                            photoEditor.setBrushDrawingMode(true);
                        FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Brush_Mode_Fragment(),
                                Config.FragmentTag.MAIN_DETAIL_BRUSH_MODE_FRAGMENT);
                        v.setBackgroundColor(getResources().getColor(R.color.purple_lv_2));
                    } else {
                        PhotoEditor photoEditor = PhotoHandler.getInstance().getPhotoEditor();
                        if (photoEditor != null)
                            photoEditor.setBrushDrawingMode(false);
                        FragmentUtil.getInstance().popBackstack();
                        setBackgroundWhite();
                        v.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
                    }
                break;

            case R.id.layout_eraser_mode:
                if (!PhotoHandler.getInstance().isEraserMode()) {
                    if (manager != null)
                        while (manager.getBackStackEntryCount() > 0)
                            manager.popBackStackImmediate();

                    PhotoEditor photoEditor = PhotoHandler.getInstance().getPhotoEditor();
                    if (photoEditor != null)
                        photoEditor.setBrushDrawingMode(true);
                    PhotoHandler.getInstance().getPhotoEditor().brushEraser();
                    PhotoHandler.getInstance().setEraserMode(true);

                    FragmentUtil.getInstance().add(new Main_Detail_Eraser_Fragment(),
                            Config.FragmentTag.MAIN_DETAIL_ERASE_MODE_FRAGMENT);

                    v.setBackgroundColor(getResources().getColor(R.color.purple_lv_2));
                } else {
                    Utils.turnOffBrushMode();
                    setBackgroundWhite();
                    v.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
                }
                break;

            default:
                break;
        }
    }

    public void hideArrow() {
        ic_arrow_edit_image.setVisibility(View.GONE);
    }

    public void setBackgroundWhite() {
        if (getActivity() == null || getActivity().isFinishing() || isDetached())
            return;

        layout_edit_image.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
        layout_insert_text.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
        layout_more_fonts.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
        layout_insert_sticker.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
        layout_brush_mode.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
        layout_eraser_mode.setBackgroundColor(getResources().getColor(R.color.white_lv_1));
    }
}