package gg.attitude.activity.account;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import gg.attitude.R;
import gg.attitude.activity.MainActivity;
import gg.attitude.base.BaseActivity;
import gg.attitude.api.ConstantValues;
import gg.attitude.listener.login.OnLoginListener;
import gg.attitude.utils.ErrorCodeUtils;
import gg.attitude.utils.AccountUtils;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class LoginActivity extends BaseActivity {

    private MaterialEditText phoneNum;
    private MaterialEditText password;
    private FancyButton login;

    private LinearLayout forgetLL;//忘记密码

    private static final int LOGIN_SUCCESS=-98;
    private static final int LOGIN_FAILED=-99;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    dismissLoadingDialog();
                    showActivity(LoginActivity.this, MainActivity.class);
                    finish();
                    break;
                case LOGIN_FAILED:
                    dismissLoadingDialog();
                    password.setError(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1));
                    break;
            }
            return false;
        }
    });
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar("登  录",true,false,false);
    }

    @Override
    protected void initViews() {
        phoneNum= (MaterialEditText) findViewById(R.id.phoneNum);
        password= (MaterialEditText) findViewById(R.id.password);
        login= (FancyButton) findViewById(R.id.btn_login);
        login.setOnClickListener(mClickListener);
        forgetLL= (LinearLayout) findViewById(R.id.forgetLL);
        forgetLL.setOnClickListener(mClickListener);
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
                    if (!isReady2Login())
                        return;
                    try2Login(phoneNum.getText().toString(),password.getText().toString());
                    break;
                case R.id.forgetLL:
                    showActivity(LoginActivity.this,ForgetPSWActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    private void try2Login(String phoneNum, String password) {

        showLoadingDialog();
        AccountUtils.try2LoginByAccount(LoginActivity.this,phoneNum, password, new OnLoginListener() {
            @Override
            public void onSuccess() {
                mHandler.sendEmptyMessageDelayed(LOGIN_SUCCESS, ConstantValues.DELAY_TIME);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(code+"-------",msg);
                Message message=new Message();
                message.what=LOGIN_FAILED;
                message.arg1=code;
                mHandler.sendMessageDelayed(message,ConstantValues.DELAY_TIME);
            }
        });
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

    private boolean isReady2Login() {
        if (phoneNum.getText().toString().length()!=11){
            phoneNum.setError("您输入的手机号码不合法");
            return false;}
        else if(password.getText().toString().length()==0){
            password.setError("密码不能为空");
            return false;
        }
        dismissKeyBoard();
        return true;
    }
}
