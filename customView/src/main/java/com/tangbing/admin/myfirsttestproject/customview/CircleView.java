package com.tangbing.admin.myfirsttestproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;
import com.tangbing.admin.myfirsttestproject.R;

import androidx.annotation.Nullable;

/**
 * Created by tangbing on 2020/4/29.
 * Describe :自定义圆
 * */
public class CircleView extends View {

    private Paint paint;
    private int colorResource= Color.RED;
    private int mWidth,mHeight;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttras(context,attrs);
        init();
    }

    private void initAttras(Context context,  @Nullable AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleViewStyle);
        colorResource=typedArray.getColor(R.styleable.CircleViewStyle_circleColor,getResources().getColor(R.color.cardview_dark_background));
        mWidth=typedArray.getDimensionPixelSize(R.styleable.CircleViewStyle_circleWidth,100);
        mHeight=typedArray.getDimensionPixelSize(R.styleable.CircleViewStyle_circleHeight,100);
        typedArray.recycle();
    }

    private void init(){
        paint=new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorResource);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        int w=getWidth();
        int h=getHeight();
        int paddingTop=getPaddingTop();
        int paddingLeft=getPaddingLeft();
        int paddingBottom=getPaddingBottom();
        int paddingRight=getPaddingBottom();

        int width=w-paddingLeft-paddingRight;
        int height=h-paddingTop-paddingBottom;
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft+(float) (width/2),paddingTop+(float)(height/2),radius,paint);
    }
}
