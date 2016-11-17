package com.zhuoxin.phonemanager.activity;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.ProgressAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.process.ProcessManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机加速界面，显示手机的基本信息和运行空间，并可以清理进程
 *
 * @author YoungHong
 * @since 2016年11月17日
 */
public class RocketActivity extends BaseActivity implements View.OnClickListener {

    private final int MSG_WHAT = 0;
    //控件
    TextView tv_brand;
    TextView tv_version;
    TextView tv_runspace;
    ProgressBar pb_runspace;
    ProgressBar pb_rocket_loading;
    Button btn_rocket_shift;
    ListView lv_rocket;
    //数据
    ActivityManager activityManager;
    boolean isSystem;//系统进程
    List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = new ArrayList<ActivityManager.RunningAppProcessInfo>();
    ProgressAdapter adapter;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            Toast.makeText(RocketActivity.this, runningAppProcessInfos.size() + "", Toast.LENGTH_LONG).show();
            adapter.setData((List<ActivityManager.RunningAppProcessInfo>) msg.obj);
            adapter.notifyDataSetChanged();
            pb_rocket_loading.setVisibility(View.GONE);
            lv_rocket.setVisibility(View.VISIBLE);
            return false;
        }
    });

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


    /**
     * 初始化控件
     */
    private void initView() {
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_runspace = (TextView) findViewById(R.id.tv_runspace);
        pb_runspace = (ProgressBar) findViewById(R.id.pb_runspace);
        pb_rocket_loading = (ProgressBar) findViewById(R.id.pb_rocket_loading);
        btn_rocket_shift = (Button) findViewById(R.id.btn_rocket_shift);
        lv_rocket = (ListView) findViewById(R.id.lv_rocket);
        btn_rocket_shift.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //品牌、版本号、运行空间
        tv_brand.setText(Build.BRAND);
        tv_version.setText("Android " + Build.VERSION.RELEASE);
        getRunMemory();
        //ListView
        adapter = new ProgressAdapter(this);
        adapter.setData(runningAppProcessInfos);
        lv_rocket.setAdapter(adapter);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    }

    private void getRunMemory() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        long total = memoryInfo.totalMem;
        long free = memoryInfo.availMem;
        long used = total - free;
        pb_runspace.setProgress((int) (100.0 * used / total));
        String totalSpace = Formatter.formatFileSize(this, total);
        String freeSpace = Formatter.formatFileSize(this, free);
        tv_runspace.setText("剩余运行内存：" + freeSpace + "/" + totalSpace);
    }

    private void asyncLoadProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ActivityManager.RunningAppProcessInfo> temp = new ArrayList<ActivityManager.RunningAppProcessInfo>();
                List<ActivityManager.RunningAppProcessInfo> infos = ProcessManager.getRunningAppProcessInfo(RocketActivity.this);
                for (ActivityManager.RunningAppProcessInfo i : infos) {
                    if (!i.processName.contains("android")) {
                        temp.add(i);
                    }
                }
                Message msg = handler.obtainMessage();
                msg.what = MSG_WHAT;
                msg.obj = temp;
                handler.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_rocket_shift:
                List<ActivityManager.RunningAppProcessInfo> clearList = adapter.getData();
                for (ActivityManager.RunningAppProcessInfo info : clearList) {
                    if (!info.processName.equals(getPackageName())) {
                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);//获得获得管理器
                        am.killBackgroundProcesses(info.processName);//通过包名杀死关联进程
                        //Process.killProcess(info.pid);
                    }

                }
                asyncLoadProgress();
                getRunMemory();
                break;
        }
    }
}
