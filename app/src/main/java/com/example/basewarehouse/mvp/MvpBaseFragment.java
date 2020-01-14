package com.example.basewarehouse.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * mvp模式下Fragment的基类
 * @param <P>presenter解析类
 */
public abstract class MvpBaseFragment <P extends IPresenter<IView>>  extends Fragment implements IView{

    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";
    protected static final String ARG_PARAM3 = "param3";

    protected String mParam1;
    protected String mParam2;
    protected Object mParam3;

    InputMethodManager imm;

    protected Context context;

    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 列表数据的展示请求参数
     */
    protected int sizeCount=10;
    protected int pagerCount=1;
    protected boolean isDropDown=false;
    protected boolean isPullUp=false;
    protected String json;
    protected int offset=0;

    protected Gson gosn;
    private boolean isCreate=false;
    protected boolean isShow=false;

    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveData();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = getContext();
        presenter = bindPresenter();
        gosn = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(initLayout(), null);
        ButterKnife.bind(this,view);
        initView();
        initData();
        return view;
    }

    /**
     * 初始化布局文件
     * @return
     */
    protected abstract int initLayout();

    /**
     * 初始化view
     */
    protected abstract  void initView();

    /**
     * 获取数据
     */
    protected abstract void initData();

    /**
     * 接受传入的参数
     */
    protected void receiveData() {
    }

    protected void openInputMethod(final View view){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    protected void closeInputMethod(){
        imm.hideSoftInputFromWindow( getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }

    protected void closeDialog(){
        if(this.getActivity()!=null&&this != null && !this.getActivity().isFinishing()) {
        }
    }
    protected void showDialog(){
        if(this.getActivity()!=null&&this != null && !this.getActivity().isFinishing()) {
        }
    }

    @Override
    public void onDestroy() {
//        NetUtils.getInstance().cancelTag(TAG);
        super.onDestroy();
    }



    /**
     * 实例化presenter
     * @return
     */
    protected abstract P bindPresenter();



    @Override
    public Activity getSelfActivity() {
        return getActivity();
    }

}
