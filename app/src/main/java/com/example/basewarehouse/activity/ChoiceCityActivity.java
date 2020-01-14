package com.example.basewarehouse.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basewarehouse.R;
import com.example.basewarehouse.app.Constants;
import com.example.basewarehouse.bean.CityBean;
import com.example.basewarehouse.mvc.ListBaseActivity;
import com.example.basewarehouse.viewholder.ListViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ChoiceCityActivity extends ListBaseActivity<CityBean, ChoiceCityActivity.ViewHolder> {

    @Override
    public void getData(int requestCode) {
        showData(Constants.cityBeanList);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title.setText("选择城市");
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
    public List<CityBean> analysisJson(String json) {
        return null;
    }

    @Override
    public int createItemView() {
        return R.layout.activity_choice_city;
    }

    @Override
    protected ViewHolder createRecyclerViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindData(ViewHolder viewHolder, CityBean cityBean, int i) {
        viewHolder.tv_city.setText(cityBean.city_name);
        viewHolder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("code",cityBean.city_code);
                setResult(2,intent);
                finish();
            }
        });
    }

    public class ViewHolder extends ListViewHolder {
        @BindView(R.id.tv_city)
        TextView tv_city;
        @BindView(R.id.ll_root)
        LinearLayout ll_root;
        public ViewHolder(View view) {
            super(view);
        }
    }

}
