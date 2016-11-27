package com.zhuoxin.phonemanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhuoxin.phonemanager.entity.TelclassInfo;
import com.zhuoxin.phonemanager.entity.TelnumberInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungHong on 2016/11/03.
 */

public class DBManager {
    /**
     * 把assets文件夹下面的的文件复制到sd卡中
     *
     * @param context
     * @param assetsPath
     * @param sdCardFile
     */
    public static void copyAssetsFileToSDCardFile(Context context, String assetsPath, File sdCardFile) {
        InputStream is = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            is = context.getAssets().open(assetsPath);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(sdCardFile));

            byte[] b = new byte[1024];
            int count = 0;
            while ((count = bis.read(b)) != -1) {
                bos.write(b, 0, count);
            }
            bos.flush();
        } catch (Exception e) {

        } finally {
            try {
                bos.close();
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 判断是否存在数据库文件
     *
     * @param context
     * @return
     */
    public static boolean isExistsTeldbFile(Context context) {
        File file = new File(context.getFilesDir(), "commonnum.db");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取数据库TelClass中的信息
     *
     * @param context
     * @return
     */
    public static List<TelclassInfo> readTeldbClasslist(Context context) {
        List<TelclassInfo> telclassInfos = new ArrayList<TelclassInfo>();
        File sqlFile = new File(context.getFilesDir(), "commonnum.db");
        if (sqlFile.exists()) {
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqlFile, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM classlist ", null);
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                int idx = cursor.getInt(cursor.getColumnIndex("idx"));
                TelclassInfo telclassInfo = new TelclassInfo(name, idx);
                telclassInfos.add(telclassInfo);
            }
        }

        return telclassInfos;

    }

    /**
     * 查询对应的信息表
     *
     * @param context
     * @param tableName
     * @return
     */
    public static List<TelnumberInfo> readTeldbNumberlist(Context context, String tableName) {
        List<TelnumberInfo> telnumberInfos = new ArrayList<TelnumberInfo>();
        File sqlFile = new File(context.getFilesDir(), "commonnum.db");
        if (sqlFile.exists()) {
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqlFile, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM  " + tableName, null);
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                String number = cursor.getString(cursor.getColumnIndex("number"));
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                TelnumberInfo telnumberInfo = new TelnumberInfo(_id, name, number);
                telnumberInfos.add(telnumberInfo);
            }
        }
        return telnumberInfos;
    }

    /**
     * 获取所有文件的地址
     *
     * @param context
     * @param tableName
     * @return
     */
    public static List<String> getFilePath(Context context) {
        List<String> filePath = new ArrayList<String>();
        File sqlFile = new File(context.getFilesDir(), "clearpath.db");
        if (sqlFile.exists()) {
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqlFile, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM softdetail", null);
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex("filepath"));
                filePath.add(path);
            }
        }
        return filePath;
    }
}
