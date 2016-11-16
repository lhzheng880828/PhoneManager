package com.zhuoxin.phonemanager.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    boolean startWhenBootComplete = false;
    ToggleButton tb_start;
    ToggleButton tb_notification;
    ToggleButton tb_push;
    ImageView iv_about;
    ImageView iv_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar("系统设置", true, false, new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        tb_start = (ToggleButton) findViewById(R.id.tb_start);
        tb_notification = (ToggleButton) findViewById(R.id.tb_notification);
        tb_push = (ToggleButton) findViewById(R.id.tb_push);
        iv_about = (ImageView) findViewById(R.id.iv_about);
        iv_help = (ImageView) findViewById(R.id.iv_help);
        tb_start.setOnClickListener(this);
        tb_notification.setOnClickListener(this);
        tb_push.setOnClickListener(this);
        iv_about.setOnClickListener(this);
        iv_help.setOnClickListener(this);
        startWhenBootComplete = getSharedPreferences("config", MODE_PRIVATE).getBoolean("startWhenBootComplete", false);
        tb_start.setChecked(startWhenBootComplete);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tb_start:
                getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("startWhenBootComplete", tb_start.isChecked()).commit();
                break;
            case R.id.tb_notification:
                if (tb_notification.isChecked()) {
                    Intent resultIntent = new Intent(this, HomeActivity.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.item_arrow_right))
                                    .setContentTitle("手机管家")
                                    .setContentText("进入Home界面")
                                    .setContentIntent(resultPendingIntent)
                                    .setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0, mBuilder.build());

                } else {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.cancel(0);
                }

                break;
            case R.id.tb_push:
                Toast.makeText(this, "该功能随后上线", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_about:
                Toast.makeText(this, "跳转到关于界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_help:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromSettings", true);
                startActivity(GuideActivity.class, bundle);
                break;
        }
    }
}
