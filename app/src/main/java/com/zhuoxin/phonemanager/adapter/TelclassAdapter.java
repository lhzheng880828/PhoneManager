package com.zhuoxin.phonemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.MyBaseAdapter;
import com.zhuoxin.phonemanager.entity.TelclassInfo;

/**
 * Created by YoungHong on 2016/11/04.
 */

public class TelclassAdapter extends MyBaseAdapter<TelclassInfo> {
    public TelclassAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_telclass, null);
            viewHolder.tv_telclass = (TextView) view.findViewById(R.id.tv_telclass);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TelclassInfo info = getItem(i);
        viewHolder.tv_telclass.setText(info.name);
        return view;
    }

    private static class ViewHolder {
        TextView tv_telclass;
    }
}
