package com.zhuoxin.phonemanager.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.SoftwareAdapter;
import com.zhuoxin.phonemanager.entity.SoftwareInfo;

import java.util.ArrayList;
import java.util.List;

public class SoftwareActivity extends AppCompatActivity {

    List<SoftwareInfo> softwareInfoList = new ArrayList<SoftwareInfo>();
    ListView ll_software;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        //数据
        List<ApplicationInfo> applicationInfolist = getPackageManager().getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (ApplicationInfo info : applicationInfolist) {
            String name = (String) getPackageManager().getApplicationLabel(info);
            String type = "";
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) >0 ){
                type = "系统";
            }else{
                type = "第三方";
            }

            Drawable drawable = info.loadIcon(getPackageManager());
            SoftwareInfo softwareInfo = new SoftwareInfo(drawable, name, type);
            softwareInfoList.add(softwareInfo);
        }
        //ll
        ll_software = (ListView) findViewById(R.id.ll_software);
        SoftwareAdapter adapter = new SoftwareAdapter(this);
        adapter.setData(softwareInfoList);
        ll_software.setAdapter(adapter);
    }


}
