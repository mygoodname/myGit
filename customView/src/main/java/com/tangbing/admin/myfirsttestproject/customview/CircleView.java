package com.tangbing.admin.myfirsttestproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.tangbing.admin.myfirsttestproject.R;

/**
 * Created by tangbing on 2020/4/29.
 * Describe :自定义圆
 * */
public class CircleView extends View {

    private Paint paint;
    private int colorResource= Color.RED;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleViewStyle);
        colorResource=typedArray.getColor(R.styleable.CircleViewStyle_circleColor,getResources().getColor(R.color.cardview_dark_background));
        typedArray.recycle();
        init();
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

        if (widthMode == MeasureSpec.AT_MOST
                && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 200);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w=getWidth();
        int h=getHeight();

        canvas.drawCircle((float) (w/2),(float)(h/2),w/2,paint);
    }
}
