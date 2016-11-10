package com.zhuoxin.phonemanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by YoungHong on 2016/11/11.
 */

public class SoftwareInfo {
    public Drawable appIcon;
    public String appName;
    public String appType;

    public SoftwareInfo(Drawable appIcon, String appName, String appType) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appType = appType;
    }
}
