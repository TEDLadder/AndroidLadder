package com.aladder.androidladder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aladder.androidladder.R;
import com.aladder.androidladder.view.text.ExpandTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandTextView expandTextView = (ExpandTextView) findViewById(R.id.main_text);
        expandTextView.setText("111\n222\n333\n444\n555\n666\n777\n");
        expandTextView.setMaxLine(5);
    }
}
