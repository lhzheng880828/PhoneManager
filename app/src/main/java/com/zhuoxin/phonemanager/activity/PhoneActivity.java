package com.zhuoxin.phonemanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.adapter.TelclassAdapter;
import com.zhuoxin.phonemanager.db.DBManager;
import com.zhuoxin.phonemanager.entity.TelclassInfo;

import java.util.List;

public class PhoneActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView ll_classlist;
    List<TelclassInfo> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ll_classlist = (ListView) findViewById(R.id.ll_classlist);
        dataList = DBManager.readTeldbClasslist(this);
        TelclassAdapter adapter = new TelclassAdapter(this);
        adapter.setData(dataList);
        ll_classlist.setAdapter(adapter);
        ll_classlist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int idx = dataList.get(position).idx;
        Intent intent = new Intent(this, PhoneNumberActivity.class);
        intent.putExtra("idx", idx);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_activity_in,R.anim.anim_activity_out);
    }
}
