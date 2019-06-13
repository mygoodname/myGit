package com.tangbing.admin.mymatrixtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.helloWorld).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MatrixActivity.class);
                startActivity(intent);
            }
        });
    }
    public void goMatrix(){
        Intent intent=new Intent(this,MatrixActivity.class);
        startActivity(intent);
    }
}
