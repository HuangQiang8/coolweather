package com.coolweather.android.gson;

import com.coolweather.android.db.City;

/**
 * Created by Administrator on 2019/7/19.
 */

public class AQI {
    public AQICity city;
    public class  AQICity{
        public String aqi;
        public String pm25;
    }
}
