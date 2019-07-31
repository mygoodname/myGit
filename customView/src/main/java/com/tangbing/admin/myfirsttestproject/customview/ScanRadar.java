package com.tangbing.admin.myfirsttestproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tangbing.admin.myfirsttestproject.R;

public class ScanRadar extends View {
    public ScanRadar(Context context) {
        super(context);
    }

    public ScanRadar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 这是绘图方法      * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        //透明度
        paint.setAlpha(80);
        paint.setStrokeWidth(2);
        float x = getMeasuredWidth();
        RectF oval = new RectF(0, 0, getRight()*2,x*2);
        paint.setColor(getResources().getColor(R.color.ownColor));
        canvas.drawArc(oval,180,90,true,paint);
    }
}