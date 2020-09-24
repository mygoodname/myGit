package com.tangbing.admin.myfirsttestproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.tangbing.admin.myfirsttestproject.R;

import androidx.annotation.Nullable;

/**
 * Created by admin on 2019/1/28.
 */

public class CounterView extends View implements View.OnClickListener{
    private Paint mPaint;
    private Rect mBounds;
    private int count;
    public CounterView(Context context) {
        super(context);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔、Rect
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        setOnClickListener(this);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w=getWidth();
        int h=getHeight();
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        Rect rect=new Rect(w/4,h/4,(w/2+w/4),(h/2+h/4));
        canvas.drawRect(rect,mPaint);
        String text=String.valueOf(count);
        mPaint.getTextBounds(text,0,text.length(),mBounds);
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setTextSize(50);
        canvas.drawText(text,(w/2-mBounds.width()/2),(h/2-mBounds.height()/2),mPaint);
    }

    @Override
    public void onClick(View v) {
        count++;
        invalidate();
    }
}
