package com.tangbing.admin.myfirsttestproject.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangbing.admin.myfirsttestproject.AnimationEndListener;
import com.tangbing.admin.myfirsttestproject.R;

/**
 * Created by tangbing on 2019/7/31.
 * Describe :
 */

public class FloatingRegionView extends FrameLayout{
    ScanRadar scanView;
    ImageView imageShow;
    TextView textShow;
    private AnimationEndListener animationEndListener;
    private Context context;
    public FloatingRegionView(@NonNull Context context) {
        this(context,null);
        this.context=context;
    }

    public FloatingRegionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public FloatingRegionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v=inflate(context, R.layout.en_floating_view1, this);
        initView(v);
    }
    private void initView(View view){
        scanView=view.findViewById(R.id.scanView);
        imageShow=view.findViewById(R.id.imageShow);
        textShow=view.findViewById(R.id.textShow);
    }

    public void translateAnimation(){
        if(scanView.getVisibility()==GONE){
            scanView.setVisibility(VISIBLE);
            TranslateAnimation translateAnimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0f);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageShow.setVisibility(VISIBLE);
                    textShow.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimation.setDuration(100);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            scanView.startAnimation(translateAnimation);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e( "onTouchEvent", "--FloatingRegionView");
        return super.onTouchEvent(event);
    }
    public void removeAnimation(){
        if(scanView.getVisibility()==VISIBLE){
            scanView.setVisibility(GONE);
            TranslateAnimation translateAnimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    imageShow.setVisibility(GONE);
                    textShow.setVisibility(GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                animationEndListener.end();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimation.setDuration(100);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            scanView.startAnimation(translateAnimation);
        }

    }
    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.animationEndListener = animationEndListener;
    }
}
