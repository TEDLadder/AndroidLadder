package com.sunladder.view.tag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Description:
 * Created by syzhugh on 2018/7/30
 */
public class TagView extends RecyclerView {

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setLayoutManager(new TagLayoutManager());
        setAdapter(new TagDefaultAdapter());
    }

    @Deprecated
    public final void setLayoutManager(LayoutManager layout) {
    }
}
