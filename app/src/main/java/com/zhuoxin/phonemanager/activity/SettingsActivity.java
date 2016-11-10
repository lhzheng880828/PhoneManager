package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    ToggleButton tb_start;
    ToggleButton tb_notification;
    ToggleButton tb_push;
    ImageView iv_about;
    ImageView iv_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar("系统设置", true, false, new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        tb_start = (ToggleButton) findViewById(R.id.tb_start);
        tb_notification = (ToggleButton) findViewById(R.id.tb_notification);
        tb_push = (ToggleButton) findViewById(R.id.tb_push);
        iv_about = (ImageView) findViewById(R.id.iv_about);
        iv_help = (ImageView) findViewById(R.id.iv_help);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tb_start:
                
                break;
            case R.id.tb_notification:
                break;
            case R.id.tb_push:
                break;
            case R.id.iv_about:
                break;
            case R.id.iv_help:
                break;
        }
    }
}
