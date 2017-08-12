package com.example.activitylifecycletest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (getSupportActionBar()!=null) getSupportActionBar().hide();
        setContentView(R.layout.activity_normal);
    }
}
