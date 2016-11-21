package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.FileManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FileManagerActivity extends BaseActivity {

    @InjectView(R.id.tv_total)
    TextView tv_total;

    //FileManager和其接口
    private FileManager fileManager = new FileManager();
    private FileManager.SearchFileListener searchFileListener = new FileManager.SearchFileListener() {
        @Override
        public void searching(long size) {
            final long s = size;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_total.setText(Formatter.formatFileSize(FileManagerActivity.this, s));
                }
            });
        }

        @Override
        public void end(boolean isExceptionEnd) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
