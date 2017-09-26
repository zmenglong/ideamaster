package gg.attitude.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.UpdateListener;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.base.BaseActivity;
import gg.attitude.bean.UserBean;
import gg.attitude.utils.AccountUtils;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.ErrorCodeUtils;

/**
 * Created by ImGG on 2016/6/23.
 * Email:gu.yuepeng@foxmail.com
 */
public class EditMyInfoActivity extends BaseActivity {

    private LinearLayout sexLL;
    private MaterialEditText nickname,age,email,place;
    private TextView sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_edit);
        initToolBar("个人信息",true,true,true);
    }
    @Override
    protected void initViews() {
        sexLL = (LinearLayout) findViewById(R.id.sexLL);
        nickname = (MaterialEditText) findViewById(R.id.nickname);
        age = (MaterialEditText) findViewById(R.id.age);
        email = (MaterialEditText) findViewById(R.id.email);
        place = (MaterialEditText) findViewById(R.id.place);
        sex = (TextView) findViewById(R.id.sex);

        nickname.setText(mUser.getNickName());
        age.setText(mUser.getAge()+"");
        email.setText(mUser.getEmail());
        place.setText(mUser.getPlace());
        sex.setText(getSexStr(mUser.getSex()));
        sexLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showChooseDialog(EditMyInfoActivity.this, new String[]{
                                "男   ♂", "女   ♀"}, "性别：", R.style.Alert_Common_Theme
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        sex.setText("男");
                                        break;
                                    case 1:
                                        sex.setText("女");
                                        break;
                                }
                            }
                        });
            }
        });
    }

    private String getSexStr(Boolean sex) {
        if (sex)    return "男";
        else    return "女";
    }
    private Boolean getSexBoolean(String sexStr) {
        if (sexStr=="男")    return true;
        else    return false;
    }

    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {
        onBackPressed();
    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {
        if (nickname.getText().toString().isEmpty()){
            nickname.setError("昵称不能为空");
            return;
        }
        if (age.getText().toString().isEmpty()){
            age.setError("年龄不能为空");
            return;
        }
        if (!email.isValid(ConstantValues.EMAIL_CHECK)){
            email.setError("邮箱格式不合法");
            return;
        }
        showLoadingDialog();
        UserBean newUser=new UserBean();
        newUser.setNickName(nickname.getText().toString());
        newUser.setSex(getSexBoolean(sex.getText().toString()));
        newUser.setAge(age.getText().toString());
        newUser.setEmail(email.getText().toString());
        newUser.setPlace(place.getText().toString());
        AccountUtils.updateUserInfo(EditMyInfoActivity.this, newUser, new UpdateListener() {
            @Override
            public void onSuccess() {
                mHandler.sendEmptyMessageDelayed(UPDATE_SUCCESS,ConstantValues.DELAY_TIME);
            }

            @Override
            public void onFailure(int i, String s) {
                Message msg=new Message();
                msg.what=UPDATE_FAILED;
                msg.arg1=i;
                mHandler.sendMessageDelayed(msg, ConstantValues.DELAY_TIME);
            }
        });
    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    @Override
    public void onBackPressed() {
        DialogUtils.showCustomConfirmOrCancelDialog(EditMyInfoActivity.this,
                "","不保存就退出吗", "确定", "取消", new DialogUtils.OnConfirmCallBack() {
                    @Override
                    public void onConfirmed(DialogInterface dialog) {
                        finish();
                    }
                });
    }

    private static final int UPDATE_SUCCESS=0;
    private static final int UPDATE_FAILED=-1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_SUCCESS:
                    showToast("请及时登录邮箱进行验证");
                    dismissLoadingDialog();
                    finish();
                    break;
                case UPDATE_FAILED:
                    dismissLoadingDialog();
                    place.setError(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1));
                    break;
            }
            return false;
        }
    });
}
