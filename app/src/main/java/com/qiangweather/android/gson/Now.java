package com.qiangweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2019/7/19.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;//温度
    @SerializedName("cond")
    public More more;//其他

    public class More{
        @SerializedName("txt")
        public String info;//天气信息
    }
}
