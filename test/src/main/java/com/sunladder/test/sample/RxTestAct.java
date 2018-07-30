package com.sunladder.test.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.sunladder.test.R;
import com.sunladder.test.rx1.round.RxCreate;

public class RxTestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_rx_test);

        // rxjava简单实现
//        new RxMine().run();

        // rxjava创建符
        new RxCreate().run();
    }
}
