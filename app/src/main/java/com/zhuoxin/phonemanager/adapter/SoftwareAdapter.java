package com.zhuoxin.phonemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.MyBaseAdapter;
import com.zhuoxin.phonemanager.entity.SoftwareInfo;

/**
 * Created by YoungHong on 2016/11/11.
 */

public class SoftwareAdapter extends MyBaseAdapter<SoftwareInfo> {
    public SoftwareAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_software, null);
            holder = new ViewHolder();
            holder.iv_appicon = (ImageView) convertView.findViewById(R.id.iv_appicon);
            holder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
            holder.tv_apptype = (TextView) convertView.findViewById(R.id.tv_apptype);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SoftwareInfo info = getItem(position);
        if (info.appIcon != null) {
            holder.iv_appicon.setImageDrawable(info.appIcon);
        }
        holder.tv_appname.setText(info.appName);
        holder.tv_apptype.setText(info.appType);

        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_appicon;
        TextView tv_appname;
        TextView tv_apptype;
    }
}
