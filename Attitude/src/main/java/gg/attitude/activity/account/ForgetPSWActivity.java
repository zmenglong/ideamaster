package gg.attitude.activity.account;

import android.os.Bundle;

import gg.attitude.R;
import gg.attitude.base.BaseActivity;
import gg.attitude.fragments.account.forgetPSW.Fragment_ForgetPSW_First;

/**
 * Created by ImGG on 2016/8/8.
 */
public class ForgetPSWActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        initToolBar(getStringRes(R.string.ResetPSW),true,false,false);
    }

    @Override
    protected void initViews() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.forget_psw_container,new Fragment_ForgetPSW_First())
                .commit();
    }

    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {
        finish();
    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }
}
