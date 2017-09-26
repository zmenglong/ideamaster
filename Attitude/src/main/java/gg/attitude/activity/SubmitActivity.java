package gg.attitude.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.kyleduo.switchbutton.SwitchButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.api.GuideInfo;
import gg.attitude.base.BaseActivity;
import gg.attitude.bean.AttitudeBean;
import gg.attitude.listener.attitude.OnSubmitListener;
import gg.attitude.utils.AttitudeUtils;
import gg.attitude.utils.ErrorCodeUtils;
import gg.attitude.utils.GuideUtils;

/**
 * Created by ImGG on 2016/6/14.
 * Email:gu.yuepeng@foxmail.com
 */

public class SubmitActivity extends BaseActivity {

    private SwitchButton isNiMing;
    private LinearLayout describeLL;//装问题描述以及a,b选项的ll
    private MaterialEditText contentTxt;//问题描述的正文，文字部分
    private MaterialEditText a;//a选项描述
    private MaterialEditText b;//b选项描述
    private MaterialEditText times2feedback;//多少人时反馈，默认50条
    private MaterialEditText when2feedback;//最迟反馈时间，默认5天
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        initToolBar("编 辑",true,true,true);
    }

    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {
        finish();
    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {
        resetAllErrorMsg_NoUnderLine(contentTxt,a,b,times2feedback,when2feedback);
        dismissKeyBoard();
        if (!isReady2Submit()) return;
        setRightIVClickable(false);
        try2Submit();
    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {
    }

    @Override
    protected void initViews() {
        isNiMing= (SwitchButton) findViewById(R.id.isNiMing);
        describeLL= (LinearLayout) findViewById(R.id.describeLL);
        contentTxt= (MaterialEditText) findViewById(R.id.contentTxt);
        a= (MaterialEditText) findViewById(R.id.a);
        b= (MaterialEditText) findViewById(R.id.b);
        times2feedback= (MaterialEditText) findViewById(R.id.times);
        when2feedback= (MaterialEditText) findViewById(R.id.when);
        GuideUtils.showGuide2View(SubmitActivity.this,describeLL, GuideInfo.SUBMIT_DESCRIBE,materialIntroListener);
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    private boolean isReady2Submit(){
        if (contentTxt.getText().toString().length()==0
                ||contentTxt.getText().toString().length()>120){
            contentTxt.setError("问题的描述不能为空，且不能超过120字");
            return false;
        }else if (a.getText().toString().length()==0||a.getText().toString().length()>24){
            a.setError("选项描述不能为空，且不能超过24字");
            return false;
        }else if (b.getText().toString().length()==0||b.getText().toString().length()>24){
            b.setError("选项描述不能为空，且不能超过24字");
            return false;
        }else if (times2feedback.getText().toString().length()==0
                ||times2feedback.getText().toString().length()>3
                ||Integer.valueOf(times2feedback.getText().toString())>300
                ||Integer.valueOf(times2feedback.getText().toString())<50){
            times2feedback.setError("条数设置应在[50,300]区间内，且不能空");
            return false;
        }else if(when2feedback.getText().toString().length()==0
                ||when2feedback.getText().toString().length()>2
                ||Integer.valueOf(when2feedback.getText().toString())>30
                ||Integer.valueOf(when2feedback.getText().toString())<1){
            when2feedback.setError("时间设置应在[1,30]区间内，且不能空");
            return false;
        }
        return true;
    }

    private void try2Submit() {
        //创建一个AttitudeBean对象，设置属性
        AttitudeBean mAttitude=new AttitudeBean();
        mAttitude.setContentTxt(contentTxt.getText().toString());
        mAttitude.setaDescription(a.getText().toString());
        mAttitude.setbDescription(b.getText().toString());
        mAttitude.setTimes2Feedback(times2feedback.getText().toString());
        mAttitude.setWhen2Feedback(when2feedback.getText().toString());
        //启动动画
        showLoadingDialog();
        AttitudeUtils.submitAttitude(SubmitActivity.this,mUser, isNiMing.isChecked()
                , mAttitude, new OnSubmitListener() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessageDelayed(SUBMIT_SUCCESS, ConstantValues.DELAY_TIME);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Message message=new Message();
                        message.what=SUBMIT_FAILURE;
                        message.arg1=i;
                        mHandler.sendMessageDelayed(message,ConstantValues.DELAY_TIME);
                    }
                });
    }
    private static final int SUBMIT_SUCCESS=100;
    private static final int SUBMIT_FAILURE=99;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SUBMIT_SUCCESS:
                    dismissLoadingDialog();
                    setRightIVClickable(true);
                    showToast("发布成功:)");
                    finish();
                    break;
                case SUBMIT_FAILURE:
                    dismissLoadingDialog();
                    setRightIVClickable(true);
                    //应该处理错误信息
                    contentTxt.setError(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1));
                    break;
            }
            return false;
        }
    });

    MaterialIntroListener materialIntroListener=new MaterialIntroListener() {
        @Override
        public void onUserClicked(String materialIntroViewId) {
            if (GuideInfo.SUBMIT_DESCRIBE.equals(materialIntroViewId)){
                GuideUtils.showGuide2View(SubmitActivity.this,times2feedback,GuideInfo.SUBMIT_2FEEDBACK,materialIntroListener);
            }else if (GuideInfo.SUBMIT_2FEEDBACK.equals(materialIntroViewId)){
                GuideUtils.showGuide2View(SubmitActivity.this,isNiMing,GuideInfo.SUBMIT_IS_NIMING,materialIntroListener);
            }else if (GuideInfo.SUBMIT_IS_NIMING.equals(materialIntroViewId)){
                GuideUtils.showGuide2View(SubmitActivity.this, getRightIV(),GuideInfo.SUBMIT_SUBMIT,materialIntroListener);
            }
        }
    };
}
