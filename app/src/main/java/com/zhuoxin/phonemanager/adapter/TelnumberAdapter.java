package com.zhuoxin.phonemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.MyBaseAdapter;
import com.zhuoxin.phonemanager.entity.TelnumberInfo;

/**
 * Created by YoungHong on 2016/11/06.
 */

public class TelnumberAdapter extends MyBaseAdapter<TelnumberInfo> {
    public TelnumberAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_telnumber, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TelnumberInfo info = getItem(position);
        viewHolder.tv_name.setText(info.name);
        viewHolder.tv_number.setText(info.number);
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_name;
        TextView tv_number;
    }
}
