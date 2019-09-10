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

public class BodyDetailHeaderDelegate extends AdapterDelegate<List<BodyItem>> {

    private Context mContext;

    public BodyDetailHeaderDelegate(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected boolean isForViewType(@NonNull List<BodyItem> items, int position) {
        return items.get(position).getType() == BodyItem.BODY_HEADER;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_tzc_head,parent,false);
        HeaderViewHolder viewHolder = new HeaderViewHolder(view);

        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BodyItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {


    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView score;
        public TextView tvBobyStyle;
        public ImageView ivBodyStyle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            score = itemView.findViewById(R.id.tv_score_total);
            tvBobyStyle = itemView.findViewById(R.id.tv_body_style);
            ivBodyStyle = itemView.findViewById(R.id.iv_body_style);
        }
    }
}
