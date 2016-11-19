package com.zhuoxin.phonemanager.utils;

import android.content.Context;
import android.os.BatteryManager;

/**
 * Created by YoungHong on 2016/11/20.
 */

public class PhoneStateUtil {
    public static int getBatteryPercent(Context context) {

        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);

        return 1;
    }
}
