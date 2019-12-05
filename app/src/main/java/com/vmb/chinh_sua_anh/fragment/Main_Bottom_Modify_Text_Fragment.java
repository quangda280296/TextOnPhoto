package com.vmb.chinh_sua_anh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.TextInputActivity;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.vmb.chinh_sua_anh.utils.FragmentUtil;
import com.vmb.chinh_sua_anh.utils.IOnTouchClickListener;
import com.vmb.chinh_sua_anh.utils.Utils;

public class Main_Bottom_Modify_Text_Fragment extends BaseFragment {

    private View layout_add_text;
    private View layout_edit_text;
    private View layout_font_text;
    private View layout_size_text;
    private View layout_color_text;
    private View layout_style_text;
    private View layout_border_text;

    private View img_font;
    private View img_size;
    private View img_color;
    private View img_style;
    private View img_border;

    @Override
    protected int getResLayout() {
        return R.layout.layout_main_bottom_modify_text;
    }

    @Override
    protected void initView(View view) {
        layout_add_text = view.findViewById(R.id.layout_add_text);
        layout_edit_text = view.findViewById(R.id.layout_edit_text);
        layout_font_text = view.findViewById(R.id.layout_font_text);
        layout_size_text = view.findViewById(R.id.layout_size_text);
        layout_color_text = view.findViewById(R.id.layout_color_text);
        layout_style_text = view.findViewById(R.id.layout_style_text);
        layout_border_text = view.findViewById(R.id.layout_border_text);

        img_font = view.findViewById(R.id.img_font);
        img_size = view.findViewById(R.id.img_size);
        img_color = view.findViewById(R.id.img_color);
        img_style = view.findViewById(R.id.img_style);
        img_border = view.findViewById(R.id.img_border);
    }

    @Override
    protected void initData() {
        layout_add_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
        layout_edit_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
        layout_font_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
        layout_size_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
        layout_color_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
        layout_style_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
        layout_border_text.setOnTouchListener(new IOnTouchClickListener(getActivity(), 1, this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add_text:
                while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                    FragmentUtil.getInstance().getManager().popBackStackImmediate();

                getActivity().startActivityForResult(new Intent(getActivity(), TextInputActivity.class),
                        Config.RequestCode.REQUEST_CODE_INSERT_TEXT);
                break;

            case R.id.layout_edit_text:
                while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                    FragmentUtil.getInstance().getManager().popBackStackImmediate();

                Bundle bundle = new Bundle();
                bundle.putString(Config.KeyIntentData.KEY_TEXT_INPUT_ACT, TextOnPhotoHandler.getInstance().getItem().getText());
                Intent intent = new Intent(getActivity(), TextInputActivity.class);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, Config.RequestCode.REQUEST_CODE_EDIT_TEXT);
                break;

            case R.id.layout_font_text:
                if (FragmentUtil.getInstance().getManager()
                        .findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_FONT_TEXT_FRAGMENT) == null) {
                    while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();

                    FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Font_Text_Fragment(),
                            Config.FragmentTag.MAIN_DETAIL_FONT_TEXT_FRAGMENT);
                    img_font.setBackground(getResources().getDrawable(R.drawable.bg_modify_text));
                } else {
                    FragmentUtil.getInstance().popBackstack();
                    setBackgroundTransparent();
                }
                break;

            case R.id.layout_size_text:
                if (FragmentUtil.getInstance().getManager()
                        .findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_SIZE_TEXT_FRAGMENT) == null) {
                    while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();

                    FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Size_Text_Fragment(),
                            Config.FragmentTag.MAIN_DETAIL_SIZE_TEXT_FRAGMENT);
                    img_size.setBackground(getResources().getDrawable(R.drawable.bg_modify_text));
                } else {
                    FragmentUtil.getInstance().popBackstack();
                    setBackgroundTransparent();
                }
                break;

            case R.id.layout_color_text:
                if (FragmentUtil.getInstance().getManager()
                        .findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_COLOR_TEXT_FRAGMENT) == null) {
                    while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();

                    FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Color_Text_Fragment(),
                            Config.FragmentTag.MAIN_DETAIL_COLOR_TEXT_FRAGMENT);
                    img_color.setBackground(getResources().getDrawable(R.drawable.bg_modify_text));
                } else {
                    FragmentUtil.getInstance().popBackstack();
                    setBackgroundTransparent();
                }
                break;

            case R.id.layout_style_text:
                if (FragmentUtil.getInstance().getManager()
                        .findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_STYLE_TEXT_FRAGMENT) == null) {
                    while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();

                    FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Style_Text_Fragment(),
                            Config.FragmentTag.MAIN_DETAIL_STYLE_TEXT_FRAGMENT);
                    img_style.setBackground(getResources().getDrawable(R.drawable.bg_modify_text));
                } else {
                    FragmentUtil.getInstance().popBackstack();
                    setBackgroundTransparent();
                }
                break;

            case R.id.layout_border_text:
                if (FragmentUtil.getInstance().getManager()
                        .findFragmentByTag(Config.FragmentTag.MAIN_DETAIL_BORDER_TEXT_FRAGMENT) == null) {
                    while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();

                    FragmentUtil.getInstance().add(R.id.detail, new Main_Detail_Border_Text_Fragment(),
                            Config.FragmentTag.MAIN_DETAIL_BORDER_TEXT_FRAGMENT);
                    img_border.setBackground(getResources().getDrawable(R.drawable.bg_modify_text));
                } else {
                    FragmentUtil.getInstance().popBackstack();
                    setBackgroundTransparent();
                }
                break;

            default:
                break;
        }
    }

    public void setBackgroundTransparent() {
        if (getActivity() == null || getActivity().isFinishing() || isDetached())
            return;

        img_font.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        img_size.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        img_color.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        img_style.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        img_border.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}