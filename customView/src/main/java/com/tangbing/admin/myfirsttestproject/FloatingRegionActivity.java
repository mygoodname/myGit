package com.tangbing.admin.myfirsttestproject;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tangbing.admin.myfirsttestproject.customview.FloatingRegionUtil;
import com.tangbing.admin.myfirsttestproject.customview.FloatingRegionView;
import com.tangbing.admin.myfirsttestproject.customview.floatView.FloatingView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by tangbing on 2019/7/31.
 * Describe :
 */

public class FloatingRegionActivity extends AppCompatActivity {
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
        FloatingRegionUtil.get().attach(this);
        FloatingRegionUtil.get().add();
        FloatingView.get().attach(this);
    }
    public void showToast(View view){
        Toast.makeText(this,"点击了哇",Toast.LENGTH_LONG).show();
    }
    public void goTest(View view){
      /*  if(floatingRegionView==null){
            frameLayout=getWindow().getDecorView().findViewById(android.R.id.content);
            floatingRegionView =new FloatingRegionView(FloatingRegionActivity.this);
            floatingRegionView.setLayoutParams(getParams());
            frameLayout.addView(floatingRegionView);
        }*/
        FloatingView.get().add();
        FloatingView.get().setBallMoveLister(new FloatBallMoveListener() {
            @Override
            public void ballMove() {
                FloatingRegionUtil.get().translateAnimation();
            }

            @Override
            public void ballStop() {
                Log.e("ballStop","ballStop");
                FloatingRegionUtil.get().removeAnimation();
            }
        });
        FloatingView.get().setLocationListener(new FloatBallLocationListener() {
            @Override
            public void hasContainer(float x, float y,boolean isUp) {
                FloatingRegionUtil.get().isHashContainer(x,y,isUp);
            }
        });
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
