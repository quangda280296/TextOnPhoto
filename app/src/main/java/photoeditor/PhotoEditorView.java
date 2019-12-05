package photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.fragment.Main_Bottom_Modify_Text_Fragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;
import com.vmb.chinh_sua_anh.utils.FragmentUtil;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.ResetRotationEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.util.Arrays;

/**
 * <p>
 * This ViewGroup will have the {@link BrushDrawingView} to draw paint on it with {@link ImageView}
 * which our source image
 * </p>
 *
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.1
 * @since 1/18/2018
 */

public class PhotoEditorView extends RelativeLayout {

    private static final String TAG = "PhotoEditorView";

    private boolean isHasFilter;
    private FilterImageView mImgSource;
    private ImageFilterView mImageFilterView;
    private StickerView mStickerView;
    private BrushDrawingView mBrushDrawingView;
    private static final int imgSrcId = 1, glFilterId = 2, stickerId = 3, brushSrcId = 4;

    public PhotoEditorView(Context context) {
        super(context);
        init(null);
    }

    public PhotoEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PhotoEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PhotoEditorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @SuppressLint("Recycle")
    private void init(@Nullable AttributeSet attrs) {

        //Setup image attributes
        mImgSource = new FilterImageView(getContext());
        mImgSource.setId(imgSrcId);
        mImgSource.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams imgSrcParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PhotoEditorView);
            Drawable imgSrcDrawable = a.getDrawable(R.styleable.PhotoEditorView_photo_src);
            if (imgSrcDrawable != null)
                mImgSource.setImageDrawable(imgSrcDrawable);

            isHasFilter = a.getBoolean(R.styleable.PhotoEditorView_is_has_filter, false);
            if (isHasFilter)
                imgSrcParam = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        imgSrcParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


        //Setup GLSurface attributes
        mImageFilterView = new ImageFilterView(getContext());
        mImageFilterView.setId(glFilterId);
        mImageFilterView.setVisibility(GONE);
        //Align GLSurface to the size of image view
        RelativeLayout.LayoutParams imgFilterParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgFilterParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgFilterParam.addRule(RelativeLayout.ALIGN_TOP, imgSrcId);
        imgFilterParam.addRule(RelativeLayout.ALIGN_BOTTOM, imgSrcId);
        imgFilterParam.addRule(RelativeLayout.ALIGN_LEFT, imgSrcId);
        imgFilterParam.addRule(RelativeLayout.ALIGN_RIGHT, imgSrcId);
        if (isHasFilter) {
            mImgSource.setOnImageChangedListener(new FilterImageView.OnImageChangedListener() {
                @Override
                public void onBitmapLoaded(@Nullable final Bitmap sourceBitmap) {
                    mImageFilterView.setFilterEffect(PhotoFilter.NONE);
                    mImageFilterView.setSourceBitmap(sourceBitmap);
                }
            });
        }


