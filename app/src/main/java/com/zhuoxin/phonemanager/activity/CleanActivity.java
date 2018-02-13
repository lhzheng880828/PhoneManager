package com.zhuoxin.phonemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.FileAdapter;
import com.zhuoxin.phonemanager.base.BaseActivity;
import com.zhuoxin.phonemanager.biz.FileManager;
import com.zhuoxin.phonemanager.biz.MemoryManager;
import com.zhuoxin.phonemanager.db.DBManager;
import com.zhuoxin.phonemanager.entity.FileInfo;
import com.zhuoxin.phonemanager.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CleanActivity extends BaseActivity {

    private static final String TAG = "CleanActivity";
    @InjectView(R.id.lv_clean)
    ListView lv_clean;
    @InjectView(R.id.btn_clean_delete)
    Button btn_clean_delete;
    FileAdapter fileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        ButterKnife.inject(this);
        initActionBar("手机清理", true, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fileAdapter = new FileAdapter(this);
        lv_clean.setAdapter(fileAdapter);
        initData();
    }

    /**
     * 查找数据并初始化列表
     */
    private void initData() {
        List<String> filePath = DBManager.getFilePath(this);
        String sdPath = MemoryManager.getPhoneInSDCardPath();
        for (String path : filePath) {
            File file = new File(sdPath, path);
            if (file.exists()) {
                //Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                FileInfo fileInfo = new FileInfo(file, FileTypeUtil.getFileIconAndTypeName(file)[0], FileTypeUtil.TYPE_ANY);
                fileAdapter.getData().add(fileInfo);
            }
        }
        fileAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_clean_delete)
    public void OnClick(View view) {
        List<FileInfo> tempList = new ArrayList<FileInfo>();
        tempList.addAll(fileAdapter.getData());
        for (FileInfo fileInfo : tempList) {
            if (fileInfo.isSelect()) {
                FileManager.getFileManager().deleteFile(fileInfo.getFile());
                fileAdapter.getData().remove(fileInfo);
            }
        }
        fileAdapter.notifyDataSetChanged();
    }
}
