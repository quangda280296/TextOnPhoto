package com.vmb.chinh_sua_anh.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FocusAwareScrollView extends NestedScrollView {

    private List<OnScrollViewListener> onScrollViewListeners = new ArrayList<>();

    public FocusAwareScrollView(Context context) {
        super(context);
    }

    public FocusAwareScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusAwareScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnScrollViewListener {
        void onScrollChanged(FocusAwareScrollView v, int l, int t, int oldl, int oldt);
    }

    public interface OnViewSeenListener {
        void onViewSeen(View v, int percentageScrolled);
    }

    public void addOnScrollListener(OnScrollViewListener l) {
        onScrollViewListeners.add(l);
    }

    public void removeOnScrollListener(OnScrollViewListener l) {
        onScrollViewListeners.remove(l);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (int i = onScrollViewListeners.size() - 1; i >= 0; i--) {
            onScrollViewListeners.get(i).onScrollChanged(this, l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }

    private boolean handleViewSeenEvent(View view, int scrollBoundsBottom, int scrollYOffset,
                                        float minSeenPercentage, OnViewSeenListener onViewSeenListener) {
        int loc[] = new int[2];
        view.getLocationOnScreen(loc);
        int viewBottomPos = loc[1] - scrollYOffset + (int) (minSeenPercentage / 100 * view.getMeasuredHeight());
        if (viewBottomPos <= scrollBoundsBottom) {
            int scrollViewHeight = this.getChildAt(0).getHeight();
            int viewPosition = this.getScrollY() + view.getScrollY() + view.getHeight();
            int percentageSeen = (int) ((double) viewPosition / scrollViewHeight * 100);
            onViewSeenListener.onViewSeen(view, percentageSeen);
            return true;
        }
        return false;
    }

    public void registerViewSeenCallBack(final ArrayList<View> views, final OnViewSeenListener onViewSeenListener) {

        final boolean[] viewSeen = new boolean[views.size()];

        FocusAwareScrollView.this.postDelayed(new Runnable() {
            @Override
            public void run() {

                final Rect scrollBounds = new Rect();
                FocusAwareScrollView.this.getHitRect(scrollBounds);
                final int loc[] = new int[2];
                FocusAwareScrollView.this.getLocationOnScreen(loc);

                FocusAwareScrollView.this.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

                    boolean allViewsSeen = true;

                    @Override
                    public void onScrollChange(NestedScrollView v, int x, int y, int oldx, int oldy) {

                        for (int index = 0; index < views.size(); index++) {

                            //Change this to adjust criteria
                            float viewSeenPercent = 1;

                            if (!viewSeen[index])
                                viewSeen[index] = handleViewSeenEvent(views.get(index), scrollBounds.bottom, loc[1], viewSeenPercent, onViewSeenListener);

                            if (!viewSeen[index])
                                allViewsSeen = false;
                        }

                        //Remove this if you want continuous callbacks
                        if (allViewsSeen)
                            FocusAwareScrollView.this.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) null);
                    }
                });
            }
        }, 500);
    }
}