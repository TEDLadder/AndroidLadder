package com.sunladder.summary.testRx.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.sunladder.summary.R;
import com.sunladder.summary.testRx.rx1.round.RxCreate;

public class RxTestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_act_test_rx);

        // rxjava简单实现
//        new RxMine().run();

        // rxjava创建符
        new RxCreate().run();
    }
}
