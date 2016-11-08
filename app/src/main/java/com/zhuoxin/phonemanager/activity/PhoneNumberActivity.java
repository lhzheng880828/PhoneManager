package com.zhuoxin.phonemanager.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.TelnumberAdapter;
import com.zhuoxin.phonemanager.db.DBManager;
import com.zhuoxin.phonemanager.entity.TelnumberInfo;

import java.util.List;

public class PhoneNumberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView tv_title;
    ListView ll_numberlist;
    List<TelnumberInfo> dataList;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_numberlist = (ListView) findViewById(R.id.ll_numberlist);
        String name = getIntent().getStringExtra("name");
        tv_title.setText(name);
        String tableName = "table" + getIntent().getIntExtra("idx", 1);
        TelnumberAdapter adapter = new TelnumberAdapter(this);
        dataList = DBManager.readTeldbNumberlist(this, tableName);
        adapter.setData(dataList);
        ll_numberlist.setAdapter(adapter);
        ll_numberlist.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        number = dataList.get(position).number;
        if (Build.VERSION.SDK_INT >= 23) {
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
                call(number);

            }
        } else {
            call(number);
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
            call(number);
        } else {
            //没获取到权限
            Toast.makeText(this, "获取权限失败，请重新获取", Toast.LENGTH_SHORT).show();
        }
    }

    private void call(final String number) {
        new AlertDialog.Builder(this)
                .setMessage("您是否确认要拨打" + number + "?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }
}
