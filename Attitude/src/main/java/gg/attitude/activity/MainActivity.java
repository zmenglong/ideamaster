package gg.attitude.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.base.BaseActivity;
import gg.attitude.bean.UserBean;
import gg.attitude.helper.MenuDrawerHelper;
import gg.attitude.utils.AccountUtils;
import gg.attitude.utils.PhotoUtils;

public class MainActivity extends BaseActivity {

    private MenuDrawerHelper menuDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar("态 度",false,true,false);
    }
    @Override
    protected void initViews() {
        //initDrawerHelper
        menuDrawerHelper=new MenuDrawerHelper(MainActivity.this);
        menuDrawerHelper.initMenuDrawer(toolbar);
        menuDrawerHelper.onClick(menuDrawerHelper.ll1);
        menuDrawerHelper.updataAll(mUser.getAvatarUrl(),mUser.getNickName());
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {
    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {
        showActivity(MainActivity.this,SubmitActivity.class);
    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }



    /**
     * 屏蔽返回键
     */
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (menuDrawerHelper.isOpen()) {
                menuDrawerHelper.onBackPressed();
                return true;
            } else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    System.exit(0);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mUser!=null)
            if (menuDrawerHelper!=null) {
                menuDrawerHelper.updateNickname(mUser.getNickName());
            }else{
                initViews();
            }
    }

    /**
     * 处理更换头像的Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.handleCameraOrPhotoResult(MainActivity.this
                , requestCode, resultCode, data, menuDrawerHelper.getAvatar()
                , new PhotoUtils.OnResetAvatarResultListener() {
                    //当相机/相册选择图片事件完成时回调
                    @Override
                    public void onSuccess(String filePath) {
                        //若获取到图片则将图片上传
                        showLoadingDialog();
                        final BmobFile bmobFile=new BmobFile(new File(filePath));
                        bmobFile.uploadblock(MainActivity.this, new UploadFileListener() {
                            //当上传成功后回调
                            @Override
                            public void onSuccess() {
                                Message msg=new Message();
                                msg.what=CHANGE_AVATAR_SUCCESS;
                                msg.obj=bmobFile;
                                mHandler.sendMessageDelayed(msg, ConstantValues.DELAY_TIME);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Message msg=new Message();
                                msg.what=CHANGE_AVATAR_FAILED;
                                msg.obj=s;
                                mHandler.sendMessageDelayed(msg,ConstantValues.DELAY_TIME);
                            }
                        });

                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        showToast(errorMsg);
                    }
                });
    }


    private static final int CHANGE_AVATAR_SUCCESS=1;
    private static final int CHANGE_AVATAR_FAILED=0;

    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_AVATAR_SUCCESS:
                    final BmobFile bmobFile=(BmobFile)msg.obj;
                    UserBean newUser=new UserBean();
                    newUser.setAvatarUrl(bmobFile.getFileUrl(MainActivity.this));
                    AccountUtils.updateUserInfo(MainActivity.this, newUser, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            dismissLoadingDialog();
                            menuDrawerHelper.updateAvatar(bmobFile.getFileUrl(MainActivity.this));
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            dismissLoadingDialog();
                            showToast("更新失败"+s);
                        }
                    });
                    break;
                case CHANGE_AVATAR_FAILED:
                    dismissLoadingDialog();
                    showToast(msg.obj.toString());
                    break;
            }
            return false;
        }
    });

    public MenuDrawerHelper getMenuDrawerHelper(){
        return menuDrawerHelper;
    }
}
