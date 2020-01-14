package com.example.basewarehouse.activity;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.basewarehouse.R;
import com.example.basewarehouse.bean.WeatherInfo;
import com.example.basewarehouse.mvc.ListBaseActivity;
import com.example.basewarehouse.viewholder.ListViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MvcListActivity extends ListBaseActivity<WeatherInfo.ForecastInfo, MvcListActivity.ViewHolder> {

    private String url;

    @Override
    public void init() {
        super.init();
        url=getIntent().getStringExtra("weatherUrl");
    }

    @Override
    public void initAddView(LinearLayout ll_top, LinearLayout ll_bottom) {
        super.initAddView(ll_top, ll_bottom);
        tv_title.setText("未来一周的天气");
    }

    @Override
    public void getData(int requestCode) {
        get(url,0);
    }


    @Override
    public List<WeatherInfo.ForecastInfo> analysisJson(String json) {
        WeatherInfo weatherInfo = gosn.fromJson(json,WeatherInfo.class);
        return weatherInfo.data.forecast;
    }

    @Override
    public int createItemView() {
        return R.layout.layout_weather_item;
    }

    @Override
    protected ViewHolder createRecyclerViewHolder(View view) {
        return new ViewHolder(view);
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
    public void onBindData(ViewHolder viewHolder,WeatherInfo.ForecastInfo forecastInfo, int i) {
        viewHolder.tv_date.setText(forecastInfo.ymd);
        viewHolder.tv_notice.setText(forecastInfo.notice);
        viewHolder.tv_fx.setText(forecastInfo.fx+"   "+forecastInfo.fl);
        viewHolder.tv_wendu.setText(forecastInfo.low+"-"+forecastInfo.high);
        viewHolder.tv_week.setText(forecastInfo.week);
        viewHolder.tv_weather.setText(forecastInfo.type);
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
