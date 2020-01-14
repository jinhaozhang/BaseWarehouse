package com.example.basewarehouse.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * 列表父类专用的viewholder
 */
public class ListViewHolder extends RecyclerView.ViewHolder{
    public ListViewHolder(View view){
        super(view);
        ButterKnife.bind(this,view);
    }
}
