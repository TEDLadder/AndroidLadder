package com.sunladder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

public class ViewMainActivity extends Activity {

    @BindView(R.id.view_main_list)
    RecyclerView mViewMainList;

    private final List<ViewItemBean> mList = new ArrayList<>();

    private ViewItemBean defaultAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_main);
        ButterKnife.bind(this);

        initItems();
        initList();
        startDefault();
    }

    private void initItems() {
        // view
        mList.add(new ViewItemBean("expand text view",
                "com.sunladder.view.sample.ExpandTextViewAct"));
        mList.add(new ViewItemBean("tantan pic browser",
                "com.sunladder.view.sample.TantanBrowserAct"));
        mList.add(new ViewItemBean("pic nine",
                "com.sunladder.view.sample.PicPanelAct"));
        mList.add(new ViewItemBean("rx test",
                "com.sunladder.test.sample.RxTestAct"));
        mList.add(new ViewItemBean("clayout 5 scroll types",
                "com.sunladder.view.coordinator.ClayoutNormalActivity"));

        // summary
        mList.add(defaultAct = new ViewItemBean("summary mXfermode",
                "com.sunladder.summary.view.xfermode.ViewXfermodeAct"));
    }

    private void initList() {
        mViewMainList.setLayoutManager(new LinearLayoutManager(mViewMainList.getContext()));
        mViewMainList.setAdapter(new ViewItemAdapter());
    }

    private void startDefault() {
        if (defaultAct != null) {
            startAct(defaultAct.viewAct);
        }
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
            textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
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
