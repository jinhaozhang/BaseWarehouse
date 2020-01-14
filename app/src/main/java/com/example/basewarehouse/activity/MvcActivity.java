package com.example.basewarehouse.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.basewarehouse.R;
import com.example.basewarehouse.bean.WeatherInfo;
import com.example.basewarehouse.mvc.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MvcActivity extends BaseActivity {

    @BindView(R.id.tv_net)
    TextView tv_net;
    @BindView(R.id.tv_list)
    TextView tv_list;

    @BindView(R.id.tv_pm10)
    TextView tv_pm10;
    @BindView(R.id.tv_pm25)
    TextView tv_pm25;
    @BindView(R.id.tv_quality)
    TextView tv_quality;
    @BindView(R.id.tv_wendu)
    TextView tv_wendu;
    @BindView(R.id.tv_shidu)
    TextView tv_shidu;
    @BindView(R.id.tv_ganmao)
    TextView tv_ganmao;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_area)
    TextView tv_area;

    private String url="/api/weather/city/101010100";

    @Override
    public int intiLayout() {
        return R.layout.layout_mvc_demo;
    }

    @Override
    public void initView() {
        tv_title.setText("mvc的模式demo");
    }

    @Override
    public void initData() {
        get(url,0);
    }

    @Override
    protected void returnData(int requestCode, String reslutData) {
        if(requestCode==0){
            WeatherInfo weatherInfo = gosn.fromJson(reslutData,WeatherInfo.class);
            tv_area.setText("地区："+weatherInfo.cityInfo.city);
            tv_time.setText("更新时间："+weatherInfo.time);
            tv_ganmao.setText("指数:"+weatherInfo.data.ganmao);
            tv_pm10.setText("pm10："+weatherInfo.data.pm10);
            tv_pm25.setText("pm25："+weatherInfo.data.pm25);
            tv_quality.setText("空气质量："+weatherInfo.data.quality);
            tv_shidu.setText("湿度："+weatherInfo.data.shidu);
            tv_wendu.setText("温度："+weatherInfo.data.wendu);
        }
        super.returnData(requestCode, reslutData);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2){
            String code =data.getStringExtra("code");
            url="/api/weather/city/"+ code;
            get(url,0);
        }
    }

    @OnClick({R.id.iv_btn,R.id.tv_list,R.id.tv_net})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_btn:
                finish();
                break;
            case R.id.tv_list:
                intent = new Intent(MvcActivity.this,MvcListActivity.class);
                intent.putExtra("weatherUrl",url);
                startActivityForResult(intent,1);
                break;
            case R.id.tv_net:
                intent = new Intent(MvcActivity.this,ChoiceCityActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }
}
