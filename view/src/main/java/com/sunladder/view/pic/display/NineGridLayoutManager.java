package com.sunladder.view.pic.display;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sun on 2018/5/27.
 * <p>
 * TODO:1.recycler回收
 */

class NineGridLayoutManager extends RecyclerView.LayoutManager {

    private final int mDividerWidth;

    NineGridLayoutManager(int dividerWidth) {
        mDividerWidth = Math.max(0, dividerWidth);
        setAutoMeasureEnabled(true);
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
        if (itemCount == 1) {
            layoutOne(recycler, state);
        } else if (itemCount == 4) {
            layoutFour(recycler, state);
        } else {
            layoutNormal(recycler, state);
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        removeAndRecycleAllViews(recycler);
        recycler.clear();
    }

    /**
     * 单图 宽占屏幕二分之一
     */
    void layoutOne(RecyclerView.Recycler recycler, RecyclerView.State state) {
        View childView = recycler.getViewForPosition(0);
        if (childView != null) {
            final int paddingLeft = getPaddingLeft();
            final int itemWidth = (getWidth() - paddingLeft - getPaddingRight()) / 2;

            ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
            layoutParams.width = itemWidth;
            addView(childView);

            measureChildWithMargins(childView, 0, 0);

            int left = paddingLeft;
            int right = left + itemWidth;
            int top = getPaddingTop();
            int bottom = top + childView.getMeasuredHeight();

            layoutDecoratedWithMargins(childView, left, top, right, bottom);
        }
    }

    /**
     * 4图 两行两列 宽占屏幕三分之二
     */
    void layoutFour(RecyclerView.Recycler recycler, RecyclerView.State state) {
        handleChild(recycler, state, 4, 3, 2);
    }

    /**
     * 正常九宫格排列
     */
    void layoutNormal(RecyclerView.Recycler recycler, RecyclerView.State state) {
        handleChild(recycler, state, Math.min(9, getItemCount()), 3, 3);
    }

    private void handleChild(RecyclerView.Recycler recycler, RecyclerView.State state,
            final int maxSize, final int columnSize, final int showNumPerLine) {
        if (showNumPerLine > columnSize) {
            throw new RuntimeException("showNumPerLine cannot be more than columnSize!");
        }
        //分割线
        int dividerWidth = mDividerWidth;
        //基本数据准备
        int width = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        //长度矫正,如有剩余作居中处理
        int usefulWidth = width - paddingLeft - paddingRight - (columnSize - 1) * dividerWidth;
        int itemWidth = (usefulWidth) / columnSize;
        int remainWidth = usefulWidth - itemWidth * columnSize;
        remainWidth = remainWidth % 2 == 0 ? remainWidth : remainWidth + 1;

        //边界值
        final int borderLeft = paddingLeft + remainWidth / 2;
        final int borderRight = getWidth() - paddingRight - remainWidth / 2;

        int layoutLeft = borderLeft;
        int layoutRight = 0;
        int layoutTop = paddingTop;
        int layoutBottom = 0;

        for (int i = 0, column; i < maxSize; i++, layoutLeft = layoutRight + dividerWidth) {
            View childView = recycler.getViewForPosition(0);
            if (childView == null) {
                continue;
            }
            int width1 = childView.getWidth();
            int height1 = childView.getHeight();
            //改变childView宽度
            ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
            layoutParams.width = itemWidth;
            layoutParams.height = itemWidth;
            addView(childView);
            measureChild(childView, 0, 0);
            int measuredHeight = childView.getMeasuredHeight();

            //布局位置计算
            column = i % showNumPerLine;
            if (column == 0) {
                layoutLeft = borderLeft;
                if (i > 0) {
                    layoutTop = layoutBottom + dividerWidth;
                }
                layoutBottom = layoutTop + measuredHeight;
            }
            layoutRight = layoutLeft + itemWidth;
            if (column == columnSize - 1) {
                layoutRight = Math.min(layoutRight, borderRight);
            }
            layoutDecorated(childView, layoutLeft, layoutTop, layoutRight, layoutBottom);
        }
    }
}
