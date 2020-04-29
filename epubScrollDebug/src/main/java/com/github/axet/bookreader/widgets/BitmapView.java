package com.github.axet.bookreader.widgets;

import android.graphics.Bitmap;

/**
 * Created by tangbing on 2020/4/15.
 * Describe :
 */
public class BitmapView {
    int pageCursorPosition;
    Bitmap bitmap;

    public BitmapView(int pageCursorPosition, Bitmap bitmap) {
        this.pageCursorPosition = pageCursorPosition;
        this.bitmap = bitmap;
    }

    public int getPageCursorPosition() {
        return pageCursorPosition;
    }

    public void setPageCursorPosition(int pageCursorPosition) {
        this.pageCursorPosition = pageCursorPosition;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
