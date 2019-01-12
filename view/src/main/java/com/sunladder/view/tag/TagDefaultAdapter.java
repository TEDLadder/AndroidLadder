package com.sunladder.view.tag;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.sunladder.view.tag.TagDefaultAdapter.TagViewHolder;

/**
 * Description:
 * Created by syzhugh on 2018/7/30
 */
public class TagDefaultAdapter extends RecyclerView.Adapter<TagViewHolder> {

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {

        public TagViewHolder(View itemView) {
            super(itemView);
        }
    }
}
