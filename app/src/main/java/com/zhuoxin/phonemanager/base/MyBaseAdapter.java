package com.zhuoxin.phonemanager.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungHong on 2016/11/04.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public LayoutInflater layoutInflater;
    List<T> dataList = new ArrayList<T>();
    Context context;

    public MyBaseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<T> dataList) {
        this.dataList.addAll(dataList);
    }
    public List<T> getData(){
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
