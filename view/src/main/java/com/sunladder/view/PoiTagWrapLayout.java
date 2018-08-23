package com.sunladder.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/9
 */
public class PoiTagWrapLayout extends LinearLayout {

    public PoiTagWrapLayout(Context context) {
        this(context, null);
    }

    public PoiTagWrapLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiTagWrapLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (notValidCount()) {
            return;
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        View firstView = getChildAt(0);
        View secondView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (notValidCount()) {
            return;
        }
    }

    boolean notValidCount() {
        return getChildCount() != 2;
    }
}
