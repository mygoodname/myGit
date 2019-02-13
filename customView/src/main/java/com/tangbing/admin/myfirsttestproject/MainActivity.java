package com.tangbing.admin.myfirsttestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tangbing.admin.myfirsttestproject.util.ActivityUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button sideslipButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sideslip:
                goSideslipList();
                break;
        }
    }
    private void goSideslipList(){
        ActivityUtils.startActivity(this,CustomListViwActivity.class);
    }
    private void initView(){
        sideslipButton=findViewById(R.id.sideslip);
        sideslipButton.setOnClickListener(this);
    }
}
