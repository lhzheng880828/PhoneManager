package com.zhuoxin.phonemanager.db;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by YoungHong on 2016/11/03.
 */
public class DBManagerTest {
    Context context ;
    Environment environment ;
    @Before
    public void setUp() throws Exception {
        context = getContext();

    }

    @Test
    public void copyAssetsFileToSDCardFile() throws Exception {
        String assetsPath = "commonnum.db";
        File sdCardFile = new File(context.getFilesDir().getAbsolutePath() , "commonnum.db");
        Log.e("test","~~~~~~~~~~~"+sdCardFile.getAbsolutePath());
        DBManager.copyAssetsFileToSDCardFile(context,assetsPath,sdCardFile);
    }



}