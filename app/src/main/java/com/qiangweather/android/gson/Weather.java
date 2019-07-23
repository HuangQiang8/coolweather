package com.qiangweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2019/7/19.
 */

public class Weather  {
    public String status;//状态
    public Basic basic;//基础信息
    public AQI aqi;//空气指数
    public  Now now;//天气信息
    public  Suggestion suggestion;//建议
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;//后几天天气预报

}
