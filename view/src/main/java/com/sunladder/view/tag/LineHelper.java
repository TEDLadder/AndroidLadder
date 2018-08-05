package com.sunladder.view.tag;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.View.MeasureSpec;
import com.sunladder.view.tag.MeasureStrategy.MeasureStrategyGroup;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Description:
 * <p>
 * 1.等比拉伸暂不支持
 * 2.列排列暂不支持
 * 3.gravity right 暂不支持
 * <p>
 * Created by Sun Yaozong on 2018/8/5
 */
class LineHelper {

    /**
     * 行数限制
     */
    private static final int MAX_LINE_NOT_SET = -1;

    private final int mMaxLine;

    /**
     * 行排列
     */
    static final int LAYOUT_DIRECTION_ROW = 0;
    static final int LAYOUT_DIRECTION_ROW_REVERSE = 1;

    /**
     * 列排列 暂不支持
     */
    private static final int LAYOUT_DIRECTION_COLUMN = 2;
    private static final int LAYOUT_DIRECTION_COLUMN_REVERSE = 3;

    private final int mLayoutDirection;

    /**
     * 布局策略
     */
    private final MeasureStrategy mDefaultMeasureStrategy = MeasureStrategy.Builder.getDefault();
    private final MeasureStrategyGroup mMeasureStrategyGroup;

    private final LayoutManager mLayoutManager;
    private final Queue<View> mViewQueue;
    private final Rect mParentBounds;
    private final Rect mTotalBounds;
    private final Rect mLineBounds;

    private int mParentWidthMode;
    private int mParentHeightMode;

    private int mLineNum;
    private int mLineViewCount;

    LineHelper(int maxLine, int layoutDirection, LayoutManager layoutManager,
            MeasureStrategyGroup measureStrategyGroup) {
        mMaxLine = maxLine >= 0 ? maxLine : MAX_LINE_NOT_SET;
        mLayoutDirection = layoutDirection >= LAYOUT_DIRECTION_ROW
                && layoutDirection <= LAYOUT_DIRECTION_COLUMN_REVERSE
                ? layoutDirection
                : LAYOUT_DIRECTION_ROW;

        mLayoutManager = layoutManager;
        mMeasureStrategyGroup = measureStrategyGroup;

        mParentBounds = new Rect();
        mTotalBounds = new Rect();
        mLineBounds = new Rect();

        mViewQueue = new LinkedList<>();
    }

    void setParentState(int widthMode, int heightMode, int left, int top, int right, int bottom) {
        reset();
        mParentWidthMode = widthMode;
        mParentHeightMode = heightMode;
        mParentBounds.set(left, top, right, bottom);
    }

    boolean notFull() {
        if (mMaxLine == 0) {
            return false;
        }
        // 高度不限
        boolean heightInfinite = parentHeightInfinite();
        boolean validLine = mMaxLine == MAX_LINE_NOT_SET || mLineNum < mMaxLine;
        return heightInfinite || validLine;
    }

    /**
     * view遍历状态
     * 0 当前行未满，view入队，next
     * 1 当前行超出行宽，需要换行
     * 2 超出控件高度，不再measure layout view
     */
    static final int ADD_STATE_STOP = -1;
    static final int ADD_STATE_NEXT_ITEM = 0;
    static final int ADD_STATE_NEW_LINE = 1;

    int measureChildItem(int index, View view) {
        if (mLayoutManager == null) {
            return ADD_STATE_STOP;
        }
        if (view == null) {
            return ADD_STATE_NEXT_ITEM;
        }

        // 是否需要换行
        final int lineRight = mLineBounds.right;
        final int borderRight = mParentBounds.right;
        if (lineRight >= borderRight) {
            return ADD_STATE_NEW_LINE;
        }

        // 是否需要拉伸，暂时不支持等比拉伸
        mLayoutManager.measureChildWithMargins(view, 0, 0);
        final MeasureStrategy itemStrategy = getItemStrategy(index, mLineNum);
        final int lineHieght = getLineHieght();
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        if (itemStrategy.stretchToLineHeight && measuredHeight < lineHieght) {
            measuredHeight = lineHieght;
        }

        int desireHeight = mLineBounds.top + measuredHeight;
        int restWidth = mParentBounds.right - mLineBounds.right;
        if (desireHeight > mParentBounds.bottom && !parentHeightInfinite()) {
            return ADD_STATE_STOP;
        } else if (measuredWidth > restWidth) {
            switch (itemStrategy.overWidthState) {
                case MeasureStrategy.OVER_WIDTH_RIGHT_TO_END:
                    view.getLayoutParams().width = restWidth;
                    mLayoutManager.measureChildWithMargins(view, 0, 0);
                    break;
                case MeasureStrategy.OVER_WIDTH_JUST_LAYOUT:
                    break;
                default:
                    return ADD_STATE_NEW_LINE;
            }
        }

        mLineBounds.right = mLineBounds.right + measuredWidth;
        mLineBounds.bottom = mLineBounds.top + measuredHeight;

        mViewQueue.add(view);

        return ADD_STATE_NEXT_ITEM;
    }

