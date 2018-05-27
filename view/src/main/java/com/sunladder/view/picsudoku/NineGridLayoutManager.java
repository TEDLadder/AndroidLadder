package com.sunladder.view.picsudoku;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sun on 2018/5/27.
 */

public class NineGridLayoutManager extends RecyclerView.LayoutManager {

    private final int mPicCount;
    private int mDividerWidth = 15;

    public NineGridLayoutManager(int picCount) {
        mPicCount = picCount;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // TODO: 2018/5/27 弄明白recyclerview回收
        removeAllViews();
        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        detachAndScrapAttachedViews(recycler);

        int itemCount = getItemCount();
        if (mPicCount == 1 && itemCount >= 1) {
            layoutOne(recycler, state);
        } else if (mPicCount == 4 && itemCount >= 4) {
            layoutFour(recycler, state);
        } else {
            layoutNormal(recycler, state);
        }
    }

    /**
     * 单图 宽占屏幕二分之一
     */
    protected void layoutOne(RecyclerView.Recycler recycler, RecyclerView.State state) {
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
    protected void layoutFour(RecyclerView.Recycler recycler, RecyclerView.State state) {
        handleChild(recycler, state, 4, 3, 2);
    }

    /**
     * 正常九宫格排列
     */
    protected void layoutNormal(RecyclerView.Recycler recycler, RecyclerView.State state) {
        handleChild(recycler, state, Math.min(9, getItemCount()), 3, 3);
    }

    private void handleChild(RecyclerView.Recycler recycler, RecyclerView.State state, final int maxSize, final int columnSize, final int realSize) {
        final int dividerWidth = mDividerWidth;
        final int partDividerWidth = dividerWidth / (columnSize - 1);

        final int width = getWidth();

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        final int itemWidth = (width - paddingLeft - paddingRight) / columnSize;

        int layoutTop = getPaddingTop();
        int layoutBottom = 0;

        for (int i = 0; i < maxSize; i++) {
            View childView = recycler.getViewForPosition(0);
            if (childView == null) {
                continue;
            }
            ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
            layoutParams.width = itemWidth;
            addView(childView);
            measureChild(childView, 0, 0);
            int measuredHeight = childView.getMeasuredHeight();

            int column = i % realSize;

            boolean noLeftHalfDivider = column == 0;
            boolean noRightHalfDivider = column == (columnSize - 1);
            if (column == 0 && i > 0) {
                layoutTop = layoutBottom + dividerWidth;
            }

            int left = paddingLeft + column * itemWidth + (noLeftHalfDivider ? 0 : partDividerWidth);
            int right = left + itemWidth - (noRightHalfDivider ? 0 : partDividerWidth);
            if (column == 0) {
                layoutBottom = layoutTop + measuredHeight;
            }
            layoutDecorated(childView, left, layoutTop, right, layoutBottom);
        }
    }

//    private void handleChild(final int maxSize, final int columnSize) {
//        final int dividerWidth = mDividerWidth;
//
//        final int width = getWidth();
//
//        final int paddingLeft = getPaddingLeft();
//        final int paddingRight = getPaddingRight();
//
//        final int stepX = (width - paddingLeft - paddingRight - (columnSize - 1) * dividerWidth) / columnSize;
//
//        int layoutX = 0;
//        int layoutY = 0;
//        int nextY = 0;
//
//        for (int i = 0; i < maxSize; i++) {
//            View childView = getChildAt(0);
//            if (childView == null) {
//                continue;
//            }
//            int column = i % columnSize;
//            if (column == 0) {
//                layoutX = paddingLeft;
//                layoutY = nextY + dividerWidth;
//            }
//
//            measureChild(childView, 0, 0);
//            int measuredHeight = childView.getMeasuredHeight();
//
//            if (column == 0) {
//                nextY = layoutY + measuredHeight;
//            }
//            layoutDecorated(childView, layoutX, layoutY, layoutX + stepX, nextY);
//            layoutX += dividerWidth;
//        }
//    }

}
