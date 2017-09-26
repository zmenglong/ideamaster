package gg.attitude.fragments.account.forgetPSW;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import gg.attitude.R;
import gg.attitude.activity.account.LoginActivity;
import gg.attitude.api.ConstantValues;
import gg.attitude.base.BaseFragment;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.ErrorCodeUtils;
import gg.attitude.utils.AccountUtils;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class Fragment_ForgetPSW_Second extends BaseFragment {
    private MaterialEditText code;
    private MaterialEditText password;
    private FancyButton btn_reset;
    private String phoneNum;

    private static final int RESET_SUCCESS =1;
    private static final int RESET_FAILURE =-1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case RESET_SUCCESS:
                    dismissLoadingDialog();
                    DialogUtils.showConfirmOrCancelDialog(mActivity, "密码找回成功，确认前往登录", new DialogUtils.OnConfirmCallBack() {
                        @Override
                        public void onConfirmed(DialogInterface dialog) {
                            showActivity(mActivity, LoginActivity.class,true);
                        }
                    });
                    break;
                case RESET_FAILURE:
                    dismissLoadingDialog();
                    password.setError(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1));
                    break;
            }
            return false;
        }
    });
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        phoneNum=getArguments().getString("phoneNum");
        return mView;
    }

    @Override
    protected void initViews(View mView) {
        code= (MaterialEditText) mView.findViewById(R.id.code);
        password= (MaterialEditText) mView.findViewById(R.id.password);
        btn_reset= (FancyButton) mView.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(mClickListener);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_forget2;
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_reset:
                    if (!isReady2Register()) return;
                    showLoadingDialog(getActivity());
                    AccountUtils.try2ResetPswBySMS(getActivity(),password.getText().toString()
                            , code.getText().toString()
                            , new ResetPasswordByCodeListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        mHandler.sendEmptyMessageDelayed(RESET_SUCCESS,ConstantValues.DELAY_TIME);
                                    }else{
                                        Message msg=new Message();
                                        msg.what=RESET_FAILURE;
                                        msg.arg1=e.getErrorCode();
                                        mHandler.sendMessageDelayed(msg, ConstantValues.DELAY_TIME);
                                    }
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };

    private boolean isReady2Register() {
        if (code.getText().toString().length()==0){
            code.setError("验证码不能为空");
            return false;
        }else if (password.getText().toString().length()<6){
            password.setError("为了您的账户安全，密码不得少于6位");
            return false;
        }
        return true;
    }
}
