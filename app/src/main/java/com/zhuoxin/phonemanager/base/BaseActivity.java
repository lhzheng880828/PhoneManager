package com.zhuoxin.phonemanager.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.view.ActionBarView;

/**
 * Created by YoungHong on 2016/11/09.
 */

public class BaseActivity extends AppCompatActivity {
    public void startActivity(Class targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    public void startActivity(Class targetActivity, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void startService(Class targetService) {
        Intent intent = new Intent(this, targetService);
        startService(intent);
    }

    public void stopService(Class targetService) {
        Intent intent = new Intent(this, targetService);
        stopService(intent);
    }

    public void initActionBar(String title, boolean hasBackButton, boolean hasMenuButton, View.OnClickListener listener) {
        ActionBarView actionBarView = (ActionBarView) findViewById(R.id.layout_actionbar);
        actionBarView.initActionBar(title, hasBackButton, hasMenuButton, listener);
    }
}
