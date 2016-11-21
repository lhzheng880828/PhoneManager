package com.zhuoxin.phonemanager.biz;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import com.zhuoxin.phonemanager.process.ProcessManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by YoungHong on 2016/11/19.
 * 存储关于运行内存的相关方法
 *
 * @author YoungHong
 */

public class MemoryManager {

    /**
     * 获取运行内存的信息
     *
     * @param context
     * @return
     */
    public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取总运存大小
     *
     * @param context
     * @return
     */
    public static long getTotalMemoryLong(Context context) {
        return getMemoryInfo(context).totalMem;
    }

    /**
     * 获取总运存大小，自动换换单位
     *
     * @param context
     * @return
     */
    public static String getTotalMemoryStr(Context context) {
        return Formatter.formatFileSize(context, getTotalMemoryLong(context));
    }

    /**
     * 获取可用内存
     *
     * @param context
     * @return
     */
    public static long getAvailableMemoryLong(Context context) {
        return getMemoryInfo(context).availMem;
    }

    /**
     * 获取可用内存，自动转换单位
     *
     * @param context
     * @return
     */
    public static String getAvailableMemoryStr(Context context) {
        return Formatter.formatFileSize(context, getAvailableMemoryLong(context));
    }

    /**
     * 获取已用运存
     *
     * @param context
     * @return
     */
    public static long getUsedMemoryLong(Context context) {
        return getTotalMemoryLong(context) - getAvailableMemoryLong(context);
    }

    /**
     * 获取已用内存，自动转换单位
     *
     * @param context
     * @return
     */
    public static String getUsedMemoryString(Context context) {
        return Formatter.formatFileSize(context, getUsedMemoryLong(context));
    }

    /**
     * 获取已使用的百分比数值
     *
     * @param context
     * @return
     */
    public static int getUsedPercent(Context context) {
        return (int) (100.0 * getUsedMemoryLong(context) / getTotalMemoryLong(context));
    }

    /**
     * 获取未使用的百分比数值
     *
     * @param context
     * @return
     */
    public static int getAvailablePercent(Context context) {
        return 100 - getUsedPercent(context);
    }

    /**
     * 获取运行中的第三方进程
     *
     * @param context
     * @return
     */
    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcess(Context context) {
        List<ActivityManager.RunningAppProcessInfo> temp = new ArrayList<ActivityManager.RunningAppProcessInfo>();
        List<ActivityManager.RunningAppProcessInfo> infos = ProcessManager.getRunningAppProcessInfo(context);
        for (ActivityManager.RunningAppProcessInfo i : infos) {
            if (!i.processName.contains("android")) {
                temp.add(i);
            }
        }
        return temp;
    }

    /**
     * 清理运行中的进程
     *
     * @param doNotCleanPackageName
     */
    public static void cleanRunningProcess(Context context, String doNotCleanPackageName) {

        List<ActivityManager.RunningAppProcessInfo> clearList = getRunningProcess(context);
        for (ActivityManager.RunningAppProcessInfo info : clearList) {
            if (!info.processName.equals(doNotCleanPackageName)) {
                ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);//获得获得管理器
                am.killBackgroundProcesses(info.processName);//通过包名杀死关联进程
            }
        }

    }

    /**
     * 获取手机内置sdcard路径, 为null表示无
     */
    public static String getPhoneInSDCardPath() {
        String sdcardState = Environment.getExternalStorageState();
        if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取手机外置sdcard路径, 为null表示无
     */
    public static String getPhoneOutSDCardPath() {
        Map<String, String> map = System.getenv();
        if (map.containsKey("SECONDARY_STORAGE")) {
            String paths = map.get("SECONDARY_STORAGE");
            // /storage/extSdCard
            String path[] = paths.split(":");
            if (path == null || path.length <= 0) {
                return null;
            }
            return path[0];
        }
        return null;
    }
}
