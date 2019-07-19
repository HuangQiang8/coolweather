package com.coolweather.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.coolweather.android.R;

import org.litepal.LitePal;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteStudioService.instance().start(this);
        LitePal.getDatabase();
    }
}
