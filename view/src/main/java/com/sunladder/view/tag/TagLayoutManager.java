package com.sunladder.view.tag;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import com.sunladder.view.tag.MeasureStrategy.MeasureStrategyGroup;

/**
 * Description:
 * Created by syzhugh on 2018/8/2
 */
public class TagLayoutManager extends RecyclerView.LayoutManager {

    private LineHelper mLineHelper;

    public TagLayoutManager() {
        this(1, false);
    }

    public TagLayoutManager(int maxLine, boolean reverse) {
        this(maxLine, reverse, null);
    }

    public TagLayoutManager(int maxLine, boolean reverse,
            MeasureStrategyGroup measureStrategyConfig) {
        setAutoMeasureEnabled(true);

        mLineHelper = new LineHelper(maxLine, 0, this, measureStrategyConfig);
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(Recycler recycler, State state) {
        detachAndScrapAttachedViews(recycler);

        int borderLeft = getPaddingLeft();
        int borderRight = getWidth() - getPaddingRight();
        int borderTop = getPaddingTop();
        int borderBottom = getHeight() - getPaddingBottom();

        mLineHelper.init(getWidthMode(), getHeightMode(),
                borderLeft, borderTop, borderRight, borderBottom);

        int index = 0;
        while (index < getItemCount() && mLineHelper.notFull()) {

            if (index > 90) {
                System.out.println(1);
            }

            View itemView = recycler.getViewForPosition(index);
            int addState = mLineHelper.measureChildItem(index, itemView);
            if (addState == LineHelper.ADD_STATE_NEXT_ITEM) {
                index++;
            } else if (addState == LineHelper.ADD_STATE_NEW_LINE) {
                mLineHelper.layoutLine();
            } else {
                break;
            }
        }
        mLineHelper.recycle();
    }

    interface CallBack {


    }
}
