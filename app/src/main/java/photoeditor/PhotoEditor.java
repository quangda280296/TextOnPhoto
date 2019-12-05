package photoeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.TextInputActivity;
import com.vmb.chinh_sua_anh.fragment.Main_Bottom_Modify_Text_Fragment;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.vmb.chinh_sua_anh.model.AlignText;
import com.vmb.chinh_sua_anh.model.TextOnPhoto;
import com.vmb.chinh_sua_anh.utils.FragmentUtil;
import com.vmb.chinh_sua_anh.widget.StickerImageView;
import com.vmb.chinh_sua_anh.widget.StickerTextView;
import com.xiaopo.flying.sticker.BackgroundDrawable;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class in initialize by {@link PhotoEditor.Builder} using a builder pattern with multiple
 * editing attributes
 * </p>
 *
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.1
 * @since 18/01/2017
 */
public class PhotoEditor implements BrushViewChangeListener {

    private static final String TAG = "PhotoEditor";
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private PhotoEditorView parentView;
    private ImageView imageView;
    private View deleteView;
    private StickerView stickerView;
    private BrushDrawingView brushDrawingView;
    private List<View> addedViews;
    private List<View> redoViews;

    private OnPhotoEditorListener mOnPhotoEditorListener;
    private boolean isTextPinchZoomable;
    private Typeface mDefaultTextTypeface;
    private Typeface mDefaultEmojiTypeface;

