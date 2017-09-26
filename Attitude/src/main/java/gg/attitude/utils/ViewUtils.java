package gg.attitude.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import gg.attitude.R;

/**
 * Created by ImGG on 2016/8/4.
 */
public class ViewUtils {
    public static final int SCALE =0;
    public static final int ALPHA=1;
    public static Animation getAnimation(Context context, View view, int type){
        switch (type){
            case SCALE:
                return AnimationUtils.loadAnimation(context, R.anim.scale);
            case ALPHA:
                return AnimationUtils.loadAnimation(context, R.anim.alpha);
            default:
                throw new RuntimeException("type应为SCALE_LARGE或ALPHA中的一种");
        }
    }
}
