package gg.attitude.activity;

import android.os.Bundle;
import android.widget.TextView;

import gg.attitude.R;
import gg.attitude.base.BaseActivity;

/**
 * Created by ImGG on 2016/8/12.
 */
public class AboutActivity extends BaseActivity {
    private TextView app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initToolBar("关于态度",true,false,false);

    }

    @Override
    protected void initViews() {
        app_name= (TextView) findViewById(R.id.app_name);
        app_name.setText(getStringRes(R.string.app_name)+" v"+getVersion());
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
