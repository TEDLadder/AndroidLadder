package com.sunladder.view.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import com.sunladder.view.R;
import com.sunladder.view.pic.display.PicPanel;
import com.sunladder.view.pic.display.PicPanelBean;
import java.util.ArrayList;
import java.util.List;

public class PicPanelAct extends AppCompatActivity {

    private final List<List<PicPanelBean>> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_pic_nine_grid);

        for (int i = 1; i <= 10; i++) {
            List<PicPanelBean> childList = new ArrayList<>(i);
            for (int i1 = 0; i1 < i; i1++) {
                childList.add(new PicPanelBean(0, 0, null, null));
            }
            mList.add(childList);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.nine_pic_list);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ViewItemAdapter());
    }

    private class ViewItemHolder extends RecyclerView.ViewHolder {

        public ViewItemHolder(View itemView) {
            super(itemView);
        }
    }

    private class ViewItemAdapter extends RecyclerView.Adapter<ViewItemHolder> {

        @Override
        public ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PicPanel view = new PicPanel(PicPanelAct.this);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            return new ViewItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewItemHolder holder, int position) {
            View itemView = holder.itemView;
            PicPanel imgView = itemView instanceof PicPanel ? ((PicPanel) itemView) : null;
            if (imgView != null) {
                List<PicPanelBean> beanList = mList.get(position);
                imgView.config(PicPanel.MODE_NINE_GRID, 15);
                imgView.setPicList(beanList);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
