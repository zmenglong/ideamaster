package gg.attitude.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import gg.attitude.bean.AttitudeBean;
import gg.attitude.bean.UserBean;
import gg.attitude.listener.attitude.OnDeleteListener;
import gg.attitude.listener.attitude.OnHandleListener;
import gg.attitude.listener.attitude.OnQueryAttitudeListener;
import gg.attitude.listener.attitude.OnSubmitListener;
import gg.attitude.listener.attitude.OnUpdateContentListener;
import gg.attitude.listener.attitude.OnUpdateFeedbackListListener;
import gg.attitude.views.card.CardSlidePanel;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public final class AttitudeUtils {

    private AttitudeUtils(){}

    /**
     * 用户发布一个新的Idea
     * @param mUser
     * @param mAttitudeBean
     * @param mOnSubmitListener
     */
    public static void submitAttitude(final Context mContext, final UserBean mUser, Boolean isAnonymity, final AttitudeBean mAttitudeBean, final OnSubmitListener mOnSubmitListener){
        mAttitudeBean.setAuthor(mUser);//设置作者
        mAttitudeBean.setAnonymity(isAnonymity);//设置是否匿名发布
        BmobRelation bmobRelation=new BmobRelation();
        bmobRelation.add(mUser);
        mAttitudeBean.setWho2Feedback(bmobRelation);//添加关联关系，即给他做出反馈
        mAttitudeBean.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mUser.add2Cares(mAttitudeBean);
                mUser.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        if (mOnSubmitListener!=null)
                            mOnSubmitListener.onSuccess();
                    }
                    @Override
                    public void onFailure(int i, String s) {

                        //Log.i("===>","内层"+i+"=="+s);//error=202 msg=username already taken.
                        if (i==202){
                            if (mOnSubmitListener!=null)
                                mOnSubmitListener.onSuccess();
                            return;
                        }
                        if (mOnSubmitListener!=null)
                            mOnSubmitListener.onFailure(i,s);
                    }
                });
            }
            @Override
            public void onFailure(int i, String s) {
                if (mOnSubmitListener!=null)
                    mOnSubmitListener.onFailure(i,s);
            }
        });
    }

    /**
     * 使用前需要检查当前的Idea的被处理数量是否超过1/3
     *
     * 暂时弃用这个功能
     *
     * 用户更新已有的idea
     * @param mAttitudeBean
     * @param contentTxt
     * @param contentPic
     * @param contentRec
     * @param mListener
     */
    @Deprecated
    public static void updateContent(Context mContext,AttitudeBean mAttitudeBean, String contentTxt, BmobFile contentPic
            , BmobFile contentRec, final OnUpdateContentListener mListener){
        mAttitudeBean.setContentTxt(contentTxt);
        mAttitudeBean.setContentPic(contentPic);
        mAttitudeBean.setContentRec(contentRec);
        mAttitudeBean.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (mListener!=null)
                    mListener.onSuccess();
            }
            @Override
            public void onFailure(int i, String s) {
                if (mListener!=null)
                    mListener.onFailure(i,s);
            }
        });
    }

    /**
     * 这个功能只有在用户的自己feedback页面才能使用，所以不需要检查用户的权限
     * delete实际上只是使idea本身的作者信息修改为null，同时设置为匿名，并没有删除idea
     * @param mAttitudeBean
     * @param mOnDeleteListener
     */
    public static void deleteAuthor(final Context mContext, final UserBean mUser, final AttitudeBean mAttitudeBean, final OnDeleteListener mOnDeleteListener){
//        mAttitudeBean.setAuthor(null);
        mAttitudeBean.remove("author");
        mAttitudeBean.remove2Feedback(mUser);
        mAttitudeBean.setAnonymity(true);
        mAttitudeBean.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                mUser.remove2Cares(mAttitudeBean);
                mUser.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        if (mOnDeleteListener!=null)
                            mOnDeleteListener.onSuccess();
                    }
                    @Override
                    public void onFailure(int i, String s) {

                        if (mOnDeleteListener!=null)
                            mOnDeleteListener.onFailure(i,s);
                    }
                });
            }
            @Override
            public void onFailure(int i, String s) {
                mOnDeleteListener.onFailure(i,s);
            }
        });
    }

    /**
     * 查询作者是 mUser 的所有的 Attitudes
     * @param mUser
     * @param mListener
     */
    public static void queryAttitudesByAuthor(Context mContext,UserBean mUser, final OnQueryAttitudeListener mListener){
        BmobQuery<AttitudeBean> mQuery=new BmobQuery<>();
        mQuery.addWhereEqualTo("author",mUser);
        mQuery.findObjects(mContext, new FindListener<AttitudeBean>() {
            @Override
            public void onSuccess(List<AttitudeBean> list) {
                if (mListener!=null)
                    mListener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                if (mListener!=null)
                    mListener.onError(i,s);
            }
        });
    }

    public static void queryAttitudeById(Context mContext, String id, final GetListener mGetListener){
        BmobQuery<AttitudeBean> mQuery=new BmobQuery<>();
        mQuery.include("author");
        mQuery.getObject(mContext, id, new GetListener<AttitudeBean>() {
            @Override
            public void onSuccess(AttitudeBean attitudeBean) {
                mGetListener.onSuccess(attitudeBean);
            }

            @Override
            public void onFailure(int i, String s) {
                mGetListener.onFailure(i,s);
            }
        });
    }

    /**
     * 获取到Attitudes（在首页展示的）以处理，排除作者是我的，排除我曾经处理过的
     * @param mUser
     * @param limit 一次加载多少项
     * @param mListener
     */
    public static void getAttitudes4Main(Context mContext,UserBean mUser, int limit, final OnQueryAttitudeListener mListener){
        List<String> mUserIds=new ArrayList<>();
        mUserIds.add(mUser.getObjectId());
        BmobQuery<AttitudeBean> mQuery= new BmobQuery<AttitudeBean>();
        mQuery.addWhereNotEqualTo("author",mUser);//作者不是我
        mQuery.addWhereEqualTo("hasFeedback",false);//尚未得到反馈
        mQuery.addWhereNotContainedIn("handledUserIDs",mUserIds);
        mQuery.setLimit(limit);
        mQuery.include("author");//将作者信息也同时查询出来
        mQuery.findObjects(mContext, new FindListener<AttitudeBean>() {
            @Override
            public void onSuccess(List<AttitudeBean> list) {
                if (mListener!=null)
                    mListener.onSuccess(list);
            }
            @Override
            public void onError(int i, String s) {
                if (mListener!=null)
                    mListener.onError(i,s);
            }
        });
    }

    /**
     * 处理Attitude，表达自己的观点，
     * @param mAttitude
     * @param mUser     处理者
     * @param handleType    --HANDLE_A/HANDLE_B
     * @param mListener
     */
    public static void handleAttitude(final Context mContext, final AttitudeBean mAttitude, final UserBean mUser, final int handleType, final OnHandleListener mListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAttitude.addHandledUser(mUser);//将处理者添加到集合中
                if (handleType== CardSlidePanel.VANISH_TYPE_LEFT)
                    mAttitude.increment("aCount");//使用原子计数器记录处理者的建议
                else if(handleType==CardSlidePanel.VANISH_TYPE_RIGHT)
                    mAttitude.increment("bCount");
                else if(handleType==CardSlidePanel.VANISH_TYPE_TOP_NOT_CARE)
                    //若是不关心，则什么都不处理，直接添加到已经处理过的，保证不再显示该问题

                if ((mAttitude.getaCount()+mAttitude.getbCount())>=(int)Integer.valueOf(mAttitude.getTimes2Feedback())){
                    //应该通知服务器做出反馈-->可以通过给每个who2Feedback中的用户发一条推送，您收到新的反馈
                    //做出反馈后应该设置mIdea.setHasFeedback(true);
                    mAttitude.setHasFeedback(true);
                }
                mAttitude.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        if (mListener!=null)
                            mListener.onSuccess();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        if (mListener!=null)
                            mListener.onFailure(i,s);
                    }


                    @Override
                    public void onFinish() {
                        if (mListener!=null)
                            mListener.onFinished();
                    }
                });
            }
        }).start();
    }

    /**
     * 添加要求反馈的user到清单中
     * @param mAttitude
     * @param mUser
     * @param mListener
     */
    public static void addFeedBack(final Context mContext, final AttitudeBean mAttitude, final UserBean mUser
            , final OnUpdateFeedbackListListener mListener){
        mAttitude.add2Feedback(mUser);
        mAttitude.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                mUser.add2Cares(mAttitude);
                mUser.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        if (mListener!=null)
                            mListener.onSuccess();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        if (mListener!=null)
                            mListener.onFailure(i,s);
                    }
                });
            }
            @Override
            public void onFailure(int i, String s) {
                if (mListener!=null)
                    mListener.onFailure(i,s);
            }
        });
    }

    /**
     * 获取在FeedBack页面显示的Attitudes
     * @param mUser
     * @param mListener
     */
    public static void getAttitude4Feedback(Context mContext,final UserBean mUser, final OnQueryAttitudeListener mListener){
        BmobQuery<AttitudeBean> mQuery= new BmobQuery<AttitudeBean>();
        mQuery.addWhereRelatedTo("cares",new BmobPointer(mUser));
        mQuery.include("author");
        mQuery.findObjects(mContext, new FindListener<AttitudeBean>() {
            @Override
            public void onSuccess(List<AttitudeBean> list) {
                if (mListener!=null)
                    mListener.onSuccess(list);
            }
            @Override
            public void onError(int i, String s) {
                if (mListener!=null)
                    mListener.onError(i,s);
            }
        });
    }
    /**
     * 从反馈清单中移除某个用户
     * @param mAttitude
     * @param mUser
     * @param mListener
     */
    public static void removeFeedBack(final Context mContext, final AttitudeBean mAttitude, final UserBean mUser
            , final OnUpdateFeedbackListListener mListener){
        mAttitude.remove2Feedback(mUser);
//        if (mAttitude.getAuthor().getObjectId()==mUser.getObjectId()){
//            //若是我的动态，则同时将这条动态的作者设置为空
//            mAttitude.remove("author");
//            mAttitude.setAnonymity(true);
//        }
        mAttitude.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                mUser.remove2Cares(mAttitude);
                mUser.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        if (mListener!=null)
                            mListener.onSuccess();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        if (mListener!=null)
                            mListener.onFailure(i,s);
                    }
                });
            }
            @Override
            public void onFailure(int i, String s) {
                if (mListener!=null)
                    mListener.onFailure(i,s);
            }
        });
    }


}
