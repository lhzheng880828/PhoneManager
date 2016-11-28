package com.zhuoxin.phonemanager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phonemanager.R;
import com.zhuoxin.phonemanager.base.MyBaseAdapter;
import com.zhuoxin.phonemanager.entity.FileInfo;
import com.zhuoxin.phonemanager.utils.FileTypeUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by YoungHong on 2016/11/24.
 */

public class FileAdapter extends MyBaseAdapter<FileInfo> {
    HashMap<String, SoftReference<Bitmap>> bitmapCache = new HashMap<String, SoftReference<Bitmap>>();
    LruCache<String,Bitmap> bitmapLruCache = new LruCache<String, Bitmap>(1024*1024){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getHeight()*value.getRowBytes();
        }
    };
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
        int width = 80;
        Bitmap bitmap = null;
        //去软引用，如果软引用为空，则重新加载。
        SoftReference<Bitmap> reference = bitmapCache.get(getItem(position));
        if (reference == null) {
            bitmap = getBitmap(getItem(position), width);
        } else {
            bitmap = reference.get();
        }
        viewHolder.iv_fileIcon.setImageBitmap(bitmap);
        viewHolder.tv_fileName.setText(getItem(position).getFile().getName());
        viewHolder.tv_fileType.setText(getItem(position).getFileType());
        return convertView;
    }

    /**
     * 根据文件类型获取文件的图标，并对图片的缩放率进行设置
     *
     * @param fileInfo
     * @param width
     * @return
     */
    private Bitmap getBitmap(FileInfo fileInfo, int width) {
        //创建Bitmap位图，获取图标
        Bitmap bitmap = null;
        //图片资源
        if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //把图片信息保存在option里面
            BitmapFactory.decodeFile(fileInfo.getFile().getAbsolutePath(), options);
            //计算缩放率并设置缩放率
            int scale = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
            scale = scale / width;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(fileInfo.getFile().getAbsolutePath(), options);

        } else {
            int icon = context.getResources().getIdentifier(fileInfo.getIconName(), "drawable", context.getPackageName());
            bitmap = BitmapFactory.decodeResource(context.getResources(), icon);
        }
        bitmapCache.put(fileInfo.getFile().getName(), new SoftReference<Bitmap>(bitmap));
        return bitmap;

    }

    static class ViewHolder {
        CheckBox cb_file;
        ImageView iv_fileIcon;
        TextView tv_fileName;
        TextView tv_fileType;
    }
}
