package com.sunladder.view.testview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.sunladder.common.log.Logger;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/1
 */
public class ChildView extends View {

    public ChildView(Context context) {
        this(context, null);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int atMost = MeasureSpec.AT_MOST;
        int exactly = MeasureSpec.EXACTLY;
        int unspecified = MeasureSpec.UNSPECIFIED;

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode1 = MeasureSpec.getMode(heightMeasureSpec);
        int size1 = MeasureSpec.getSize(heightMeasureSpec);
        Logger.printMsg("----");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
