package com.sunladder.view.tantanbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sun on 2018/5/23.
 */

public class CardLayoutManager extends RecyclerView.LayoutManager {

    private static final float SCALE_THRESHOLD = 0.65F;
    private static final float SCALE_STEP = 0.12F;

    private static final int TRANSLATION_THRESHOLD = 100;
    private static final int TRANSLATION_STEP = 20;

    private static final int MAX_SHOW_COUNT = 3;

    private final int mMaxShowCount;

    public CardLayoutManager() {
        this(MAX_SHOW_COUNT);
    }

    public CardLayoutManager(int showCount) {
        this.mMaxShowCount = showCount;
    }

    @Override

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        removeAllViews();

        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        detachAndScrapAttachedViews(recycler);

        int itemCount = getItemCount();
        int lastIndex = mMaxShowCount - 1;
        for (int index = lastIndex; index >= 0; index--) {
            View childView = recycler.getViewForPosition(index);
            addView(childView);
            measureChildWithMargins(childView, 0, 0);


            // TODO: 2018/5/23 考虑padiing
            int width = getWidth();
            int measuredWidth = getDecoratedMeasuredWidth(childView);
            int left = (width - measuredWidth) / 2;
            int right = left + measuredWidth;

            int height = getHeight();
            int measuredHeight = getDecoratedMeasuredHeight(childView);
            int top = (height - measuredHeight) / 2;
            int bottom = top + measuredHeight;

            layoutDecoratedWithMargins(childView, left, top, right, bottom);

            // TODO: 2018/5/23 属性配置
            float scale = 1 - index * SCALE_STEP;
            scale = scale > SCALE_THRESHOLD ? scale : SCALE_THRESHOLD;
            childView.setScaleX(scale);
//            childView.setScaleY(scale);

            int translation = index * TRANSLATION_STEP;
            translation = translation < TRANSLATION_THRESHOLD ? translation : TRANSLATION_THRESHOLD;
            childView.setTranslationY(translation);

            childView.setOnTouchListener(index == 0 ? mTouchListener : null);
        }
    }

    private final View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };
}