    void layoutLine() {
        if (mLayoutManager == null) {
            return;
        }

        final int parentWidth = getParentWidth();
        final boolean leftToRight = mLayoutDirection == LAYOUT_DIRECTION_ROW;
        int totalWidth = 0;

        int layoutX = mLineBounds.left;
        while (!mViewQueue.isEmpty()) {
            View item = mViewQueue.poll();
            if (item == null) {
                continue;
            }
            mLayoutManager.addView(item);

            MeasureStrategy itemStrategy = getItemStrategy(
                    mLayoutManager.getPosition(item), mLineNum);
            int measuredWidth = item.getMeasuredWidth();
            int measuredHeight = item.getMeasuredHeight();

            int left, top, right, bottom;
            left = layoutX;
            right = layoutX + measuredWidth;

            // 处理对齐模式
            switch (itemStrategy.alignMode) {
                case MeasureStrategy.ALIGN_MODE_TOP:
                    top = mLineBounds.top;
                    if (itemStrategy.stretchToLineHeight) {
                        bottom = mLineBounds.bottom;
                    } else {
                        bottom = top + measuredHeight;
                    }
                    break;
                case MeasureStrategy.ALIGN_MODE_CENTER:
                    top = getLineCenterY() - measuredHeight / 2;
                    bottom = top + measuredHeight;
                    break;
                case MeasureStrategy.ALIGN_MODE_BOTTOM:
                    bottom = mLineBounds.bottom;
                    top = bottom - measuredHeight;
                    break;
                default:
                    top = mLineBounds.top;
                    bottom = top + measuredHeight;
                    break;
            }
            // 宽高校验
            left = Math.max(left, mParentBounds.left);
            top = Math.max(top, mParentBounds.top);
            right = Math.min(right, mParentBounds.right);
            bottom = Math.min(bottom, mParentBounds.bottom);

            totalWidth += (right - left);
            if (!leftToRight) {
                int extra = parentWidth - totalWidth;
                left = left + extra;
                right = right + extra;
            }

            mLayoutManager.layoutDecoratedWithMargins(item, left, top, right, bottom);
            layoutX = right;
        }

        nextLine();
    }

    void recycle() {
        mParentWidthMode = 0;
        mParentHeightMode = 0;

        mParentBounds.setEmpty();
        mTotalBounds.setEmpty();
        mLineBounds.setEmpty();

        mLineNum = 0;
        mLineViewCount = 0;
        mViewQueue.clear();
    }

    private void reset() {
        recycle();
    }

    private void nextLine() {
        mLineNum++;
        mLineBounds.left = mParentBounds.left;
        mLineBounds.top = mLineBounds.bottom;
        mLineBounds.right = mLineBounds.left;
    }

    private boolean parentHeightInfinite() {
        return mParentHeightMode == MeasureSpec.UNSPECIFIED;
    }

    private int getParentWidth() {
        return mParentBounds.right - mParentBounds.left;
    }

    private int getParentHeight() {
        return mParentBounds.bottom - mParentBounds.top;
    }

    private int getLineHieght() {
        return mLineBounds.bottom - mLineBounds.top;
    }

    private int getLineCenterY() {
        return (mLineBounds.top + mLineBounds.bottom) / 2;
    }

    @NonNull
    private MeasureStrategy getItemStrategy(int index, int lineNum) {
        MeasureStrategy itemStrategy = null;
        if (mMeasureStrategyGroup != null) {
            itemStrategy = mMeasureStrategyGroup.getItemStrategy(index);
            if (itemStrategy == null) {
                itemStrategy = mMeasureStrategyGroup.getLineStrategy(lineNum);
            }
            if (itemStrategy == null) {
                itemStrategy = mMeasureStrategyGroup.getGlobalStrategy();
            }
        }
        return itemStrategy != null ? itemStrategy : mDefaultMeasureStrategy;
    }
}
