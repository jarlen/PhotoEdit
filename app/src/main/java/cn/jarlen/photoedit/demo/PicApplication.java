package cn.jarlen.photoedit.demo;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * DESCRIBE:
 * Created by jarlen on 2016/10/11.
 */

public class PicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "fab25e4f00", false);
    }
}
