package com.example.luweicheng.thread;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Refersh extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refersh);
        textView= (TextView) findViewById(R.id.texts);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refersh);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent,
                R.color.green,R.color.colorPrimaryDark,R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                textView.setText("正在刷新");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       textView.setText("刷新完成");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },8000);
            }
        });
    }
}
