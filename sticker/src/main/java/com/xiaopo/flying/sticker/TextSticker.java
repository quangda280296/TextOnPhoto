package com.xiaopo.flying.sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

public class TextSticker extends Sticker {

    private static final String TAG = "TextSticker";
    private static final String mEllipsis = "…";
    private Alignment alignment;
    private final Context context;
    private BackgroundDrawable drawable;
    private boolean isEnableStroke;
    private int line;
    private float lineSpacingExtra;
    private float lineSpacingMultiplier;
    private BackgroundDrawable mBackup;
    private int mStrokeColor;
    private float mStrokeWidth;
    private int mTextColor;
    private float maxTextSizePixels;
    private boolean minTextSize;
    private float minTextSizePixels;
    private final Rect realBounds;
    private float realTextSize;
    private StaticLayout staticLayout;
    private CharSequence text;
    private final TextPaint textPaint;
    private final Rect textRect;

    public TextSticker(@NonNull Context context) {
        this(context, null);
    }

    public TextSticker(@NonNull Context context, @Nullable BackgroundDrawable backgroundDrawable) {
        this.line = 1;
        this.realTextSize = 35.0f;
        this.minTextSize = false;
        this.lineSpacingMultiplier = 1.0f;
        this.lineSpacingExtra = 0.0f;
        this.isEnableStroke = false;
        this.mStrokeWidth = 0.0f;
        this.mStrokeColor = 0;
        this.mTextColor = 0;
        this.context = context;
        this.drawable = backgroundDrawable;
        if (backgroundDrawable == null) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_background);
            backgroundDrawable = new BackgroundDrawable();
            backgroundDrawable.setSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            backgroundDrawable.setColor(0);
            this.drawable = backgroundDrawable;
        }
        this.textPaint = new TextPaint(1);
        this.realBounds = new Rect(0, 0, getWidth(), getHeight());
        this.textRect = new Rect(0, 0, getWidth(), getHeight());
        this.minTextSizePixels = convertSpToPx(2.0f);
        this.maxTextSizePixels = convertSpToPx(32.0f);
        this.alignment = Alignment.ALIGN_CENTER;
        this.textPaint.setTextSize(this.maxTextSizePixels);
    }

    public void draw(@NonNull Canvas canvas) {
        Matrix matrix = getMatrix();
        canvas.save();
        canvas.concat(matrix);
        if (this.drawable != null) {
            this.drawable.setBounds(this.realBounds);
            this.drawable.draw(canvas);
        }
        canvas.restore();
        if (this.staticLayout != null) {
            if (this.isEnableStroke) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("DRAW STROKE:");
                stringBuilder.append(this.mStrokeWidth);
                Log.d(str, stringBuilder.toString());
                canvas.save();
                canvas.concat(matrix);
                if (this.mStrokeWidth != 0.0f) {
                    this.textPaint.setAntiAlias(true);
                    this.textPaint.setStyle(Style.STROKE);
                    this.textPaint.setStrokeJoin(Join.ROUND);
                    this.textPaint.setStrokeMiter(0.0f);
                    setTextColor(this.mStrokeColor);
                    this.textPaint.setStrokeWidth(this.mStrokeWidth);
                    this.staticLayout.draw(canvas);
                }
                canvas.restore();
                canvas.save();
                canvas.concat(matrix);
                this.textPaint.setStyle(Style.FILL);
                setTextColor(this.mTextColor);
                this.staticLayout.draw(canvas);
                canvas.restore();
            } else {
                canvas.save();
                canvas.concat(matrix);
                if (this.textRect.width() == getWidth()) {
                    canvas.translate(0.0f, (float) ((getHeight() / 2) - (this.staticLayout.getHeight() / 2)));
                } else {
                    canvas.translate((float) this.textRect.left, (float) ((this.textRect.top + (this.textRect.height() / 2)) - (this.staticLayout.getHeight() / 2)));
                }
                this.staticLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    @NonNull
    public TextSticker setAlpha(@IntRange(from = 0, to = 255) int i) {
        this.textPaint.setAlpha(i);
        return this;
    }

    @NonNull
    public Drawable getDrawable() {
        return this.drawable;
    }

    public TextSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = (BackgroundDrawable) drawable;
        this.realBounds.set(0, 0, getWidth(), getHeight());
        this.textRect.set(0, 0, getWidth(), getHeight());
        return this;
    }

    public BackgroundDrawable getBackgroundDrawable() {
        return this.mBackup;
    }

    public TextSticker setBackgroundDrawable(@NonNull BackgroundDrawable backgroundDrawable) {
        this.drawable = backgroundDrawable;
        this.mBackup = backgroundDrawable;
        this.realBounds.set(0, 0, getWidth(), getHeight());
        this.textRect.set(0, 0, getWidth(), getHeight());
        return this;
    }

    @NonNull
    public TextSticker setDrawable(@NonNull Drawable drawable, @Nullable Rect rect) {
        this.drawable = (BackgroundDrawable) drawable;
        this.realBounds.set(0, 0, getWidth(), getHeight());
        if (rect == null) {
            this.textRect.set(0, 0, getWidth(), getHeight());
        } else {
            this.textRect.set(rect.left, rect.top, rect.right, rect.bottom);
        }
        return this;
    }

    @NonNull
    public TextSticker setTypeface(@Nullable Typeface typeface) {
        this.textPaint.setTypeface(typeface);
        return this;
    }

    @NonNull
    public TextSticker setTextColor(@ColorInt int i) {
        this.textPaint.setColor(i);
        return this;
    }

    @NonNull
    public TextSticker setTextAlign(@NonNull Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @NonNull
    public TextSticker setMaxTextSize(@Dimension(unit = 2) float f) {
        this.minTextSize = false;
        this.realTextSize = f;
        this.textPaint.setTextSize(convertSpToPx(f));
        this.maxTextSizePixels = this.textPaint.getTextSize();
        return this;
    }

    @NonNull
    public TextSticker setMinTextSize(float f) {
        this.minTextSizePixels = convertSpToPx(f);
        return this;
    }

    @NonNull
    public TextSticker setLineSpacing(float f, float f2) {
        this.lineSpacingMultiplier = f2;
        this.lineSpacingExtra = f;
        return this;
    }

    @NonNull
    public TextSticker setText(@Nullable CharSequence charSequence) {
        this.text = charSequence;
        return this;
    }

    @Nullable
    public CharSequence getText() {
        return this.text;
    }

    @NonNull
    public TextSticker resizeText() {
        int height = this.textRect.height();
        int width = this.textRect.width();
        CharSequence text = getText();
        if (text != null && text.length() > 0 && height > 0 && width > 0) {
            if (this.maxTextSizePixels > 0.0f) {
                float f = this.maxTextSizePixels;
                int textHeightPixels = getTextHeightPixels(text, width, f);
                while (textHeightPixels > height && f > this.minTextSizePixels) {
                    f = Math.max(f - 2.0f, this.minTextSizePixels);
                    textHeightPixels = getTextHeightPixels(text, width, f);
                }
                if (f >= this.minTextSizePixels || textHeightPixels <= height) {
                    this.textPaint.setTextSize(f);
                    this.staticLayout = new StaticLayout(this.text, this.textPaint, this.textRect.width(),
                            this.alignment, this.lineSpacingMultiplier, this.lineSpacingExtra, true);
                    return this;
                }
                this.minTextSize = true;
                Log.d(TAG, "TRIM TRIM 0000");
                return this;
            }
        }
        return this;
    }

    /**
     * Resize this view's text size with respect to its width and height
     * (minus padding). You should always call this method after the initialization.
     */
    /*@NonNull
    public TextSticker resizeText() {
        final int availableHeightPixels = textRect.height();

        final int availableWidthPixels = textRect.width();

        final CharSequence text = getText();

        // Safety check
        // (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null
                || text.length() <= 0
                || availableHeightPixels <= 0
                || availableWidthPixels <= 0
                || maxTextSizePixels <= 0) {
            return this;
        }

        float targetTextSizePixels = maxTextSizePixels;
        int targetTextHeightPixels =
                getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels);

        // Until we either fit within our TextView
        // or we have reached our minimum text size,
        // incrementally try smaller sizes
        while (targetTextHeightPixels > availableHeightPixels
                && targetTextSizePixels > minTextSizePixels) {
            targetTextSizePixels = Math.max(targetTextSizePixels - 2, minTextSizePixels);

            targetTextHeightPixels =
                    getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels);
        }

        // If we have reached our minimum text size and the text still doesn't fit,
        // append an ellipsis
        // (NOTE: Auto-ellipsize doesn't work hence why we have to do it here)
        if (targetTextSizePixels == minTextSizePixels
                && targetTextHeightPixels > availableHeightPixels) {
            // Make a copy of the original TextPaint object for measuring
            TextPaint textPaintCopy = new TextPaint(textPaint);
            textPaintCopy.setTextSize(targetTextSizePixels);

            // Measure using a StaticLayout instance
            StaticLayout staticLayout =
                    new StaticLayout(text, textPaintCopy, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                            lineSpacingMultiplier, lineSpacingExtra, false);

            // Check that we have a least one line of rendered text
            if (staticLayout.getLineCount() > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line and add an ellipsis
                int lastLine = staticLayout.getLineForVertical(availableHeightPixels) - 1;

                if (lastLine >= 0) {
                    int startOffset = staticLayout.getLineStart(lastLine);
                    int endOffset = staticLayout.getLineEnd(lastLine);
                    float lineWidthPixels = staticLayout.getLineWidth(lastLine);
                    float ellipseWidth = textPaintCopy.measureText(mEllipsis);

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--;
                        lineWidthPixels =
                                textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString());
                    }

                    setText(text.subSequence(0, endOffset) + mEllipsis);
                }
            }
        }
        textPaint.setTextSize(targetTextSizePixels);
        staticLayout =
                new StaticLayout(this.text, textPaint, textRect.width(), alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);
        return this;
    }*/
    public float getMinTextSizePixels() {
        return this.minTextSizePixels;
    }

    protected int getTextHeightPixels(@NonNull CharSequence charSequence, int i, float f) {
        this.textPaint.setTextSize(f);
        return new StaticLayout(charSequence, this.textPaint, i, Alignment.ALIGN_NORMAL, this.lineSpacingMultiplier, this.lineSpacingExtra, true).getHeight();
    }

    private float convertSpToPx(float f) {
        return f * this.context.getResources().getDisplayMetrics().scaledDensity;
    }

    public TextPaint getTextPaint() {
        return this.textPaint;
    }

    public TextSticker setShadowLayer(float f, float f2, float f3, int i) {
        this.textPaint.setShadowLayer(f, f2, f3, i);
        return this;
    }

    public void clearShadowLayer() {
        this.textPaint.clearShadowLayer();
    }

    public void setShader(Shader shader) {
        this.textPaint.setShader(shader);
    }

    public void clearShader() {
        this.textPaint.setShader(null);
    }

    public TextSticker setStroke(float f, int i, int i2) {
        this.isEnableStroke = true;
        this.mStrokeColor = i;
        this.mStrokeWidth = f;
        this.mTextColor = i2;
        return this;
    }

    public void clearStroke(int i) {
        this.isEnableStroke = false;
        this.mStrokeColor = 0;
        this.mStrokeWidth = 0.0f;
        this.mTextColor = i;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int i) {
        this.line = i;
    }

    public float getRealTextSize() {
        return this.realTextSize;
    }

    public boolean isMinTextSize() {
        return this.minTextSize;
    }
}