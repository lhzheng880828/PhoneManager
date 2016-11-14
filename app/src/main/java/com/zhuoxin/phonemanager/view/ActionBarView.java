package com.zhuoxin.phonemanager.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;

/**
 * Created by YoungHong on 2016/11/09.
 */

public class ActionBarView extends RelativeLayout {
    private ImageView iv_back;
    private ImageView iv_menu;
    private TextView tv_title;

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //填充布局到当前ActionBarView
        inflate(context, R.layout.layout_actionbar, this);
        this.setBackgroundResource(R.drawable.shape_blue);
        if(Build.VERSION.SDK_INT>=21){
            this.setElevation(30);
        }
        //初始化控件
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        tv_title = (TextView) findViewById(R.id.tv_title);

    }


    public void initActionBar(String title, boolean hasBackButton, boolean hasMenuButton, OnClickListener listener) {
        tv_title.setText(title);
        if (hasBackButton) {
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setOnClickListener(listener);
        } else {
            iv_back.setVisibility(View.INVISIBLE);
        }
        if (hasMenuButton) {
            iv_menu.setVisibility(View.VISIBLE);
            iv_menu.setOnClickListener(listener);
        } else {
            iv_menu.setVisibility(View.INVISIBLE);
        }
    }
}
