package com.tangbing.admin.myfirsttestproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.SeekBar;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.tangbing.admin.myfirsttestproject.customview.CircleProgressView;
import com.tangbing.admin.myfirsttestproject.customview.HorizontalProgressView;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by tangbing on 2019/4/30.
 * Describe :
 */

public class CustomProgressActivity extends AppCompatActivity {


    private SeekBar mSeekBarOuter;
    private SeekBar mSeekBarInner;
    private LobsterShadeSlider mSeekBarColor;
    private SeekBar mSeekBarProgress;
    private CircleProgressView mCircleProgressNormal;
    private CircleProgressView mCircleProgressFillIn;
    private CircleProgressView mCircleProgressFillInArc;
    private HorizontalProgressView mHorizontalProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress);
        mCircleProgressNormal = $(R.id.circle_progress_normal);
        mCircleProgressFillIn = $(R.id.circle_progress_fill_in);
        mCircleProgressFillInArc = $(R.id.circle_progress_fill_in_arc);
        mHorizontalProgressView = $(R.id.horizontal_progress);
        mSeekBarColor = $(R.id.seek_bar_color);
        mSeekBarInner = $(R.id.seek_bar_inner);
        mSeekBarOuter = $(R.id.seek_bar_outer);
        mSeekBarProgress = $(R.id.seek_bar_progress);

        mSeekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleProgressNormal.setProgress(progress);
                mCircleProgressFillIn.setProgress(progress);
                mCircleProgressFillInArc.setProgress(progress);
                mHorizontalProgressView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSeekBarInner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleProgressNormal.setNormalBarSize(progress == 0 ? 1 : progress / 6);
//                mCircleProgressFillIn.setRadius(progress == 0 ? 20 : progress / 6 + 20);
                mCircleProgressFillInArc.setInnerPadding(progress == 0 ? 1 : progress / 6);
                mHorizontalProgressView.setNormalBarSize(progress == 0 ? 1 : progress / 6);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarOuter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleProgressNormal.setReachBarSize(progress == 0 ? 1 : progress / 6);
                mCircleProgressFillIn.setReachBarSize(progress == 0 ? 1 : progress / 6);
                mHorizontalProgressView.setReachBarSize(progress == 0 ? 1 : progress / 6);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarColor.addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {
                mCircleProgressNormal.setReachBarColor(color);
                mCircleProgressNormal.setNormalBarColor(adjustAlpha(color, 0.3f));

                mHorizontalProgressView.setReachBarColor(color);
                mHorizontalProgressView.setNormalBarColor(adjustAlpha(color, 0.3f));

                mCircleProgressFillIn.setReachBarColor(color);
                mCircleProgressFillIn.setNormalBarColor(adjustAlpha(color, 0.3f));

                mCircleProgressFillInArc.setReachBarColor(color);
                mCircleProgressFillInArc.setOuterColor(adjustAlpha(color, 0.3f));

            }

            @Override
            public void onColorSelected(@ColorInt int color) {

            }
        });
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }
}
