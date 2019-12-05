package com.vmb.chinh_sua_anh.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vm.TextOnPhoto.PhotoEditor.R;

public abstract class StickerImage extends FrameLayout {

    public static final String TAG = "StickerImage()";
    private BorderView iv_border;
    ImageView iv_delete;
    ImageView iv_flip;
    ImageView iv_reset;
    ImageView iv_scale;

    // For scalling
    private float this_orgX = -1, this_orgY = -1;
    private float scale_orgX = -1, scale_orgY = -1;
    private double scale_orgWidth = -1, scale_orgHeight = -1;
    // For rotating
    private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;
    // For moving
    private float move_orgX = -1, move_orgY = -1;

    private double centerX, centerY;

    private final static int BUTTON_SIZE_DP = 30;
    private final static int SELF_SIZE_DP = 100;

    public StickerImage(Context context) {
        super(context);
        init(context);
    }

    public StickerImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.iv_border = new BorderView(context);
        this.iv_delete = new ImageView(context);
        this.iv_flip = new ImageView(context);
        this.iv_reset = new ImageView(context);
        this.iv_scale = new ImageView(context);

        this.iv_delete.setImageResource(R.drawable.sticker_ic_close);
        this.iv_flip.setImageResource(R.drawable.sticker_ic_flip);
        this.iv_reset.setImageResource(R.drawable.sticker_ic_reset_rotation);
        this.iv_scale.setImageResource(R.drawable.sticker_ic_scale);

        this.setId(android.R.id.widget_frame);
        this.setTag("DraggableViewGroup");
        this.iv_border.setTag("iv_border");
        this.iv_delete.setTag("iv_delete");
        this.iv_flip.setTag("iv_flip");
        this.iv_reset.setTag("iv_reset");
        this.iv_scale.setTag("iv_scale");

        int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;
        int size = convertDpToPixel(SELF_SIZE_DP, getContext());

        RelativeLayout.LayoutParams this_params = new RelativeLayout.LayoutParams(size, size);
        this_params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        LayoutParams iv_main_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iv_main_params.setMargins(margin, margin, margin, margin);

        LayoutParams iv_border_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iv_border_params.setMargins(margin, margin, margin, margin);

        // Button
        LayoutParams iv_delete_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_delete_params.gravity = Gravity.TOP | Gravity.LEFT;

        LayoutParams iv_flip_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_flip_params.gravity = Gravity.TOP | Gravity.RIGHT;

        LayoutParams iv_reset_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_reset_params.gravity = Gravity.BOTTOM | Gravity.LEFT;

        LayoutParams iv_scale_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_scale_params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        // set
        this.setLayoutParams(this_params);
        this.addView(getMainView(), iv_main_params);
        this.addView(iv_border, iv_border_params);
        this.addView(iv_delete, iv_delete_params);
        this.addView(iv_flip, iv_flip_params);
        this.addView(iv_reset, iv_reset_params);
        this.addView(iv_scale, iv_scale_params);

