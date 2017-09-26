package gg.attitude.activity.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.UpdateListener;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.base.BaseActivity;
import gg.attitude.utils.AccountUtils;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.ErrorCodeUtils;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ImGG on 2016/8/10.
 */
public class ChangePSWActivity extends BaseActivity implements View.OnClickListener{

    private MaterialEditText password_ex;
    private MaterialEditText password1,password2;
    private FancyButton btn_changePSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        initToolBar(getStringRes(R.string.Change_psw),true,false,false);
    }

    @Override
    protected void initViews() {
        password_ex= (MaterialEditText) findViewById(R.id.password_ex);
        password1= (MaterialEditText) findViewById(R.id.password1);
        password2= (MaterialEditText) findViewById(R.id.password2);

        btn_changePSW= (FancyButton) findViewById(R.id.btn_changePSW);
        btn_changePSW.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_changePSW:
                if (!isReady2ChangePSW())return;
                showLoadingDialog();
                AccountUtils.try2ResetPsw(ChangePSWActivity.this,password_ex.getText().toString()
                        , password1.getText().toString(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessageDelayed(CHANGE_SUCCESS, ConstantValues.DELAY_TIME);
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Message msg=new Message();
                        msg.what=CHANGE_FAILURE;
                        msg.arg1=i;
                        mHandler.sendMessageDelayed(msg, ConstantValues.DELAY_TIME);
                    }
                });


                break;
        }
    }
    private boolean isReady2ChangePSW() {
        if ("".equals(password_ex.getText().toString())){
            password_ex.setError("原密码不能为空");
            return false;
        }else if (password_ex.getText().toString().equals(password1.getText().toString())){
            password1.setError("新旧密码不能相同");
            return false;
        }else if (password1.getText().toString().length()<6){
            password1.setError("为了您的账户安全，新密码不得少于6位");
            return false;
        }else if (!password1.getText().toString().equals(password2.getText().toString())){
            password2.setError("两次输入的密码不同，请核实后重试");
            return false;
        }
        return true;
    }

    private static final int CHANGE_SUCCESS =1;
    private static final int CHANGE_FAILURE =-1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_SUCCESS:
                    dismissLoadingDialog();
                    DialogUtils.showConfirmOrCancelDialog(ChangePSWActivity.this, "密码修改成功", new DialogUtils.OnConfirmCallBack() {
                        @Override
                        public void onConfirmed(DialogInterface dialog) {
                            finish();
                        }
                    });
                    break;
                case CHANGE_FAILURE:
                    dismissLoadingDialog();
                    password_ex.setError(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1));
                    break;
            }
            return false;
        }
    });
}
