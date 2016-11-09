package com.zhuoxin.phonemanager.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {

    //找到对应的控件
    ViewPager vp_guide;
    ImageView iv_circle_red;
    TextView tv_skip;
    //存储View
    List<View> viewList = new ArrayList<View>();
    int redCirclePadding;
    float scaleLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            initView();
            startService(MusicService.class);
        } else {
            startActivity(HomeActivity.class);
            finish();
        }


    }


    private void initView() {
        //初始化
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        iv_circle_red = (ImageView) findViewById(R.id.iv_circle_red);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        redCirclePadding = iv_circle_red.getPaddingLeft();
        scaleLength = getDensity() * 40;
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
                startActivity(HomeActivity.class);
                finish();
            }
        });
        //设置Adapter中的数据
        viewList.add(getLayoutInflater().inflate(R.layout.layout_pager_guide1, null));
        viewList.add(getLayoutInflater().inflate(R.layout.layout_pager_guide2, null));
        viewList.add(getLayoutInflater().inflate(R.layout.layout_pager_guide3, null));
        //创建PagerAdapte
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        vp_guide.setAdapter(pagerAdapter);
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                iv_circle_red.setPadding((int) (redCirclePadding + scaleLength * (position + positionOffset)), 0, 0, 0);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == viewList.size() - 1) {
                    tv_skip.setVisibility(View.VISIBLE);
                } else {
                    tv_skip.setVisibility(View.INVISIBLE);
                }
                iv_circle_red.setPadding((int) (redCirclePadding + scaleLength * position), 0, 0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void finish() {
        stopService(MusicService.class);
        super.finish();
    }

    private float getDensity() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        return density;
    }

}
