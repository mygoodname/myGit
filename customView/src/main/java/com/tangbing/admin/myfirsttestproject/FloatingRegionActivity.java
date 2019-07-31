package com.tangbing.admin.myfirsttestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.tangbing.admin.myfirsttestproject.customview.FloatingRegionView;

/**
 * Created by tangbing on 2019/7/31.
 * Describe :
 */

public class FloatingRegionActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_floatingregion);
    }

    public void goTest(View view){
        FrameLayout frameLayout=getWindow().getDecorView().findViewById(android.R.id.content);
        FloatingRegionView floatingRegionView=new FloatingRegionView(this);
        floatingRegionView.setLayoutParams(getParams());
        frameLayout.addView(floatingRegionView);
    }
    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
               ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
//        params.setMargins(0, params.topMargin, params.rightMargin, 0);
        return params;
    }
}
