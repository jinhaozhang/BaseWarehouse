package com.example.basewarehouse.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.example.basewarehouse.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


public class RefreshLayoutView extends SmartRefreshLayout {
    public RefreshLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public RefreshLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public RefreshLayoutView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
//        autoRefresh();//自动刷新
        autoLoadMore();//自动加载
        setEnableScrollContentWhenLoaded(true);
        setEnableFooterFollowWhenLoadFinished(true);//是否在全部加载结束之后Footer跟随内容1.0.4
//        setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
        setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        setEnableOverScrollBounce(true);//是否启用越界回弹
//设置 Header 为 MaterialHeader
        //设置 Header 为 ClassicsFooter 比较经典的样式
        ClassicsHeader header =new ClassicsHeader(context);
        header.setArrowDrawable(getResources().getDrawable(R.mipmap.xiala));
        setRefreshHeader(header);
        //设置 Footer 为 经典样式
        setRefreshFooter(new ClassicsFooter(context));
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(onRefreshAndrLoadListener!=null){
                    onRefreshAndrLoadListener.onRefresh(refreshLayout);
                }
            }
        });
        setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(onRefreshAndrLoadListener!=null){
                    onRefreshAndrLoadListener.onLoadMore(refreshLayout);
                }
            }
        });
    }

    private OnRefreshAndrLoadListener onRefreshAndrLoadListener;
    public void setOnRefreshAndrLoadListener(OnRefreshAndrLoadListener onRefreshAndrLoadListener){
        this.onRefreshAndrLoadListener=onRefreshAndrLoadListener;
    }

    public interface OnRefreshAndrLoadListener{
        void onRefresh(RefreshLayout refreshlayout);
        void onLoadMore(RefreshLayout refreshlayout);
    }
}
