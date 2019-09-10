package com.lee.yueftshare.adapter;

import android.content.Context;

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;
import com.lee.yueftshare.delegate.BodyDetailHeaderDelegate;
import com.lee.yueftshare.delegate.BodyDetailNormalDelegate;

public class BodyDetailAdapter extends ListDelegationAdapter {

    public BodyDetailAdapter(Context mContext ){
        delegatesManager.addDelegate(new BodyDetailHeaderDelegate(mContext));
        delegatesManager.addDelegate(new BodyDetailNormalDelegate(mContext));
    }

}
