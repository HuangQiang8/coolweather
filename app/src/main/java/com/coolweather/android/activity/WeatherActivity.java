package com.coolweather.android.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.R;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUitl;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degressTxt;
    private TextView weahterInfoTxt;
    private LinearLayout forecastLayout;
    private TextView aqiTxt;
    private TextView pm25Txt;
    private TextView comfortTxt;
    private TextView carWashTxt;
    private TextView sportTxt;
    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        intView();//初始化控件
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weahterString = sharedPreferences.getString("weather", null);
        if (weahterString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weahterString);
            showWeatherInfo(weather);
        } else {
            //无缓存是去服务器查询天气
            String weahter_id = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeahter(weahter_id);
        }
        String bingPic = sharedPreferences.getString("bing_pic",null);
        if(bingPic !=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
    }

    private void intView() {
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.update_date);
        degressTxt = (TextView) findViewById(R.id.degree_txt);
        weahterInfoTxt = (TextView) findViewById(R.id.weather_info_txt);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiTxt = (TextView) findViewById(R.id.aqi_txt);
        pm25Txt = (TextView) findViewById(R.id.pm25_txt);
        comfortTxt = (TextView) findViewById(R.id.comfort_txt);
        carWashTxt = (TextView) findViewById(R.id.car_wash_txt);
        sportTxt = (TextView) findViewById(R.id.sport_txt);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
    }


    /**
     * 根据天气id请求城市天气信息
     *
     * @param weahter_id
     */
    private void requestWeahter(String weahter_id) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weahter_id + "&key=7ff3c1dac3714ab3b953cd3d6c305c48";
        HttpUitl.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status.equals("ok")) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences((WeatherActivity.this)).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * 处理并展示Weahter实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degressTxt.setText(degree + "℃");
        weahterInfoTxt.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_txt);
            TextView infoText = (TextView) view.findViewById(R.id.info_txt);
            TextView maxText = (TextView) view.findViewById(R.id.max_txt);
            TextView minText = (TextView) view.findViewById(R.id.min_txt);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi !=null){
            aqiTxt.setText(weather.aqi.city.aqi);
            pm25Txt.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度 : " + weather.suggestion.comfort.comfortTxt;
        String carWash = "洗车指数 : " + weather.suggestion.carWash.carWashTxt;
        String sport = "运动建议 : " + weather.suggestion.sport.sportTxt;
        comfortTxt.setText(comfort);
        carWashTxt.setText(carWash);
        sportTxt.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUitl.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
              final String bingPic = response.body().string();
              SharedPreferences.Editor editor = PreferenceManager
                      .getDefaultSharedPreferences(WeatherActivity.this).edit();
              editor.putString("bing_pic",bingPic);
              editor.apply();
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                  }
              });
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }

        });
    }
}
