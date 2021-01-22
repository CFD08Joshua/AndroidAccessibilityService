package com.example.androidaccessibilityservice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogDumper extends Thread{

    String cmd = null;
    SimpleDateFormat sdf;
    private Process logcatProcess;
    private BufferedReader reader = null;
    private boolean isRunning = true;
    private String pid;
    private FileOutputStream out = null;

    LogDumper(String pid, String dir) {
        sdf = new SimpleDateFormat("MM-dd", Locale.CHINA);
        this.pid = pid;
        try {
            out = new FileOutputStream(new File(dir, "Anti-recall-"
                    + sdf.format(new Date()) + ".log"), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*
         *
         * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
         *
         * 顯示當前mPID程序的 E和W等級的日誌.
         *
         */
        cmd = "logcat *:e *:i | grep \"(" + this.pid + ")\"";
//            cmd = "logcat -s";

    }

    public void stopLogs() {
        isRunning = false;
    }

    @Override
    public void run() {
        sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        try {
            logcatProcess = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(
                    logcatProcess.getInputStream()), 4096);
            String line;
            while (isRunning && (line = reader.readLine()) != null) {
                if (!isRunning) {
                    break;
                }
                if (line.length() == 0) {
                    continue;
                }

                if (out != null && line.contains(pid)) {
                    out.write((line + "\n").getBytes());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logcatProcess != null) {
                logcatProcess.destroy();
                logcatProcess = null;
            }
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }

        }

    }

}
