package com.tangbing.admin.animation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.Window;

/**
 * Created by tangbing on 2019/9/17.
 * Describe :
 */

public class PageActivity extends Activity {
    protected void onCreate(android.os.Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        PageWidget pageWidget = new PageWidget(this);

        Display display = getWindowManager().getDefaultDisplay();
        int width  = display.getWidth();
        int height = display.getHeight();

        pageWidget.SetScreen(width, height);

        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hh);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.mipmap.default_cover);

        Bitmap foreImage = Bitmap.createScaledBitmap(bm1, width, height,false);
        Bitmap bgImage = Bitmap.createScaledBitmap(bm2, width, height,false);

        pageWidget.setBgImage(bgImage);
        pageWidget.setForeImage(foreImage);

        setContentView(pageWidget);

        super.onCreate(savedInstanceState);
    }

}
