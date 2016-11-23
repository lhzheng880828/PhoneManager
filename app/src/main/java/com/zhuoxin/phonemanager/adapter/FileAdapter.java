package com.zhuoxin.phonemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.MyBaseAdapter;
import com.zhuoxin.phonemanager.entity.FileInfo;

/**
 * Created by YoungHong on 2016/11/24.
 */

public class FileAdapter extends MyBaseAdapter<FileInfo> {
    public FileAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_file, null);
            viewHolder = new ViewHolder();
            viewHolder.cb_file = (CheckBox) convertView.findViewById(R.id.cb_file);
            viewHolder.iv_fileIcon = (ImageView) convertView.findViewById(R.id.iv_fileIcon);
            viewHolder.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
            viewHolder.tv_fileType = (TextView) convertView.findViewById(R.id.tv_fileType);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cb_file.setTag(position);
        viewHolder.cb_file.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getItem((Integer) viewHolder.cb_file.getTag()).setSelect(isChecked);
            }
        });
        viewHolder.cb_file.setChecked(getItem(position).isSelect());
        //图标
        viewHolder.tv_fileName.setText(getItem(position).getFile().getName());
        viewHolder.tv_fileType.setText(getItem(position).getFileType());
        return convertView;
    }

    static class ViewHolder {
        CheckBox cb_file;
        ImageView iv_fileIcon;
        TextView tv_fileName;
        TextView tv_fileType;
    }
}
