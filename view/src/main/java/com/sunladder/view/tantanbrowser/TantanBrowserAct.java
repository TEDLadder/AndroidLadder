package com.sunladder.view.tantanbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunladder.view.R;

public class TantanBrowserAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_tantan_browser);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tantan_main_list);
        recyclerView.setLayoutManager(new CardLayoutManager());
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
            return 10;
        }
    }
}
