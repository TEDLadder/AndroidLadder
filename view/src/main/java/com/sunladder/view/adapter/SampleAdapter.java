package com.sunladder.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sunladder.view.adapter.SampleAdapter.SampleViewHolder;


public class SampleAdapter extends RecyclerView.Adapter<SampleViewHolder> {

    private static final int DEFAULT_SIZE = 30;

    private int mDataSize;

    public SampleAdapter() {
        this(DEFAULT_SIZE);
    }

    public SampleAdapter(int dataSize) {
        mDataSize = dataSize;
    }

    @Override
    public int getItemCount() {
        return mDataSize;
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 30, 0, 30);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, 120);
        textView.setLayoutParams(layoutParams);
        return new SampleViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        View itemView = holder.itemView;
        TextView textView = itemView instanceof TextView ? ((TextView) itemView) : null;
        if (textView != null) {
            textView.setText(String.valueOf(position));
        }
    }

    public static class SampleViewHolder extends RecyclerView.ViewHolder {

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

    }
}
