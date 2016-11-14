package com.zhuoxin.phonemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.db.DBManager;

import java.io.File;

public class SplashActivity extends BaseActivity {

    ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_logo.startAnimation(alphaAnimation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!DBManager.isExistsTeldbFile(this)) {
            String assetsPath = "commonnum.db";
            File sdCardFile = new File(getFilesDir().getAbsolutePath(), "commonnum.db");
            DBManager.copyAssetsFileToSDCardFile(this, assetsPath, sdCardFile);
        }
    }
}
