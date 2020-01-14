package com.example.basewarehouse.mvp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basewarehouse.viewholder.ListViewHolder;

import java.util.List;

public abstract class BaseRecyclerAdapter<T,K extends ListViewHolder>  extends RecyclerView.Adapter<K> {

    private int layoutId;
    private List<T> list;

    public BaseRecyclerAdapter(int layoutId, List<T> list){
        this.layoutId=layoutId;
        this.list=list;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public K onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,null);
        return createRecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull K holder, int position) {
        showItemData(holder,list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refreshData(List<T> lis) {
        list .addAll(lis);
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

    protected abstract K createRecyclerViewHolder(View view);

    protected abstract void showItemData( K holder,T t,int position);


}
