package com.tangbing.admin.myfirsttestproject.viewtest_one;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tangbing.admin.myfirsttestproject.R;

/**
 * Created by admin on 2019/1/29.
 */

public class CustomListView extends ListView implements GestureDetector.OnGestureListener,View.OnTouchListener{
    private int mSelectedItem;
    private boolean isShowDetele=false;
    private GestureDetector mGestureDetector;
    private View deleteButton;
    private ViewGroup mItemLayout;
    OnDeleteListener mOnDeleteListener;
    public interface OnDeleteListener {
        void onDeleteItem(int position);
    }
    // 设置删除监听事件
    public void setOnDeleteListener(OnDeleteListener listener) {
               mOnDeleteListener = listener;
            }
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
       /* if(isShowDetele){
            hideDelete();
            return false;
        }*/
        return mGestureDetector.onTouchEvent(event);
    }
    //滑屏，用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
    //  参数解释：
    //e1：第1个ACTION_DOWN MotionEvent
    //e2：最后一个ACTION_MOVE MotionEvent
    //velocityX：X轴上的移动速度，像素/秒
    //velocityY：Y轴上的移动速度，像素/秒
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(Math.abs(velocityX) > Math.abs(velocityY)){
            if(e1.getX()-e2.getX()>50){//向左滑动
                View deleteButton= LayoutInflater.from(getContext()).inflate(R.layout.view_delte_button,null);
                deleteButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemLayout.removeViewAt(1);
                        isShowDetele=false;
                        deleteItem();
                    }
                });
                if(isShowDetele){
                    if(mItemLayout!=null){
                        isShowDetele=false;
                        mItemLayout.removeViewAt(1);
                    }
                }
                mItemLayout= (ViewGroup) getChildAt(mSelectedItem-getFirstVisiblePosition());
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                deleteButton.setLayoutParams(params);
                mItemLayout.addView(deleteButton);
                isShowDetele=true;
            }
        }
        return false;
    }
    private void deleteItem(){
        mOnDeleteListener.onDeleteItem(mSelectedItem);
    }
    //用户按下屏幕就会触发
    @Override
    public boolean onDown(MotionEvent e) {
            mSelectedItem=pointToPosition((int)e.getX(),(int)e.getY());
            return false;
    }
    // 隐藏删除按钮
     public void hideDelete() {
        mItemLayout.removeView(deleteButton);
        deleteButton = null;
        isShowDetele = false;
     }
    //如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，那么onShowPress就会执行，具体这个瞬间是多久，我也不清楚呃……
    @Override
    public void onShowPress(MotionEvent e) {

    }
    //从名子也可以看出,一次单独的轻击抬起操作,也就是轻击一下屏幕，立刻抬起来，才会有这个触发，当然,如果除了Down以外还有其它操作,那就不再算是Single操作了,所以也就不会触发这个事件
    //触发顺序：点击一下非常快的（不滑动）Touchup：   
    // onDown->onSingleTapUp->onSingleTapConfirmed 
    // 点击一下稍微慢点的（不滑动）Touchup：
    // onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    //在屏幕上拖动事件。无论是用手拖动view，或者是以抛的动作滚动，都会多次触发,这个方法       在ACTION_MOVE动作发生时就会触发
    //滑屏：手指触动屏幕后，稍微滑动后立即松开
    // onDown-----》onScroll----》onScroll----》onScroll----》………----->onFling
    // 拖动
    // onDown------》onScroll----》onScroll------》onFiling
    //可见，无论是滑屏，还是拖动，影响的只是中间OnScroll触发的数量多少而已，最终都会触发onFling事件！

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    //长按触摸屏，超过一定时长，就会触发这个事件    触发顺序：onDown->onShowPress->onLongPress
    @Override
    public void onLongPress(MotionEvent e) {

    }


}
