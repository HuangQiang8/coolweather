package com.coolweather.android;

import android.app.Application;

import org.litepal.LitePal;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

/**
 * Created by Administrator on 2019/7/18.
 */

public class CoolApplication extends Application {

    private static CoolApplication instance = null;
    @Override
    public void onCreate() {
        SQLiteStudioService.instance().start(this);
        CoolApplication.instance = this;
        super.onCreate();
    }
    public synchronized static CoolApplication getInstance() {
        return instance;
    }

}
