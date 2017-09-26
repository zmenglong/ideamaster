package gg.attitude.fragments.account.register;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import gg.attitude.R;
import gg.attitude.activity.MainActivity;
import gg.attitude.base.BaseFragment;
import gg.attitude.api.ConstantValues;
import gg.attitude.listener.login.OnRegistListener;
import gg.attitude.utils.ErrorCodeUtils;
import gg.attitude.utils.AccountUtils;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class Fragment_Register_Second extends BaseFragment {
    private MaterialEditText code;
    private MaterialEditText password;
    private FancyButton btn_register;
    private String phoneNum;

    private static final int REGISTER_SUCCESS=1;
    private static final int REGISTER_FAILURE=-1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case REGISTER_SUCCESS:
                    dismissLoadingDialog();
                    showActivity(getActivity(),MainActivity.class,true);
                    break;
                case REGISTER_FAILURE:
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
        btn_register= (FancyButton) mView.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(mClickListener);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_register2;
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_register:
                    if (!isReady2Register()) return;
                    showLoadingDialog(getActivity());
                    AccountUtils.try2Regist(getActivity(),phoneNum
                            , password.getText().toString(), code.getText().toString()
                            , new OnRegistListener() {
                                @Override
                                public void onSuccess() {
                                    mHandler.sendEmptyMessageDelayed(REGISTER_SUCCESS, ConstantValues.DELAY_TIME);
                                }
                                @Override
                                public void onFailure(int i, String s) {
                                    Message message=new Message();
                                    message.what=REGISTER_FAILURE;
                                    message.arg1=i;
                                    mHandler.sendMessageDelayed(message,ConstantValues.DELAY_TIME);
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
