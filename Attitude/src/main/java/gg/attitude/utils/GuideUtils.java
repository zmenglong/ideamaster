package gg.attitude.utils;

import android.app.Activity;
import android.view.View;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

/**
 * Created by ImGG on 2016/8/4.
 */
public final class GuideUtils {
    private GuideUtils(){}

    public static void showGuide2View(Activity activity, View targetView, String info,MaterialIntroListener materialIntroListener){

        new MaterialIntroView.Builder(activity)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(false)//是否传递点击事件给下层View
                .setInfoText(info)
                .setTarget(targetView)
                .setTargetPadding(25)
                .dismissOnTouch(true)
                .setUsageId(info) //THIS SHOULD BE UNIQUE ID
                .setListener(materialIntroListener)
                .show();
    }
    public static void showTinyGuide2View(Activity activity, View targetView, String info,MaterialIntroListener materialIntroListener){

        new MaterialIntroView.Builder(activity)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(false)//是否传递点击事件给下层View
                .setInfoText(info)
                .setTarget(targetView)
                .setTargetPadding(25)
                .dismissOnTouch(true)
                .setUsageId(info) //THIS SHOULD BE UNIQUE ID
                .setListener(materialIntroListener)
                .show();
    }

}
