package com.tangbing.admin.myfirsttestproject.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tangbing.admin.myfirsttestproject.R;

/**
 * Created by tangbing on 2019/7/31.
 * Describe :
 */

public class FloatingRegionView extends FrameLayout{
    public FloatingRegionView(@NonNull Context context) {
        this(context,null);
    }

    public FloatingRegionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public FloatingRegionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.en_floating_view, this);
    }
}
