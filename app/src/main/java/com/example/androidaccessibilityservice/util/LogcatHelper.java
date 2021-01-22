package com.example.androidaccessibilityservice.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogcatHelper {

    private static LogcatHelper INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper logDumper = null;
    private int pid;


    private LogcatHelper(Context context) {
        init(context);
        pid = android.os.Process.myPid();
    }

    public static LogcatHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LogcatHelper(context);
        }
        return INSTANCE;
    }

    /**
     * 初始化目录
     */
    private void init(Context context) {
        // 优先保存到SD卡中
//        PATH_LOGCAT = Environment.getExternalStorageDirectory() + File.separator + "Anti-recall";
        PATH_LOGCAT = context.getExternalFilesDir("logs") + File.separator;

        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            System.out.println("logcat: make dir: " + file.mkdirs());
        }


    }

    public void start() {
        if (logDumper == null) {
            logDumper = new LogDumper(String.valueOf(pid), PATH_LOGCAT);
            logDumper.start();
        }
    }

    public void stop() {
        if (logDumper != null) {
            logDumper.stopLogs();
            logDumper = null;
        }
    }

}
