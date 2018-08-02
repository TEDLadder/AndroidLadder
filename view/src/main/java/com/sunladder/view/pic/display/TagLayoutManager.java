package com.sunladder.view.pic.display;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import com.sunladder.common.log.Logger;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/2
 */
public class TagLayoutManager extends RecyclerView.LayoutManager {

    /*方向*/
    boolean mReverse = false;

    /*行数*/
    private final int mMaxLine = 1;

    /*对齐策略*/

    private boolean mCenter = true;
    private int borderLeft;
    private int borderRight;
    private int borderTop;
    private int borderBottom;
    private TagLines mTagLine;

    /*是否拉伸*/

    public TagLayoutManager() {
        mTagLine = new TagLines();
        setAutoMeasureEnabled(true);
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(Recycler recycler, State state) {
        removeAllViews();
        detachAndScrapAttachedViews(recycler);

        borderLeft = getPaddingLeft();
        borderRight = getWidth() - getPaddingRight();
        borderTop = getPaddingTop();
        borderBottom = getHeight() - getPaddingBottom();

        mTagLine.init();
        int index = 0;
        while (index < getItemCount() && mTagLine.lineCount < mMaxLine) {
            View itemView = recycler.getViewForPosition(index);
            if (itemView == null) {
                continue;
            }

            measureChild(itemView, 0, 0);
            switch (mTagLine.addOne(itemView)) {
                case STATE_OVER_WIDTH:
                    mTagLine.layout(this);
                    continue;
                case STATE_OVER_HEIGHT:
                    mTagLine.recycle();
                    return;
                default:
                    index++;
                    break;
            }
        }
    }

    @Override
    public void onLayoutCompleted(State state) {
        super.onLayoutCompleted(state);

        Logger.printVar("child", getChildCount());
        Logger.printVar("item", getItemCount());
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
    }

    public static final int STATE_NEXT = 0;
    public static final int STATE_OVER_WIDTH = 1;
    public static final int STATE_OVER_HEIGHT = 2;

    private class TagLines {

        int lineCount;
        int lineCenterY;

        int lineLeft;
        int lineRight;
        int lineTop;
        int lineBottom;

        final Queue<View> queue = new LinkedList<>();

        int addOne(View view) {
            if (view == null) {
                return STATE_NEXT;
            }
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            int desireWidth = lineRight + measuredWidth;
            int desireHeight = Math.max(lineBottom, lineTop + measuredHeight);

            if (desireWidth > borderRight) {
                return STATE_OVER_WIDTH;
            } else if (desireHeight > borderBottom) {
                return STATE_OVER_HEIGHT;
            }

            lineRight = desireWidth;
            lineBottom = desireHeight;

            lineCenterY = (int) ((lineTop + lineBottom) / 2.0F);

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

                int measuredWidth = item.getMeasuredWidth();
                int measuredHeight = item.getMeasuredHeight();

                int right = layoutX + measuredWidth;
                int top = mCenter ? lineCenterY - measuredHeight / 2 : lineTop;
                int bottom = top + measuredHeight;
                layoutManager.layoutDecorated(item, layoutX, top, right, bottom);

                Logger.printMsg(
                        String.format("\n    %d\n%d      %d\n    %d", top, layoutX, right, bottom));

                layoutX = right;
            }
            Logger.printMsg("------------------------------");
            next();
        }

        void recycle() {
            queue.clear();
            lineCount = 0;
            lineCenterY = 0;
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
    }
}
