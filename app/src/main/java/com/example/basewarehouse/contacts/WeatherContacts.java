package com.example.basewarehouse.contacts;

import com.example.basewarehouse.bean.WeatherInfo;
import com.example.basewarehouse.mvp.IPresenter;
import com.example.basewarehouse.mvp.IView;
import com.example.basewarehouse.mvp.UIView;

public final class WeatherContacts {
    public interface WeatherInfoUi extends UIView{
        void showWeatherInfo(WeatherInfo weatherInfo);
    }

    public interface GetWeatherInfoData extends IPresenter<IView>{
        void getWeatherInfo(int code,String url);
    }
}
