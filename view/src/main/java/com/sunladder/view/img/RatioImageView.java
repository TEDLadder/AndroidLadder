package com.sunladder.view.img;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * An ImageView whose height depends on its width and the ratio.
 */

public class RatioImageView extends AppCompatImageView {

    private float mRatio = 1.0F;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.FIT_XY);
        setBackground(new ColorDrawable(context.getColor(android.R.color.holo_blue_light)));
    }

    public void setRatio(float ratio) {
        if (ratio > 0 && ratio != mRatio) {
            mRatio = ratio;
            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(measuredWidth, (int) (measuredWidth * mRatio));
        }
    }
}
