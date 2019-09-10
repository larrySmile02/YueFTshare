package com.lee.yueftshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lee.yueftshare.activity.TZCBodyDetailActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView ivSexy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivSexy = findViewById(R.id.ivSexy);
        ivSexy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TZCBodyDetailActivity.class));
            }
        });
    }
}
