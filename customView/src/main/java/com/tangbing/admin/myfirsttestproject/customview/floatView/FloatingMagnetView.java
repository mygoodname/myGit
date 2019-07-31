package com.tangbing.admin.myfirsttestproject.customview.floatView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.tangbing.admin.myfirsttestproject.FloatBallMoveListener;
import com.tangbing.admin.myfirsttestproject.util.Utils;


/**
 * @ClassName FloatingMagnetView
 * @Description 磁力吸附悬浮窗
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:02
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:02
 */
public class FloatingMagnetView extends FrameLayout {
    private FloatBallMoveListener floatBallMoveListener;
    public static final int MARGIN_EDGE = 13;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private MagnetViewListener mMagnetViewListener;
    private static final int TOUCH_TIME_THRESHOLD = 150;
    private long mLastTouchDownTime;
    protected MoveAnimator mMoveAnimator;
    protected int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;
    private Context context;
    public void setMagnetViewListener(MagnetViewListener magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    public FloatingMagnetView(Context context) {
        this(context, null);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        mMoveAnimator = new MoveAnimator();
        mStatusBarHeight = SystemUtils.getStatusBarHeight(getContext());
        setClickable(true);
        updateSize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeOriginalTouchParams(event);
                updateSize();
                mMoveAnimator.stop();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition(event);
                break;
            case MotionEvent.ACTION_UP:
                moveToEdge();
                if (isOnClickEvent()) {
                    dealClickEvent();
                }
                break;
        }
        return true;
    }

    protected void dealClickEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onClick(this);
        }
    }

    public void setFloatBallMoveListener(FloatBallMoveListener floatBallMoveListener) {
        this.floatBallMoveListener = floatBallMoveListener;
    }

    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD;
    }

    private void updateViewPosition(MotionEvent event) {
        Log.e("updateViewPosition",(event.getRawX()-mOriginalRawX)+"");
        if(event.getRawX()-mOriginalRawX!=0){
            floatBallMoveListener.ballMove();
        }
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight;
        }
        Log.e("updateViewPosition","getHeight: "+getHeight());
        if (desY > mScreenHeight - getHeight()- Utils.dp2px(context,20)) {
            desY = mScreenHeight - getHeight()- Utils.dp2px(context,20);
            Log.d("updateViewPosition","mOriginalY: "+mOriginalY+"event.getRawY():"+event.getRawY()+"mOriginalRawY: "+mOriginalRawY+"desY: ++"+desY);
        }
        Log.d("updateViewPosition","mOriginalY: "+mOriginalY+"event.getRawY():"+event.getRawY()+"mOriginalRawY: "+mOriginalRawY+"desY: "+desY);
        setY(desY);
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        int[] location = new int[2] ;
      /*  mOriginalX = getX();
        mOriginalY = getY();*/
//        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
//        getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
//        location [0]--->x坐标,location [1]--->y坐标
        mOriginalX = getX();
//        mOriginalY = location [1];
        mOriginalY =  getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
        Log.e("updateViewPosition","mOriginalY: "+mOriginalY+"mOriginalRawY: "+mOriginalRawY+"mScreenHeight: "+mScreenHeight);
    }

    protected void updateSize() {
        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    public void moveToEdge() {
        float moveDistance = isNearestLeft() ? MARGIN_EDGE : mScreenWidth - MARGIN_EDGE;
        mMoveAnimator.start(moveDistance, getY());
    }

    protected boolean isNearestLeft() {
        int middle = mScreenWidth / 2;
        return getX() < middle;
    }

    public void onRemove() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onRemove(this);
        }
    }

    protected class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - getX()) * progress;
            float deltaY = (destinationY - getY()) * progress;
            move(deltaX, deltaY,destinationX);

            if (progress < 1) {
                handler.post(this);
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

    private void move(float deltaX, float deltaY,float destinationX) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
        if((getX()+deltaX)==destinationX){
            floatBallMoveListener.ballStop();
        }
    }

}
