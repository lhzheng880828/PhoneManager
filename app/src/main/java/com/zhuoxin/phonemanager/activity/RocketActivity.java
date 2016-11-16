package com.zhuoxin.phonemanager.activity;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.SoftwareAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.entity.SoftwareInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES;

public class RocketActivity extends BaseActivity implements View.OnClickListener {

    //控件
    TextView tv_brand;
    TextView tv_version;
    TextView tv_runspace;
    ProgressBar pb_runspace;
    ProgressBar pb_rocket_loading;
    Button btn_rocket_clear;
    Button btn_rocket_shift;
    ListView lv_rocket;
    CheckBox cb_rocket;
    //数据
    ActivityManager activityManager;
    boolean isSystem;//系统进程
    List<SoftwareInfo> softwareInfoList = new ArrayList<SoftwareInfo>();
    SoftwareAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        initActionBar("手机加速", true, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
        asyncLoadProgress();
    }


    private void initView() {
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_runspace = (TextView) findViewById(R.id.tv_runspace);
        pb_runspace = (ProgressBar) findViewById(R.id.pb_runspace);
        pb_rocket_loading = (ProgressBar) findViewById(R.id.pb_rocket_loading);
        btn_rocket_clear = (Button) findViewById(R.id.btn_rocket_clear);
        btn_rocket_shift = (Button) findViewById(R.id.btn_rocket_shift);
        cb_rocket = (CheckBox) findViewById(R.id.cb_rocket);
        lv_rocket = (ListView) findViewById(R.id.lv_rocket);
        btn_rocket_clear.setOnClickListener(this);
        btn_rocket_shift.setOnClickListener(this);
    }

    private void initData() {
        adapter = new SoftwareAdapter(this);
        adapter.setData(softwareInfoList);
        lv_rocket.setAdapter(adapter);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    }

    private void asyncLoadProgress() {
        pb_rocket_loading.setVisibility(View.VISIBLE);
        lv_rocket.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                softwareInfoList.clear();
                List<ActivityManager.RunningAppProcessInfo> runAppInfos = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo info : runAppInfos) {
                    Log.v(getClass().getName(),info.processName);
                    String packageName = info.processName;
                    Drawable appicon = null;
                    String appname = "";
                    boolean system = false;
                    String appversion = "";
                    try {
                        ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, MATCH_UNINSTALLED_PACKAGES);
                        appicon = getPackageManager().getApplicationIcon(packageName);
                        appname = (String) getPackageManager().getApplicationLabel(appInfo);
                        system = (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) >= 0;
                        appversion = getPackageManager().getPackageInfo(packageName, 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    SoftwareInfo softwareInfo = new SoftwareInfo(false, appicon, appname, system, packageName, appversion);
                    softwareInfoList.add(softwareInfo);
                }
                Log.v(getClass().getName(),softwareInfoList.size()+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_rocket_loading.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        lv_rocket.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_rocket_clear:
                break;
            case R.id.btn_rocket_shift:
                break;
        }
    }
}
