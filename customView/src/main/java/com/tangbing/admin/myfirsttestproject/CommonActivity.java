package com.tangbing.admin.myfirsttestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.tangbing.admin.myfirsttestproject.util.ActivityUtils;

/**
 * Created by admin on 2019/3/4.
 */

public class CommonActivity extends AppCompatActivity{
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
}
