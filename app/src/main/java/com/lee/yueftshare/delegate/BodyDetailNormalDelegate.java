package com.lee.yueftshare.delegate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.lee.yueftshare.R;
import com.lee.yueftshare.bean.BodyItem;

import java.util.List;

/**
 * Created by yue , 2019-
 * */
public class BodyDetailNormalDelegate extends AdapterDelegate<List<BodyItem>> {

    private Context mContext;

    public BodyDetailNormalDelegate(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected boolean isForViewType(@NonNull List<BodyItem> items, int position) {
        return items.get(position).getType() != BodyItem.BODY_HEADER;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_tzc_body,parent,false);
        return new NormalViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BodyItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        BodyItem item = items.get(position);
        if(item.getType() == BodyItem.BODY_WEIGH){

            ((NormalViewHolder)holder).ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tzc_weigh2x));
            ((NormalViewHolder)holder).title.setText(mContext.getResources().getString(R.string.tzc_weigh));
            ((NormalViewHolder)holder).value.setText(item.getValue()+"kg");
            ((NormalViewHolder)holder).type.setText(getTypeName(item.getStatue()));
        }
    }

    private String getTypeName(int type){
        String typeName = "";
        if(type == BodyItem.TYPE_STANDARD){
            typeName = mContext.getResources().getString(R.string.tzc_standard_style);
        }
        return typeName;
    }

    class NormalViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivIcon;
        public TextView title;
        public TextView value;
        public TextView type;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_body_icon);
            title = itemView.findViewById(R.id.tv_title);
            value = itemView.findViewById(R.id.tv_value);
            type = itemView.findViewById(R.id.tv_type);
        }
    }
}
