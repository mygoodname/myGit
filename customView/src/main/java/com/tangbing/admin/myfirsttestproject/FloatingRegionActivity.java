package com.tangbing.admin.myfirsttestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.tangbing.admin.myfirsttestproject.customview.FloatingRegionView;
import com.tangbing.admin.myfirsttestproject.customview.floatView.FloatingView;

/**
 * Created by tangbing on 2019/7/31.
 * Describe :
 */

public class FloatingRegionActivity extends AppCompatActivity{
    FloatingRegionView floatingRegionView;
    FrameLayout frameLayout=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_floatingregion);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingView.get().attach(this);
    }

    public void goTest(View view){
        if(floatingRegionView==null){
            frameLayout=getWindow().getDecorView().findViewById(android.R.id.content);
            floatingRegionView =new FloatingRegionView(FloatingRegionActivity.this);
            floatingRegionView.setLayoutParams(getParams());
            frameLayout.addView(floatingRegionView);
        }
        FloatingView.get().add();
        FloatingView.get().setBallMoveLister(new FloatBallMoveListener() {
            @Override
            public void ballMove() {
                floatingRegionView.translateAnimation();
            }

            @Override
            public void ballStop() {
                Log.e("ballStop","ballStop");
                floatingRegionView.removeAnimation();
            }
        });
       /* FrameLayout frameLayout=getWindow().getDecorView().findViewById(android.R.id.content);
        floatingRegionView =new FloatingRegionView(this);
        floatingRegionView.setLayoutParams(getParams());
        frameLayout.addView(floatingRegionView);*/
    }
    public void goTest1(View view){
        final FrameLayout frameLayout=getWindow().getDecorView().findViewById(android.R.id.content);
        floatingRegionView.setAnimationEndListener(new AnimationEndListener() {
            @Override
            public void end() {
                frameLayout.removeView(floatingRegionView);
            }
        });
        floatingRegionView.removeAnimation();
    }
    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
               ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
//        params.gravity = Gravity.CENTER ;
//        params.setMargins(0, params.topMargin, params.rightMargin, 0);
        return params;
    }
}
