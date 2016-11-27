package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.MemoryManager;
import com.zhuoxin.phonemanager.db.DBManager;
import com.zhuoxin.phonemanager.view.CleanCircleView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Home界面，包括ActionBar、一键清理、底部导航栏目组
 *
 * @author YoungHong
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    //一键加速按钮
    @InjectView(R.id.ccv_home_clean)
    CleanCircleView ccv_home_clean;
    @InjectView(R.id.tv_home_clean)
    TextView tv_home_clean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initActionBar("手机管家", false, true, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRunningMemory();
    }


    /**
     * 注入单击事件
     *
     * @param v
     */
    @OnClick({R.id.iv_home_clean, R.id.tv_home_clean, R.id.iv_menu, R.id.tv_telnumber, R.id.tv_software, R.id.tv_rocket, R.id.tv_phone, R.id.tv_file, R.id.tv_clean})
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
            case R.id.tv_clean:
                DBManager.copyAssetsFileToSDCardFile(this, "clearpath.db", new File(this.getFilesDir() ,"clearpath.db"));
                startActivity(CleanActivity.class);
                break;
        }
    }

    /**
     * 获取运行空间
     */
    private void getRunningMemory() {
        //获取当前运行控件所占百分比
        int process = MemoryManager.getUsedPercent(this);
        ccv_home_clean.setAngle((int) (3.6 * process), 10);
        tv_home_clean.setText(process + "%");
    }
}
