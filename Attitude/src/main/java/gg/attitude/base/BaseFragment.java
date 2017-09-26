package gg.attitude.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.api.ErrorImageType;
import gg.attitude.api.ShowStyle;
import gg.attitude.bean.UserBean;
import gg.attitude.fragments.main.AttitudeFragment;
import gg.attitude.fragments.main.FeedbackFragment;
import gg.attitude.fragments.main.SettingsFragment;
import gg.attitude.fragments.main.SquareFragment;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.ErrorCodeUtils;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public abstract class BaseFragment extends Fragment{
    protected View mView;
    protected BaseActivity mActivity;
    protected UserBean mUser;//获取到当前的登陆用户
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(getLayoutRes(),null);
        mActivity=(BaseActivity)getActivity();
        mUser=mActivity.mUser;
        initViews(mView);
        return mView;
    }

    protected abstract void initViews(View mView);
    protected abstract int getLayoutRes();


    protected abstract void reLoadDataEventAfterLoadError();//当加载失败出现Error页面后点击重新加载事件
    private LinearLayout error_ll;
    private TextView mErrorMsg;
    private ImageView mErrorIV;

    /**
     * 显示加载错误页面
     * @param errorMsg  错误信息
     * @param dismissViews  要隐藏的Views
     */
    protected void showErrorView(String errorMsg,int type, View...dismissViews) {
        if (error_ll==null)
            error_ll= (LinearLayout) mView.findViewById(R.id.error_ll);
        if (mErrorMsg==null)
            mErrorMsg= (TextView) mView.findViewById(R.id.error_msg);
        if (mErrorIV==null)
            mErrorIV= (ImageView) mView.findViewById(R.id.error_iv);
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
    protected void showErrorView(int errorMsgCode,int type,View...dismissViews){
        showErrorView(ErrorCodeUtils.getErrorMsgFromCode(errorMsgCode),type,dismissViews);
    }
    protected void dismissErrorView(View...dismissViews){
        if (error_ll==null)
            error_ll= (LinearLayout) mView.findViewById(R.id.error_ll);
        if (error_ll!=null){
            error_ll.setVisibility(View.GONE);
            for (View view:dismissViews) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }


    protected void showFragment(Fragment targetFragment, int container){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransitionStyle(android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(container, targetFragment);
        transaction.commit();
    }

    /**
     * 有过渡动画的fragment切换
     * @param targetFragment
     * @param container
     * @param showStyle
     */
    @Deprecated
    protected void showFragment(Fragment targetFragment, int container,int showStyle){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (showStyle== ShowStyle.FRAGMENT_FADE){
            transaction.setCustomAnimations(android.support.design.R.anim.abc_fade_in
                    , android.support.design.R.anim.abc_fade_out);}
        else if(showStyle== ShowStyle.TRANSLATE_X){
            transaction.setCustomAnimations(R.anim.translate_in_x
                    , R.anim.translate_out_x);}
        else if(showStyle== ShowStyle.TRANSLATE_Y){
            transaction.setCustomAnimations(R.anim.translate_in_y
                    , R.anim.translate_out_y);}
        transaction.replace(container, targetFragment);
        transaction.commit();
    }


    protected void showActivity(Activity mActivity,Class<?> clazz,boolean finishThis){
        Intent mIntent=new Intent(mActivity,clazz);
        startIntent(mIntent);
        if (finishThis){
            mActivity.finish();
        }
    }
    protected void startIntent(Intent mIntent){
        startActivity(mIntent);
        int SDKversion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(SDKversion  >= 5) {
            mActivity.overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
        }
    }
    private Dialog mLoading;
    public void showLoadingDialog(Activity mActivity){
        if (mLoading==null){
            mLoading= DialogUtils.creatLoadingDialog(mActivity);
        }
        mLoading.show();
    }
    public void dismissLoadingDialog(){
        if (mLoading!=null) {
            mLoading.dismiss();
        }
    }


    protected BaseFragment getFromFragment(int fromFragmentCode){
        switch (fromFragmentCode){
            case ConstantValues.ATTITUDE_FRAGMENT:
                return new AttitudeFragment();
            case ConstantValues.FEEDBACK_FRAGMENT:
                return new FeedbackFragment();
            case ConstantValues.SETTINGS_FRAGMENT:
                return new SettingsFragment();
            case ConstantValues.SQUARE_FRAGMENT:
                return new SquareFragment();
        }
        return null;
    }
    protected String getStringRes(int stringRes) {
        return getResources().getString(stringRes);
    }

    protected void showSnackBar(View v,String msg){
        Snackbar.make(v,">"+msg+"<",Snackbar.LENGTH_SHORT).show();
    }
    protected void showSnackBarByStringRes(View v, int stringRes){
        showSnackBar(v,getStringRes(stringRes));
    }
    protected void showToast(String msg){
        Toast.makeText(mActivity,">"+msg+"<",Toast.LENGTH_SHORT).show();
    }
    protected void showToast(int stringRes){
        showToast(getStringRes(stringRes));
    }
}
