package com.sunladder.view.tantanbrowser;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.sunladder.common.log.Logger;

/**
 * Created by Sun on 2018/5/26.
 */

public class CardLayoutHelper {

    private static final int THRESHOLD_ROTATION = 15;

    private static final float THRESHOLD_SCALE = 0.65F;
    private static final float SCALE_STEP = 0.12F;

    private static final int THRESHOLD_TRANSLATION = 100;
    private static final int STEP_TRANSLATION = 20;

    private static final int MAX_SHOW_COUNT = 3;

    private final int mMaxShowCount;

    private final CardLayoutManager mCardLayoutManager;
    private final CardTouchHelperCallBack mCardTouchHelperCallBack;
    private final CardLayoutListener mCardLayoutListener;

    public CardLayoutHelper(CardLayoutListener listener) {
        this(MAX_SHOW_COUNT, listener);
    }

    public CardLayoutHelper(int maxCount, CardLayoutListener listener) {
        mCardLayoutManager = new CardLayoutManager();
        mCardTouchHelperCallBack = new CardTouchHelperCallBack();
        mMaxShowCount = maxCount;
        mCardLayoutListener = listener;
    }

    public CardLayoutManager getCardLayoutManager() {
        return mCardLayoutManager;
    }

    public CardTouchHelperCallBack getCardTouchHelperCallBack() {
        return mCardTouchHelperCallBack;
    }

    public interface CardLayoutListener {

        void onSwipeChanged(int position, float ratio);

        void onSwiped(int position);

        void onSwipeCanceld(int position);
    }

    private class CardLayoutManager extends RecyclerView.LayoutManager {

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
            int lastIndex = Math.min(mMaxShowCount - 1, itemCount - 1);
            for (int index = lastIndex; index >= 0; index--) {
                View childView = recycler.getViewForPosition(index);
                addView(childView);
                measureChildWithMargins(childView, 0, 0);

                // TODO: 2018/5/23 考虑padding
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
                scale = scale > THRESHOLD_SCALE ? scale : THRESHOLD_SCALE;
                childView.setScaleX(scale);
//            childView.setScaleY(scale);

                int translation = index * STEP_TRANSLATION;
                translation = translation < THRESHOLD_TRANSLATION ? translation : THRESHOLD_TRANSLATION;
                childView.setTranslationY(translation);
            }
        }
    }

    private class CardTouchHelperCallBack extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Logger.printCurrentMethod();
            Logger.printVar("position", viewHolder.getAdapterPosition());
            if (mCardLayoutListener != null) {
                mCardLayoutListener.onSwiped(viewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            float ratio = dX / recyclerView.getWidth();
            float absRatio = Math.abs(ratio);
            ratio = absRatio > 1 ? ratio / absRatio : ratio;
            Logger.printVar("ratio", ratio);

            View itemView = viewHolder.itemView;
            itemView.setAlpha(1 - Math.abs(ratio));
            itemView.setPivotY(itemView.getBottom());
            itemView.setRotation(ratio * THRESHOLD_ROTATION);

            if (isCurrentlyActive) {
                if (absRatio > 0 && mCardLayoutListener != null) {
                    mCardLayoutListener.onSwipeChanged(viewHolder.getAdapterPosition(), ratio);
                }
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setRotation(0);
        }
    }
}
