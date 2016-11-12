package com.zhuoxin.phonemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
            holder.cb_app = (CheckBox) convertView.findViewById(R.id.cb_app);
            holder.iv_appicon = (ImageView) convertView.findViewById(R.id.iv_appicon);
            holder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
            holder.tv_appversion = (TextView) convertView.findViewById(R.id.tv_appversion);
            holder.tv_packagename = (TextView) convertView.findViewById(R.id.tv_packagename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //设置控件的内容
        SoftwareInfo info = getItem(position);
        holder.cb_app.setChecked(info.hasDelete);
        if (info.appIcon != null) {
            holder.iv_appicon.setImageDrawable(info.appIcon);
        }
        holder.tv_appname.setText(info.appName);
        holder.tv_appversion.setText(info.appVersion);
        holder.tv_packagename.setText(info.packageName);

        return convertView;
    }

    private static class ViewHolder {
        CheckBox cb_app;
        ImageView iv_appicon;
        TextView tv_appname;
        TextView tv_appversion;
        TextView tv_packagename;
    }
}
