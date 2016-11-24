package com.zhuoxin.phonemanager.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.FileAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.FileManager;
import com.zhuoxin.phonemanager.entity.FileInfo;
import com.zhuoxin.phonemanager.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件展示类
 */
public class FileActivity extends BaseActivity {
    String fileType;
    ListView lv_file;
    FileAdapter adapter;
    Button btn_file_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        getFileType();
        initActionBar(fileType, true, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_file = (ListView) findViewById(R.id.lv_file);
        btn_file_delete = (Button) findViewById(R.id.btn_file_delete);
        adapter = new FileAdapter(this);
        adapter.setData(getFileList(fileType));
        lv_file.setAdapter(adapter);
        lv_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo fileInfo = adapter.getItem(position);
                File file = fileInfo.getFile();
                // 取出此文件的后缀名　－> MIMEType
                String type = FileTypeUtil.getMIMEType(file);
                // 打开这个文件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), type);
                startActivity(intent);
            }
        });

        btn_file_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FileInfo> tempList = new ArrayList<FileInfo>();
                tempList.addAll(adapter.getData());
                for (FileInfo info : tempList) {
                    if (info.isSelect()) {
                        adapter.getData().remove(info);
                        FileManager.getFileManager().getAnyFileList().remove(info);
                        FileManager.getFileManager().getAudioFileList().remove(info);
                        long size = FileManager.getFileManager().getAnyFileSize();
                        FileManager.getFileManager().setAnyFileSize(size -= info.getFile().length());
                        info.getFile().delete();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getFileType() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        fileType = bundle.getString("fileType", "所有文件");
    }

    private List<FileInfo> getFileList(String fileType) {
        switch (fileType) {
            case "文本文件":
                return FileManager.getFileManager().getDocFileList();

            case "视频文件":
                return FileManager.getFileManager().getVideoFileList();

            case "音频文件":
                return FileManager.getFileManager().getAudioFileList();

            case "图像文件":
                return FileManager.getFileManager().getImgFileList();

            case "压缩文件":
                return FileManager.getFileManager().getRarFileList();

            case "apk文件":
                return FileManager.getFileManager().getApkFileList();
            default:
                return FileManager.getFileManager().getAnyFileList();
        }
    }
}
