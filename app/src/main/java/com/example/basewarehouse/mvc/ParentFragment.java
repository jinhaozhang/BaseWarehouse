package com.example.basewarehouse.mvc;

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

import com.example.basewarehouse.R;
import com.example.basewarehouse.net.NetCallBack;
import com.example.basewarehouse.net.NetUtils;
import com.google.gson.Gson;
import com.walkermanx.photopicker.PhotoPreview;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public abstract class ParentFragment extends Fragment {

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
    protected boolean isAllLoad=false;//列表数据是否加载完

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveData();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = getContext();
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
     * 接受传进来的数据值
     */
    public void receiveData(){

    }

    /**
     * 初始化布局文件
     */
    public abstract int initLayout();

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 获取数据
     */
    public abstract void initData();

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

    /**
     * 设置浏览大图片
     * @param current
     * @param imgList
     */
    protected void showPhoto(int current, ArrayList<String> imgList){
        //图片浏览,全屏模式
        ArrayList<String> imgData = new ArrayList<>();
        imgData.clear();
        imgData.addAll(imgList);
        PhotoPreview.builder()
                //设置浏览的图片数据
                .setPhotos(imgData)
                //设置点击后浏览的是第几张图
                .setCurrentItem(current)
                //浏览时不要标题栏
                //setShowDeleteButton浏览时显示删除按钮.
                .setShowToolbar(false)
                //设置主题色系 toolBar背景色 statusBar颜色 以及toolBar 文本/overflow Icon着色
                .setThemeColors(R.color.colorPrimary, R.color.colorPrimary, R.color.white)
                //设置toolBar标题栏于NavigationIcon的边距
                .setToolbarTitleMarginStart(R.dimen.__picker_toolbar_title_margin_start)
                //开启浏览时长按后显示PopuWindow,分享、保存、取消 等，可以自定义。
//                            .setOnLongClickListData(onLongClickListData)
                .start(getActivity());
    }

    /**
     * post的请求方法
     * @param postUrl
     * @param json
     */
    protected void post(String postUrl, String json, final int requestCode){
        NetUtils.getInstance().post(TAG,postUrl, json, new NetCallBack() {
            @Override
            public void onSucess(String data) {
                returnData(requestCode,data);
                closeDialog();
            }

            @Override
            public void onException(int code, String message) {
                otherException(code,requestCode,message);
                closeDialog();
            }

            @Override
            public void onNetFailure(String content) {
                netError(requestCode,content);
                closeDialog();
            }

            @Override
            public void onBeforeRes() {
                beforeNet(requestCode);
                if(isDropDown||isPullUp){

                }else {
                    showDialog();
                }
            }
        });
    }
    /**
     * get的请求方法
     * @param postUrl
     */
    protected void get(String postUrl, final int requestCode){
        NetUtils.getInstance().get(TAG,postUrl, new NetCallBack() {
            @Override
            public void onSucess(String data) {
                returnData(requestCode,data);
                closeDialog();
            }

            @Override
            public void onException(int code, String message) {
                otherException(code,requestCode,message);
                closeDialog();
            }

            @Override
            public void onNetFailure(String content) {
                netError(requestCode,content);
                closeDialog();
            }

            @Override
            public void onBeforeRes() {
                beforeNet(requestCode);
                if(isDropDown||isPullUp){

                }else {
                    showDialog();
                }
            }
        });
    }

    /**
     * 返回的请求的数据
     * @param requestCode
     * @param reslutData
     */
    protected void returnData(int requestCode, String reslutData){}

    /**
     * 网络失败的返回
     * @param requestCode
     * @param conyent
     */
    protected void netError(int requestCode, String conyent){
        if(isPullUp&&pagerCount>1){
            pagerCount--;
        }
    }

    /**
     * 其他错误的返回数据
     * @param requestCode
     * @param message
     */
    protected void otherException(int erreoCode, int requestCode, String message){
        if(isPullUp&&pagerCount>1){
            pagerCount--;
        }
    }

    /**
     * 请求之前要做的操作
     * @param requestCode
     */
    protected void beforeNet(int requestCode){}

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

    protected void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

}
