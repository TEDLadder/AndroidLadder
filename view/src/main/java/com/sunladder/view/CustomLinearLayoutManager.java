package com.sunladder.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/16
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(Recycler recycler, State state) {
        super.onLayoutChildren(recycler, state);

        int firstCompletelyVisibleItemPosition = findFirstCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = findFirstVisibleItemPosition();

        System.out.println("");
    }
}
