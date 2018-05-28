package com.sunladder.view.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunladder.view.R;
import com.sunladder.view.img.RatioImageView;
import com.sunladder.view.picsudoku.NineGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class PicNineGridAct extends AppCompatActivity {

    public static final int PIC_COUNT = 5;

    private final List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_nine_grid);

        for (int i = 0; i < PIC_COUNT; i++) {
            mList.add(i + "");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.nine_pic_list);
        NineGridLayoutManager layoutManager = new NineGridLayoutManager(PIC_COUNT, 15);
        recyclerView.setLayoutManager(layoutManager);
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
            RatioImageView view = new RatioImageView(PicNineGridAct.this);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            return new ViewItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewItemHolder holder, int position) {
            View itemView = holder.itemView;
            ImageView imgView = itemView instanceof ImageView ? ((ImageView) itemView) : null;
            if (imgView != null) {
                imgView.setImageResource(R.mipmap.ic_launcher);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
