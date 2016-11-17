package com.zhuoxin.phonemanager.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.MyBaseAdapter;

/**
 * Created by YoungHong on 2016/11/17.
 */

public class ProgressAdapter extends MyBaseAdapter<ActivityManager.RunningAppProcessInfo> {
    public ProgressAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_software, null);
            holder = new ViewHolder();
            holder.cb_delete = (CheckBox) convertView.findViewById(R.id.cb_delete);
            holder.iv_appicon = (ImageView) convertView.findViewById(R.id.iv_appicon);
            holder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
            holder.tv_appversion = (TextView) convertView.findViewById(R.id.tv_appversion);
            holder.tv_packagename = (TextView) convertView.findViewById(R.id.tv_packagename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //设置控件的内容
        ActivityManager.RunningAppProcessInfo info = getItem(position);
        holder.cb_delete.setVisibility(View.GONE);
        holder.tv_appversion.setVisibility(View.INVISIBLE);
        holder.tv_packagename.setText(info.processName);
        try {
            holder.iv_appicon.setImageDrawable(context.getPackageManager().getApplicationIcon(info.processName));
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(info.processName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            holder.tv_appname.setText(context.getPackageManager().getApplicationLabel(applicationInfo));

        } catch (PackageManager.NameNotFoundException e) {
            holder.iv_appicon.setImageResource(R.mipmap.ic_launcher);
            holder.tv_appname.setText("未知应用");
        }
        return convertView;
    }

    private static class ViewHolder {
        CheckBox cb_delete;
        ImageView iv_appicon;
        TextView tv_appname;
        TextView tv_appversion;
        TextView tv_packagename;
    }
}
