package com.tangbing.admin.myfirsttestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tangbing.admin.myfirsttestproject.adapter.CustomListViewAdapter;
import com.tangbing.admin.myfirsttestproject.customview.CustomListView;

import java.util.ArrayList;

/**
 * Created by admin on 2019/1/29.
 */

public class CustomListViwActivity extends AppCompatActivity{
    CustomListView customList;
    ArrayList<String> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customlist);
        initCotentList();
        initView();
    }
    private void initView(){
        customList=findViewById(R.id.customList);
        final CustomListViewAdapter adapter=new CustomListViewAdapter(this,0,list);
        customList.setAdapter(adapter);
        customList.setOnDeleteListener(new CustomListView.OnDeleteListener() {
            @Override
            public void onDeleteItem(int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void initCotentList(){
        for (int i=0;i<20;i++){
            list.add("子项目："+i);
        }
    }
}
