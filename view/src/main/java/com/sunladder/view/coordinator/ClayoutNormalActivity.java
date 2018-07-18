package com.sunladder.view.coordinator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;
import com.sunladder.view.R;
import com.sunladder.view.adapter.SampleAdapter;

public class ClayoutNormalActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clayout_normal);
        setSupportActionBar(null);

        mToolbar = (Toolbar) findViewById(R.id.toolbar1);
        mToolbar.setTitle("scroll");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleAdapter());

        mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("scroll|enterAlways");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleAdapter());

        mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        mToolbar.setTitle("scroll|enterAlways|enterAlwaysCollapsed");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView3);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleAdapter());

        mToolbar = (Toolbar) findViewById(R.id.toolbar4);
        mToolbar.setTitle("scroll|exitUntilCollapsed");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView4);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleAdapter());

        mToolbar = (Toolbar) findViewById(R.id.toolbar5);
        mToolbar.setTitle("scroll|snap");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView5);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleAdapter());

        final ToggleButton button = (ToggleButton) findViewById(R.id.change_bt);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(button.isChecked());
            }
        });
        changeState(button.isChecked());
    }

    private void changeState(boolean checked) {
        View page1 = findViewById(R.id.page_1);
        View page2 = findViewById(R.id.page_2);
        page1.setVisibility(checked ? View.GONE : View.VISIBLE);
        page2.setVisibility(checked ? View.VISIBLE : View.GONE);
    }
}
