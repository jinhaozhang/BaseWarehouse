package com.example.basewarehouse.mvp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.basewarehouse.R;
import com.example.basewarehouse.utils.TS;
import com.example.basewarehouse.view.RecyclerViewNoBugLinearLayoutManager;
import com.example.basewarehouse.viewholder.ListViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * mvp模式下Fragment的列表展示基类
 * @param <T> 实体对象
 * @param <K>RecyclerView.ViewHolder的子类
 * @param <P>presenter解析类
 */
public abstract class MvpBaseListFragment<T,K extends ListViewHolder,P extends IPresenter<IView>> extends MvpBaseFragment{

    @BindView(R.id.ll_top)
    protected LinearLayout ll_top;//列表顶部需要添加的展示内容
    @BindView(R.id.ll_bottom)
    protected LinearLayout ll_bottom;//列表底部需要添加的展示内容
    @BindView(R.id.ll_network)
    protected LinearLayout ll_netowrk;
    @BindView(R.id.ll_empty)
    protected LinearLayout ll_empty;
    @BindView(R.id.tv_again)
    protected TextView tv_again;
    @BindView(R.id.rl)
    protected RefreshLayout refreshLayout;
    @BindView(R.id.rv)
    protected RecyclerView rv;

    @BindView(R.id.tv_content)
    protected TextView tv_content;
    @BindView(R.id.iv_empty)
    protected ImageView iv_empty;

    @BindView(R.id.root_ll)
    protected LinearLayout root_ll;

    protected List<T> list = new ArrayList<>();
    protected MvpRecycleAdapter recyclerAdapter;


    @Override
    protected int initLayout() {
        return  R.layout.layout_list_base_fragment;
    }

    @Override
    protected void initView() {
        initAddView(ll_top,ll_bottom);
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(getContext());//纵向线性布局
        rv.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(!isDropDown){
                    isDropDown=true;
                    pagerCount=1;
                    offset=0;
                    getData(0);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if(!isPullUp){
                    isPullUp=true;
                    pagerCount++;
                    getData(0);
                }
            }
        });
        tv_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerCount=1;
                list.clear();
                if(recyclerAdapter!=null){
                    recyclerAdapter.notifyDataSetChanged();
                }
                getData(0);
            }
        });
    }


    @Override
    protected void initData() {
        getData(0);
    }

    /**
     * 展示数据的列表
     * @param listData
     */
    protected void showData(List<T> listData){
        if(isDropDown){
            list.clear();
        }
        if(isPullUp&&listData.size()==0){
            TS.show("数据已全部加载完成！");
            refreshLayout.finishLoadMoreWithNoMoreData();
        }else {
            refreshLayout.finishLoadMore();
        }
        ll_bottom.setVisibility(View.VISIBLE);
        if(recyclerAdapter==null){
            list.addAll(listData);
            recyclerAdapter  = new MvpRecycleAdapter(initItemView(),list);
            rv.setAdapter(recyclerAdapter);
        }else {
            if(isPullUp) {
                recyclerAdapter.addData(listData);
            }else {
                recyclerAdapter.refreshData(listData);
            }
        }
        if(list.size()==0){
            ll_empty.setVisibility(View.VISIBLE);
        }else {
            ll_empty.setVisibility(View.GONE);
        }
        isPullUp=false;
        isDropDown=false;
        refreshLayout.finishRefresh();
    }

    /**
     * 列表结束刷新
     */
    protected void finishRefreshData(){
        if(isPullUp&&pagerCount>0){
            pagerCount--;
        }
        isDropDown=false;
        isPullUp=false;
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    protected P bindPresenter() {
        return presenterBind();
    }

    /**
     * 绑定presenter
     * @return
     */
    protected abstract P presenterBind();

    /**
     * 初始化列表的条目布局
     * @return
     */
    protected abstract int initItemView();

    /**
     * 头部 或底部增加布局
     * @param ll_top
     * @param ll_bottom
     */
    protected void initAddView(LinearLayout ll_top, LinearLayout ll_bottom){

    }

    /**
     * 获取列表的数据
     * @param i
     */
    protected abstract void getData(int i);

    /**
     * 返回列表的数据
     * @param holder
     * @param o
     * @param position
     */
    protected abstract void showDataItem(K holder, T o, int position);

    /**
     * 初始化列表的viewholder
     * @param view
     * @return
     */
    protected abstract K initViewHolder(View view);


    protected class MvpRecycleAdapter extends BaseRecyclerAdapter<T,K> {
        public MvpRecycleAdapter(int layoutId, List list) {
            super(layoutId, list);
        }

        @Override
        protected K createRecyclerViewHolder(View view) {
            return initViewHolder(view);
        }

        @Override
        protected void showItemData(K holder, T t, int position) {
            showDataItem(holder,t,position);
        }

    }


}
