package com.tangbing.admin.myfirsttestproject.viewtest_one;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by admin on 2019/1/29.
 */

public class CustomListView extends ListView implements GestureDetector.OnGestureListener,View.OnTouchListener{
    private int mSelectedItem;
    private boolean isShowDetele=false;
    private GestureDetector mGestureDetector;
    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector=new GestureDetector(getContext(),this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!isShowDetele){
            hideDelete();
            return false;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        if(!isShowDetele)
            mSelectedItem=pointToPosition((int)e.getX(),(int)e.getY());
            return false;
    }
    // 隐藏删除按钮
     public void hideDelete() {
        /*mItemLayout.removeView(mDeleteBtn);
        mDeleteBtn = null;*/
        isShowDetele = false;
        }
    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


}
