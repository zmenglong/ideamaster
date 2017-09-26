package gg.attitude.helper;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import gg.attitude.R;
import gg.attitude.activity.EditMyInfoActivity;
import gg.attitude.activity.MainActivity;
import gg.attitude.fragments.main.AttitudeFragment;
import gg.attitude.fragments.main.FeedbackFragment;
import gg.attitude.fragments.main.SettingsFragment;
import gg.attitude.fragments.main.SquareFragment;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.PhotoUtils;
import gg.attitude.utils.PicassoUtils;
import gg.attitude.views.CircleImageView;

/**
 * 作者：cp on 2016/4/23 20:23
 * 邮箱：18018982073@163.com
 */
public class MenuDrawerHelper implements View.OnClickListener {

    private MainActivity activity;

    DrawerLayout drawerLayout;
    public LinearLayout header,ll1,ll2,ll3,ll4;

    private CircleImageView avatar;//头像
    private TextView nickname;//姓名

    private CircleImageView civ1,civ2,civ3,civ4;

    public CircleImageView getAvatar() {
        return avatar;
    }

    public MenuDrawerHelper(MainActivity activity) {
        this.activity = activity;
    }


    private ActionBarDrawerToggle toggle;
    private boolean fromLL=false;
    public void initMenuDrawer(Toolbar toolbar) {
        drawerLayout= (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.open, R.string.close){
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        header= (LinearLayout) activity.findViewById(R.id.headerLL);//drawer的头部
        ll1= (LinearLayout) activity.findViewById(R.id.ll1);
        ll2= (LinearLayout) activity.findViewById(R.id.ll2);
        ll3= (LinearLayout) activity.findViewById(R.id.ll3);
        ll4= (LinearLayout) activity.findViewById(R.id.ll4);

        civ1= (CircleImageView) ll1.findViewById(R.id.civ1);//选中item的指示器
        civ2= (CircleImageView) ll2.findViewById(R.id.civ2);
        civ3= (CircleImageView) ll3.findViewById(R.id.civ3);
        civ4= (CircleImageView) ll4.findViewById(R.id.civ4);

        avatar = (CircleImageView) header.findViewById(R.id.myTouxiang);
        nickname= (TextView) header.findViewById(R.id.myNickname);


        header.setOnClickListener(this);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
        avatar.setOnClickListener(this);

    }

    public void onBackPressed() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public boolean isOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private AttitudeFragment mAttitudeFragment;
    private SquareFragment mSquareFragment;
    private FeedbackFragment mFeedbackFragment;
    private SettingsFragment mSettingsFragment;
    private int currentID=0;
    @Override
    public void onClick(View v) {
        onBackPressed();
        switch (v.getId()){
            case R.id.myTouxiang:
                selectPhotoClick();
                break;
            case R.id.headerLL:
                Intent mIntent=new Intent(activity,EditMyInfoActivity.class);
                activity.startActivity(mIntent);
                int SDKversion = Integer.valueOf(android.os.Build.VERSION.SDK);
                if(SDKversion  >= 5) {
                    activity.overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
                }
                break;
            case R.id.ll1:
                if (currentID!=R.id.ll1) {
                    mAttitudeFragment = new AttitudeFragment();
                    activity.setTextCenter(R.string.main_title_attitude);
                    showFragment(mAttitudeFragment);
                    setSelectItem(civ1);
                }
                currentID=v.getId();
                break;
            case R.id.ll2:
                if (currentID!=R.id.ll2) {
                    mFeedbackFragment=new FeedbackFragment();
                    activity.setTextCenter(R.string.main_title_feedback);
                    showFragment(mFeedbackFragment);
                    setSelectItem(civ2);
                }
                currentID=v.getId();
                break;
            case R.id.ll3:
                if (currentID!=R.id.ll3) {
                    mSquareFragment = new SquareFragment();
                    activity.setTextCenter(R.string.main_title_squares);
                    showFragment(mSquareFragment);
                    setSelectItem(civ3);
                }
                currentID=v.getId();
                break;
            case R.id.ll4:
                if (currentID!=R.id.ll4) {
                    if (mSettingsFragment==null) {
                        mSettingsFragment=new SettingsFragment();
                    }
                    activity.setTextCenter(R.string.main_title_settings);
                    showFragment(mSettingsFragment);
                    setSelectItem(civ4);
                }
                currentID=v.getId();
                break;
        }
    }

    private void setSelectItem(View v) {
        civ1.setVisibility(View.GONE);
        civ2.setVisibility(View.GONE);
        civ3.setVisibility(View.GONE);
        civ4.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }


    private void selectPhotoClick() {
        DialogUtils.showChooseDialog(activity, new String[]{"拍照", "相册"}, "选择头像", R.style.Alert_Common_Theme, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 相机
                        PhotoUtils.openCamera(activity);
                        break;
                    case 1:
                        // 相册
                        PhotoUtils.openPhoto(activity);
                        break;
                }
            }
        });
    }

    private void showFragment(Fragment mFragment){
        activity.showFragment(mFragment,R.id.container);
    }

    public void updataAll(String avatarUrl,String nickname){
//        Log.i("updataAll-------------",avatarUrl+"---"+nickname);
        updateAvatar(avatarUrl);
        updateNickname(nickname);
    }
    public void updateAvatar(String avatarUrl){
        PicassoUtils.setAvatar2ImageView(activity,avatarUrl,avatar);
    }
    public void updateNickname(String nickname){
        this.nickname.setText(nickname);
    }

    public ActionBarDrawerToggle getToggle(){
        return toggle;
    }
}
