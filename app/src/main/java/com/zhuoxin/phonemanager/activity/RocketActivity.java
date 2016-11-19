package com.zhuoxin.phonemanager.activity;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.ProgressAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.utils.RAMUtil;

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

    /**
     * 获取运行空间
     */
    private void getRunMemory() {
        pb_runspace.setProgress(RAMUtil.getUsedPercent(this));
        tv_runspace.setText("剩余运行内存：" + RAMUtil.getAvailableMemoryStr(this) + "/" + RAMUtil.getTotalMemoryStr(this));
    }

    private void asyncLoadProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ActivityManager.RunningAppProcessInfo> temp = RAMUtil.getRunningProcess(RocketActivity.this);
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
                RAMUtil.cleanRunningProcess(this, getPackageName());
                asyncLoadProgress();
                getRunMemory();
                break;
        }
    }
}
