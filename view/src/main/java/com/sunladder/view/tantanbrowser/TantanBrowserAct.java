package com.sunladder.view.tantanbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunladder.view.R;

import java.util.ArrayList;
import java.util.List;

public class TantanBrowserAct extends AppCompatActivity {

    private final List<String> mList = new ArrayList<>();
    private ViewItemAdapter mAdapter;
    private ItemTouchHelper mTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_tantan_browser);

        for (int i = 0; i < 10; i++) {
            mList.add(i + "");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tantan_main_list);
        CardLayoutHelper cardLayoutHelper = new CardLayoutHelper(new CardLayoutHelper.CardLayoutListener() {
            @Override
            public void onSwipeChanged(int position, float ratio) {

            }

            @Override
            public void onSwiped(int position) {
                mList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onSwipeCanceld(int position) {

            }
        });
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(cardLayoutHelper.getCardLayoutManager());
        mTouchHelper = new ItemTouchHelper(cardLayoutHelper.getCardTouchHelperCallBack());
        mTouchHelper.attachToRecyclerView(recyclerView);
        mAdapter = new ViewItemAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private class ViewItemHolder extends RecyclerView.ViewHolder {
        public ViewItemHolder(View itemView) {
            super(itemView);
        }
    }

    private class ViewItemAdapter extends RecyclerView.Adapter<ViewItemHolder> {

        @Override
        public ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(TantanBrowserAct.this);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(720, 720);
            imageView.setLayoutParams(layoutParams);
            return new ViewItemHolder(imageView);
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
