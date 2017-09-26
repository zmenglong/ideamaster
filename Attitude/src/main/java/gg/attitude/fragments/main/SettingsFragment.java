package gg.attitude.fragments.main;

import android.view.View;
import android.widget.LinearLayout;

import cn.bmob.v3.BmobUser;
import gg.attitude.R;
import gg.attitude.activity.AboutActivity;
import gg.attitude.activity.EditMyInfoActivity;
import gg.attitude.activity.account.ChangePSWActivity;
import gg.attitude.activity.account.WelcomeActivity;
import gg.attitude.base.BaseFragment;
import gg.attitude.bean.UserBean;
import mehdi.sakout.fancybuttons.FancyButton;

import static android.view.View.OnClickListener;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class SettingsFragment extends BaseFragment {
    private LinearLayout info,changePSW,about_us;
    private FancyButton logout;
    @Override
    protected void initViews(View mView) {
        info= (LinearLayout) mView.findViewById(R.id.info);
        changePSW= (LinearLayout) mView.findViewById(R.id.changePassword);
        about_us= (LinearLayout) mView.findViewById(R.id.about_us);
        logout= (FancyButton) mView.findViewById(R.id.logout);
        info.setOnClickListener(mClickListener);
        changePSW.setOnClickListener(mClickListener);
        about_us.setOnClickListener(mClickListener);
        logout.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.info:
                    showActivity(mActivity, EditMyInfoActivity.class,false);
                    break;
                case R.id.changePassword:
                    showActivity(mActivity, ChangePSWActivity.class,false);
                    break;
                case R.id.about_us:
                    showActivity(mActivity, AboutActivity.class,false);
                    break;
                case R.id.logout:
                    BmobUser.logOut(mActivity);   //清除缓存用户对象
                    mUser = BmobUser.getCurrentUser(mActivity, UserBean.class); // 现在的currentUser是null了
                    showActivity(mActivity, WelcomeActivity.class,true);
                    break;
            }
        }
    };
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }
}
