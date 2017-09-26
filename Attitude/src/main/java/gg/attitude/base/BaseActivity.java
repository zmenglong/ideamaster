package gg.attitude.base;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import gg.attitude.R;
import gg.attitude.activity.account.LoginActivity;
import gg.attitude.api.ErrorImageType;
import gg.attitude.bean.UserBean;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.ErrorCodeUtils;

/**
 * Created by ImGG on 2016/6/6.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;
    private TextView tvCenter;
    private ImageView ivFeedBack;
    private ImageView rightIV;
    protected UserBean mUser;

    /**
     * 当initToolBar中显示了左边的返回按钮，同时
     */
    protected abstract void initViews();
    protected abstract void doWhenLeftBtnIsOnClick_Toolbar();
    protected abstract void doWhenRightBtnIsOnClick_Toolbar();
    protected abstract void doWhenFeedbackBtnIsOnClick_Toolbar();


    /**
     * 需要说明一点：若想要使用Toolbar要直接调用initToolBar，initToolbar调用后会自动调用initView
     * 若不想使用toolbar则直接调用initView即可
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,  getStringRes(R.string.BMOB_APP_ID));
        Bugtags.start(getStringRes(R.string.BUGTAGS_APP_ID), getApplication()
                , Bugtags.BTGInvocationEventShake);
        mUser= BmobUser.getCurrentUser(getBaseContext(),UserBean.class);
    }

    protected void doOnUserIsNull() {
    }


    /**
     * 初始化什么都没有的Toolbar，只有一个tvCenter
     */
    protected final void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.tb_common);
        appBarLayout = (AppBarLayout) findViewById(R.id.abl_common);
        if (toolbar==null) throw new RuntimeException("请在"+getCallingActivity()+"对应的xml文件中添加toolbar");
        setSupportActionBar(toolbar);
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        tvCenter = (TextView) findViewById(R.id.tv_tb_base);
        ivFeedBack = (ImageView) findViewById(R.id.iv_feedback);
        rightIV = (ImageView) findViewById(R.id.rightIV);
        initViews();
    }

    /**
     * 在不是MainActivity的其它Activity中初始化Toolbar
     * 想要调用initToolbar，必须在布局文件中include-toolbar
     * @param titleCenter   修改标题
     * @param hasLeftBtn    是否有左按钮
     * @param hasRightBtn   是否有右按钮
     * @param isSubmit      是否是Submit页面，若是：则左右按钮分别是×和√；否则是←和
     */
    protected final void initToolBar(String titleCenter,boolean hasLeftBtn
            ,boolean hasRightBtn,boolean isSubmit) {
        toolbar = (Toolbar) findViewById(R.id.tb_common);
        appBarLayout = (AppBarLayout) findViewById(R.id.abl_common);
        if (toolbar==null) throw new RuntimeException("请在"+getLocalClassName()+"对应的xml文件中添加toolbar");
        setSupportActionBar(toolbar);
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        tvCenter = (TextView) findViewById(R.id.tv_tb_base);
        // 将字体文件保存在assets/fonts/目录下，www.linuxidc.com创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/RZBlack.TTF");
        // 应用字体
        tvCenter.setTypeface(typeFace);
        ivFeedBack = (ImageView) findViewById(R.id.iv_feedback);
        rightIV = (ImageView) findViewById(R.id.rightIV);
        tvCenter.setText(titleCenter);
        if (hasLeftBtn){
            initLeftBtn(isSubmit);
        }
        if(hasRightBtn){
            initRightBtn(isSubmit);
        }
        initViews();
    }



    /**
     * 初始化左侧的按钮为返回按钮，同时将他显示出来
     * 需要在调用的Activity中设置其点击事件
     * @param isSubmit  当前页面是否为submit页面，若是则左边btn设置为 x
     */
    private final void initLeftBtn(boolean isSubmit) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if (isSubmit){
//            toolbar.setNavigationIcon(R.drawable.cross);
//        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWhenLeftBtnIsOnClick_Toolbar();
            }
        });
    }
    private final void initRightBtn(boolean isSubmit) {
        if (rightIV ==null){
            return;
        }
        if (isSubmit){
            rightIV.setImageResource(R.drawable.tick);
        }else{
            rightIV.setImageResource(R.drawable.edit);}
        rightIV.setVisibility(View.VISIBLE);
        rightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWhenRightBtnIsOnClick_Toolbar();
            }
        });
    }

    protected final void setRightIVClickable(boolean clickable){
        rightIV.setClickable(clickable);
    }


    /**
     * 设置Toolbar右侧的按钮为可见
     * 设置按钮被点击一次后即消失
     */
    protected final void hasFeedBack(boolean has){
        if (has) {
            ivFeedBack.setVisibility(View.VISIBLE);
            ivFeedBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doWhenFeedbackBtnIsOnClick_Toolbar();
                }
            });
        }
        else
            ivFeedBack.setVisibility(View.GONE);
    }

    /**
     * 通用的切换Fragment
     *
     * @param targetFragment
     * @param content
     */
    public final void showFragment(Fragment targetFragment, int content) {
        getSupportFragmentManager().beginTransaction()
                .replace(content, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public final void setTextCenter(String title){
        tvCenter.setText(title);
    }
    public final void setTextCenter(int stringRes){
        tvCenter.setText(getStringRes(stringRes));
    }
    protected final String getStringRes(int stringRes) {
        return getResources().getString(stringRes);
    }

    protected final void showToast(String str){
        Toast.makeText(BaseActivity.this, "-->"+str, Toast.LENGTH_SHORT).show();
    }
    protected final void showToast(int ResId){
        Toast.makeText(BaseActivity.this, "-->"+getStringRes(ResId), Toast.LENGTH_SHORT).show();
    }
    protected final void goLogin(){
        BmobUser.logOut(getBaseContext());   //清除缓存用户对象
        mUser = BmobUser.getCurrentUser(getBaseContext(),UserBean.class); // 现在的currentUser是null了
        showActivity(this,LoginActivity.class);
    }
    protected final void showActivity(Context mContext, Class<?> mClazz){
        Intent mIntent=new Intent(mContext, mClazz);
        startIntent(mIntent);
    }
    public void startIntent(Intent mIntent){
        startActivity(mIntent);
        int SDKversion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(SDKversion  >= 5) {
            overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
        }
    }

    private Dialog mLoading;
    public final void showLoadingDialog(){
        if (mLoading==null){
            mLoading= DialogUtils.creatLoadingDialog(BaseActivity.this);
        }
        if (!mLoading.isShowing())
            mLoading.show();
    }
    public final void dismissLoadingDialog(){
        if (mLoading!=null&&mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    @Override
    public void finish() {
        super.finish();
        int SDKversion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(SDKversion  >= 5) {
            overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
        }
    }

    protected abstract void reLoadDataEventAfterLoadError();//当加载失败出现Error页面后点击重新加载事件
    private LinearLayout error_ll;
    private TextView mErrorMsg;
    private ImageView mErrorIV;
    /**
     * 显示加载错误页面
     * @param errorMsg  错误信息
     * @param dismissViews  要隐藏的Views
     */
    protected final void showErrorView(String errorMsg,int type, View...dismissViews) {
        if (error_ll==null)
            error_ll= (LinearLayout)findViewById(R.id.error_ll);
        if (mErrorMsg==null)
            mErrorMsg= (TextView)findViewById(R.id.error_msg);
        if (mErrorIV==null)
            mErrorIV= (ImageView) findViewById(R.id.error_iv);
        if (error_ll!=null&&mErrorMsg!=null&&mErrorIV!=null){
            error_ll.setVisibility(View.VISIBLE);
            switch (type){
                case ErrorImageType.HAPPY:
                    mErrorIV.setImageResource(R.drawable.happy);
                    break;
                case ErrorImageType.ANGRY:
                    mErrorIV.setImageResource(R.drawable.angry);
                    break;
                case ErrorImageType.SAD:
                    mErrorIV.setImageResource(R.drawable.sad);
                    break;
                case ErrorImageType.EMBARRASS:
                    mErrorIV.setImageResource(R.drawable.embarrass);
                    break;
                case ErrorImageType.NAUTY:
                    mErrorIV.setImageResource(R.drawable.nauty);
                    break;
            }
            mErrorMsg.setText(errorMsg);
            for (View view:dismissViews) {
                view.setVisibility(View.GONE);
            }
            error_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reLoadDataEventAfterLoadError();
                }
            });
        }
    }
    protected final void showErrorView(int errorMsgCode,int type,View...dismissViews){
        showErrorView(ErrorCodeUtils.getErrorMsgFromCode(errorMsgCode),type,dismissViews);
    }
    protected final void dismissErrorView(View...dismissViews){
        if (error_ll==null)
            error_ll= (LinearLayout)findViewById(R.id.error_ll);
        if (error_ll!=null){
            error_ll.setVisibility(View.GONE);
            for (View view:dismissViews) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    protected final void resetAllErrorMsg_NoUnderLine(MaterialEditText...editTexts){
        for (int i=0;i<editTexts.length;i++){
            editTexts[i].setError("");
        }
    }
    protected final void dismissKeyBoard(){
        //控制软键盘消失
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取当前应用版本号
     * @return
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Bugtags回调
        Bugtags.onResume(this);
        mUser= BmobUser.getCurrentUser(getBaseContext(),UserBean.class);
        if(mUser == null){
            doOnUserIsNull();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }


    public View getRightIV(){
        return rightIV;
    }
}

