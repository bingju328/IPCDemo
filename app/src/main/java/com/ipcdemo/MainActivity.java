package com.ipcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ipcdemo.client.JVClientActivity;
import com.ipcdemo.client.KTClientActivity;

public class MainActivity extends AppCompatActivity {
    private TextView java_aidl,kotlin_aidl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        java_aidl = (TextView) findViewById(R.id.java_aidl);
        kotlin_aidl = (TextView) findViewById(R.id.kotlin_aidl);
    }

    private void initEvent() {
        java_aidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, JVClientActivity.class));
            }
        });
        kotlin_aidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KTClientActivity.class));
            }
        });
    }
}
