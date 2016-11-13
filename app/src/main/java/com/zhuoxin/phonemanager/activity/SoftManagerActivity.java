package com.zhuoxin.phonemanager.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.view.PiechartView;

import java.io.File;

public class SoftManagerActivity extends BaseActivity implements View.OnClickListener {


    PiechartView pv_phonespace;
    ProgressBar pb_phonespace;
    TextView tv_phonespace;
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
        //初始化控件
        pv_phonespace = (PiechartView) findViewById(R.id.pv_phonespace);
        pb_phonespace = (ProgressBar) findViewById(R.id.pb_phonespace);
        tv_phonespace = (TextView) findViewById(R.id.tv_phonespace);
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        rl_system = (RelativeLayout) findViewById(R.id.rl_system);
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        rl_all.setOnClickListener(this);
        rl_system.setOnClickListener(this);
        rl_user.setOnClickListener(this);
        int permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            initProgress();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initProgress();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("请跳转到权限界面手动设置权限").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }).setNegativeButton("CANCEL", null).create();
            alertDialog.show();

        }
    }

    private void initProgress() {

        File file = Environment.getExternalStorageDirectory();
        //获取数据，定义角度
        long total = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        String totalSpace = Formatter.formatFileSize(this, total);
        String currentSpace = Formatter.formatFileSize(this, freeSpace);

        Log.v("test", currentSpace + "---------" + totalSpace);

        int present = 100 - (int) (100.0 * freeSpace / total);
        int angle = (int) (3.6 * present);
        pv_phonespace.drawPiechart(0, angle);
        pb_phonespace.setMax(100);
        pb_phonespace.setProgress(present);
        tv_phonespace.setText("可用空间:" + currentSpace + "/" + totalSpace);
        Toast.makeText(this, present + "%", Toast.LENGTH_LONG).show();
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
