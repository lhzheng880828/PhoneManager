package com.zhuoxin.phonemanager.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.SoftwareAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.entity.SoftwareInfo;

import java.util.ArrayList;
import java.util.List;

public class SoftwareActivity extends BaseActivity {

    List<SoftwareInfo> softwareInfoList = new ArrayList<SoftwareInfo>();
    ListView ll_software;

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
    }

    private void initView() {
        String appType = getIntent().getBundleExtra("bundle").getString("appType", "all");
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
                }
            } else if (appType.equals("user")) {
                if (!softwareInfo.isSystem) {
                    softwareInfoList.add(softwareInfo);
                }
            } else {
                softwareInfoList.add(softwareInfo);
            }
        }
        //ll
        ll_software = (ListView) findViewById(R.id.ll_software);
        SoftwareAdapter adapter = new SoftwareAdapter(this);
        adapter.setData(softwareInfoList);
        ll_software.setAdapter(adapter);
    }

}
