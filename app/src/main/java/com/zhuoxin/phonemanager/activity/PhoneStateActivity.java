package com.zhuoxin.phonemanager.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.MemoryManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
        initMessage();
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

    private void initMessage() {
        //手机信息
        tv_brand.setText("手机品牌：" + Build.BRAND);
        tv_version.setText("系统版本：Android " + Build.VERSION.RELEASE);
        //cpu信息
        tv_cputype.setText("CPU型号：" + getCpuName());
        tv_cpucore.setText("CPU核心数：" + getCpuCount());
        //ram信息
        tv_totalram.setText("全部运行内存：" + MemoryManager.getTotalMemoryStr(this));
        tv_freearm.setText("剩余运行内存：" + MemoryManager.getAvailableMemoryStr(this));
        //分辨率
        tv_screen.setText("屏幕分辨率：" + getScreenDisplay());
        tv_camera.setText("相机分辨率：" + getCameraDisplay());
        //基带版本
        tv_base.setText("基带版本：" + Build.VERSION.INCREMENTAL);
        tv_root.setText("是否Root：" + isRoot());
    }


    public String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("model name")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;
    }


    public int getCpuCount() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        int count = 0;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("processor")) {
                    count++;
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return count;
    }

    public String getScreenDisplay() {

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y + "*" + point.x;
    }

    public String getCameraDisplay() {
        String maxSize = "";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Camera camera = Camera.open();
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
                Camera.Size size = null;
                for (Camera.Size s : sizes) {
                    if (size == null) {
                        size = s;
                    } else if (size.height * s.width < s.height * s.width) {
                        size = s;
                    }
                }
                maxSize = size.width + "*" + size.height;
                camera.release();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
            }
        }

        return maxSize;
    }

    /**
     * 判断手机是否Root
     *
     * @return
     */
    public boolean isRoot() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }
        return bool;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
