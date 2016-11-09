package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar("系统设置", true, false, new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
