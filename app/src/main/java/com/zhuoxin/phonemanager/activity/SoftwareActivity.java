package com.zhuoxin.phonemanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.SoftwareAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.entity.SoftwareInfo;

import java.util.ArrayList;
import java.util.List;

public class SoftwareActivity extends BaseActivity implements View.OnClickListener {

    List<SoftwareInfo> softwareInfoList = new ArrayList<SoftwareInfo>();
    ProgressBar pb_software;
    ListView ll_software;
    CheckBox cb_deleteAll;
    Button btn_delete;
    SoftwareAdapter adapter;
    BroadcastReceiver receiver;
    String appType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        initActionBar("手机软件", true, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        registerBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        //初始化控件
        pb_software = (ProgressBar) findViewById(R.id.pb_software);
        ll_software = (ListView) findViewById(R.id.ll_software);
        cb_deleteAll = (CheckBox) findViewById(R.id.cb_deleteAll);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        appType = getIntent().getBundleExtra("bundle").getString("appType", "all");
        cb_deleteAll.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        asyncLoadData();

    }

    private void asyncLoadData() {
        pb_software.setVisibility(View.VISIBLE);
        ll_software.setVisibility(View.INVISIBLE);
        softwareInfoList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //数据
                List<ApplicationInfo> applicationInfolist = getPackageManager().getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
                for (ApplicationInfo info : applicationInfolist) {
                    String name = (String) getPackageManager().getApplicationLabel(info);
                    boolean isSystem = true;
                    if ((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                        isSystem = true;
                    } else {
                        isSystem = false;
                    }
                    String packageName = info.packageName;
                    String version = "";
                    try {
                        version = getPackageManager().getPackageInfo(packageName, 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    Drawable drawable = info.loadIcon(getPackageManager());
                    SoftwareInfo softwareInfo = new SoftwareInfo(false, drawable, name, isSystem, packageName, version);
                    if (appType.equals("system")) {
                        if (softwareInfo.isSystem) {
                            softwareInfoList.add(softwareInfo);
                            cb_deleteAll.setClickable(false);
                        }
                    } else if (appType.equals("user")) {
                        if (!softwareInfo.isSystem) {
                            softwareInfoList.add(softwareInfo);
                        }
                    } else {
                        softwareInfoList.add(softwareInfo);
                    }
                }

                adapter = new SoftwareAdapter(SoftwareActivity.this);
                adapter.setData(softwareInfoList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_software.setVisibility(View.GONE);
                        ll_software.setVisibility(View.VISIBLE);
                        ll_software.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    //注册广播接收者
    private void registerBroadcastReceiver() {
        //新建广播接收者
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "删除了啦啦啦啦啦", Toast.LENGTH_LONG).show();
                asyncLoadData();
            }
        };
        //注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cb_deleteAll:
                List<SoftwareInfo> infoList = adapter.getData();
                for (SoftwareInfo info : infoList) {
                    if (!info.isSystem) {
                        info.hasDelete = cb_deleteAll.isChecked();
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_delete:
                List<SoftwareInfo> infosList = adapter.getData();
                for (SoftwareInfo info : infosList) {
                    if (info.hasDelete) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + info.packageName));
                        startActivity(intent);
                    }

                }
                break;
        }

    }
}
