package com.lee.yueftshare.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.sharefacebooktwitter.ShareFTdelegate;
import com.lee.yueftshare.R;
import com.lee.yueftshare.adapter.BodyDetailAdapter;
import com.lee.yueftshare.bean.BodyItem;
import com.lee.yueftshare.model.IBodyData;
import com.lee.yueftshare.util.DialogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


public class TZCBodyDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = TZCBodyDetailActivity.class.getSimpleName();
    public static final String BODY_SCORE = "bodyScore";
    public static final String BODY_TYPE = "bodyType";
    public static final String BODY_AGE = "bodyAge";
    private List<IBodyData> items = new ArrayList<>();
    private RecyclerView recView;
    private BodyDetailAdapter adapter;
    private TextView tvShare;
    private TextView tvSave;
    private Dialog bottomDialog;
    private View viewBottomDialog;
    private RelativeLayout rootRlt;
    private LinearLayout lltFacebook;
    private LinearLayout lltTwitter;
    private TextView tvCancelShare;
    private ShareFTdelegate delegate;
    RxPermissions rxPermissions ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_body);
        initView();
        initDelegate();
        initData();


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        delegate.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtil.cancelDialog();
    }

    private void initDelegate() {
        delegate = new ShareFTdelegate(this, new ShareFTdelegate.ShareListener() {
            @Override
            public void shareSuc() {
                shareSuccess();
            }

            @Override
            public void shareFail() {
                shareFailure();
            }
        });
    }

    private void initData(){
        BodyItem item1 = new BodyItem(BodyItem.BODY_HEADER,BodyItem.TYPE_STANDARD,"Vincent",90);
        items.add(item1);
        for(int i = 0; i < 10; i++ ){
            BodyItem item = new BodyItem(BodyItem.BODY_WEIGH,BodyItem.TYPE_STANDARD,getString(R.string.tzc_weigh),60);
            items.add(item);
        }

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }


    private void initView() {
        recView = findViewById(R.id.rec);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recView.setLayoutManager(layoutManager);
        adapter = new BodyDetailAdapter(this);
        recView.setAdapter(adapter);
        tvShare = findViewById(R.id.tv_share);
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        rootRlt = findViewById(R.id.rlt_root);
        bottomDialog = new Dialog(this);
        viewBottomDialog = LayoutInflater.from(this).inflate(R.layout.view_share_bottom_dialog, rootRlt, false);
        lltFacebook = viewBottomDialog.findViewById(R.id.llt_facebook);
        lltFacebook.setOnClickListener(this);
        lltTwitter = viewBottomDialog.findViewById(R.id.llt_twitter);
        lltTwitter.setOnClickListener(this);
        tvCancelShare = viewBottomDialog.findViewById(R.id.tv_cancel_share);
        tvCancelShare.setOnClickListener(this);
        rxPermissions = new RxPermissions(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_share) {
            rxPermissions.
                    request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).
                    subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean)  {
                            if(aBoolean){
                                showShareDialog();
                            }
                        }
                    });

        } else if (id == R.id.llt_twitter) {
            delegate.shareToTitter(recView);
        } else if (id == R.id.llt_facebook) {
            delegate.shareToFaceBook(recView);
        } else if (id == R.id.tv_cancel_share) {
            if (bottomDialog != null && bottomDialog.isShowing()) {
                bottomDialog.dismiss();
            }
        }else if(id == R.id.tv_save){
            delegate.saveImg(recView, new ShareFTdelegate.SaveImgListener() {
                @Override
                public void saveSuc() {
                   Toast.makeText(TZCBodyDetailActivity.this, getString(R.string.tzc_save_suc),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void saveFail() {

                }
            });
        }
    }

    private void showShareDialog() {
        DialogUtil.customBottomDialog(bottomDialog, viewBottomDialog, this);
    }

    private void shareSuccess() {
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
        Toast.makeText(this, getString(R.string.tzc_share_suc), Toast.LENGTH_SHORT).show();
    }

    private void shareFailure() {
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
        Toast.makeText(this, getString(R.string.tzc_share_fail), Toast.LENGTH_SHORT).show();
    }
}
