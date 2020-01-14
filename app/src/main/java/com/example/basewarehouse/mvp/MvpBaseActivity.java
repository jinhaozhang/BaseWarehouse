package com.example.basewarehouse.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.example.basewarehouse.R;
import com.example.basewarehouse.utils.ActivityManager;
import com.example.basewarehouse.utils.StatusBarUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * https://www.jianshu.com/p/479aca31d993
 *  mvp模式下Fragment的基类
 * @param <P>presenter解析类
 * */
public abstract class MvpBaseActivity<P extends IPresenter<IView>> extends AppCompatActivity implements IView {

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

    protected P presenter;
    private InputMethodManager imm;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white);
        ActivityManager.getInstance().addActivity(this);
        init();
        //设置布局
        setContentView(intiLayout());
        ButterKnife.bind(this);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        presenter = bindPresenter();
        initView();
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();


    @Override
    public Activity getSelfActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束时，将presenter与view之间的联系断开，防止出现内存泄露
         */
        if (presenter != null) {
            presenter.detachView();
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


    /**
     * 初始化数据
     */
    public void init(){

    }

    /**

     * 设置布局

     * @return

     */

    public abstract int intiLayout();

    /**
     * 实例化presenter
     * @return
     */
    protected abstract P bindPresenter();
}
