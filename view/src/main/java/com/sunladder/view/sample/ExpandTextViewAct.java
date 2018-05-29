package com.sunladder.view.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunladder.view.R;
import com.sunladder.view.text.ExpandTextView;


public class ExpandTextViewAct extends AppCompatActivity {

    ExpandTextView mMainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_expand_textview);

        mMainText = findViewById(R.id.widget_expand_text);
        mMainText.setText("111\n222\n333\n444\n555\n666\n777\n");
        mMainText.setMaxLine(5);
    }
}
