package com.sunladder.view.testview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/3
 */
public class ParentView extends LinearLayout {

    public ParentView(Context context) {
        this(context, null);
    }

    public ParentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TextView textView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(200, 200);
        textView.setGravity(Gravity.CENTER);
        textView.setText("1234567890");
        textView.setTextSize(20);
        addView(textView, layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);

            int left = l;
            int top = t;
            int right = left + item.getMeasuredWidth();
            int bottom = top + item.getMeasuredHeight();
            item.layout(left, top, right, bottom);
        }
    }
}
