package com.example.basewarehouse.mvc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basewarehouse.R;
import com.example.basewarehouse.bean.PageBean;
import com.example.basewarehouse.utils.TS;
import com.example.basewarehouse.view.RecyclerViewNoBugLinearLayoutManager;
import com.example.basewarehouse.viewholder.ListViewHolder;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *  用于展示列表数据的基类Fragment
 * @param <T>
 * @param <K>
 */
public abstract class ListBaseFragment<T,K extends ListViewHolder> extends ParentFragment {

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
    protected RecyclerAdapter recyclerAdapter;
    protected PageBean pageBean;

    @Override
    public void receiveData() {

    }

    @Override
    public int initLayout() {
        return R.layout.layout_list_base_fragment;
    }

    @Override
    public void initView() {
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
                getData(0);
            }
        });
    }

    @Override
    public void initData() {
        getData(0);
    }

    @Override
    protected void returnData(int requestCode, String reslutData) {
        if(requestCode==0){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(reslutData);
                String data =jsonObject.getString("data");
                if(jsonObject.has("pageBar")){
                    String pageBarJson =jsonObject.getString("pageBar");
                    pageBean=new Gson().fromJson(pageBarJson, PageBean.class);
                }
                List<T> listData = analysisJson(data);
                if(isDropDown){
                    list.clear();
                }
                if(isPullUp&&listData.size()==0){
                    TS.show("数据已全部加载完成！");
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }else {
                    refreshLayout.finishLoadMore();
                }
                showData(listData);
            } catch (JSONException e) {
                e.printStackTrace();
                TS.show(e.getMessage());
            }
            isPullUp=false;
            isDropDown=false;
            refreshLayout.finishRefresh();
        }else {
            returnOtherData(requestCode, reslutData);
        }
        super.returnData(requestCode, reslutData);
    }

    protected void showData(List<T> listData){
        ll_bottom.setVisibility(View.VISIBLE);
        if(recyclerAdapter==null){
            list.addAll(listData);
            recyclerAdapter = new RecyclerAdapter(createItemView());
            rv.setAdapter(recyclerAdapter);
        }else {
            if(isPullUp) {
                recyclerAdapter.addData(listData);
            }else {
                recyclerAdapter.refreshData(listData);
            }
        }
        beforeShwoData();
        if(list.size()==0){
            ll_empty.setVisibility(View.VISIBLE);
        }else {
            ll_empty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void netError(int requestCode, String conyent) {
        if(requestCode==0&&isPullUp&&pagerCount>1){
            pagerCount--;
            finishRefreshData();
        }else {
            ll_netowrk.setVisibility(View.VISIBLE);
            closeDialog();
        }
        super.netError(requestCode, conyent);
    }

    @Override
    protected void otherException(int errorCode, int requestCode, String message) {
        if(requestCode==0&&isPullUp&&pagerCount>1){
            pagerCount--;
            finishRefreshData();
        }else {
            ll_netowrk.setVisibility(View.VISIBLE);
            closeDialog();
        }
        super.otherException(errorCode, requestCode, message);
    }

    @Override
    protected void beforeNet(int requestCode) {
        ll_empty.setVisibility(View.GONE);
        ll_netowrk.setVisibility(View.GONE);
        if(isPullUp||isDropDown){

        }else {
            ll_bottom.setVisibility(View.GONE);
        }
        super.beforeNet(requestCode);
    }

    /**
     * 列表结束刷新
     */
    protected void finishRefreshData(){
        isDropDown=false;
        isPullUp=false;
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    protected class RecyclerAdapter extends RecyclerView.Adapter<K>{

        private int layoutId;

        public RecyclerAdapter(int layoutId){
            this.layoutId=layoutId;
        }

        @NonNull
        @Override
        public K onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId,null);
            return createRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull K viewHolder, final int position) {
            onBindData(viewHolder,position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void refreshData(List<T> lis) {
            list = lis;
            notifyDataSetChanged();
        }
        public void addData(List<T> lis) {
            int oldSize = list.size();
            list.addAll(lis);
            notifyItemRangeInserted(oldSize, lis.size());
        }

        public void removeItem(int position) {
            if (position >= 0 && position < list.size()) {
                list.remove(position);
//                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 列表顶部是否添加view
     */
    public void initAddView(LinearLayout ll_top, LinearLayout ll_bottom){

    }

    /**
     * 显示数据之前的处理
     */
    public void beforeShwoData(){

    }

    /**
     * 请求网络，获取数据
     */
    public abstract void getData(int requestCode);

    /**
     * 解析数组数据
     */
    public abstract List<T> analysisJson(String json);
    /**
     * 返回其它相应的数据
     */
    public void returnOtherData(int requestCode, String reslut){

    }

    /**
     * 获取条目的布局
     */
    public abstract int createItemView();

    /**
     * 实例化viewholder
     * @param view
     * @return
     */
    protected abstract K createRecyclerViewHolder(View view);

    /**
     * 绑定条目的数据
     */
    public abstract void onBindData(K viewHolder, final int i);
}
