package com.sunladder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import java.util.ArrayList;
import java.util.List;

public class ViewMainActivity extends Activity {

    @BindView(R.id.view_main_list)
    RecyclerView mViewMainList;

    private final List<ViewItemBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_main);
        ButterKnife.bind(this);

        initItems();
        initList();

//        startDefault();

        FlexboxLayout view = findViewById(R.id.flex);
        view.setFlexWrap(FlexWrap.WRAP);
        for (int i = 0; i < 50; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setText(i + "");
            textView.setBackgroundResource(R.drawable.drawable);
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    133, 133);
            view.addView(textView, layoutParams);
        }
    }

    private void initItems() {
        mList.add(new ViewItemBean("expand text view",
                "com.sunladder.view.sample.ExpandTextViewAct"));
        mList.add(new ViewItemBean("tantan pic browser",
                "com.sunladder.view.sample.TantanBrowserAct"));
        mList.add(new ViewItemBean("pic nine", "com.sunladder.view.sample.PicPanelAct"));
        mList.add(new ViewItemBean("rx test", "com.sunladder.test.sample.RxTestAct"));
        mList.add(new ViewItemBean("clayout 5 scroll types",
                "com.sunladder.view.coordinator.ClayoutNormalActivity"));
    }

    private void initList() {
        mViewMainList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewMainList.setAdapter(new ViewItemAdapter());
    }

    private void startDefault() {
        startAct(mList.get(2).viewAct);
    }

    private void startAct(String clazzName) {
        Intent intent = new Intent();
        intent.setClassName(this, clazzName);
        startActivity(intent);
    }

    private class ViewItemHolder extends RecyclerView.ViewHolder {

        public ViewItemHolder(View itemView) {
            super(itemView);
        }
    }

    private class ViewItemAdapter extends RecyclerView.Adapter<ViewItemHolder> {

        @Override
        public ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 30, 0, 30);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, 120);
            textView.setLayoutParams(layoutParams);
            return new ViewItemHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewItemHolder holder, int position) {
            View itemView = holder.itemView;
            TextView textView = itemView instanceof TextView ? ((TextView) itemView) : null;
            if (textView != null) {
                final ViewItemBean itemBean = mList.get(position);
                textView.setText(itemBean.viewName);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAct(itemBean.viewAct);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private class ViewItemBean {

        String viewName;
        String viewAct;

        public ViewItemBean(String viewName, String viewAct) {
            this.viewName = viewName;
            this.viewAct = viewAct;
        }
    }


}
