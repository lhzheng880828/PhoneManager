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
    public Context context;
    List<T> dataList = new ArrayList<T>();

    public MyBaseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public List<T> getData() {
        return dataList;
    }

    public void setData(List<T> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
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
