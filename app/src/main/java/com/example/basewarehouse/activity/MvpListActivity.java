package com.example.basewarehouse.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.basewarehouse.R;
import com.example.basewarehouse.bean.WeatherInfo;
import com.example.basewarehouse.contacts.WeatherContacts;
import com.example.basewarehouse.mvp.IPresenter;
import com.example.basewarehouse.mvp.IView;
import com.example.basewarehouse.mvp.MvpBaseListActivity;
import com.example.basewarehouse.presenter.WeatherPresenter;
import com.example.basewarehouse.viewholder.ListViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

public class MvpListActivity extends MvpBaseListActivity<WeatherInfo.ForecastInfo, MvpListActivity.ViewHolder, WeatherPresenter> implements WeatherContacts.WeatherInfoUi {


    private String code;

    @Override
    public void init() {
        super.init();
        code = getIntent().getStringExtra("code");
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("未来一周的天气");
    }

    @Override
    protected IPresenter<IView> presenterBind() {
        return new WeatherPresenter(this);
    }

    @Override
    protected int initItemView() {
        return R.layout.layout_weather_item;
    }

    @Override
    protected void showDataItem(ViewHolder viewHolder, WeatherInfo.ForecastInfo forecastInfo, int position) {
        viewHolder.tv_date.setText(forecastInfo.ymd);
        viewHolder.tv_notice.setText(forecastInfo.notice);
        viewHolder.tv_fx.setText(forecastInfo.fx+"   "+forecastInfo.fl);
        viewHolder.tv_wendu.setText(forecastInfo.low+"-"+forecastInfo.high);
        viewHolder.tv_week.setText(forecastInfo.week);
        viewHolder.tv_weather.setText(forecastInfo.type);
    }

    @Override
    protected ViewHolder initViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void getData(int requestCode) {
        ((WeatherPresenter)presenter).getWeatherInfo(0,code);
    }

    @Override
    public void showWeatherInfo(WeatherInfo weatherInfo) {
        showData(weatherInfo.data.forecast);
    }

    @OnClick({R.id.iv_btn})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_btn:
                finish();
                break;
        }
    }

    @Override
    public void showFailUI(int requesCode) {

    }

    @Override
    public void showBeforeNetUI(int requesCode) {

    }

    public class ViewHolder extends ListViewHolder {
        @BindView(R.id.tv_notice)
        TextView tv_notice;
        @BindView(R.id.tv_fx)
        TextView tv_fx;
        @BindView(R.id.tv_wendu)
        TextView tv_wendu;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_week)
        TextView tv_week;
        @BindView(R.id.tv_weather)
        TextView tv_weather;
        public ViewHolder(View view) {
            super(view);
        }
    }
}