    private PhotoEditor(Builder builder) {
        this.context = builder.context;
        this.parentView = builder.parentView;
        this.imageView = builder.imageView;
        this.deleteView = builder.deleteView;
        this.brushDrawingView = builder.brushDrawingView;
        this.stickerView = builder.stickerView;
        this.isTextPinchZoomable = builder.isTextPinchZoomable;
        this.mDefaultTextTypeface = builder.textTypeface;
        this.mDefaultEmojiTypeface = builder.emojiTypeface;

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        brushDrawingView.setBrushViewChangeListener(this);
        addedViews = new ArrayList<>();
        redoViews = new ArrayList<>();

        if (builder.active == false)
            return;

        MultiTouchListener multiTouchOnParentView = getMultiTouchListener(true);
        multiTouchOnParentView.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {

            }

            @Override
            public void onLongClick() {

            }

            @Override
            public void onTouch() {
                turnOffControlStickerView();
                FragmentManager manager = FragmentUtil.getInstance().getManager();
                if (manager != null)
                    while (manager.getBackStackEntryCount() > 0)
                        manager.popBackStackImmediate();
            }
        });
        parentView.setOnTouchListener(multiTouchOnParentView);
    }

    public void turnOffControlStickerView() {
        stickerView.turnOffControl();
    }

    public void flipVertically() {
        parentView.getSource().setRotationX(imageView.getRotationX() == -180f ? 0f : -180f);
    }

    public void flipHorizontally() {
        parentView.getSource().setRotationY(imageView.getRotationY() == -180f ? 0f : -180f);
    }

    public void rotate() {
        float rotation = adjustAngle(parentView.getSource().getRotation() + 90f);

        parentView.getSource().setRotation(rotation);
        parentView.getBrushDrawingView().setRotation(rotation);
        parentView.getStickerView().setRotation(rotation);

        parentView.getBrushDrawingView().clearAll();
        parentView.getStickerView().resetPositionSticker(rotation);
        parentView.postInvalidate();
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    /**
     * This will add image on {@link PhotoEditorView} which you drag,rotate_left and scale using pinch
     * if {@link PhotoEditor.Builder#setPinchTextScalable(boolean)} enabled
     *
     * @param desiredImage bitmap image you want to add
     *//*
    public void addImage(Bitmap desiredImage) {
        brushDrawingView.setBrushDrawingMode(false);
        final View imageRootView = getLayout(ViewType.IMAGE);

        final ImageView imageView = imageRootView.findViewById(R.id.imgPhotoEditorImage);
        imageView.setImageBitmap(desiredImage);

        final FrameLayout frmBorder = imageRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = imageRootView.findViewById(R.id.imgPhotoEditorClose);
        final ImageView imgRotation = imageRootView.findViewById(R.id.imgPhotoEditorRotation);
        final ImageView imgScale = imageRootView.findViewById(R.id.imgPhotoEditorScale);
        final ImageView imgFlip = imageRootView.findViewById(R.id.imgPhotoEditorFlip);

        final MultiTouchListener multiTouchListener = getMultiTouchListener(false);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {

            }

            @Override
            public void onLongClick() {

            }

            @Override
            public void onTouch() {
                frmBorder.setBackgroundResource(R.drawable.rounded_border_tv);
                imgClose.setVisibility(View.VISIBLE);
                imgRotation.setVisibility(View.VISIBLE);
                imgScale.setVisibility(View.VISIBLE);
                imgFlip.setVisibility(View.VISIBLE);

                if (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 0)
                    FragmentUtil.getInstance().getManager().popBackStackImmediate();
            }
        });

        imageRootView.setOnTouchListener(multiTouchListener);
        addViewToParent(imageRootView, ViewType.IMAGE);
    }*/

    // Add Sticker
    public void addSticker(Bitmap desiredImage) {
        brushDrawingView.setBrushDrawingMode(false);
        DrawableSticker drawableSticker = new DrawableSticker(new BitmapDrawable(context.getResources(), desiredImage));
        drawableSticker.getMatrix().postScale(0.6f, 0.6f);
        parentView.setIconForSticker(context);
        stickerView.addSticker(drawableSticker);
    }

    // Add Text
    public void addText(final Context context, String text, int lineCount) {
        final String TAG = "addTextPhotoEditor()";

        brushDrawingView.setBrushDrawingMode(false);
        Log.i(TAG, "text = " + text);
        Log.i(TAG, "lineCount = " + lineCount);

        TextOnPhoto textOnPhoto = new TextOnPhoto(context);
        textOnPhoto.setText(text);
        textOnPhoto.setTextLine(lineCount);

        Typeface typeface = textOnPhoto.getTypeface();
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(textOnPhoto.getTextSize());

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int width = (rect.width() + 20) / lineCount;
        int height = (rect.height() * lineCount) + 20;

        BackgroundDrawable backgroundDrawable = new BackgroundDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setSize(width, height);
        backgroundDrawable.setBounds(5, 5, 5, 5);
        textOnPhoto.setShapeDrawable(backgroundDrawable);

        TextSticker textSticker = new TextSticker(context);
        textSticker.setBackgroundDrawable(backgroundDrawable);
        textSticker.setTypeface(textOnPhoto.getTypeface());
        textSticker.setText(text);
        textSticker.setTextColor(textOnPhoto.getTextColor());
        textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        textSticker.setMaxTextSize(textOnPhoto.getTextSize());
        textSticker.resizeText();
        textSticker.setKeyManager(TextOnPhotoHandler.getInstance().getList().size() - 1);
        if (lineCount > 1)
            textSticker.getMatrix().postScale(0.75f, 0.75f);
        parentView.setIconForText(context);
        stickerView.addSticker(textSticker);
        TextOnPhotoHandler.getInstance().setSticker(textSticker);

        while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 0)
            FragmentUtil.getInstance().getManager().popBackStackImmediate();

        FragmentUtil.getInstance().add(R.id.bottom, new Main_Bottom_Modify_Text_Fragment(),
                Config.FragmentTag.MAIN_BOTTOM_MODIFY_TEXT_FRAGMENT);
    }

    public void requestEditText() {
        if (context instanceof Activity) {

            while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                FragmentUtil.getInstance().getManager().popBackStackImmediate();

            Bundle bundle = new Bundle();
            bundle.putString(Config.KeyIntentData.KEY_TEXT_INPUT_ACT, TextOnPhotoHandler.getInstance().getItem().getText());
            Intent intent = new Intent(context, TextInputActivity.class);
            intent.putExtras(bundle);
            ((Activity) context).startActivityForResult(intent, Config.RequestCode.REQUEST_CODE_EDIT_TEXT);
        }
    }

    /**
     * This will update the text and color on provided view
     *
     * @param inputText text to update {@link TextView}
     */
    public void editText(String inputText, int lineCount) {
        TextOnPhoto textOnPhoto = TextOnPhotoHandler.getInstance().getItem();

        if (textOnPhoto != null && !(textOnPhoto.getTextLine() == lineCount && inputText.equals(textOnPhoto.getText()))) {
            textOnPhoto.setText(inputText);
            textOnPhoto.setTextLine(lineCount);
            Paint paint = new Paint();
            paint.setTypeface(textOnPhoto.getTypeface());
            paint.setTextSize(textOnPhoto.getTextSize());
            Rect rect = new Rect();
            paint.getTextBounds(inputText, 0, inputText.length(), rect);
            int width = (rect.width() + 20) / lineCount;
            int height = (rect.height() * lineCount) + 20;
            BackgroundDrawable backgroundDrawable = new BackgroundDrawable();
            backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
            backgroundDrawable.setColor(textOnPhoto.getShapeDrawable().getBkgColor());
            backgroundDrawable.setSize(width, height);
            backgroundDrawable.setCornerRadius(textOnPhoto.getShapeDrawable().getCornerRadius());
            backgroundDrawable.setBounds(5, 5, 5, 5);
            backgroundDrawable.setAlpha(textOnPhoto.getShapeDrawable().getAlpha());

            Sticker sticker = TextOnPhotoHandler.getInstance().getSticker();
            if (sticker instanceof TextSticker) {
                TextSticker textSticker = (TextSticker) sticker;
                textSticker.setBackgroundDrawable(backgroundDrawable);
                textSticker.setText(inputText);
                textSticker.setLine(lineCount);
                textSticker.resizeText();
                this.stickerView.replace(textSticker);
                this.stickerView.invalidate();
                textOnPhoto.setShapeDrawable(backgroundDrawable);
            }
        }
    }

    public void changeTextFont(Typeface typeface) {
        Sticker sticker = TextOnPhotoHandler.getInstance().getSticker();
        if (sticker instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) sticker;
            textSticker.setTypeface(typeface);
            textSticker.resizeText();
            this.stickerView.replace(textSticker);
            this.stickerView.invalidate();
            TextOnPhoto textOnPhoto = TextOnPhotoHandler.getInstance().getItem();
            textOnPhoto.setTypeface(typeface);
        }
    }

    public void changeTextSize(int inputSize) {
        TextOnPhoto textOnPhoto = TextOnPhotoHandler.getInstance().getItem();

        String charSequence = textOnPhoto.getText();
        int textLine = textOnPhoto.getTextLine();
        Paint paint = new Paint();
        paint.setTypeface(textOnPhoto.getTypeface());
        float f = (float) inputSize;
        paint.setTextSize(f);
        Rect rect = new Rect();
        paint.getTextBounds(charSequence, 0, charSequence.length(), rect);
        int width = (rect.width() + 20) / textLine;
        int height = (rect.height() * textLine) + 20;

        BackgroundDrawable backgroundDrawable = new BackgroundDrawable();
        backgroundDrawable.setColor(textOnPhoto.getShapeDrawable().getBkgColor());

        int backgroundHeight = textOnPhoto.getShapeDrawable().getHeight();
        if (backgroundHeight > height) {
            height = backgroundHeight;
        }

        backgroundDrawable.setSize(width, height);
        backgroundDrawable.setCornerRadius(textOnPhoto.getShapeDrawable().getCornerRadius());
        backgroundDrawable.setBounds(2, 2, 2, 2);
        backgroundDrawable.setAlpha(textOnPhoto.getShapeDrawable().getAlpha());

        TextSticker tS = new TextSticker(context);
        tS.setBackgroundDrawable(backgroundDrawable);
        tS.setTypeface(textOnPhoto.getTypeface());
        tS.setMaxTextSize(f);
        tS.setTextColor(textOnPhoto.getTextColor());
        tS.setLine(textLine);
        tS.setText(charSequence);
        tS.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        tS.resizeText();

        Sticker sticker = TextOnPhotoHandler.getInstance().getSticker();
        if (sticker instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) sticker;
            if (tS.isMinTextSize()) {
                Log.d("ManagerTextEditor", "MIN SIZE");
                textSticker.resizeText();
                this.stickerView.replace(textSticker);
                //this.stickerView.invalidate();
            } else {
                Log.d("ManagerTextEditor", "NEW SIZE");
                textSticker.setBackgroundDrawable(backgroundDrawable);
                textSticker.setMaxTextSize(f);
                textSticker.resizeText();
                this.stickerView.replace(textSticker);
                //this.stickerView.invalidate();
                textOnPhoto.setShapeDrawable(backgroundDrawable);
                textOnPhoto.setTextSize(f);
            }
        }
    }

    public void changeTextColor(int textColor) {
        Sticker sticker = TextOnPhotoHandler.getInstance().getSticker();
        if (sticker instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) sticker;
            textSticker.clearShadowLayer();
            textSticker.clearStroke(textColor);
            textSticker.clearShader();
            textSticker.setTextColor(textColor);
            textSticker.resizeText();
            this.stickerView.replace(textSticker);
            this.stickerView.invalidate();
            TextOnPhoto textOnPhoto = TextOnPhotoHandler.getInstance().getItem();
            textOnPhoto.clearStrokeAndShadow();
            textOnPhoto.setTextColor(textColor);
        }
    }

    public void updateTextStyle() {
        updateTextStyle(false);
    }

    public void updateTextStyle(boolean isClearStyle) {
        updateTextStyle(null, false, isClearStyle);
    }

    public void updateTextStyle(Shader shader) {
        updateTextStyle(shader, true, false);
    }

    public void updateTextStyle(Shader shader, boolean isRandomStyle, boolean isClearStyle) {
        TextOnPhoto textOnPhoto = TextOnPhotoHandler.getInstance().getItem();
        Sticker sticker = TextOnPhotoHandler.getInstance().getSticker();
        if (sticker instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) sticker;

            int i;
            Layout.Alignment[] alignmentArr = new Layout.Alignment[]{Layout.Alignment.ALIGN_CENTER, Layout.Alignment.ALIGN_NORMAL, Layout.Alignment.ALIGN_OPPOSITE};
            if (textOnPhoto.getAlignText() == AlignText.LEFT) {
                i = 1;
            } else if (textOnPhoto.getAlignText() == AlignText.RIGHT) {
                i = 2;
            } else {
                i = 0;
            }
            textSticker.setTextAlign(alignmentArr[i]);

            if (isRandomStyle) {
                textSticker.setText(textOnPhoto.getText());
                textSticker.clearStroke(textOnPhoto.getTextColor());
                textSticker.clearShadowLayer();
                if (shader != null)
                    textSticker.setShader(shader);
            } else {
                textSticker.clearShader();
                textSticker.setTextColor(textOnPhoto.getTextColor());
                textSticker.setText(textOnPhoto.getText());
                textSticker.setStroke((float) textOnPhoto.getStrokeWidth(), textOnPhoto.getStrokeColor(), textOnPhoto.getTextColor());
                textSticker.setShadowLayer((float) textOnPhoto.getShadowRadius(), (float) textOnPhoto.getShadowDxDy(),
                        (float) textOnPhoto.getShadowDxDy(), textOnPhoto.getShadowColor());
            }

            textSticker.getTextPaint().setFakeBoldText(textOnPhoto.isStyleTypeFaceBold());
            textSticker.getTextPaint().setStrikeThruText(textOnPhoto.isStyleTypeFaceStriker());
            textSticker.getTextPaint().setUnderlineText(textOnPhoto.isStyleTypeFaceUnderline());
            textSticker.getTextPaint().setTextSkewX(textOnPhoto.isStyleTypeFaceItalic() ? -0.25f : 0.0f);

            if (isClearStyle) {
                textSticker.clearShader();
                textSticker.clearStroke(textOnPhoto.getTextColor());
                textSticker.clearShadowLayer();
            }

            textSticker.resizeText();
            this.stickerView.replace(textSticker);
            //this.stickerView.invalidate();
        }
    }

    public void applyBackground() {
        TextOnPhoto textOnPhoto = TextOnPhotoHandler.getInstance().getItem();

        Paint paint = new Paint();
        paint.setTypeface(textOnPhoto.getTypeface());
        paint.setTextSize(textOnPhoto.getTextSize());
        Rect rect = new Rect();
        paint.getTextBounds(textOnPhoto.getText(), 0, textOnPhoto.getText().length(), rect);
        int textLine = textOnPhoto.getTextLine();
        int width = (rect.width() + 20) / textLine;
        int size = (rect.height() * textLine) + 20;
        BackgroundDrawable backgroundDrawable = new BackgroundDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setColor(textOnPhoto.getShapeDrawable().getBkgColor());
        if (textOnPhoto.getShapeDrawable().getWidth() > 0) {
            backgroundDrawable.setSize(textOnPhoto.getShapeDrawable().getWidth(), textOnPhoto.getShapeDrawable().getHeight());
        } else {
            backgroundDrawable.setSize(width, size);
        }
        backgroundDrawable.setCornerRadius(textOnPhoto.getShapeDrawable().getCornerRadius());
        backgroundDrawable.setBounds(2, 2, 2, 2);
        backgroundDrawable.setAlpha(textOnPhoto.getShapeDrawable().getAlpha());
        textOnPhoto.setShapeDrawable(backgroundDrawable);

        Sticker sticker = TextOnPhotoHandler.getInstance().getSticker();
        if (sticker instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) sticker;
            textSticker.setBackgroundDrawable(backgroundDrawable);
            textSticker.resizeText();
            this.stickerView.replace(textSticker);
            //this.stickerView.invalidate();
        }
    }

    /*public void addEmoji(String emojiName) {
        addEmoji(null, emojiName);
    }

    public void addEmoji(Typeface emojiTypeface, String emojiName) {
        brushDrawingView.setBrushDrawingMode(false);
        final View emojiRootView = getLayout(ViewType.EMOJI);
        final MagicTextView emojiTextView = emojiRootView.findViewById(android.R.id.switch_widget);
        final FrameLayout frmBorder = emojiRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = emojiRootView.findViewById(R.id.imgPhotoEditorClose);

        if (emojiTypeface != null) {
            emojiTextView.setTypeface(emojiTypeface);
        }
        emojiTextView.setTextSize(56);
        emojiTextView.setText(emojiName);
        MultiTouchListener multiTouchListener = getMultiTouchListener(false);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
            }

            @Override
            public void onLongClick() {
            }

            @Override
            public void onTouch() {

            }
        });
        emojiRootView.setOnTouchListener(multiTouchListener);
        addViewToParent(emojiRootView, ViewType.EMOJI);
    }*/

    /*private void addViewToParent(View rootView, ViewType viewType) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentView.addView(rootView, params);
        addedViews.add(rootView);
        if (mOnPhotoEditorListener != null)
            mOnPhotoEditorListener.onAddViewListener(viewType, addedViews.size());
    }*/

    @NonNull
    private MultiTouchListener getMultiTouchListener(boolean isParentView) {
        MultiTouchListener multiTouchListener = new MultiTouchListener(
                deleteView,
                parentView,
                this.imageView,
                isTextPinchZoomable,
                mOnPhotoEditorListener,
                isParentView);

        //multiTouchListener.setOnMultiTouchListener(this);

        return multiTouchListener;
    }

    /**
     * Get root view by its type i.e image,text and emoji
     *
     * @param viewType image,text or emoji
     * @return rootview
     */
    /*private View getLayout(final ViewType viewType) {
        View rootView = null;
        switch (viewType) {
            case TEXT:
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_text, null);
                MagicTextView txtText = rootView.findViewById(android.R.id.switch_widget);
                if (txtText != null) {
                    txtText.setGravity(Gravity.CENTER);
                    //txtText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SVN-Steady.otf"));
                }
                break;
            case IMAGE:
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_image, null);
                break;
            case EMOJI:
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_text, null);
                MagicTextView txtTextEmoji = rootView.findViewById(android.R.id.switch_widget);
                if (txtTextEmoji != null) {
                    if (mDefaultEmojiTypeface != null) {
                        txtTextEmoji.setTypeface(mDefaultEmojiTypeface);
                    }
                    txtTextEmoji.setGravity(Gravity.CENTER);
                    txtTextEmoji.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
                break;
        }

        if (rootView != null) {
            //We are setting tag as ViewType to identify what type of the view it is
            //when we remove the view from stack i.e onRemoveViewListener(ViewType viewType, int numberOfAddedViews);
            rootView.setTag(viewType);
            *//*final View finalRootView = rootView;

            final ImageView imgClose = rootView.findViewById(R.id.imgPhotoEditorClose);
            final ImageView imgEdit = rootView.findViewById(R.id.imgPhotoEditorEdit);

            if (imgClose != null) {
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewUndo(finalRootView, viewType);
                    }
                });
            }

            if (imgEdit != null) {
                imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof Activity) {

                            while (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 1)
                                FragmentUtil.getInstance().getManager().popBackStackImmediate();

                            Bundle bundle = new Bundle();
                            bundle.putString(Config.NAME_TEXT, TextOnPhotoHandler.getInstance().getItem().getText());
                            Intent intent = new Intent(context, TextInputActivity.class);
                            intent.putExtras(bundle);
                            ((Activity) context).startActivityForResult(intent, Config.RequestCode.REQUEST_EDIT_TEXT);
                        }
                    }
                });
            }*//*
        }
        return rootView;
    }*/

    /**
     * Enable/Disable drawing mode to draw on {@link PhotoEditorView}
     *
     * @param brushDrawingMode true if mode is enabled
     */
    public void setBrushDrawingMode(boolean brushDrawingMode) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushDrawingMode(brushDrawingMode);
    }

    /**
     * @return true is brush mode is enabled
     */
    public Boolean getBrushDrawableMode() {
        return brushDrawingView != null && brushDrawingView.getBrushDrawingMode();
    }

    /**
     * set the size of bursh user want to paint on canvas i.e {@link BrushDrawingView}
     *
     * @param size size of brush
     */
    public void setBrushSize(float size) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushSize(size);
    }

    /**
     * set opacity/transparency of brush while painting on {@link BrushDrawingView}
     *
     * @param opacity opacity is in form of percentage
     */
    public void setOpacity(@IntRange(from = 0, to = 100) int opacity) {
        if (brushDrawingView != null) {
            opacity = (int) ((opacity / 100.0) * 255.0);
            brushDrawingView.setOpacity(opacity);
        }
    }

    /**
     * set brush color which user want to paint
     *
     * @param color color value for paint
     */
    public void setBrushColor(@ColorInt int color) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushColor(color);
    }

    /**
     * set the eraser size
     * <br></br>
     * <b>Note :</b> Eraser size is different from the normal brush size
     *
     * @param brushEraserSize size of eraser
     */
    public void setBrushEraserSize(float brushEraserSize) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushEraserSize(brushEraserSize);
    }

    void setBrushEraserColor(@ColorInt int color) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushEraserColor(color);
    }

    /**
     * @return provide the size of eraser
     * @see PhotoEditor#setBrushEraserSize(float)
     */
    public float getEraserSize() {
        return brushDrawingView != null ? brushDrawingView.getEraserSize() : 0;
    }

    /**
     * @return provide the size of eraser
     * @see PhotoEditor#setBrushSize(float)
     */
    public float getBrushSize() {
        if (brushDrawingView != null)
            return brushDrawingView.getBrushSize();
        return 0;
    }

    public int getOpacity() {
        if (brushDrawingView != null) {
            int mOpacity = brushDrawingView.getOpacity();
            return (mOpacity * 100 / 255);
        }
        return 0;
    }

    /**
     * @return provide the size of eraser
     * @see PhotoEditor#setBrushColor(int)
     */
    public int getBrushColor() {
        if (brushDrawingView != null)
            return brushDrawingView.getBrushColor();
        return 0;
    }

    /**
     * <p>
     * Its enables eraser mode after that whenever user drags on screen this will erase the existing
     * paint
     * <br>
     * <b>Note</b> : This eraser will work on paint views only
     * <p>
     */
    public void brushEraser() {
        if (brushDrawingView != null)
            brushDrawingView.brushEraser();
    }

    /*private void viewUndo() {
        if (addedViews.size() > 0) {
            parentView.removeView(addedViews.remove(addedViews.size() - 1));
            if (mOnPhotoEditorListener != null)
                mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
        }
    }*/

    private void viewUndo(View removedView, ViewType viewType) {
        if (addedViews.size() > 0) {
            if (addedViews.contains(removedView)) {
                parentView.removeView(removedView);
                addedViews.remove(removedView);
                redoViews.add(removedView);
                if (mOnPhotoEditorListener != null) {
                    mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
                    mOnPhotoEditorListener.onRemoveViewListener(viewType, addedViews.size());
                }
            }
        }
    }

    /**
     * Undo the last operation perform on the {@link PhotoEditor}
     *
     * @return true if there nothing more to undo
     */
    public boolean undo() {
        if (addedViews.size() > 0) {
            View removeView = addedViews.get(addedViews.size() - 1);
            if (removeView instanceof BrushDrawingView) {
                return brushDrawingView != null && brushDrawingView.undo();
            } else {
                addedViews.remove(addedViews.size() - 1);
                parentView.removeView(removeView);
                redoViews.add(removeView);
            }
            if (mOnPhotoEditorListener != null) {
                mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
                Object viewTag = removeView.getTag();
                if (viewTag != null && viewTag instanceof ViewType) {
                    mOnPhotoEditorListener.onRemoveViewListener(((ViewType) viewTag), addedViews.size());
                }
            }
        }
        return addedViews.size() != 0;
    }

    /**
     * Redo the last operation perform on the {@link PhotoEditor}
     *
     * @return true if there nothing more to redo
     */
    public boolean redo() {
        if (redoViews.size() > 0) {
            View redoView = redoViews.get(redoViews.size() - 1);
            if (redoView instanceof BrushDrawingView) {
                return brushDrawingView != null && brushDrawingView.redo();
            } else {
                redoViews.remove(redoViews.size() - 1);
                parentView.addView(redoView);
                addedViews.add(redoView);
            }
            Object viewTag = redoView.getTag();
            if (mOnPhotoEditorListener != null && viewTag != null && viewTag instanceof ViewType) {
                mOnPhotoEditorListener.onAddViewListener(((ViewType) viewTag), addedViews.size());
            }
        }
        return redoViews.size() != 0;
    }

    private void clearBrushAllViews() {
        if (brushDrawingView != null)
            brushDrawingView.clearAll();
    }

    /**
     * Removes all the edited operations performed {@link PhotoEditorView}
     * This will also clear the undo and redo stack
     */
    public void clearAllViews() {
        for (int i = 0; i < addedViews.size(); i++) {
            parentView.removeView(addedViews.get(i));
        }
        if (addedViews.contains(brushDrawingView)) {
            parentView.addView(brushDrawingView);
        }
        addedViews.clear();
        redoViews.clear();
        clearBrushAllViews();
    }

    @UiThread
    public void clearHelperBox() {
        for (int i = 0; i < parentView.getChildCount(); i++) {
            View childAt = parentView.getChildAt(i);
            StickerImageView stickerImageView = childAt.findViewById(android.R.id.widget_frame);
            if (stickerImageView != null) {
                stickerImageView.setControlsVisibility(false);
            }
            StickerTextView stickerTextView = childAt.findViewById(android.R.id.switch_widget);
            if (stickerTextView != null) {
                stickerTextView.setControlsVisibility(false);
            }
        }
    }

    /**
     * Setup of custom effect using effect type and set parameters values
     *
     * @param customEffect {@link CustomEffect.Builder#setParameter(String, Object)}
     */
    public void setFilterEffect(CustomEffect customEffect) {
        parentView.setFilterEffect(customEffect);
    }

    /**
     * Set pre-define filter available
     *
     * @param filterType type of filter want to apply {@link PhotoEditor}
     */
    public void setFilterEffect(PhotoFilter filterType) {
        parentView.setFilterEffect(filterType);
    }

    /**
     * A callback to save the edited image asynchronously
     */
    public interface OnSaveListener {

        /**
         * Call when edited image is saved successfully on given path
         *
         * @param imagePath path on which image is saved
         */
        void onSuccess(@NonNull String imagePath);

        /**
         * Call when failed to saved image on given path
         *
         * @param exception exception thrown while saving image
         */
        void onFailure(@NonNull Exception exception);
    }


    /**
     * @param imagePath      path on which image to be saved
     * @param onSaveListener callback for saving image
     * @see OnSaveListener
     * @deprecated Use {@link #saveAsFile(String, OnSaveListener)} instead
     */
    @SuppressLint("StaticFieldLeak")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Deprecated
    public void saveImage(@NonNull final String imagePath, @NonNull final OnSaveListener onSaveListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        saveAsFile(imagePath, onSaveListener);
    }

    /**
     * Save the edited image on given path
     *
     * @param fileName       path on which image to be saved
     * @param onSaveListener callback for saving image
     * @see OnSaveListener
     */
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void saveAsFile(@NonNull final String fileName, @NonNull final OnSaveListener onSaveListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        saveAsFile(fileName, new SaveSettings.Builder().build(), onSaveListener);
    }

    /**
     * Save the edited image on given path
     *
     * @param fileName       path on which image to be saved
     * @param saveSettings   builder for multiple save options {@link SaveSettings}
     * @param onSaveListener callback for saving image
     * @see OnSaveListener
     */
    @SuppressLint("StaticFieldLeak")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void saveAsFile(@NonNull final String fileName,
                           @NonNull final SaveSettings saveSettings,
                           @NonNull final OnSaveListener onSaveListener) {
        final String TAG = "saveAsFile()";
        Log.i(TAG, "File name: " + fileName);
        parentView.saveFilter(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                new AsyncTask<String, String, Exception>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        turnOffControlStickerView();
                        //clearHelperBox();
                        if (parentView != null)
                            parentView.setDrawingCacheEnabled(false);
                    }

                    @SuppressLint("MissingPermission")
                    @Override
                    protected Exception doInBackground(String... strings) {
                        // Create a media folder
                        String pathFolder = Environment.getExternalStorageDirectory() + "/" + Config.Directory.SAVE_DIRECTORY + "/";
                        Log.i(TAG, "pathFolder = " + pathFolder);
                        File create_folder = new File(pathFolder);
                        if (!create_folder.exists())
                            create_folder.mkdirs();

                        // Create a media file name
                        File file = new File(create_folder, fileName);
                        try {
                            final FileOutputStream out = new FileOutputStream(file, false);
                            if (parentView != null) {
                                parentView.setDrawingCacheEnabled(true);
                                Bitmap drawingCache = saveSettings.isTransparencyEnabled()
                                        ? BitmapUtil.removeTransparency(parentView.getDrawingCache())
                                        : parentView.getDrawingCache();
                                drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out);
                            }
                            OutputStreamWriter osw = new OutputStreamWriter(out);
                            String string = "";
                            osw.write(string);
                            out.flush();
                            out.close();

                            Log.d(TAG, "Filed Saved Successfully");
                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "Failed to save File: " + e.getMessage());
                            return e;
                        }
                    }

                    @Override
                    protected void onPostExecute(Exception e) {
                        super.onPostExecute(e);
                        if (e == null) {
                            //Clear all views if its enabled in save settings
                            if (saveSettings.isClearViewsEnabled())
                                clearAllViews();
                            onSaveListener.onSuccess(Environment.getExternalStorageDirectory() + "/" +
                                    Config.Directory.SAVE_DIRECTORY + "/" + fileName);
                        } else {
                            onSaveListener.onFailure(e);
                        }
                    }

                }.execute();
            }

            @Override
            public void onFailure(Exception e) {
                onSaveListener.onFailure(e);
            }
        });
    }

    /**
     * Save the edited image as bitmap
     *
     * @param onSaveBitmap callback for saving image as bitmap
     * @see OnSaveBitmap
     */
    @SuppressLint("StaticFieldLeak")
    public void saveAsBitmap(@NonNull final OnSaveBitmap onSaveBitmap) {
        saveAsBitmap(new SaveSettings.Builder().build(), onSaveBitmap);
    }

    /**
     * Save the edited image as bitmap
     *
     * @param saveSettings builder for multiple save options {@link SaveSettings}
     * @param onSaveBitmap callback for saving image as bitmap
     * @see OnSaveBitmap
     */
    @SuppressLint("StaticFieldLeak")
    public void saveAsBitmap(@NonNull final SaveSettings saveSettings,
                             @NonNull final OnSaveBitmap onSaveBitmap) {
        parentView.saveFilter(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                new AsyncTask<String, String, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        //clearHelperBox();
                        turnOffControlStickerView();
                        if (parentView != null)
                            parentView.setDrawingCacheEnabled(false);
                    }

                    @Override
                    protected Bitmap doInBackground(String... strings) {
                        if (parentView != null) {
                            parentView.setDrawingCacheEnabled(true);
                            return saveSettings.isTransparencyEnabled() ?
                                    BitmapUtil.removeTransparency(parentView.getDrawingCache())
                                    : parentView.getDrawingCache();
                        } else {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        if (bitmap != null) {
                            if (saveSettings.isClearViewsEnabled()) clearAllViews();
                            onSaveBitmap.onBitmapReady(bitmap);
                        } else {
                            onSaveBitmap.onFailure(new Exception("Failed to load the bitmap"));
                        }
                    }

                }.execute();
            }

            @Override
            public void onFailure(Exception e) {
                onSaveBitmap.onFailure(e);
            }
        });
    }

    private static String convertEmoji(String emoji) {
        String returnedEmoji;
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = new String(Character.toChars(convertEmojiToInt));
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    /**
     * Callback on editing operation perform on {@link PhotoEditorView}
     *
     * @param onPhotoEditorListener {@link OnPhotoEditorListener}
     */
    public void setOnPhotoEditorListener(@NonNull OnPhotoEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }

    /**
     * Check if any changes made need to save
     *
     * @return true if nothing is there to change
     */

    public boolean isCacheEmpty() {
        return addedViews.size() == 0 && redoViews.size() == 0;
    }


    @Override
    public void onViewAdd(BrushDrawingView brushDrawingView) {
        if (redoViews.size() > 0) {
            redoViews.remove(redoViews.size() - 1);
        }
        addedViews.add(brushDrawingView);
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onAddViewListener(ViewType.BRUSH_DRAWING, addedViews.size());
        }
    }

    @Override
    public void onViewRemoved(BrushDrawingView brushDrawingView) {
        if (addedViews.size() > 0) {
            View removeView = addedViews.remove(addedViews.size() - 1);
            if (!(removeView instanceof BrushDrawingView)) {
                parentView.removeView(removeView);
            }
            redoViews.add(removeView);
        }
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
            mOnPhotoEditorListener.onRemoveViewListener(ViewType.BRUSH_DRAWING, addedViews.size());
        }
    }

    @Override
    public void onStartDrawing() {
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onStartViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    @Override
    public void onStopDrawing() {
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onStopViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }


    /**
     * Builder pattern to define {@link PhotoEditor} Instance
     */
    public static class Builder {

        private Context context;
        private PhotoEditorView parentView;
        private ImageView imageView;
        private View deleteView;
        private StickerView stickerView;
        private BrushDrawingView brushDrawingView;
        private Typeface textTypeface;
        private Typeface emojiTypeface;
        //By Default pinch zoom on text is enabled
        private boolean isTextPinchZoomable = true;

        private boolean active;

        /**
         * Building a PhotoEditor which requires a Context and PhotoEditorView
         * which we have setup in our xml layout
         *
         * @param context         context
         * @param photoEditorView {@link PhotoEditorView}
         */
        public Builder(Context context, PhotoEditorView photoEditorView) {
            this.context = context;
            parentView = photoEditorView;
            imageView = photoEditorView.getSource();
            brushDrawingView = photoEditorView.getBrushDrawingView();
            stickerView = photoEditorView.getStickerView();
        }

        public Builder setDeleteView(View deleteView) {
            this.deleteView = deleteView;
            return this;
        }

        /**
         * set default text font to be added on image
         *
         * @param textTypeface typeface for custom font
         * @return {@link Builder} instant to build {@link PhotoEditor}
         */
        public Builder setDefaultTextTypeface(Typeface textTypeface) {
            this.textTypeface = textTypeface;
            return this;
        }

        /**
         * set default font specific to add emojis
         *
         * @param emojiTypeface typeface for custom font
         * @return {@link Builder} instant to build {@link PhotoEditor}
         */
        public Builder setDefaultEmojiTypeface(Typeface emojiTypeface) {
            this.emojiTypeface = emojiTypeface;
            return this;
        }

        /**
         * set false to disable pinch to zoom on text insertion.By deafult its true
         *
         * @param isTextPinchZoomable flag to make pinch to zoom
         * @return {@link Builder} instant to build {@link PhotoEditor}
         */
        public Builder setPinchTextScalable(boolean isTextPinchZoomable) {
            this.isTextPinchZoomable = isTextPinchZoomable;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        /**
         * @return build PhotoEditor instance
         */
        public PhotoEditor build() {
            return new PhotoEditor(this);
        }
    }

    /*public static ArrayList<String> getEmojis(Context context) {
        ArrayList<String> convertedEmojiList = new ArrayList<>();
        String[] emojiList = context.getResources().getStringArray(R.array.photo_editor_emoji);
        for (String emojiUnicode : emojiList) {
            convertedEmojiList.add(convertEmoji(emojiUnicode));
        }
        return convertedEmojiList;
    }*/
}