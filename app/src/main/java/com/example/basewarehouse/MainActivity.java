package com.example.basewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.basewarehouse.activity.MvcActivity;
import com.example.basewarehouse.app.Constants;
import com.example.basewarehouse.bean.CityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_mvc)
    Button btn_mvc;
    @BindView(R.id.btn_mvp)
    Button btn_mvp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        btn_mvp.setOnClickListener(this);
        btn_mvc.setOnClickListener(this);
    }

    private void initData(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    getAssets().open("city.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constants.cityBeanList.addAll(new Gson().fromJson(stringBuilder.toString(),new TypeToken<List<CityBean>>(){}.getType()));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v==btn_mvc){
            intent = new Intent(MainActivity.this, MvcActivity.class);
            startActivity(intent);
        }else if(v==btn_mvp){

        }
    }
}
