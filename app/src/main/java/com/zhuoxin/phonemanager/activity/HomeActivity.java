package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.MemoryManager;
import com.zhuoxin.phonemanager.view.CleanCircleView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Home界面，包括ActionBar、一键清理、底部导航栏目组
 *
 * @author YoungHong
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    //一键加速按钮
    CleanCircleView ccv_home_clean;
    ImageView iv_home_clean;
    TextView tv_home_clean;
    //底部菜单按钮
    TextView tv_telnumber;
    TextView tv_software;
    TextView tv_rocket;
    TextView tv_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initActionBar("手机管家", false, true, this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRunningMemory();
    }

    /**
     * 初始化本页面的控件及相关侦听事件的设置
     */
    private void initView() {

        //一键加速控件初始化
        ccv_home_clean = (CleanCircleView) findViewById(R.id.ccv_home_clean);
        iv_home_clean = (ImageView) findViewById(R.id.iv_home_clean);
        iv_home_clean.setOnClickListener(this);
        tv_home_clean = (TextView) findViewById(R.id.tv_home_clean);
        tv_home_clean.setOnClickListener(this);
        //底部控件初始化
        tv_telnumber = (TextView) findViewById(R.id.tv_telnumber);
        tv_telnumber.setOnClickListener(this);
        tv_software = (TextView) findViewById(R.id.tv_software);
        tv_software.setOnClickListener(this);
        tv_rocket = (TextView) findViewById(R.id.tv_rocket);
        tv_rocket.setOnClickListener(this);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
    }

    @OnClick(R.id.tv_file)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_home_clean:
            case R.id.tv_home_clean:
                MemoryManager.cleanRunningProcess(this, getPackageName());
                getRunningMemory();
                break;
            case R.id.iv_menu:
                startActivity(SettingsActivity.class);
                break;
            case R.id.tv_telnumber:
                startActivity(PhoneActivity.class);
                break;
            case R.id.tv_software:
                startActivity(SoftManagerActivity.class);
                break;
            case R.id.tv_rocket:
                startActivity(RocketActivity.class);
                break;
            case R.id.tv_phone:
                startActivity(PhoneStateActivity.class);
                break;
            case R.id.tv_file:
                startActivity(FileManagerActivity.class);
                break;
        }
    }

    private void getRunningMemory() {
        //获取当前运行控件所占百分比
        int process = MemoryManager.getUsedPercent(this);
        ccv_home_clean.setAngle((int) (3.6 * process), 6);
        tv_home_clean.setText(process + "%");
    }
}
