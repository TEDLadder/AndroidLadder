package com.aladder.androidladder.view.text;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladder.androidladder.R;

/**
 * Created by Sun on 2018/4/12.
 */

public class ExpandTextView extends LinearLayout {

    private static final String TAG = "ExpandTextView";

    private final int DEFAULT_MAX_LINE = 5;

    private final TextView mTextView;
    private final ImageView mImageView;

    private boolean mNotOver;
    private boolean mExpandState;
    private Boolean mLastExpandState;

    private int mMaxLines = DEFAULT_MAX_LINE;

    private boolean mAnimationProcessing;
    private ValueAnimator mValueAnimator;
    private AnimatorListenerAdapter mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    private int mRealHeight;
    private int mCollapseHeight;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        mTextView = new CustomTextView(context);
        addView(mTextView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mImageView = new ImageView(context);
        mImageView.setImageResource(R.mipmap.ic_launcher);
        addView(mImageView, 120, 120);
        mImageView.setOnClickListener(mBtClickListener);
    }

    public void setText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }

    public void setMaxLine(int lines) {
        mMaxLines = lines;
    }

    private void needButton(boolean show) {
        mImageView.setVisibility(show ? VISIBLE : GONE);
    }

    private void changeBtOrientation(boolean expand) {
        mImageView.setRotation(expand ? 180 : 0);
    }

    private final OnClickListener mBtClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            changeTextViewState();
        }
    };

    private void changeTextViewState() {
        if (mNotOver) {
            return;
        }

        mLastExpandState = mExpandState;
        mExpandState = !mExpandState;

        int start, end;
        start = mTextView.getHeight();
        end = mExpandState ? mRealHeight : mCollapseHeight;

        ValueAnimator expandAnimator = getExpandAnimator();
        expandAnimator.setIntValues(start, end);
        expandAnimator.start();
    }

    @NonNull
    public ValueAnimator getExpandAnimator() {
        if (mValueAnimator == null) {
            mValueAnimator = new ValueAnimator();
        }
        if (mAnimatorListener == null) {
            mAnimatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mAnimationProcessing = true;
                    changeBtOrientation(mExpandState);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mAnimationProcessing = false;
                    if (mLastExpandState != null) {
                        mExpandState = mLastExpandState;
                        changeBtOrientation(mExpandState);
                        mTextView.requestLayout();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimationProcessing = false;
                    mLastExpandState = null;
                }
            };
            mValueAnimator.addListener(mAnimatorListener);
        }
        if (mAnimatorUpdateListener == null) {
            mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object animatedValue = animation.getAnimatedValue();
                    Integer value = animatedValue instanceof Integer ? ((Integer) animatedValue) : null;
                    if (value != null) {
                        mTextView.setHeight(value);
                    }
                }
            };
            mValueAnimator.addUpdateListener(mAnimatorUpdateListener);
        }
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return mValueAnimator;
    }

    private final class CustomTextView extends AppCompatTextView {

        public CustomTextView(Context context) {
            this(context, null);
        }

        public CustomTextView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            //动画进行中不改变测量
            if (mAnimationProcessing) {
                return;
            }

            //记录真实高度
            if (mRealHeight == 0) {
                mRealHeight = getMeasuredHeight();
            }

            //影响首次绘制
            int measuredHeight = getMeasuredHeight();
            boolean over = getLineCount() > mMaxLines;
            needButton(over);
            if (over) {
                if (mExpandState) {
                    return;
                }
                int height = Math.min(getLineHeight() * mMaxLines, measuredHeight);
                setMeasuredDimension(getMeasuredWidth(), height);

                if (mCollapseHeight == 0) {
                    mCollapseHeight = height;
                }
            } else {
                mNotOver = true;
            }
        }
    }
}
