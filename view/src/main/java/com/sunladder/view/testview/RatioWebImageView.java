package com.sunladder.view.testview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/2
 */
public class RatioWebImageView extends android.support.v7.widget.AppCompatImageView {

    private float ratio = 1.0F;

    public RatioWebImageView(Context context) {
        super(context);
    }

    public RatioWebImageView(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioWebImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param ratio = height / width
     */
    public void setHWRatio(float ratio) {
        ratio = Math.abs(ratio);
        if (!validRatio(ratio)) {
            ratio = 1.0F;
        }
        if (Float.compare(ratio, this.ratio) != 0) {
            this.ratio = ratio;
            requestLayout();
        }
    }

    /**
     * @param ratio = width / height
     */
    public void setWHRatio(float ratio) {
        setHWRatio(1 / ratio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        int measureHeight = (int) (measuredWidth * this.ratio);
        if (modeHeight != MeasureSpec.UNSPECIFIED) {
            measureHeight = Math.min(maxHeight, measureHeight);
        }
        setMeasuredDimension(measuredWidth, measureHeight);
    }

    private boolean validRatio(float ratio) {
        return !(Float.isNaN(ratio) || Float.isInfinite(ratio));
    }
}
