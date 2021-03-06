package com.tangbing.admin.myfirsttestproject;

import android.os.Bundle;

import android.view.View;
import android.view.Window;

import com.tangbing.admin.myfirsttestproject.util.ActivityUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by admin on 2019/3/4.
 */

public class CommonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_commonactivity);
    }
    public void goCustomView(View view){
        ActivityUtils.startActivity(this,CustomViewActivity.class);
    }
    public void customDeleteListView(View view){
        ActivityUtils.startActivity(this,CustomListViwActivity.class);
    }
    public void customProgress(View view){
        ActivityUtils.startActivity(this,CustomProgressActivity.class);
    }
    public void customWave(View view){
        ActivityUtils.startActivity(this,WavyActivity.class);
    }
    public void customFloatingView(View view){
        ActivityUtils.startActivity(this,FloatingRegionActivity.class);
    }
}
