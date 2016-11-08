package com.zhuoxin.phonemanager.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.TelnumberAdapter;
import com.zhuoxin.phonemanager.db.DBManager;
import com.zhuoxin.phonemanager.entity.TelnumberInfo;

import java.util.List;

public class PhoneNumberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView ll_numberlist;
    List<TelnumberInfo> dataList;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        String tableName = "table" + getIntent().getIntExtra("idx", 1);
        ll_numberlist = (ListView) findViewById(R.id.ll_numberlist);
        TelnumberAdapter adapter = new TelnumberAdapter(this);
        dataList = DBManager.readTeldbNumberlist(this, tableName);
        adapter.setData(dataList);
        ll_numberlist.setAdapter(adapter);
        ll_numberlist.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        //权限检查
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //如果没获取到权限，弹窗获取权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                //解释后再获取权限
                Toast.makeText(this, "需要获取权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);

            } else {

                showMessageOKCancel("请您打开设置界面中的[权限]选项，然后选中[电话]选项。",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);

                                /*
                                ActivityCompat.requestPermissions(PhoneNumberActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, 1);*/
                            }
                        });
            }
        } else {
            String number = dataList.get(position).number;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //获取到了权限

        } else {
            //没获取到权限
            Toast.makeText(this, "获取权限失败，请重新获取", Toast.LENGTH_SHORT).show();
        }
    }
}
