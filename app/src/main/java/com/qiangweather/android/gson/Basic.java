package com.qiangweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2019/7/19.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

    
}
