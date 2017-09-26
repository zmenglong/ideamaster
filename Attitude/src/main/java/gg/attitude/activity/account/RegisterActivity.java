package gg.attitude.activity.account;

import android.os.Bundle;

import gg.attitude.R;
import gg.attitude.base.BaseActivity;
import gg.attitude.fragments.account.register.Fragment_Register_First;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class RegisterActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar("注  册",true,false,false);
    }

    @Override
    protected void initViews() {
        //将注册的首页添加到container中
        getSupportFragmentManager().beginTransaction()
                .add(R.id.register_container,new Fragment_Register_First())
                .commit();
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {
        onBackPressed();
    }


    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }

    @Override
    public void onBackPressed() {
        showActivity(this,WelcomeActivity.class);
        finish();
    }
}