        //Setup StickerView
        mStickerView = new StickerView(getContext());
        mStickerView.setId(stickerId);
        //Align sticker to the size of image view
        RelativeLayout.LayoutParams stickerParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stickerParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        stickerParam.addRule(RelativeLayout.ALIGN_TOP, imgSrcId);
        stickerParam.addRule(RelativeLayout.ALIGN_BOTTOM, imgSrcId);
        stickerParam.addRule(RelativeLayout.ALIGN_LEFT, imgSrcId);
        stickerParam.addRule(RelativeLayout.ALIGN_RIGHT, imgSrcId);
        //default ic_launcher layout
        mStickerView.setBackgroundColor(Color.TRANSPARENT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int size = (int) (((float) displayMetrics.widthPixels / 100.0f) * 7.0f);
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.sticker_ic_close), size, size), BitmapStickerIcon.LEFT_TOP);
        mStickerView.setMinScaleWidth(((float) bitmapStickerIcon.getWidth()) * 1.3f);
        mStickerView.setLocked(false);
        mStickerView.setConstrained(true);
        mStickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
                if (PhotoHandler.getInstance().getPhotoEditor().getBrushDrawableMode()) {
                    return;
                }

                if (sticker instanceof TextSticker) {
                    setIconForText(getContext());
                    if (FragmentUtil.getInstance().getManager().findFragmentByTag(Config.FragmentTag.
                            MAIN_BOTTOM_MODIFY_TEXT_FRAGMENT) == null)
                        FragmentUtil.getInstance().add(R.id.bottom, new Main_Bottom_Modify_Text_Fragment(),
                                Config.FragmentTag.MAIN_BOTTOM_MODIFY_TEXT_FRAGMENT);

                    TextOnPhotoHandler.getInstance().setiD(sticker.getKeyManager());
                    TextOnPhotoHandler.getInstance().setSticker(sticker);
                } else {
                    setIconForSticker(getContext());
                    if (FragmentUtil.getInstance().getManager().getBackStackEntryCount() > 0)
                        FragmentUtil.getInstance().getManager().popBackStackImmediate();
                }
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });


        //Setup brush view
        mBrushDrawingView = new BrushDrawingView(getContext());
        mBrushDrawingView.setVisibility(GONE);
        mBrushDrawingView.setId(brushSrcId);
        //Align brush to the size of image view
        RelativeLayout.LayoutParams brushParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        brushParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        brushParam.addRule(RelativeLayout.ALIGN_TOP, imgSrcId);
        brushParam.addRule(RelativeLayout.ALIGN_BOTTOM, imgSrcId);
        brushParam.addRule(RelativeLayout.ALIGN_LEFT, imgSrcId);
        brushParam.addRule(RelativeLayout.ALIGN_RIGHT, imgSrcId);


        //Add image source
        addView(mImgSource, imgSrcParam);

        //Add Gl FilterView
        addView(mImageFilterView, imgFilterParam);

        //Add StickerView
        addView(mStickerView, stickerParam);

        //Add brush view
        addView(mBrushDrawingView, brushParam);
    }

    protected void setIconForText(Context context) {
        //currently you can config your own icons and ic_launcher event
        //the event you can custom
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int size = (int) (((float) displayMetrics.widthPixels / 100.0f) * 7.0f);
        //deleteIcon
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                R.drawable.sticker_ic_close), size, size),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteTextEvent());
        //scaleIcon
        BitmapStickerIcon scaleIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                R.drawable.sticker_ic_scale), size, size),
                BitmapStickerIcon.RIGHT_BOTOM);
        scaleIcon.setIconEvent(new ZoomIconEvent());
        //flipHIcon
        BitmapStickerIcon editTextIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                R.drawable.sticker_ic_edit_text), size, size),
                BitmapStickerIcon.RIGHT_TOP);
        editTextIcon.setIconEvent(new TextEvent());
        //flipBothIcon
        BitmapStickerIcon flipBothIcon =
                new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                        R.drawable.sticker_ic_reset_rotation), size, size),
                        BitmapStickerIcon.LEFT_BOTTOM);
        flipBothIcon.setIconEvent(new ResetRotationEvent());
        //setIcons
        mStickerView.setIcons(Arrays.asList(deleteIcon, scaleIcon, editTextIcon, flipBothIcon));
    }

    protected void setIconForSticker(Context context) {
        //currently you can config your own icons and ic_launcher event
        //the event you can custom
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int size = (int) (((float) displayMetrics.widthPixels / 100.0f) * 7.0f);
        //deleteIcon
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                R.drawable.sticker_ic_close), size, size),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        //scaleIcon
        BitmapStickerIcon scaleIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                R.drawable.sticker_ic_scale), size, size),
                BitmapStickerIcon.RIGHT_BOTOM);
        scaleIcon.setIconEvent(new ZoomIconEvent());
        //flipHIcon
        BitmapStickerIcon flipHIcon = new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                R.drawable.sticker_ic_flip), size, size),
                BitmapStickerIcon.RIGHT_TOP);
        flipHIcon.setIconEvent(new FlipHorizontallyEvent());
        //flipBothIcon
        BitmapStickerIcon resetRotationIcon =
                new BitmapStickerIcon(resizeDrawable(ContextCompat.getDrawable(context,
                        R.drawable.sticker_ic_reset_rotation), size, size),
                        BitmapStickerIcon.LEFT_BOTTOM);
        resetRotationIcon.setIconEvent(new ResetRotationEvent());
        //setIcons
        mStickerView.setIcons(Arrays.asList(deleteIcon, scaleIcon, flipHIcon, resetRotationIcon));
    }

    private Drawable resizeDrawable(Drawable drawable, int size_1, int size_2) {
        return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(),
                size_1, size_2, false));
    }

    public Bitmap getSourceBitmap() {
        return mImgSource.getBitmap();
    }

    /**
     * Source image which you want to edit
     *
     * @return source ImageView
     */
    public ImageView getSource() {
        return mImgSource;
    }

    BrushDrawingView getBrushDrawingView() {
        return mBrushDrawingView;
    }

    public StickerView getStickerView() {
        return mStickerView;
    }

    void saveFilter(@NonNull final OnSaveBitmap onSaveBitmap) {
        if (mImageFilterView.getVisibility() == VISIBLE) {
            mImageFilterView.saveBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(final Bitmap saveBitmap) {
                    Log.e(TAG, "saveFilter: " + saveBitmap);
                    mImgSource.setImageBitmap(saveBitmap);
                    mImageFilterView.setVisibility(GONE);
                    onSaveBitmap.onBitmapReady(saveBitmap);
                }

                @Override
                public void onFailure(Exception e) {
                    onSaveBitmap.onFailure(e);
                }
            });
        } else {
            onSaveBitmap.onBitmapReady(mImgSource.getBitmap());
        }
    }

    void setFilterEffect(PhotoFilter filterType) {
        mImageFilterView.setVisibility(VISIBLE);
        if (!isHasFilter)
            mImageFilterView.setSourceBitmap(mImgSource.getBitmap());
        mImageFilterView.setFilterEffect(filterType);
    }

    void setFilterEffect(CustomEffect customEffect) {
        mImageFilterView.setVisibility(VISIBLE);
        if (!isHasFilter)
            mImageFilterView.setSourceBitmap(mImgSource.getBitmap());
        mImageFilterView.setFilterEffect(customEffect);
    }
}