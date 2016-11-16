package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_telnumber;
    TextView tv_software;
    TextView tv_rocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initActionBar("手机管家", false, true, this);
        initView();
    }

    private void initView() {
        tv_telnumber = (TextView) findViewById(R.id.tv_telnumber);
        tv_telnumber.setOnClickListener(this);
        tv_software = (TextView) findViewById(R.id.tv_software);
        tv_software.setOnClickListener(this);
        tv_rocket = (TextView) findViewById(R.id.tv_rocket);
        tv_rocket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.iv_menu:
                startActivity(SettingsActivity.class);
                break;
            case R.id.tv_telnumber:
                startActivity(PhoneActivity.class);
                break;
            case R.id.tv_software:
                startActivity(SoftManagerActivity.class);
                break;
            case R.id.tv_rocket:
                startActivity(RocketActivity.class);
                break;
        }
    }
}
