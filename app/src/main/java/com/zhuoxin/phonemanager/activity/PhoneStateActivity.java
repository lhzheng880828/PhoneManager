package com.zhuoxin.phonemanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 手机状态检测页面
 *
 * @author YoungHong
 */
public class PhoneStateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PhoneStateActivity";
    ProgressBar pb_phonestate_battery;
    View v_phonestate_battery;
    TextView tv_phonestate_battery;
    //注解控件
    @InjectView(R.id.tv_brand)
    TextView tv_brand;
    @InjectView(R.id.tv_version)
    TextView tv_version;
    @InjectView(R.id.tv_cputype)
    TextView tv_cputype;
    @InjectView(R.id.tv_cpucore)
    TextView tv_cpucore;
    @InjectView(R.id.tv_totalram)
    TextView tv_totalram;
    @InjectView(R.id.tv_freeram)
    TextView tv_freearm;
    @InjectView(R.id.tv_screen)
    TextView tv_screen;
    @InjectView(R.id.tv_camera)
    TextView tv_camera;
    @InjectView(R.id.tv_base)
    TextView tv_base;
    @InjectView(R.id.tv_root)
    TextView tv_root;

    //广播接收者
    BroadcastReceiver receiver;
    int batteryProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        ButterKnife.inject(this);
        initActionBar("手机检测", true, false, this);
        initView();
        initBattary();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        pb_phonestate_battery = (ProgressBar) findViewById(R.id.pb_phonestate_battery);
        v_phonestate_battery = findViewById(R.id.v_phonestate_battery);
        tv_phonestate_battery = (TextView) findViewById(R.id.tv_phonestate_battery);
    }

    /**
     * 初始化电池
     */
    private void initBattary() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //电池温度
                int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                //把它转成百分比
                batteryProcess = (level * 100) / scale;
                pb_phonestate_battery.setProgress(batteryProcess);
                tv_phonestate_battery.setText(batteryProcess + "%");

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
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
