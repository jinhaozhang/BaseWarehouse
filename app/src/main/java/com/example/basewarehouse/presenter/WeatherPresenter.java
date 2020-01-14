package com.example.basewarehouse.presenter;

import com.example.basewarehouse.bean.WeatherInfo;
import com.example.basewarehouse.contacts.WeatherContacts;
import com.example.basewarehouse.mvp.BasePresenter;
import com.example.basewarehouse.net.CallBackListener;
import com.example.basewarehouse.net.NetTools;

public class WeatherPresenter extends BasePresenter<WeatherContacts.WeatherInfoUi> implements WeatherContacts.GetWeatherInfoData {


    public WeatherPresenter(WeatherContacts.WeatherInfoUi view) {
        super(view);
    }

    @Override
    public void getWeatherInfo(int code,String url) {
        NetTools.getInstance().get("/api/weather/city/" + url, new CallBackListener<WeatherInfo>() {
            @Override
            public void onSucessData(int code, String responsejson, WeatherInfo weatherInfo) {
                MvpRef.get().showWeatherInfo(weatherInfo);
            }

            @Override
            public void onFailure(int ce, String msg) {
                MvpRef.get().showFailUI(code);
            }

            @Override
            public void onBeforeResponse() {
                MvpRef.get().showBeforeNetUI(code);
            }
        });
    }
}
