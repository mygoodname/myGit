package com.tangbing.admin.utilstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView inText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        inText=findViewById(R.id.in_Text);
        button=findViewById(R.id.goAction);
        button.setOnClickListener(this);
    }
    private void action(){
        Object du=4.0d;
        Object fl=4.00f;
        Object l=4.00;
        Character ch=4;
        Byte by=4;
        String string="4.0000";
        int i=ObjectToInterger.objectToInt(du);
        inText.setText("du="+du.toString()+"  objectToInt:"+i+"fl="+fl.toString()+"  objectToInt:"+ObjectToInterger.objectToInt(fl)+"l="+l.toString()+"  objectToInt:"+ObjectToInterger.objectToInt(l)+
                "ch="+ch.toString()+"  objectToInt:"+ObjectToInterger.objectToInt(ch)+"by="+by.toString()+"  objectToInt:"+ObjectToInterger.objectToInt(by)+"string="+string.toString()+"  objectToInt:"+ObjectToInterger.objectToInt(string));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goAction:
                action();
                break;
        }
    }
}
