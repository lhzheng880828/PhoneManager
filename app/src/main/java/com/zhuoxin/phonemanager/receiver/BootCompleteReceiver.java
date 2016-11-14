package com.zhuoxin.phonemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhuoxin.phonemanager.service.MusicService;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean startWhenBootComplete = context.getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("startWhenBootComplete", false);
        if (startWhenBootComplete) {
            Intent i = new Intent(context, MusicService.class);
            context.startService(i);
        }
    }
}
