package com.example.basewarehouse.mvc;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.example.basewarehouse.R;
import com.example.basewarehouse.net.NetCallBack;
import com.example.basewarehouse.net.NetUtils;
import com.example.basewarehouse.utils.ActivityManager;
import com.example.basewarehouse.utils.StatusBarUtils;
import com.google.gson.Gson;
import com.walkermanx.photopicker.PhotoPreview;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected Context context;

    /**
     * 列表数据的展示请求参数
     */
    protected int sizeCount=10;
    protected int pagerCount=1;
    protected int offset=0;
    protected boolean isDropDown=false;
    protected boolean isPullUp=false;

    @BindView(R.id.iv_btn)
    protected ImageButton iv_btn;
    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.iv_right)
    protected ImageButton iv_right;
    @BindView(R.id.txt_right)
    protected TextView txt_right;
    @BindView(R.id.rl_root)
    protected RelativeLayout rl_root;
    @BindView(R.id.v_line)
    protected View v_line;
    @BindView(R.id.iv_join)
    protected ImageButton iv_join;
    @BindView(R.id.iv_order_zhuan)
    protected ImageButton iv_order_zhuan;
    @BindView(R.id.txt_left)
    protected TextView txt_left;

    /***封装toast对象**/

    private static Toast toast;
    InputMethodManager imm;
    protected Gson gosn;
    protected String json;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white);
        ActivityManager.getInstance().addActivity(this);
        init();
        //设置布局
        setContentView(intiLayout());
        ButterKnife.bind(this);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        gosn = new Gson();

        initView();
        //设置数据

        initData();
    }
    public void init(){

    }


    /**
     * post的请求方法
     * @param postUrl
     * @param json
     */
    protected void post(String postUrl,String json, final int requestCode){
        NetUtils.getInstance().post(TAG,postUrl, json, new NetCallBack() {
            @Override
            public void onSucess(String data) {
                returnData(requestCode,data);
                closeDialog();
            }

            @Override
            public void onException(int code,String message) {
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
            }
        });
    }
    /**
     * get的请求方法
     * @param postUrl
     */
    protected void get(String postUrl,final int requestCode){
        NetUtils.getInstance().get(TAG,postUrl, new NetCallBack() {
            @Override
            public void onSucess(String data) {
                returnData(requestCode,data);
                closeDialog();
            }

            @Override
            public void onException(int code,String message) {
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
            }
        });
    }

    /**
     * 返回的请求的数据
     * @param requestCode
     * @param reslutData
     */
    protected void returnData(int requestCode,String reslutData){}

    /**
     * 网络失败的返回
     * @param requestCode
     * @param conyent
     */
    protected void netError(int requestCode,String conyent){

    }

    /**
     * 其他错误的返回数据
     * @param requestCode
     * @param message
     */
    protected void otherException(int errorCode,int requestCode,String message){

    }

    /**
     * 请求之前要做的操作
     * @param requestCode
     */
    protected void beforeNet(int requestCode){
        if(isDropDown||isPullUp){

        }else {
            showDialog();
        }
    }


    /**

     * 设置布局

     * @return

     */

    public abstract int intiLayout();

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 设置数据
     */
    public abstract void initData();

    /**     * 显示短toast     * @param msg     */
    public void toastShort(String msg){
        if (null == toast) {
            toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(msg);
            toast.show();
        } else {
            toast.setText(msg);
            toast.show();
        }
    }

    public void openInputMethod(final View view){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    public void closeInputMethod(View view){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }, 200);
    }

    public void closeInputMethod(){
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    protected void closeDialog(){
        if(this != null && !this.isFinishing()) {
        }
    }
    protected void showDialog(){
        if(this != null && !this.isFinishing()) {
        }
    }

    protected void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
//        NetUtils.getInstance().cancelTag(TAG);
        super.onDestroy();
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
                .start(BaseActivity.this);
    }

    @Override
    public void onBackPressed() {
        SmartSwipeWrapper wrapper = SmartSwipe.peekWrapperFor(this);
        if (wrapper != null) {
            List<SwipeConsumer> consumers = wrapper.getAllConsumers();
            if (!consumers.isEmpty()) {
                for (SwipeConsumer consumer : consumers) {
                    if (consumer != null) {
                        if (consumer.isLeftEnable()) {
                            consumer.smoothLeftOpen();
                            return;
                        } else if (consumer.isTopEnable()) {
                            consumer.smoothTopOpen();
                            return;
                        }
                    }
                }
            }
        }
        super.onBackPressed();
    }
}
