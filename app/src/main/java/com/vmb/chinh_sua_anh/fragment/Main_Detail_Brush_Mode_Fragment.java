package com.vmb.chinh_sua_anh.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.adapter.ColorPickerAdapter;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerPanelView;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import net.margaritov.preference.colorpicker.ColorPickerView;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Detail_Brush_Mode_Fragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener,
        ColorPickerView.OnColorChangedListener{

    private View custom_color;
    private CircleImageView custom_picker_view;

    private LinearLayout expand;
    private FrameLayout btn_expand_brush;
    private TextView lbl_touch;

    private SeekBar seekbar_brush_size;
    private TextView lbl_brush_size;

    private SeekBar seekbar_opacity;
    private TextView lbl_opacity;

    @Override
    protected int getResLayout() {
        return R.layout.layout_detail_brush;
    }

    @Override
    protected void initView(View view) {
        custom_color = view.findViewById(R.id.custom_color);
        custom_picker_view = view.findViewById(R.id.custom_picker_view);

        expand = view.findViewById(R.id.expand);
        btn_expand_brush = view.findViewById(R.id.btn_expand_brush);
        lbl_touch = view.findViewById(R.id.lbl_touch);

        seekbar_brush_size = view.findViewById(R.id.seekbar_brush_size);
        lbl_brush_size = view.findViewById(R.id.lbl_brush_size);

        seekbar_opacity = view.findViewById(R.id.seekbar_opacity);
        lbl_opacity = view.findViewById(R.id.lbl_opacity);
    }

    @Override
    protected void initData() {
        seekbar_brush_size.setProgress((int) PhotoHandler.getInstance().getPhotoEditor().getBrushSize());
        seekbar_brush_size.setOnSeekBarChangeListener(this);

        seekbar_opacity.setProgress(PhotoHandler.getInstance().getPhotoEditor().getOpacity());
        seekbar_opacity.setOnSeekBarChangeListener(this);

        initBrushMode();

        custom_picker_view.setColorFilter(PhotoHandler.getInstance().getPhotoEditor().getBrushColor());
        custom_color.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_expand_brush:
                if (expand.getVisibility() == View.GONE) {
                    lbl_touch.setText(getActivity().getString(R.string.touch_to_collapse));
                    YoYo.with(Techniques.FadeIn)
                            .duration(200)
                            .playOn(getActivity().findViewById(R.id.expand));
                    expand.setVisibility(View.VISIBLE);
                } else {
                    lbl_touch.setText(getActivity().getString(R.string.touch_to_expand));
                    YoYo.with(Techniques.FadeOut)
                            .duration(200)
                            .playOn(getActivity().findViewById(R.id.expand));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expand.setVisibility(View.GONE);
                        }
                    }, 200);
                }
                break;

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

            case R.id.custom_color:
                setUp(PhotoHandler.getInstance().getPhotoEditor().getBrushColor());
                setHexValueEnabled(false);
                setAlphaSliderVisible(true);
                dialog.show();
                break;

            default:
                break;
        }
    }

    public void initBrushMode() {
        RecyclerView rvColor = getActivity().findViewById(R.id.rvColors);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvColor.setLayoutManager(layoutManager);
        rvColor.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                custom_picker_view.setColorFilter(colorCode);
                PhotoHandler.getInstance().getPhotoEditor().setBrushColor(colorCode);
            }
        });
        rvColor.setAdapter(colorPickerAdapter);
        btn_expand_brush.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbar_brush_size:
                PhotoHandler.getInstance().getPhotoEditor().setBrushSize(seekBar.getProgress());
                lbl_brush_size.setText(progress + "");
                break;

            case R.id.seekbar_opacity:
                PhotoHandler.getInstance().getPhotoEditor().setOpacity(seekBar.getProgress());
                lbl_opacity.setText(progress + "");
                break;

            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
                if(getActivity() == null)
                    return;

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
        custom_picker_view.setColorFilter(color);
        PhotoHandler.getInstance().getPhotoEditor().setBrushColor(color);
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

    public boolean getHexValueEnabled() {
        return mHexValueEnabled;
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