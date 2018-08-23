package com.sunladder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sunladder.view.CustomLinearLayoutManager;
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
//        ViewGroup inflate = (ViewGroup) LayoutInflater.from(this)
//                .inflate(R.layout.view_activity_tagpop, null);
//        setContentView(inflate);
//
//        final TagPopupWindow tagPopupWindow = new TagPopupWindow(this);
//
//        for (int i = 0; i < inflate.getChildCount(); i++) {
//            final View childAt = inflate.getChildAt(i);
//            childAt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tagPopupWindow.showTagInfo(ViewMainActivity.this, childAt, "123123123\n13\n13\n13\n13");
//                }
//            });
//        }

        initItems();
        initList();

//        startDefault();

//        RatioWebImageView ratioWebImageView = findViewById(R.id.ratio);
//        ratioWebImageView.setWHRatio(1);
//
//        FlexboxLayout view = findViewById(R.id.flex);
//        view.setFlexWrap(FlexWrap.WRAP);
//        for (int i = 0; i < 50; i++) {
//            TextView textView = new TextView(this);
//            textView.setGravity(Gravity.CENTER);
//            textView.setText(i + "");
//            textView.setBackgroundResource(R.drawable.drawable);
//            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
//                    133, 133);
//            view.addView(textView, layoutParams);
//        }
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

        for (int i = 0; i < 15; i++) {

            mList.add(new ViewItemBean(i + "", null));
        }
    }

    private void initList() {
//        final MeasureStrategy measureStrategy = new Builder()
//                .setStretchToLineHeight(true)
//                .setOverWidthState(MeasureStrategy.OVER_WIDTH_JUST_LAYOUT)
//                .setAlignMode(MeasureStrategy.ALIGN_MODE_CENTER)
//                .build();
//        TagLayoutManager tagLayoutManager = new TagLayoutManager(100, false,
//                new DefaultMeasureStrategyGroup() {
//                    @Override
//                    public MeasureStrategy getGlobalStrategy() {
//                        return measureStrategy;
//                    }
//
//                    @Override
//                    public int lineMaxView(int lineNum) {
//                        if (lineNum == 0) {
//                            return 1;
//                        }
//                        return super.lineMaxView(lineNum);
//                    }
//                });
//        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
//        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
//        flexboxLayoutManager.setFlexWrap(FlexWrap.NOWRAP);
//        flexboxLayoutManager.setMaxLine(1);
        mViewMainList.setLayoutManager(new CustomLinearLayoutManager(this));
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
//            TextView textView = new TextView(parent.getContext());
//            textView.setGravity(Gravity.CENTER);
//            textView.setPadding(0, 30, 0, 30);
//            FlexboxLayoutManager.LayoutParams layoutParams = new FlexboxLayoutManager.LayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            textView.setLayoutParams(layoutParams);

            View view = new View(parent.getContext());
            view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setBackgroundColor(Color.parseColor("#00ff00"));
            return new ViewItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewItemHolder holder, int position) {
            View itemView = holder.itemView;

//            if (position == 1) {
//                itemView.getLayoutParams().height = 300;
//                itemView.getLayoutParams().width = 300;
//                itemView.setLayoutParams(itemView.getLayoutParams());
//            }

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