        this.setOnTouchListener(mTouchListener);
        this.iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StickerImage.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) StickerImage.this.getParent());
                    myCanvas.removeView(StickerImage.this);
                }
            }
        });
        this.iv_flip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "flip the view");

                View mainView = getMainView();
                mainView.setRotationY(mainView.getRotationY() == -180f ? 0f : -180f);
                mainView.invalidate();
                requestLayout();
            }
        });
        this.iv_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setRotation(0.0f);
            }
        });
        this.iv_scale.setOnTouchListener(mTouchListener);
    }

    public boolean isFlip() {
        return getMainView().getRotationY() == -180f;
    }

    protected abstract View getMainView();

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (view.getTag().equals("DraggableViewGroup")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "sticker view action down");
                        move_orgX = event.getRawX();
                        move_orgY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v(TAG, "sticker view action move");
                        float offsetX = event.getRawX() - move_orgX;
                        float offsetY = event.getRawY() - move_orgY;
                        StickerImage.this.setX(StickerImage.this.getX() + offsetX);
                        StickerImage.this.setY(StickerImage.this.getY() + offsetY);
                        move_orgX = event.getRawX();
                        move_orgY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v(TAG, "sticker view action up");
                        break;
                }
            } else if (view.getTag().equals("iv_scale")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "iv_scale action down");

                        this_orgX = StickerImage.this.getX();
                        this_orgY = StickerImage.this.getY();

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();
                        scale_orgWidth = StickerImage.this.getLayoutParams().width;
                        scale_orgHeight = StickerImage.this.getLayoutParams().height;

                        rotate_orgX = event.getRawX();
                        rotate_orgY = event.getRawY();

                        centerX = StickerImage.this.getX() +
                                ((View) StickerImage.this.getParent()).getX() +
                                (float) StickerImage.this.getWidth() / 2;


                        //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
                        int result = 0;
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            result = getResources().getDimensionPixelSize(resourceId);
                        }
                        double statusBarHeight = result;
                        centerY = StickerImage.this.getY() +
                                ((View) StickerImage.this.getParent()).getY() +
                                statusBarHeight +
                                (float) StickerImage.this.getHeight() / 2;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v(TAG, "iv_scale action move");

                        rotate_newX = event.getRawX();
                        rotate_newY = event.getRawY();

                        double angle_diff = Math.abs(
                                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                                        - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

                        Log.v(TAG, "angle_diff: " + angle_diff);

                        double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
                        double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

                        int size = convertDpToPixel(SELF_SIZE_DP, getContext());
                        if (length2 > length1
                                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                                ) {
                            //scale up
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            StickerImage.this.getLayoutParams().width += offset;
                            StickerImage.this.getLayoutParams().height += offset;
                            onScaling(true);
                            //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
                            //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
                        } else if (length2 < length1
                                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                                && StickerImage.this.getLayoutParams().width > size / 2
                                && StickerImage.this.getLayoutParams().height > size / 2) {
                            //scale down
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            StickerImage.this.getLayoutParams().width -= offset;
                            StickerImage.this.getLayoutParams().height -= offset;
                            onScaling(false);
                        }

                        //rotate_left

                        double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                        Log.v(TAG, "log angle: " + angle);

                        //setRotation
                        float distanceX = rotate_newX - scale_orgX;
                        float distanceY = rotate_newY - scale_orgY;
                        Log.i(TAG, "distanceX = " + distanceX);
                        Log.i(TAG, "distanceY = " + distanceY);

                        if (distanceX != 0.0 && distanceY != 0.0)
                            setRotation((float) angle - 45);
                        Log.v(TAG, "getRotation(): " + getRotation());

                        onRotating();

                        rotate_orgX = rotate_newX;
                        rotate_orgY = rotate_newY;

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();

                        postInvalidate();
                        requestLayout();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v(TAG, "iv_scale action up");
                        break;
                }
            }
            return true;
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    private float[] getRelativePos(float absX, float absY) {
        Log.v("ken", "getRelativePos getX:" + ((View) this.getParent()).getX());
        Log.v("ken", "getRelativePos getY:" + ((View) this.getParent()).getY());
        float[] pos = new float[]{
                absX - ((View) this.getParent()).getX(),
                absY - ((View) this.getParent()).getY()
        };
        Log.v(TAG, "getRelativePos absY:" + absY);
        Log.v(TAG, "getRelativePos relativeY:" + pos[1]);
        return pos;
    }

    /*public void setControlItemsHidden(boolean isHidden) {
        if (isHidden) {
            iv_border.setVisibility(View.INVISIBLE);
            iv_scale.setVisibility(View.INVISIBLE);
            iv_delete.setVisibility(View.INVISIBLE);
            iv_flip.setVisibility(View.INVISIBLE);
        } else {
            iv_border.setVisibility(View.VISIBLE);
            iv_scale.setVisibility(View.VISIBLE);
            iv_delete.setVisibility(View.VISIBLE);
            iv_flip.setVisibility(View.VISIBLE);
        }
    }*/

    protected View getImageViewFlip() {
        return iv_flip;
    }

    protected void onScaling(boolean scaleUp) {
    }

    protected void onRotating() {
    }

    private class BorderView extends View {

        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BorderView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw sticker border

            LayoutParams params = (LayoutParams) this.getLayoutParams();

            Log.v(TAG, "params.leftMargin: " + params.leftMargin);

            Rect border = new Rect();
            border.left = (int) this.getLeft() - params.leftMargin;
            border.top = (int) this.getTop() - params.topMargin;
            border.right = (int) this.getRight() - params.rightMargin;
            border.bottom = (int) this.getBottom() - params.bottomMargin;
            Paint borderPaint = new Paint();
            borderPaint.setStrokeWidth(10);
            borderPaint.setColor(getContext().getResources().getColor(R.color.purple_lv_3));
            borderPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(border, borderPaint);
        }
    }

    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public void setControlsVisibility(boolean isVisible) {
        if (!isVisible) {
            iv_border.setVisibility(View.GONE);
            iv_delete.setVisibility(View.GONE);
            iv_flip.setVisibility(View.GONE);
            iv_reset.setVisibility(View.GONE);
            iv_scale.setVisibility(View.GONE);
        } else {
            iv_border.setVisibility(View.VISIBLE);
            iv_delete.setVisibility(View.VISIBLE);
            iv_flip.setVisibility(View.VISIBLE);
            iv_reset.setVisibility(View.VISIBLE);
            iv_scale.setVisibility(View.VISIBLE);
        }
    }
}