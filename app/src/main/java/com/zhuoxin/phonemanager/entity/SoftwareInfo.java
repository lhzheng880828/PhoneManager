package com.zhuoxin.phonemanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by YoungHong on 2016/11/11.
 */

public class SoftwareInfo {
    public boolean hasDelete;
    public Drawable appIcon;
    public String appName;
    public boolean isSystem;
    public String packageName;
    public String appVersion;

    public SoftwareInfo(boolean hasDelete, Drawable appIcon, String appName, boolean isSystem, String packageName, String appVersion) {
        this.hasDelete = hasDelete;
        this.appIcon = appIcon;
        this.appName = appName;
        this.isSystem = isSystem;
        this.packageName = packageName;
        this.appVersion = appVersion;
    }
}
