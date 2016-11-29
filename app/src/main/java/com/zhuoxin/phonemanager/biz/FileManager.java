package com.zhuoxin.phonemanager.biz;

import android.util.Log;

import com.zhuoxin.phonemanager.entity.FileInfo;
import com.zhuoxin.phonemanager.utils.FileTypeUtil;

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

    ;
    /**
     * 外置存储卡目录( 可能为 null)
     */
    public static File outSdcardDir = null;
    private static FileManager fileManager = new FileManager();

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
    public boolean isSearching = false;
    /**
     * 任意文件( 非目录) 集合
     */
    private ArrayList<FileInfo> anyFileList = new ArrayList<FileInfo>();
    private long anyFileSize = 0;//  任意文件总大小( 在搜索过程中，实时迭加，计算总大小)
    /**
     * 文档文件集合
     */
    private ArrayList<FileInfo> docFileList = new ArrayList<FileInfo>();
    private long docFileSize; // 文档文件总大小(同上)
    /**
     * 视频文件集合
     */
    private ArrayList<FileInfo> videoFileList = new ArrayList<FileInfo>();
    private long videoFileSize; // 视频文件总大小(同上)
    /**
     * 音乐文件集合
     */
    private ArrayList<FileInfo> audioFileList = new ArrayList<FileInfo>();
    private long audioFileSize; // 音乐文件总大小(同上)
    /**
     * 图像文件集合
     */
    private ArrayList<FileInfo> imgFileList = new ArrayList<FileInfo>();
    private long imgFileSize; // 图像文件总大小(同上)
    /**
     * ZIP文件集合
     */
    private ArrayList<FileInfo> rarFileList = new ArrayList<FileInfo>();
    private long rarFileSize; // ZIP文件总大小(同上)
    /**
     * APK文件集合
     */
    private ArrayList<FileInfo> apkFileList = new ArrayList<FileInfo>();
    private long apkFileSize; // APK文件总大小(同上)
    /**
     * 侦听回调接口
     */
    private SearchFileListener listener;

    private FileManager() {
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    // 初始化相关变量(在每次重新开始搜索前,如searchSDCardFile())------------------------------------------------
    private void initData() {
        isSearching = true;
        anyFileSize = 0;
        docFileSize = 0;
        videoFileSize = 0;
        audioFileSize = 0;
        imgFileSize = 0;
        rarFileSize = 0;
        apkFileSize = 0;
        anyFileList.clear();
        docFileList.clear();
        videoFileList.clear();
        audioFileList.clear();
        imgFileList.clear();
        rarFileList.clear();
        apkFileList.clear();
    }

    /**
     * 搜索存储卡目录下的所有文件,结果实时保存在 {@link #anyFileList}内
     */
    public void searchSDCardFile() {
        if (isSearching) {
            return;
        } else {
            initData();
            searchFile(inSdcardDir, true); // 传入false标记, 不让运算反馈结束
            searchFile(outSdcardDir, false); // 传入true标记, 让运算反馈结束
        }
    }

    // 搜索 文件方法
    private void searchFile(File file, boolean endFlag) {

        //#1排除" 不正常" 文件
        if (file == null || !file.canRead() || !file.exists()) {
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
            if (endFlag) {
                isSearching = false;
                callbackSearchFileListenerEnd(false);// 回调接口end()方法,搜索结束，完成，非异常结束
            }
        } else {
            //#3如果是文件( 非目录)
            Log.d(TAG, "searchFile:-- " + file.getPath());
            //  判断文件大小
            if (file.length() <= 0) {
                return;
            }

            //如果文件名称中没有“.”  未知文件类型
            if (file.getName().lastIndexOf('.') == -1) {
                return;
            }
            //获取图标以及文件类型
            String[] iconAndTypeNames = FileTypeUtil.getFileIconAndTypeName(file);
            final String iconName = iconAndTypeNames[0]; // 此文件使用什么图像(在Res中的图标文件名称)
            final String typeName = iconAndTypeNames[1]; // 此文件是属什么类型的
            // 添加到所有文件的集合
            FileInfo fileInfo = new FileInfo(file, iconName, typeName);
            anyFileList.add(fileInfo);
            // 迭加计算总文件大小
            anyFileSize += file.length();
            // 分类
            if (typeName.equals(FileTypeUtil.TYPE_APK)) {
                apkFileSize += file.length();
                apkFileList.add(fileInfo);
            } else if (typeName.equals(FileTypeUtil.TYPE_AUDIO)) {
                audioFileSize += file.length();
                audioFileList.add(fileInfo);
            } else if (typeName.equals(FileTypeUtil.TYPE_IMAGE)) {
                imgFileSize += file.length();
                imgFileList.add(fileInfo);
            } else if (typeName.equals(FileTypeUtil.TYPE_TXT)) {
                docFileSize += file.length();
                docFileList.add(fileInfo);
            } else if (typeName.equals(FileTypeUtil.TYPE_VIDEO)) {
                videoFileSize += file.length();
                videoFileList.add(fileInfo);
            } else if (typeName.equals(FileTypeUtil.TYPE_ZIP)) {
                rarFileSize += file.length();
                rarFileList.add(fileInfo);
            }
            //  回调接口 searching  方法( 用作通知调用者数据更新了)
            callbackSearchFileListenerSearching(anyFileSize);
            return;
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
    private void callbackSearchFileListenerEnd(boolean endFlag) {
        if (listener != null) {
            listener.end(endFlag);
        }
    }

    public ArrayList<FileInfo> getAnyFileList() {
        return anyFileList;
    }

    public ArrayList<FileInfo> getDocFileList() {
        return docFileList;
    }

    public ArrayList<FileInfo> getVideoFileList() {
        return videoFileList;
    }

    public ArrayList<FileInfo> getAudioFileList() {
        return audioFileList;
    }

    public ArrayList<FileInfo> getImgFileList() {
        return imgFileList;
    }

    public ArrayList<FileInfo> getRarFileList() {
        return rarFileList;
    }

    public ArrayList<FileInfo> getApkFileList() {
        return apkFileList;
    }

    public long getAnyFileSize() {
        return anyFileSize;
    }

    public void setAnyFileSize(long anyFileSize) {
        this.anyFileSize = anyFileSize;
    }

    public long getRarFileSize() {
        return rarFileSize;
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public void deleteFile(File file) {
        //如果是文件夹
        if (file.isDirectory()) {
            File fileList[] = file.listFiles();
            //如果文件夹中没有其他的内容，则直接删除并返回
            if (fileList == null || fileList.length <= 0) {
                file.delete();
                return;
            }
            //如果有数据，则递归调用
            for (File f : fileList) {
                deleteFile(f);
            }
            //如果文件夹中的内容都清空了，把这个文件夹删除
            file.delete();

        } else {
            file.delete();
        }
    }


    /**
     * 搜索过程的监听{@link#setSearchFileListener(SearchFileListener listener)}
     */
    public interface SearchFileListener {
        //每找到一个文件，来调用searching方法
        void searching(long size);

        //搜索结束时调用
        void end(boolean endFlag);
    }
}
