package gg.attitude.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import gg.attitude.R;
import gg.attitude.activity.account.WelcomeActivity;
import gg.attitude.base.BaseActivity;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class SplashActivity extends BaseActivity {
    private static final int TO_MAIN =1;
    private static final int TO_WELCOME =2;

    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case TO_MAIN:
                    showActivity(SplashActivity.this,MainActivity.class);
                    finish();
                    break;
                case TO_WELCOME:
                    showActivity(SplashActivity.this, WelcomeActivity.class);
                    finish();
                    break;
            }
            return false;
        }
    });



    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
        if(mUser == null){
            mHandler.sendEmptyMessageDelayed(TO_WELCOME,2000);
        }else{
            mHandler.sendEmptyMessageDelayed(TO_MAIN,2000);
        }
    }

    @Override
    protected void initViews() {
        appName = (TextView) findViewById(R.id.app_name);
        // 将字体文件保存在assets/fonts/目录下.创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/RZBlack.TTF");
        // 应用字体
        appName.setTypeface(typeFace);
        appName.setText("态 度");
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }
}
