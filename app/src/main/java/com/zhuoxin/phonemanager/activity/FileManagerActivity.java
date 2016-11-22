package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.FileManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

public class FileManagerActivity extends BaseActivity {

    @InjectView(R.id.tv_total)
    TextView tv_total;
    @InjectViews({R.id.pb_anyFile, R.id.pb_docFile, R.id.pb_videoFile, R.id.pb_audioFile, R.id.pb_imgFile, R.id.pb_rarFile, R.id.pb_apkFile})
    List<ProgressBar> pbList;
    @InjectViews({R.id.iv_anyFile, R.id.iv_docFile, R.id.iv_videoFile, R.id.iv_audioFile, R.id.iv_imgFile, R.id.iv_rarFile, R.id.iv_apkFile})
    List<ImageView> ivList;
    //FileManager和其接口
    private FileManager fileManager = new FileManager();
    private FileManager.SearchFileListener searchFileListener = new FileManager.SearchFileListener() {
        @Override
        public void searching(long size) {
            final long s = size;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_total.setText("已找到：" + Formatter.formatFileSize(FileManagerActivity.this, s));
                }
            });
        }

        @Override
        public void end(boolean endFlag) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < pbList.size(); i++) {
                        pbList.get(i).setVisibility(View.GONE);
                        ivList.get(i).setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(FileManagerActivity.this, "搜索完毕", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        ButterKnife.inject(this);
        initActionBar("文件管理", true, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        asyncLoadData();
    }

    private void asyncLoadData() {
        fileManager.setSearchFileListener(searchFileListener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileManager.searchSDCardFile();
            }
        }).start();
    }
}
