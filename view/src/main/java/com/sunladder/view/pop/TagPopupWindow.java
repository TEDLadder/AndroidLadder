package com.sunladder.view.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.sunladder.view.R;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/16
 */
public class TagPopupWindow extends PopupWindow {


    private final TagDesViewGroup mRootView;
    private final AlphaAnimation mOpenAnimation;
    private final AlphaAnimation mCloseAnimation;

    public TagPopupWindow(Context context) {
        super(context);

        mRootView = new TagDesViewGroup(context);
        //点击取消
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                dismiss();
                return true;
            }
        });

        mOpenAnimation = new AlphaAnimation(0.7F, 1.0F);
        mOpenAnimation.setDuration(150);
        mCloseAnimation = new AlphaAnimation(1.0F, 0.7F);
        mCloseAnimation.setDuration(100);
        mCloseAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // nothing to do
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TagPopupWindow.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // nothing to do
            }
        });

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(null);
        setContentView(mRootView);
    }

    public void showTagInfo(Activity activity, View view, String description) {
        if (mRootView.showTagInfo(activity, view, description)) {
            ViewGroup contentView = activity.getWindow().findViewById(android.R.id.content);
            showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            openAnimate();
        }
    }

    private void openAnimate() {
        mRootView.startAnimation(mOpenAnimation);
    }

    private void closeAnimate() {
        mRootView.startAnimation(mCloseAnimation);
    }

    @Override
    public void dismiss() {
        closeAnimate();
    }

    private class TagDesViewGroup extends ViewGroup {

        private static final int GAP_WIDTH = 9;
        private static final int PADDING = 9;

        private TextView mTextView;
        private Rect mShadowBound;
        private Rect mTextBound;
        private Drawable mBackgroundDrawable;
        private Paint mPaint;

        public boolean showTagInfo(Activity activity, View view, String description) {
            float x = getX();

            int screenWidth = 1080;
            int screenHeight = 1920 - 60;

            //获取距离屏幕左右宽度
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            int viewMeasuredWidth = view.getMeasuredWidth();
            int viewMeasuredHeight = view.getMeasuredHeight();

            int anchorLeft = location[0];
            int anchorRight = anchorLeft + viewMeasuredWidth;
//            int anchorTop = location[1] - StatusBarUtils.getStatusBarHeight(activity);
            int anchorTop = location[1] - 60;
            int anchorBottom = anchorTop + viewMeasuredHeight;

            int leftSpace = anchorLeft - GAP_WIDTH;
            int rightSpace = screenWidth - anchorRight - GAP_WIDTH;
            int topSpace = anchorBottom;
            int bottomSpace = screenHeight - anchorTop;

            mTextView.setText(description);
            mTextView.measure(
                    MeasureSpec
                            .makeMeasureSpec(Math.max(leftSpace, rightSpace), MeasureSpec.AT_MOST),
                    MeasureSpec
                            .makeMeasureSpec(Math.max(topSpace, bottomSpace), MeasureSpec.AT_MOST));
            int tvWidth = mTextView.getMeasuredWidth();
            int tvHeight = mTextView.getMeasuredHeight();

            boolean validHorizontal = true;
            if (tvWidth < rightSpace) {
                mTextBound.left = anchorRight + GAP_WIDTH;
                mTextBound.right = mTextBound.left + tvWidth;
            } else if (tvWidth < leftSpace) {
                mTextBound.right = anchorLeft - GAP_WIDTH;
                mTextBound.left = mTextBound.right - tvWidth;
            } else {
                validHorizontal = false;
            }

            boolean validVertical = true;
            if (tvHeight < bottomSpace) {
                mTextBound.top = anchorTop;
                mTextBound.bottom = mTextBound.top + tvHeight;
            } else if (tvHeight < topSpace) {
                mTextBound.bottom = anchorBottom;
                mTextBound.top = mTextBound.bottom - tvHeight;
            } else {
                validVertical = false;
            }

            if (!validHorizontal || !validVertical) {
                return false;
            }

            int shadowTop = Math.max(Math.min(anchorTop, mTextBound.top) - PADDING, 0);
            int shadowBottom = Math.min(
                    Math.max(anchorBottom, mTextBound.bottom) + PADDING, screenHeight);

            int shadowLeft = Math.max(Math.min(anchorLeft, mTextBound.left) - PADDING, 0);
            int shadowRight = Math.min(
                    Math.max(anchorRight, mTextBound.right) + PADDING, screenWidth);
            mShadowBound.set(shadowLeft, shadowTop, shadowRight, shadowBottom);

            return true;
        }

        public TagDesViewGroup(Context context) {
            this(context, null);
        }

        public TagDesViewGroup(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TagDesViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mTextView = new TextView(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            addView(mTextView, layoutParams);
            mTextBound = new Rect();
            mShadowBound = new Rect();
            mBackgroundDrawable = context.getDrawable(R.drawable.tag_shadow_bg);
            setWillNotDraw(false);

            mPaint = new Paint();
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            mTextView.layout(mTextBound.left, mTextBound.top, mTextBound.right, mTextBound.bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int layerID = canvas.saveLayer(0, 0,
                    canvas.getWidth(), canvas.getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

            mPaint.setColor(Color.parseColor("#99000000"));
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

            mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            canvas.drawRoundRect(new RectF(mShadowBound), 15, 15, mPaint);
            mPaint.reset();

            canvas.restoreToCount(layerID);

            mBackgroundDrawable.setBounds(mShadowBound);
            mBackgroundDrawable.draw(canvas);
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }
    }
}
