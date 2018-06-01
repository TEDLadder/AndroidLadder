package com.sunladder.view.pic.display;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.ViewGroup;

class FacebookLayoutManager extends RecyclerView.LayoutManager {

    private final int mDividerWidth;
    private boolean mLandscape;

    FacebookLayoutManager(int dividerWidth) {
        mDividerWidth = Math.max(0, dividerWidth);
        setAutoMeasureEnabled(true);
    }

    public void setLandscape(boolean landscape) {
        mLandscape = landscape;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        removeAllViews();
        detachAndScrapAttachedViews(recycler);

        int itemCount = getItemCount();
        if (itemCount <= 1) {
            layoutOne(recycler, state);
        } else {
            if (mLandscape) {
                layoutLandscape(recycler, state);
            } else {
                layoutProtrait(recycler, state);
            }
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        removeAndRecycleAllViews(recycler);
        recycler.clear();
    }

    void layoutOne(Recycler recycler, State state) {
    }

    void layoutLandscape(Recycler recycler, State state) {
    }

    void layoutProtrait(Recycler recycler, State state) {
    }

    void handleFirstGroup() {
    }

    void handleSecondGroup() {
    }
}
