package com.zhuoxin.phonemanager.activity;

import android.content.BroadcastReceiver;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;

/**
 * 手机状态检测页面
 *
 * @author YoungHong
 */
public class PhoneStateActivity extends BaseActivity implements View.OnClickListener {

    ProgressBar pb_phonestate_battery;
    View v_phonestate_battery;
    TextView tv_phonestate_battery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        initActionBar("手机检测", true, false, this);
        initView();
        initBattary();
    }

    private void initView() {
        pb_phonestate_battery = (ProgressBar) findViewById(R.id.pb_phonestate_battery);
        v_phonestate_battery = findViewById(R.id.v_phonestate_battery);
        tv_phonestate_battery = (TextView) findViewById(R.id.tv_phonestate_battery);
    }

    private void initBattary() {
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        if (Build.VERSION.SDK_INT>=21) {
            pb_phonestate_battery.setProgress(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW));
            tv_phonestate_battery.setText(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) + "%");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
