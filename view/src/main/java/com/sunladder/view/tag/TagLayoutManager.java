package com.sunladder.view.tag;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import com.sunladder.view.tag.MeasureStrategy.Builder;
import com.sunladder.view.tag.MeasureStrategy.MeasureStrategyGroup;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/2
 */
public class TagLayoutManager extends RecyclerView.LayoutManager {

    /*方向*/
    boolean mReverse;

    /*行数*/
    private int mMaxLine;

    /*对齐策略*/
    private final MeasureStrategy mDefaultMeasureStrategy;
    private final MeasureStrategyGroup mMeasureStrategyConfig;

    private int borderLeft;
    private int borderRight;
    private int borderTop;
    private int borderBottom;

    private TagLines mTagLine;

    public TagLayoutManager() {
        this(1, false);
    }

    public TagLayoutManager(int maxLine, boolean reverse) {
        this(maxLine, reverse, null);
    }

    public TagLayoutManager(int maxLine, boolean reverse,
            MeasureStrategyGroup measureStrategyConfig) {

        this.mMaxLine = maxLine;
        this.mReverse = reverse;

        mTagLine = new TagLines();
        mMeasureStrategyConfig = measureStrategyConfig;
        mDefaultMeasureStrategy = Builder.getDefault();
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(Recycler recycler, State state) {
        detachAndScrapAttachedViews(recycler);

        borderLeft = getPaddingLeft();
        borderRight = getWidth() - getPaddingRight();
        borderTop = getPaddingTop();
        borderBottom = getHeight() - getPaddingBottom();

        mTagLine.init();

        int index = 0;
        while (index < getItemCount() && mTagLine.lineCount < mMaxLine) {
            View itemView = recycler.getViewForPosition(index);
            measureChildWithMargins(itemView, 0, 0);
            switch (mTagLine.addOne(index, itemView)) {
                case STATE_OVER_WIDTH:
                    //换行 queue中view进行布局
                    mTagLine.layout(this);
                    continue;
                case STATE_OVER_HEIGHT:
                    //超出高度 不再继续
                    mTagLine.recycle();
                    return;
                default:
                    //当前行不变，view继续入队
                    index++;
                    break;
            }
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
    }

    /**
     * view遍历状态
     * 0 当前行未满，view入队，next
     * 1 当前行超出行宽，需要换行
     * 2 超出控件高度，不再measure layout view
     */
    public static final int STATE_NEXT = 0;
    public static final int STATE_OVER_WIDTH = 1;
    public static final int STATE_OVER_HEIGHT = 2;

    private class TagLines {

        int lineCount;

        int lineLeft;
        int lineRight;
        int lineTop;
        int lineBottom;

        boolean outerNewLineRequested;

        final Queue<View> queue = new LinkedList<>();

        int addOne(int index, View view) {
            if (view == null) {
                return STATE_NEXT;
            }

            MeasureStrategy itemStrategy = getItemStrategy(index, lineCount);
            if (lineRight >= borderRight) {
                return STATE_OVER_WIDTH;
            }
            if (outerRequestNewLine(index)) {
                if (outerNewLineRequested) {
                    outerNewLineRequested = false;
                } else {
                    outerNewLineRequested = true;
                    return STATE_OVER_WIDTH;
                }
            }

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            int lineHieght = getLineHieght();
            // 暂时不支持等比拉伸
            if (itemStrategy.stretchToLineHeight && measuredHeight < lineHieght) {
                measuredHeight = lineHieght;
            }

            int desireWidth = lineRight + measuredWidth;
            int desireHeight = Math.max(lineBottom, lineTop + measuredHeight);

            if (desireWidth > borderRight) {
                switch (itemStrategy.overWidthState) {
                    case MeasureStrategy.OVER_WIDTH_RIGHT_TO_END:
                        desireWidth = borderRight;
                        view.getLayoutParams().width = borderRight - lineRight;
                        view.getLayoutParams().height = lineHieght;
                        measureChildWithMargins(view, 0, 0);
                        break;
                    case MeasureStrategy.OVER_WIDTH_JUST_LAYOUT:
                        break;
                    default:
                        return STATE_OVER_WIDTH;
                }
            } else if (desireHeight > borderBottom) {
                return STATE_OVER_HEIGHT;
            }

            lineRight = desireWidth;
            lineBottom = desireHeight;

            queue.add(view);

            return STATE_NEXT;
        }

        void layout(LayoutManager layoutManager) {
            int layoutX = lineLeft;
            while (!queue.isEmpty()) {
                View item = queue.poll();
                if (item == null) {
                    continue;
                }
                layoutManager.addView(item);

                MeasureStrategy itemStrategy = getItemStrategy(
                        layoutManager.getPosition(item), lineCount);

                int measuredWidth = item.getMeasuredWidth();
                int measuredHeight = item.getMeasuredHeight();

                int left, top, right, bottom;
                left = layoutX;
                right = layoutX + measuredWidth;
                switch (itemStrategy.alignMode) {
                    case MeasureStrategy.ALIGN_MODE_TOP:
                        top = lineTop;
                        if (itemStrategy.stretchToLineHeight) {
                            bottom = lineBottom;
                        } else {
                            bottom = top + measuredHeight;
                        }
                        break;
                    case MeasureStrategy.ALIGN_MODE_CENTER:
                        top = getCenterY() - measuredHeight / 2;
                        bottom = top + measuredHeight;
                        break;
                    case MeasureStrategy.ALIGN_MODE_BOTTOM:
                        bottom = lineBottom;
                        top = bottom - measuredHeight;
                        break;
                    default:
                        top = lineTop;
                        bottom = top + measuredHeight;
                        break;
                }
                layoutManager.layoutDecoratedWithMargins(item, left, top, right, bottom);

                layoutX = right;
            }
            next();
        }

        int getLineHieght() {
            return lineBottom - lineTop;
        }

        int getCenterY() {
            return (int) ((lineTop + lineBottom) / 2.0F);
        }

        void recycle() {
            queue.clear();
            lineCount = 0;
        }

        private void next() {
            lineCount++;
            lineLeft = borderLeft;
            lineTop = lineBottom;
            lineRight = lineLeft;
            lineBottom = lineTop;
        }

        public void init() {
            lineTop = borderTop;
            lineLeft = borderLeft;
            lineRight = lineLeft;
            lineBottom = lineTop;
        }

        boolean outerRequestNewLine(int index) {
            return mMeasureStrategyConfig != null
                    ? mMeasureStrategyConfig.newLine(index)
                    : false;
        }
    }

    MeasureStrategy getItemStrategy(int index, int lineNum) {
        MeasureStrategy itemStrategy = null;
        if (mMeasureStrategyConfig != null) {
            itemStrategy = mMeasureStrategyConfig.getItemStrategy(index);
            if (itemStrategy == null) {
                itemStrategy = mMeasureStrategyConfig.getLineStrategy(lineNum);
            }
            if (itemStrategy == null) {
                itemStrategy = mMeasureStrategyConfig.getGlobalStrategy();
            }
        }
        return itemStrategy != null ? itemStrategy : mDefaultMeasureStrategy;
    }
}
