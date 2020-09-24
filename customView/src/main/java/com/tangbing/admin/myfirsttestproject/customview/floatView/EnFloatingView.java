package com.tangbing.admin.myfirsttestproject.customview.floatView;

import android.content.Context;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.tangbing.admin.myfirsttestproject.FloatBallMoveListener;
import com.tangbing.admin.myfirsttestproject.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * @ClassName EnFloatingView
 * @Description 悬浮窗
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:04
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:04
 */
public class EnFloatingView extends FloatingMagnetView {

    private final CircleImageView floatImage;
    private Context context;
//    public RelativeLayout floatLayout;
    public EnFloatingView(@NonNull Context context) {
        super(context, null);
        this.context=context;
        inflate(context, R.layout.en_floating_view, this);
        floatImage = findViewById(R.id.floatImage);
//        floatLayout = findViewById(R.id.floatLayout);
    }

    public void setIconImage(@DrawableRes int resId){

        floatImage.setImageResource(resId);
    }
   /* public void setImageUrl(String url){
        Glide.with(context).load(url).error(R.mipmap.ic_default).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                //加载完成后的处理
                floatImage.setImageDrawable(resource);
//                startRotate();
            }
        });
    }*/
    Animation animation;


    public void startRotate() {
        if (floatImage != null) {
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
            }
            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
            animation.setInterpolator(lin);
            floatImage.post(new Runnable() {
                @Override
                public void run() {
                    floatImage.startAnimation(animation);
                }
            });
        }
    }
    public void stopRotate() {
        if (floatImage == null) return;
        if (floatImage.getAnimation() == null) return;
        floatImage.clearAnimation();
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }
    public boolean isAnimationing() {
        if (floatImage == null) return false;
        if (floatImage.getAnimation() == null) return false;
        if (floatImage.getAnimation() != null) return true;
        return false;
    }
}
