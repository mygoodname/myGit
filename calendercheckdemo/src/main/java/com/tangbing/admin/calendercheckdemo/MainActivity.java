package com.tangbing.admin.calendercheckdemo;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tangbing.admin.calendercheckdemo.adpter.DateAdapter;
import com.tangbing.admin.calendercheckdemo.model.SignDateModel;
import com.tangbing.admin.calendercheckdemo.utils.DateUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DateAdapter dateAdapter;
    TextView start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setData();
    }
    private void initView(){
        recyclerView=findViewById(R.id.dateRecycleView);
        start=findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus();
            }
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,7);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
                 outRect.top=20;
            }
        });
        dateAdapter=new DateAdapter(this);
        recyclerView.setAdapter(dateAdapter);
    }
    private void setData(){
        int days=DateUtil.getCurrentMouthDays();
        ArrayList<SignDateModel> arrayList=new ArrayList<>(35);
        int firstDate= DateUtil.getFirstDayOfMouth();
        String currentData=DateUtil.getCurrentDate();
        SignDateModel signDateModel=null;
        int j=1;
        for(int i=0;i<days+firstDate-1;i++){
             signDateModel=new SignDateModel();
             if(i>=firstDate-1){
                 if(j>days) return;
                 String data=DateUtil.getDateOfMouth(j);
                 if(currentData.equals(data)){
                     signDateModel.setStatus(SignDateModel.STATUS_CURRENT_DAY);
                 }
                 signDateModel.setContent(data);
                 signDateModel.setDay(j+"");
                 j++;
             }
             arrayList.add(signDateModel);
        }
        dateAdapter.setData(arrayList);
    }
    private void setStatus(){
        String data[]={"2019-03-19","2019-03-05","2019-03-01","2019-03-20"};
        dateAdapter.setSignStatus(data);
    }
}
