package com.zhuoxin.phonemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

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
        initView();
        startMusicService();

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
                Intent intent = new Intent(GuideActivity.this, PhoneActivity.class);
                startActivity(intent);
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

    private void startMusicService() {
        Intent intent = new Intent(GuideActivity.this, MusicService.class);
        startService(intent);
    }

    @Override
    public void finish() {
        Intent intent = new Intent(GuideActivity.this, MusicService.class);
        stopService(intent);
        super.finish();
    }

    private float getDensity() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        return density;
    }

}
