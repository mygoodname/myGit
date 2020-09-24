package com.tangbing.admin.myfirsttestproject.customview;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.tangbing.admin.myfirsttestproject.customview.floatView.EnContext;
import com.tangbing.admin.myfirsttestproject.customview.floatView.FloatingView;

import androidx.core.view.ViewCompat;

/**
 * Created by tangbing on 2019/8/1.
 * Describe :
 */

public class FloatingRegionUtil {
    private FrameLayout mContainer;
    public FloatingRegionView floatingRegionView;
    private static volatile FloatingRegionUtil mInstance;
    public static FloatingRegionUtil get() {
        if (mInstance == null) {
            synchronized (FloatingView.class) {
                if (mInstance == null) {
                    mInstance = new FloatingRegionUtil();
                }
            }
        }
        return mInstance;
    }
    public FloatingRegionUtil attach(Activity activity) {
        attach(getActivityRoot(activity));
        return this;
    }
    public void translateAnimation(){
        if(floatingRegionView!=null)
        floatingRegionView.translateAnimation();
    }
    public void removeAnimation(){
        if(floatingRegionView!=null)
            floatingRegionView.removeAnimation();
    }
    public void isHashContainer(float x,float y,boolean isUp){
        int[] location = new  int[2];
        if(floatingRegionView!=null&&floatingRegionView.scanView!=null){
            floatingRegionView.scanView.getLocationInWindow(location);
            int x1=location[0];
            int y1=location[1];
            int x2=floatingRegionView.scanView.getWidth()+x1;
            int y2=floatingRegionView.scanView.getHeight()+y1;
            Log.e("hasContainer","x1:"+x1+" y1:"+y1+" x2:"+x2+" y2:"+y2+"   x :"+x+" y:"+y);

            if(x>x1&&x<x2&&y>y1&&y<y2){
                if(isUp){
                    FloatingView.get().remove();
                    floatingRegionView.hideLayout();
                }else
                    floatingRegionView.scaleImage(true);
            }else floatingRegionView.scaleImage(false);
        }
    }
    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public FloatingRegionUtil attach(FrameLayout container) {
        if (container == null || floatingRegionView == null) {
            mContainer = container;
            return this;
        }
        if (floatingRegionView.getParent() == container) {
            return this;
        }
        if (mContainer != null && floatingRegionView.getParent() == mContainer) {
            mContainer.removeView(floatingRegionView);
        }
        mContainer = container;
        container.addView(floatingRegionView);
        return this;
    }
    public FloatingRegionUtil remove() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (floatingRegionView == null) {
                    return;
                }
                if (ViewCompat.isAttachedToWindow(floatingRegionView) && mContainer != null) {
                    mContainer.removeView(floatingRegionView);
                }
                floatingRegionView = null;
            }
        });
        return this;
    }
    public FloatingRegionUtil add() {
        ensureMiniPlayer(EnContext.get());
        return this;
    }
    private void ensureMiniPlayer(Context context) {
        synchronized (this) {
            if (floatingRegionView != null) {
                return;
            }
            floatingRegionView = new FloatingRegionView(context.getApplicationContext());
            floatingRegionView.setLayoutParams(getParams());
            addViewToWindow(floatingRegionView);
        }
    }
    private void addViewToWindow(final FloatingRegionView view) {
        if (mContainer == null) {
            return;
        }
        mContainer.addView(view);
    }
    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
//        params.setMargins(13, params.topMargin, params.rightMargin, 56);
        return params;
    }
}
