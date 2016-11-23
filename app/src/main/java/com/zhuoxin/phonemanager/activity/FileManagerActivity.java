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

public class FileManagerActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tv_total)
    TextView tv_total;
    @InjectViews({R.id.pb_anyFile, R.id.pb_docFile, R.id.pb_videoFile, R.id.pb_audioFile, R.id.pb_imgFile, R.id.pb_rarFile, R.id.pb_apkFile})
    List<ProgressBar> pbList;
    @InjectViews({R.id.iv_anyFile, R.id.iv_docFile, R.id.iv_videoFile, R.id.iv_audioFile, R.id.iv_imgFile, R.id.iv_rarFile, R.id.iv_apkFile})
    List<ImageView> ivList;
    //FileManager和其接口
    private FileManager fileManager = FileManager.getFileManager();
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
                        ivList.get(i).setOnClickListener(FileManagerActivity.this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.iv_anyFile:
                bundle.putString("fileType", "所有文件");
                startActivity(FileActivity.class, bundle);
                break;
            case R.id.iv_docFile:
                bundle.putString("fileType", "文本文件");
                startActivity(FileActivity.class, bundle);
                break;
            case R.id.iv_videoFile:
                bundle.putString("fileType", "视频文件");
                startActivity(FileActivity.class, bundle);
                break;
            case R.id.iv_audioFile:
                bundle.putString("fileType", "音频文件");
                startActivity(FileActivity.class, bundle);
                break;
            case R.id.iv_imgFile:
                bundle.putString("fileType", "图像文件");
                startActivity(FileActivity.class, bundle);
                break;
            case R.id.iv_rarFile:
                bundle.putString("fileType", "压缩文件");
                startActivity(FileActivity.class, bundle);
                break;
            case R.id.iv_apkFile:
                bundle.putString("fileType", "apk文件");
                startActivity(FileActivity.class, bundle);
                break;
        }
    }
}
