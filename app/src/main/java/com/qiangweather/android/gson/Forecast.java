package com.qiangweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2019/7/19.
 */

public class Forecast {
    public String date;
    @SerializedName("cond")
    public More more;
    @SerializedName("tmp")
    public Temperature temperature;

    public class More{
        @SerializedName("txt_d")
        public String info;//天气信息
    }
    public class Temperature{
        public String max;//最高温度
        public String min;//最低温度

    }
}
