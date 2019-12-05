package com.vmb.chinh_sua_anh.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.chinh_sua_anh.Interface.IOnStylesClickListener;
import com.vmb.chinh_sua_anh.adapter.StylesAdapter;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.vmb.chinh_sua_anh.model.AlignText;
import com.vmb.chinh_sua_anh.model.Styles;
import com.vmb.chinh_sua_anh.model.TextOnPhoto;
import com.vmb.chinh_sua_anh.utils.IOnTouchClickListener;
import com.vmb.chinh_sua_anh.utils.Utils;
import com.vmb.chinh_sua_anh.widget.ManagerStyles;
import com.xiaopo.flying.sticker.GradientManager;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerPanelView;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import net.margaritov.preference.colorpicker.ColorPickerView;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;

public class Main_Detail_Style_Text_Fragment extends BaseFragment
        implements IOnStylesClickListener, ColorPickerView.OnColorChangedListener {

    private ManagerStyles managerStyles;
    private int type = 0;

    private TextView btnClearStyles;
    private TextView btnRandomStyles;

    private RecyclerView layoutListStyles;

    private TextView btnColorStrokeStyle;
    private TextView btnColorShadowStyle;

    private StepperTouch btn_touch_stroke;
    private StepperTouch btn_touch_shadow;

    private View btnNormalStyle;
    private View btnBoldStyle;
    private View btnItalicStyle;
    private View btnUnderlineStyle;
    private View btnStrikeStyle;

    private View btnGravityLeft;
    private View btnGravityCenter;
    private View btnGravityRight;

    @Override
    protected int getResLayout() {
        return R.layout.layout_style_text;
    }

    @Override
    protected void initView(View view) {
        btnClearStyles = view.findViewById(R.id.btnClearStyles);
        btnRandomStyles = view.findViewById(R.id.btnRandomStyles);

        layoutListStyles = view.findViewById(R.id.layoutListStyles);

        btnColorStrokeStyle = view.findViewById(R.id.btnColorStrokeStyle);
        btnColorShadowStyle = view.findViewById(R.id.btnColorShadowStyle);

        btn_touch_stroke = view.findViewById(R.id.btn_touch_stroke);
        btn_touch_shadow = view.findViewById(R.id.btn_touch_shadow);

        btnNormalStyle = view.findViewById(R.id.btnNormalStyle);
        btnBoldStyle = view.findViewById(R.id.btnBoldStyle);
        btnItalicStyle = view.findViewById(R.id.btnItalicStyle);
        btnUnderlineStyle = view.findViewById(R.id.btnUnderlineStyle);
        btnStrikeStyle = view.findViewById(R.id.btnStrikeStyle);

        btnGravityLeft = view.findViewById(R.id.btnGravityLeft);
        btnGravityCenter = view.findViewById(R.id.btnGravityCenter);
        btnGravityRight = view.findViewById(R.id.btnGravityRight);

        Utils.setTag(getActivity(), view.findViewById(R.id.align));
    }

    @Override
    protected void initData() {
        managerStyles = new ManagerStyles();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutListStyles.setLayoutManager(mLayoutManager);
        layoutListStyles.setHasFixedSize(true);

        StylesAdapter stylesAdapter = new StylesAdapter(getActivity(), managerStyles.getStylesArrayList());
        stylesAdapter.setmListener(this);
        this.layoutListStyles.setAdapter(stylesAdapter);

        loadStyleTextData();

        btnColorStrokeStyle.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        btnColorShadowStyle.setOnTouchListener(new OnTouchClickListener(this, getActivity()));

        btnClearStyles.setOnTouchListener(new OnTouchClickListener(this, getActivity()));
        btnRandomStyles.setOnTouchListener(new OnTouchClickListener(this, getActivity()));

        btnNormalStyle.setOnTouchListener(new IOnTouchClickListener(getActivity(), 5, this));
        btnBoldStyle.setOnTouchListener(new IOnTouchClickListener(getActivity(), 5, this));
        btnItalicStyle.setOnTouchListener(new IOnTouchClickListener(getActivity(), 5, this));
        btnUnderlineStyle.setOnTouchListener(new IOnTouchClickListener(getActivity(), 5, this));
        btnStrikeStyle.setOnTouchListener(new IOnTouchClickListener(getActivity(), 5, this));

        btnGravityLeft.setOnTouchListener(new IOnTouchClickListener(getActivity(), 4, this));
        btnGravityCenter.setOnTouchListener(new IOnTouchClickListener(getActivity(), 4, this));
        btnGravityRight.setOnTouchListener(new IOnTouchClickListener(getActivity(), 4, this));

        btn_touch_stroke.stepper.setMin(0);
        btn_touch_stroke.enableSideTap(true);
        btn_touch_stroke.stepper.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int i, boolean b) {
                TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();
                item.setStrokeWidth(i);
                if (i == 0) {
                    item.setStrokeColor(item.getTextColor());
                }

                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
            }
        });

        btn_touch_shadow.stepper.setMin(0);
        btn_touch_shadow.enableSideTap(true);
        btn_touch_shadow.stepper.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int i, boolean b) {
                TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();
                item.setShadowRadius(i);
                item.setShadowDxDy(i);
                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
            }
        });
    }

    public void loadStyleTextData() {
        TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();

        this.btn_touch_shadow.stepper.setValue(item.getShadowDxDy());
        this.btn_touch_stroke.stepper.setValue(item.getStrokeWidth());

        int stroke = item.getStrokeColor();
        if (stroke == 0) {
            stroke = getResources().getColor(R.color.choose_color_yellow);
            item.setStrokeColor(stroke);
        }

        int shadow = item.getShadowColor();
        if (shadow == 0) {
            shadow = getResources().getColor(R.color.choose_color_red);
            item.setShadowColor(shadow);
        }

        this.btnColorStrokeStyle.setBackgroundColor(stroke);
        this.btnColorShadowStyle.setBackgroundColor(shadow);

        switch (item.getAlignText()) {
            case CENTER:
                this.btnGravityCenter.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                break;

            case LEFT:
                this.btnGravityLeft.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                break;

            case RIGHT:
                this.btnGravityRight.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                break;

            default:
                break;
        }

        if(item.isStyleTypeFaceNormal())
            this.btnNormalStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
        else {
            if(item.isStyleTypeFaceBold())
                this.btnBoldStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));

            if(item.isStyleTypeFaceItalic())
                this.btnItalicStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));

            if(item.isStyleTypeFaceUnderline())
                this.btnUnderlineStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));

            if(item.isStyleTypeFaceStriker())
                this.btnStrikeStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
        }
    }

    @Override
    public void onClick(View v) {
        TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();

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

            case R.id.btnClearStyles:
                defaultStyle();
                break;

            case R.id.btnRandomStyles:
                randomStyles();
                btn_touch_stroke.stepper.setValue(item.getStrokeWidth());
                btn_touch_shadow.stepper.setValue(item.getShadowDxDy());
                break;

            case R.id.btnColorShadowStyle:
                type = 2;
                setUp(item.getShadowColor());
                setHexValueEnabled(false);
                setAlphaSliderVisible(true);
                dialog.show();
                break;

            case R.id.btnColorStrokeStyle:
                type = 1;
                setUp(item.getStrokeColor());
                setHexValueEnabled(false);
                setAlphaSliderVisible(true);
                dialog.show();
                break;

            case R.id.btnGravityCenter:
                v.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setAlignText(AlignText.CENTER);
                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
                break;

            case R.id.btnGravityLeft:
                v.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setAlignText(AlignText.LEFT);
                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
                break;

            case R.id.btnGravityRight:
                v.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setAlignText(AlignText.RIGHT);
                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
                break;

            case R.id.btnBoldStyle:
                applyBold(item);
                break;

            case R.id.btnItalicStyle:
                applyItalic(item);
                break;

            case R.id.btnNormalStyle:
                applyNormal(item);
                break;

            case R.id.btnStrikeStyle:
                applyStrike(item);
                break;

            case R.id.btnUnderlineStyle:
                applyUnderline(item);
                break;

            default:
                break;
        }
    }

    private void applyNormal(TextOnPhoto item) {
        btnNormalStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
        btnBoldStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnItalicStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnUnderlineStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnStrikeStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        item.setStyleTypeFaceNormal(true);
        TextOnPhotoHandler.getInstance().setItem(item);
        PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
    }

    private void applyBold(TextOnPhoto item) {
        boolean check = item.isStyleTypeFaceBold();
        if (check) {
            item.setStyleTypeFaceBold(false);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
            btnBoldStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if (!item.isStyleTypeFaceBold()
                    && !item.isStyleTypeFaceItalic()
                    && !item.isStyleTypeFaceUnderline()
                    && !item.isStyleTypeFaceStriker()) {
                btnNormalStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setStyleTypeFaceNormal(true);
            }
        } else {
            btnNormalStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btnBoldStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
            item.setStyleTypeFaceBold(true);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
        }
        TextOnPhotoHandler.getInstance().setItem(item);
    }

    private void applyItalic(TextOnPhoto item) {
        boolean check = item.isStyleTypeFaceItalic();
        if (check) {
            item.setStyleTypeFaceItalic(false);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
            btnItalicStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if (!item.isStyleTypeFaceBold()
                    && !item.isStyleTypeFaceItalic()
                    && !item.isStyleTypeFaceUnderline()
                    && !item.isStyleTypeFaceStriker()) {
                btnNormalStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setStyleTypeFaceNormal(true);
            }
        } else {
            btnNormalStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btnItalicStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
            item.setStyleTypeFaceItalic(true);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
        }
    }

    private void applyUnderline(TextOnPhoto item) {
        boolean check = item.isStyleTypeFaceUnderline();
        if (check) {
            item.setStyleTypeFaceUnderline(false);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
            btnUnderlineStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if (!item.isStyleTypeFaceBold()
                    && !item.isStyleTypeFaceItalic()
                    && !item.isStyleTypeFaceUnderline()
                    && !item.isStyleTypeFaceStriker()) {
                btnNormalStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setStyleTypeFaceNormal(true);
            }
        } else {
            btnNormalStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btnUnderlineStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
            item.setStyleTypeFaceUnderline(true);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
        }
    }

    private void applyStrike(TextOnPhoto item) {
        boolean check = item.isStyleTypeFaceStriker();
        if (check) {
            item.setStyleTypeFaceStriker(false);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
            btnStrikeStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if (!item.isStyleTypeFaceBold()
                    && !item.isStyleTypeFaceItalic()
                    && !item.isStyleTypeFaceUnderline()
                    && !item.isStyleTypeFaceStriker()) {
                btnNormalStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
                item.setStyleTypeFaceNormal(true);
            }
        } else {
            btnNormalStyle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btnStrikeStyle.setBackgroundColor(getResources().getColor(R.color.bg_btn_style));
            item.setStyleTypeFaceStriker(true);
            PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
        }
    }

    private void randomStyles() {
        TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();
        item.clearStrokeAndShadow();

        /*Shader randomLinearGradient;
        GradientManager gradientManager =
                new GradientManager(new Point(item.getShapeDrawable().getWidth(), item.getShapeDrawable().getWidth()));
        int nextInt = new Random().nextInt(3);
        if (nextInt == 0) {
            randomLinearGradient = gradientManager.getRandomLinearGradient();
        } else if (nextInt == 1) {
            randomLinearGradient = gradientManager.getRandomRadialGradient();
        } else {
            randomLinearGradient = gradientManager.getRandomSweepGradient();
        }*/

        List<Styles> list = managerStyles.getStylesArrayList();
        if (list.size() > 0) {
            Styles styles = list.get(Utils.rand(0, list.size() - 1));
            CharSequence ClearStyles = ManagerStyles.ClearStyles(item.getText());
            item.setText(ClearStyles.toString());

            item.setStrokeWidth(styles.getStrokeWidth());
            item.setStrokeColor(styles.getStrokeColor());
            item.setShadowColor(styles.getShadowColor());
            item.setShadowDxDy(styles.getShadowDxDy());
            item.setShadowRadius(5);

            btnColorStrokeStyle.setBackgroundColor(item.getStrokeColor());
            btn_touch_stroke.stepper.setValue(item.getStrokeWidth());

            btnColorShadowStyle.setBackgroundColor(item.getShadowColor());
            btn_touch_shadow.stepper.setValue(item.getShadowDxDy());
        }
        PhotoHandler.getInstance().getPhotoEditor().updateTextStyle(null);
    }

    public void defaultStyle() {
        TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();
        item.clearStrokeAndShadow();
        PhotoHandler.getInstance().getPhotoEditor().updateTextStyle(true);

        btnColorStrokeStyle.setBackgroundColor(item.getStrokeColor());
        btn_touch_stroke.stepper.setValue(item.getStrokeWidth());

        btnColorShadowStyle.setBackgroundColor(item.getShadowColor());
        btn_touch_shadow.stepper.setValue(item.getShadowDxDy());
    }

    @Override
    public void OnStylesClick(Styles styles) {
        TextOnPhoto item = TextOnPhotoHandler.getInstance().getItem();
        CharSequence ClearStyles = ManagerStyles.ClearStyles(item.getText());
        item.setText(ClearStyles.toString());

        item.setStrokeWidth(styles.getStrokeWidth());
        item.setStrokeColor(styles.getStrokeColor());
        item.setShadowColor(styles.getShadowColor());
        item.setShadowDxDy(styles.getShadowDxDy());
        item.setShadowRadius(5);

        btnColorStrokeStyle.setBackgroundColor(item.getStrokeColor());
        btn_touch_stroke.stepper.setValue(item.getStrokeWidth());

        btnColorShadowStyle.setBackgroundColor(item.getShadowColor());
        btn_touch_shadow.stepper.setValue(item.getShadowDxDy());
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
                Activity activity = getActivity();
                if(activity == null)
                    return;

                if (activity.getResources().getConfiguration().orientation != mOrientation) {
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

        switch (type) {
            case 1:
                TextOnPhotoHandler.getInstance().getItem().setStrokeColor(color);
                btnColorStrokeStyle.setBackgroundColor(color);
                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
                break;

            case 2:
                TextOnPhotoHandler.getInstance().getItem().setShadowColor(color);
                btnColorShadowStyle.setBackgroundColor(color);
                PhotoHandler.getInstance().getPhotoEditor().updateTextStyle();
                break;
        }
		/*
        if (mListener != null) {
			mListener.onColorChanged(color);
		}
		*/
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