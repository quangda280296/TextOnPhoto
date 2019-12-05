package com.vmb.chinh_sua_anh.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.vmb.chinh_sua_anh.model.TextOnPhoto;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerPanelView;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import net.margaritov.preference.colorpicker.ColorPickerView;

import java.util.Locale;

public class Main_Detail_Border_Text_Fragment extends BaseFragment implements
        ColorPickerView.OnColorChangedListener,
        SeekBar.OnSeekBarChangeListener {

    private View color_picker_view;

    private AppCompatSeekBar seekbar_alpha;
    private AppCompatSeekBar seekbar_radius;
    private AppCompatSeekBar seekbar_width;
    private AppCompatSeekBar seekbar_height;

    @Override
    protected int getResLayout() {
        return R.layout.layout_border_text;
    }

    @Override
    protected void initView(View view) {
        color_picker_view = view.findViewById(R.id.color_picker_view);
        seekbar_alpha = view.findViewById(R.id.seekbar_alpha);
        seekbar_radius = view.findViewById(R.id.seekbar_radius);
        seekbar_width = view.findViewById(R.id.seekbar_width);
        seekbar_height = view.findViewById(R.id.seekbar_height);
    }

    @Override
    protected void initData() {
        seekbar_alpha.setOnSeekBarChangeListener(this);
        seekbar_radius.setOnSeekBarChangeListener(this);
        seekbar_width.setOnSeekBarChangeListener(this);
        seekbar_height.setOnSeekBarChangeListener(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        seekbar_width.setMax(widthPixels / 3);
        seekbar_height.setMax(widthPixels / 3);

        TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();
        if (!item.isSetBackground()) {
            item.getShapeDrawable().setColor(Color.BLACK);
            item.getShapeDrawable().setAlpha(60);
            item.getShapeDrawable().setCornerRadius(10);
            item.setSetBackground(true);
        }
        PhotoHandler.getInstance().getPhotoEditor().applyBackground();

        seekbar_alpha.setProgress(item.getShapeDrawable().getAlpha());
        seekbar_radius.setProgress((int) item.getShapeDrawable().getCornerRadius());

        seekbar_width.setProgress(item.getShapeDrawable().getWidth());
        seekbar_height.setProgress(item.getShapeDrawable().getHeight());

        color_picker_view.setBackgroundColor(item.getShapeDrawable().getBkgColor());
        color_picker_view.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbar_alpha:
                TextOnPhotoHandler.getInstance().getItem().getShapeDrawable().setAlpha(progress);
                if (fromUser)
                    PhotoHandler.getInstance().getPhotoEditor().applyBackground();
                break;

            case R.id.seekbar_radius:
                TextOnPhotoHandler.getInstance().getItem().getShapeDrawable().setCornerRadius(progress);
                if (fromUser)
                    PhotoHandler.getInstance().getPhotoEditor().applyBackground();
                break;

            case R.id.seekbar_width:
                TextOnPhotoHandler.getInstance().getItem().getShapeDrawable().setWidth(progress);
                if (fromUser)
                    PhotoHandler.getInstance().getPhotoEditor().applyBackground();
                break;

            case R.id.seekbar_height:
                TextOnPhotoHandler.getInstance().getItem().getShapeDrawable().setHeight(progress);
                if (fromUser)
                    PhotoHandler.getInstance().getPhotoEditor().applyBackground();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnApplyColor:
                dialog.dismiss();
                break;

            case R.id.btnCancelColor:
                dialog.dismiss();
                break;

            case R.id.old_color_panel:
                dialog.dismiss();
                break;

            case R.id.new_color_panel:
                if (mListener != null) {
                    mListener.onColorChanged(mNewColor.getColor());
                }
                dialog.dismiss();
                break;

            case R.id.color_picker_view:
                setUp(TextOnPhotoHandler.getInstance().getItem().getShapeDrawable().getBkgColor());
                setHexValueEnabled(false);
                setAlphaSliderVisible(true);
                dialog.show();
                break;

            default:
                break;
        }
    }

    private ColorPickerView mColorPicker;

    private ColorPickerPanelView mOldColor;
    private ColorPickerPanelView mNewColor;

    private EditText mHexVal;
    private boolean mHexValueEnabled = false;
    private ColorStateList mHexDefaultTextColor;

    private ColorPickerDialog.OnColorChangedListener mListener;
    private int mOrientation;
    private View mLayout;

    private AlertDialog dialog;

    private void setUp(int color) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mLayout = inflater.inflate(R.layout.dialog_color_picker, null);
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getActivity().getResources().getConfiguration().orientation != mOrientation) {
                    final int oldcolor = mOldColor.getColor();
                    final int newcolor = mNewColor.getColor();
                    mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    setUp(oldcolor);
                    mNewColor.setColor(newcolor);
                    mColorPicker.setColor(newcolor);
                }
            }
        });

        mOrientation = getActivity().getResources().getConfiguration().orientation;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(mLayout);
        dialog = alert.create();

        mColorPicker = mLayout.findViewById(R.id.color_picker_view);
        mOldColor = mLayout.findViewById(R.id.old_color_panel);
        mNewColor = mLayout.findViewById(R.id.new_color_panel);

        mLayout.findViewById(R.id.btnCancelColor).setOnClickListener(this);
        mLayout.findViewById(R.id.btnApplyColor).setOnClickListener(this);

        mHexVal = mLayout.findViewById(R.id.hex_val);
        mHexVal.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mHexDefaultTextColor = mHexVal.getTextColors();

        ((LinearLayout) mOldColor.getParent()).setPadding(
                Math.round(mColorPicker.getDrawingOffset()),
                0,
                Math.round(mColorPicker.getDrawingOffset()),
                0
        );

        mOldColor.setOnClickListener(this);
        mNewColor.setOnClickListener(this);
        mColorPicker.setOnColorChangedListener(this);
        mOldColor.setColor(color);
        mColorPicker.setColor(color, true);
    }

    @Override
    public void onColorChanged(int color) {
        mNewColor.setColor(color);
        if (mHexValueEnabled)
            updateHexValue(color);
		/*
        if (mListener != null) {
			mListener.onColorChanged(color);
		}
		*/
        color_picker_view.setBackgroundColor(color);
        TextOnPhotoHandler.getInstance().getItem().getShapeDrawable().setColor(color);
    }

    public void setHexValueEnabled(boolean enable) {
        mHexValueEnabled = enable;
        if (enable) {
            mHexVal.setVisibility(View.VISIBLE);
            updateHexLengthFilter();
            updateHexValue(getColor());
        } else
            mHexVal.setVisibility(View.GONE);
    }

    private void updateHexLengthFilter() {
        if (getAlphaSliderVisible())
            mHexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        else
            mHexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
    }

    private void updateHexValue(int color) {
        if (getAlphaSliderVisible()) {
            mHexVal.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            mHexVal.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        mHexVal.setTextColor(mHexDefaultTextColor);
    }

    public void setAlphaSliderVisible(boolean visible) {
        mColorPicker.setAlphaSliderVisible(visible);
        if (mHexValueEnabled) {
            updateHexLengthFilter();
            updateHexValue(getColor());
        }
    }

    public boolean getAlphaSliderVisible() {
        return mColorPicker.getAlphaSliderVisible();
    }

    public int getColor() {
        return mColorPicker.getColor();
    }
}