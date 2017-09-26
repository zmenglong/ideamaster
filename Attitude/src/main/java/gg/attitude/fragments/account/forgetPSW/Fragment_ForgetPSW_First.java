package gg.attitude.fragments.account.forgetPSW;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.base.BaseFragment;
import gg.attitude.listener.OnQueryListener;
import gg.attitude.listener.login.OnGetCodeListener;
import gg.attitude.utils.AccountUtils;
import gg.attitude.utils.ErrorCodeUtils;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class Fragment_ForgetPSW_First extends BaseFragment {

    private static final int PHONE_OK=1;
    private static final int PHONE_WRONG =-1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case PHONE_OK:
                    dismissLoadingDialog();
                    Fragment_ForgetPSW_Second mFragment=new Fragment_ForgetPSW_Second();
                    Bundle mBundle=new Bundle();
                    mBundle.putString("phoneNum",phoneNum.getText().toString());
                    mFragment.setArguments(mBundle);
                    showFragment(mFragment,R.id.forget_psw_container);
                    break;
                case PHONE_WRONG:
                    dismissLoadingDialog();
                    phoneNum.setError(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1));

                    break;
            }
            return false;
        }
    });

    private MaterialEditText phoneNum;
    private FancyButton btn_getCode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return mView;
    }

    @Override
    protected void initViews(View mView) {
        phoneNum= (MaterialEditText) mView.findViewById(R.id.phoneNum);
        btn_getCode= (FancyButton) mView.findViewById(R.id.btn_getCode);
        btn_getCode.setOnClickListener(mClickListener);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_forget1;
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_getCode:
                    if (!isReady2GetCode())
                        return;
                showLoadingDialog(getActivity());
                    String phone=phoneNum.getText().toString();
                    AccountUtils.queryUserExsit(mActivity,phone, new OnQueryListener() {
                        @Override
                        public void onSuccess(List mList) {
                            if (mList.isEmpty()){
                                Message message=new Message();
                                message.what= PHONE_WRONG;
                                message.arg1=-998;//自定义错误码，手机尚未被注册
                                mHandler.sendMessageDelayed(message,ConstantValues.DELAY_TIME);
                                return;
                            }
                            AccountUtils.try2GetCode(mActivity,phoneNum.getText().toString()
                                    ,ConstantValues.RESET,new OnGetCodeListener() {
                                @Override
                                public void onSuccess(Integer smsId, BmobException arg1) {
                                    mHandler.sendEmptyMessageDelayed(PHONE_OK, ConstantValues.DELAY_TIME);
                                }

                                @Override
                                public void onFailure(Integer smsId, BmobException arg1) {
                                    Log.i(arg1.getMessage(),arg1.getErrorCode()+"--");
                                    Message message=new Message();
                                    message.what= PHONE_WRONG;
                                    message.arg1=arg1.getErrorCode();
                                    mHandler.sendMessageDelayed(message,ConstantValues.DELAY_TIME);
                                }
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Message message=new Message();
                            message.what= PHONE_WRONG;
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

    private boolean isReady2GetCode() {
        if (phoneNum.getText().toString().length()==11)
            return true;
        phoneNum.setError("您的手机号码不合法");
        return false;
    }
}
