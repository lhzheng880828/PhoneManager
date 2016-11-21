package com.zhuoxin.phonemanager.biz;

import android.util.Log;

import com.zhuoxin.phonemanager.entity.FileInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by YoungHong on 2016/11/21.
 * 文件管理类
 * 主要完成文件大小的读取，分类以及删除
 *
 * @author YoungHong
 */

public class FileManager {
    private static final String TAG = "FileManager";
    /**
     * 内置存储卡目录( 可能为 null)
     */
    public static File inSdcardDir = null;
    /**
     * 外置存储卡目录( 可能为 null)
     */
    public static File outSdcardDir = null;

    static {
        //如果有手机内置 sdcard  卡路径, 进行内置 File  实例化( 防止 File file =new File(null))
        if (MemoryManager.getPhoneInSDCardPath() != null) {
            inSdcardDir = null;
            inSdcardDir = new File(MemoryManager.getPhoneInSDCardPath());
        }
        //如果有手机外置 sdcard  卡路径, 进行外置 File  实例化( 防止 File file =new File(null))
        if (MemoryManager.getPhoneOutSDCardPath() != null) {
            outSdcardDir = null;
            outSdcardDir = new
                    File(MemoryManager.getPhoneOutSDCardPath());
        }
    }

    //搜寻结束的标志
    public boolean isStopSearch = false;
    /**
     * 任意文件( 非目录) 集合
     */
    private ArrayList<FileInfo> anyFileList = new ArrayList<FileInfo>();
    //  任意文件总大小( 在搜索过程中，实时迭加，计算总大小)
    private long anyFileSize = 0;
    private SearchFileListener listener;

    /**
     * 搜索存储卡目录下的所有文件,结果实时保存在 {@link #anyFileList}内
     */
    public void searchSDCardFile() {
        if (anyFileList == null || anyFileList.size() <= 0) {
            anyFileList.clear();
            anyFileSize = 0;
            searchFile(inSdcardDir, false); // 传入false标记, 不让运算反馈结束
            searchFile(outSdcardDir, true); // 传入true标记, 让运算反馈结束
        } else {
            // 直接回调非异常结束
            callbackSearchFileListenerEnd(false);
        }
    }

    // 搜索 文件方法
    private void searchFile(File file, boolean isFirstRunFlag) {
        // ----中止搜索------
        if (isStopSearch) {
            // 是首次运行的结束(搜索结束)
            if (isFirstRunFlag) {
                callbackSearchFileListenerEnd(true);// 回调接口end()方法,搜索结束(异常结束)
            }
            return;
        }
        //#1排除" 不正常" 文件
        if (file == null || !file.canRead() || !file.exists()) {
            if (isFirstRunFlag) {
                callbackSearchFileListenerEnd(true);// 回调接口end()方法,搜索结束(异常结束)
            }
            return;
        } else if (file.isDirectory()) {
            // #2  如果是目录
            Log.d(TAG, "searchFile:++ " + file.getPath());
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                File tmpFile = files[i];
                //  递归
                searchFile(tmpFile, false);
            }

            // 是首次运行的结束(搜索结束)
            if (isFirstRunFlag) {
                callbackSearchFileListenerEnd(false);// 回调接口end()方法,搜索结束，完成，非异常结束
            }
        } else {
            //#3如果是文件( 非目录)
            Log.d(TAG, "searchFile:-- " + file.getPath());
            //  判断文件大小
            if (file.length() <= 0) {
                return;
            }
            anyFileList.add(new FileInfo(file, null, file.getName()));
            anyFileSize += file.length();
            //  回调接口 searching  方法( 用作通知调用者数据更新了)
            callbackSearchFileListenerSearching(anyFileSize);
        }

    }

    /**
     * 设置文件查找监听
     */
    public void setSearchFileListener(SearchFileListener listener) {
        this.listener = listener;
    }

    /**
     * 用来回调 SearchFileListener  接口内方法
     */
    private void callbackSearchFileListenerSearching(long size) {

        if (listener != null) {
            listener.searching(size);
        }
    }

    /**
     * 用来回调 SearchFileListener接口内方法
     */
    private void callbackSearchFileListenerEnd(boolean isExceptionEnd) {
        if (listener != null) {
            listener.end(isExceptionEnd);
        }
    }


    /**
     * 搜索过程的监听{@link#setSearchFileListener(SearchFileListener listener)}
     */
    public interface SearchFileListener {
        //每找到一个文件，来调用searching方法
        void searching(long size);

        //搜索结束时调用
        void end(boolean isExceptionEnd);
    }

}
