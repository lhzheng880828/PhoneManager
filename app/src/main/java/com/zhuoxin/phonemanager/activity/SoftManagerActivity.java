package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.view.PiechartView;

public class SoftManagerActivity extends BaseActivity implements View.OnClickListener {

    PiechartView pv_softmgr;
    RelativeLayout rl_all;
    RelativeLayout rl_system;
    RelativeLayout rl_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        initActionBar("软件管理", true, false, this);
        initView();
    }

    private void initView() {
        pv_softmgr = (PiechartView) findViewById(R.id.pv_phonespace);
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        rl_system = (RelativeLayout) findViewById(R.id.rl_system);
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        pv_softmgr.drawPiechart(0, 180);
        rl_all.setOnClickListener(this);
        rl_system.setOnClickListener(this);
        rl_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_all:
                bundle.putString("appType", "all");
                startActivity(SoftwareActivity.class, bundle);
                break;
            case R.id.rl_system:
                bundle.putString("appType", "system");
                startActivity(SoftwareActivity.class, bundle);
                break;
            case R.id.rl_user:
                bundle.putString("appType", "user");
                startActivity(SoftwareActivity.class, bundle);
                break;
        }
    }
}
