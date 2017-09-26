package gg.attitude.utils;

import android.content.Context;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import gg.attitude.api.ConstantValues;
import gg.attitude.bean.AttitudeBean;
import gg.attitude.bean.UserBean;
import gg.attitude.listener.OnQueryListener;
import gg.attitude.listener.login.OnGetCodeListener;
import gg.attitude.listener.login.OnLoginListener;
import gg.attitude.listener.login.OnRegistListener;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public final class AccountUtils {
    private AccountUtils(){}
    /**
     * 尝试登陆操作
     * @param mUserName
     * @param mPassword
     * @param mLoginListener
     */
    public static void try2Login(Context mContext,String mUserName, String mPassword, final OnLoginListener mLoginListener){
        UserBean mUser = new UserBean();
        mUser.setUsername(mUserName);
        mUser.setPassword(mPassword);
        mUser.login(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mLoginListener.onSuccess();
            }
            @Override
            public void onFailure(int code, String msg) {
                mLoginListener.onFailure(code,msg);
            }
        });
    }
    /**
     * 尝试登陆操作
     * @param mUserName
     * @param mPassword
     * @param mLoginListener
     */
    public static void try2LoginByAccount(Context mContext,String mUserName, String mPassword, final OnLoginListener mLoginListener){
        UserBean.loginByAccount(mContext, mUserName, mPassword, new LogInListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e==null){
                    mLoginListener.onSuccess();
                }
                else {
                    mLoginListener.onFailure(e.getErrorCode(),e.getMessage());
                }
            }
        });
    }

    /**
     * 尝试获取验证码
     * @param phoneNum
     * @param type  获取验证码的类型
     * @param mListener
     */
    public static void try2GetCode(Context mContext,String phoneNum,int type,final OnGetCodeListener mListener) {
        String codeType="IdeaMaster";//默认是注册使用
        if (type== ConstantValues.REGISTER)
            codeType=ConstantValues.SMS_CODE_REGISTER;
        else if(type==ConstantValues.RESET)
            codeType=ConstantValues.SMS_CODE_RESET;
        BmobSMS.requestSMSCode(mContext,phoneNum,codeType,
                new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer smsId, BmobException arg1) {
                        // TODO 自动生成的方法存根
                        if(arg1!=null){
                            mListener.onFailure(smsId,arg1);
                        }else {
                            mListener.onSuccess(smsId,arg1);
                        }
                    }
                });
    }

    /**
     * 采用短信验证码的方式注册
     * @param phone
     * @param password
     * @param code
     * @param mListener
     */
    public static void try2Regist(Context mContext,String phone, String password, String code,final OnRegistListener mListener){
        final UserBean mUser=new UserBean();
        mUser.setUsername(phone);//使用手机号码登陆
        mUser.setMobilePhoneNumber(phone);//设置为手机验证用户
        mUser.setPassword(password);
        mUser.setPsw(password);
        mUser.setAge("0");//默认年龄为0岁
        mUser.setSex(true);//默认性别为男
        mUser.setNew(true);//标记为新用户
        mUser.setAvatarUrl("default");//当用户没有指定时为用户指定一个默认的
        mUser.setNickName("Attitude_"+phone.hashCode());//提供一个默认的Name

        AttitudeBean guideAttitude=new AttitudeBean();
        guideAttitude.setObjectId("5ca050c6a5");//这是由官方账号发布的，用于新用户在用户引导当中显示在反馈页面的动态
        mUser.add2Cares(guideAttitude);//将这条动态添加到新注册用户的关心列表中

        mUser.signOrLogin(mContext, code, new SaveListener() {
                    @Override
                    public void onSuccess() {

                        mListener.onSuccess();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        mListener.onFailure(i,s);
                    }
                });
    }


    /**
     * 请求通过短信验证码的方式重置密码
     * @param password
     * @param code
     * @param mListener
     */
    public static void try2ResetPswBySMS(Context mContext,String password, String code, ResetPasswordByCodeListener mListener){
        BmobUser.resetPasswordBySMSCode(mContext, code, password, mListener);
    }

    /**
     * 请求修改密码
     * @param psw_ex
     * @param password
     * @param mListener
     */
    public static void try2ResetPsw(Context mContext,String psw_ex, String password, UpdateListener mListener){
        BmobUser.updateCurrentUserPassword(mContext,psw_ex,password,mListener);
    }

    public static void queryUserExsit(Context mContext, String phoneNum, final OnQueryListener mListener){
        BmobQuery<UserBean> mQuery=new BmobQuery<>();
        mQuery.addWhereEqualTo("username",phoneNum);
        mQuery.findObjects(mContext, new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> list) {
                if (mListener!=null)
                    mListener.onSuccess(list);
            }
            @Override
            public void onError(int i, String s) {
                if (mListener!=null)
                    mListener.onFailure(i,s);
            }
        });
    }

    /**
     * 根据传进来的newUser中set的信息来更新用户信息
     * @param mContext
     * @param newUser
     * @param mListener
     */
    public static void updateUserInfo(Context mContext,UserBean newUser
            ,final UpdateListener mListener){
        UserBean mUser= BmobUser.getCurrentUser(mContext,UserBean.class);
        newUser.update(mContext, mUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                mListener.onSuccess();
            }
            @Override
            public void onFailure(int i, String s) {
                mListener.onFailure(i,s);
            }
        });
    }
}
