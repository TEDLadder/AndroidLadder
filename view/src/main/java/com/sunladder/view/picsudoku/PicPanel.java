package com.sunladder.view.picsudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sunladder.view.R;
import com.sunladder.view.img.RatioImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sun Yaozong on 2018/5/29.
 * <p>
 * TODO:1.复用混乱问题
 */

public class PicPanel<T extends IPicPanelBean> extends FrameLayout {

    public static final int MODE_NINE_GRID = 0;
    public static final int MODE_FACEBOOK = 1;

    protected int mShowMode;
    protected List<T> mList;

    protected RecyclerView mInnerRecyclerView;
    private PicPanelAdapter mAdapter;

    public PicPanel(Context context) {
        this(context, null);
    }

    public PicPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PicPanel);
        typedArray.getInt(R.styleable.PicPanel_showType, MODE_NINE_GRID);
        typedArray.recycle();

        mInnerRecyclerView = new RecyclerView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mInnerRecyclerView, layoutParams);

        mAdapter = new PicPanelAdapter();
        mInnerRecyclerView.setAdapter(mAdapter);
    }

    public void config(int mode, ItemDecoration itemDecoration) {
        this.config(mode, 0);
        if (itemDecoration != null) {
            mInnerRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    public void config(int mode, int dividerWidth) {
        mShowMode = mode;
        LayoutManager layoutManager = generateLayoutManager(mode, dividerWidth);
        mInnerRecyclerView.setLayoutManager(layoutManager);
    }

    public void setPicList(List<T> picList) {
        if (picList != null && picList.size() > 0) {
            if (mList == null) {
                mList = new ArrayList<>();
            } else {
                mAdapter.notifyItemRangeRemoved(0, mList.size());
                mList.clear();
            }
            mList.addAll(picList);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected LayoutManager generateLayoutManager(int showMode, int dividerWidth) {
        if (showMode == MODE_FACEBOOK) {

        }
        return new NineGridLayoutManager(dividerWidth);
    }

    private class PicPanelAdapter extends RecyclerView.Adapter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RatioImageView imageView = new RatioImageView(parent.getContext());
            return new PicPanelViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            View itemView = holder.itemView;
            ImageView imageView = itemView instanceof ImageView ? ((ImageView) itemView) : null;
            if (imageView != null) {
                T pic = mList.get(position);
                imageView.setImageResource(R.mipmap.ic_launcher);
                imageView.setBackground(new ColorDrawable(Color.parseColor("#330000ff")));
            }
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }
    }

    private class PicPanelViewHolder extends RecyclerView.ViewHolder {

        public PicPanelViewHolder(View itemView) {
            super(itemView);
        }
    }
}
