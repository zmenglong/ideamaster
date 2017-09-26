package gg.attitude.fragments.main;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.api.ErrorImageType;
import gg.attitude.api.GuideInfo;
import gg.attitude.base.BaseActivity;
import gg.attitude.base.BaseFragment;
import gg.attitude.bean.AttitudeBean;
import gg.attitude.bean.CardItemBean;
import gg.attitude.listener.attitude.OnHandleListener;
import gg.attitude.listener.attitude.OnQueryAttitudeListener;
import gg.attitude.listener.attitude.OnUpdateFeedbackListListener;
import gg.attitude.utils.AttitudeUtils;
import gg.attitude.utils.ErrorCodeUtils;
import gg.attitude.utils.GuideUtils;
import gg.attitude.views.card.CardSlidePanel;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class AttitudeFragment extends BaseFragment {
    private static final int GET_DATA_SUCCESS=25;
    private static final int GET_DATA_FAILURE=-25;
    private static final int DATA_HANDLE_FINISH=100;

    public static final int NO_DATA=-2;


    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    dismissErrorView(contentRL);
                    //设置progressBar的步长，同时初始化progressBar进度为0
                    step=10000/mAttitudes.size();
                    mProgressBar.setProgress(0);
                    attitudeBeans2CardItemBeans();
                    dismissLoadingDialog();

                    //显示在card上的用户引导，start起点
                    GuideUtils.showTinyGuide2View(getActivity(),slidePanel.getCard()
                            ,GuideInfo.INFO_CARD,materialIntroListener);
                    break;
                case GET_DATA_FAILURE:
                    showErrorView(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1),ErrorImageType.SAD,contentRL);
                    dismissLoadingDialog();
                    break;
                case DATA_HANDLE_FINISH:
                    showErrorView("本次您共为"+mAttitudes.size()+"个小伙伴出谋划策。点击加载更多", ErrorImageType.HAPPY,contentRL);
                    dismissLoadingDialog();
                    break;
            }
            return false;
        }
    });

    private List<AttitudeBean> mAttitudes;//获取到的原始Attitudes
    private View contentRL;//承载内容的RL

    private CardSlidePanel slidePanel;
    private List<CardItemBean> beanList = new ArrayList<CardItemBean>();//用来加载显示的Beans

    private ProgressBar mProgressBar;//显示当前进度的progressBar
    private int step;//progressBar每次增长的步长


    @Override
    protected void initViews(View mView) {
        contentRL= mView.findViewById(R.id.contentRL);
        mProgressBar= (ProgressBar) mView.findViewById(R.id.mProgressBar);
        initCardLayout();
    }

    private void initCardLayout() {
        slidePanel= (CardSlidePanel)mView
                .findViewById(R.id.image_slide_panel);
        slidePanel.setCardSwitchListener(cardSwitchListener);
        prepareDataList();

    }

    MaterialIntroListener materialIntroListener=new MaterialIntroListener() {
        @Override
        public void onUserClicked(String materialIntroViewId) {
            if (materialIntroViewId.equals(GuideInfo.INFO_CARD)){
                //响应了Card的点击后显示卡片的提示
                GuideUtils.showGuide2View(getActivity(),slidePanel.getBottomLayout()
                        , GuideInfo.INFO_CARD_BOTTOM_LAYOUT,materialIntroListener);
            }else if (materialIntroViewId.equals(GuideInfo.INFO_CARD_BOTTOM_LAYOUT)){
                //响应了底部的的点击后显示卡片的提示
                GuideUtils.showGuide2View(getActivity(),slidePanel.getCare()
                        ,GuideInfo.INFO_CARD_CARE,materialIntroListener);
            }else if (materialIntroViewId.equals(GuideInfo.INFO_CARD_CARE)){
                GuideUtils.showGuide2View(getActivity(),slidePanel.getCard_X()
                        ,GuideInfo.INFO_CARD_X,materialIntroListener);
            }else if(materialIntroViewId.equals(GuideInfo.INFO_CARD_X)){
                GuideUtils.showGuide2View(getActivity(),((BaseActivity)getActivity()).getRightIV()
                        , GuideInfo.INFO_RIGHT_IV,materialIntroListener);
            }else{
                Snackbar.make(contentRL,"欢迎来到态度！您的意见，我们十分关注",Snackbar.LENGTH_LONG).show();
            }

        }
    };

    CardSlidePanel.CardSwitchListener cardSwitchListener= new CardSlidePanel.CardSwitchListener() {

        @Override
        public void onShow(int index) {
            mProgressBar.incrementProgressBy(step);
        }

        @Override
        public void onCardVanish(int index, final int type) {
            //btn给锁上
//            slidePanel.getLeftBtn().setClickable(false);
//            slidePanel.getRightBtn().setClickable(false);
            AttitudeUtils.handleAttitude(mActivity, mAttitudes.get(index), mUser
                    , type, new OnHandleListener() {
                        @Override
                        public void onSuccess() {
//                            slidePanel.getLeftBtn().setClickable(true);
//                            slidePanel.getRightBtn().setClickable(true);
                        }
                        @Override
                        public void onFailure(int i, String s) {
//                            slidePanel.getLeftBtn().setClickable(true);
//                            slidePanel.getRightBtn().setClickable(true);
                        }
                        @Override
                        public void onFinished() {
                        }
                    });
            if (index==mAttitudes.size()-1){
                mHandler.sendEmptyMessage(DATA_HANDLE_FINISH);
            }
        }

        @Override
        public void onAvatarClick(View cardView, int index) {
        }

        @Override
        public void onCareClick(int index) {
            AttitudeUtils.addFeedBack(mActivity,mAttitudes.get(index), mUser, new OnUpdateFeedbackListListener() {
                @Override
                public void onSuccess() {
                    showToast("关注成功，当问题得到反馈时也会抄送给您一份");
                }

                @Override
                public void onFailure(int i, String s) {
                    showToast("关注失败:"+s);
                }
            });
        }
    };


    private void prepareDataList() {
        showLoadingDialog(mActivity);
        AttitudeUtils.getAttitudes4Main(mActivity,mUser
                , ConstantValues.LOAD_ATTITUDE_COUNT, new OnQueryAttitudeListener() {
                    @Override
                    public void onSuccess(List<AttitudeBean> list) {
                        if (list.size()<4){
                            //至少有4个数据时才能显示
                            Message msg=new Message();
                            msg.arg1=NO_DATA;//查看ErrorCodeUtils.getErrorMsgFromCode(int)中的错误码及信息
                            msg.what=GET_DATA_FAILURE;
                            mHandler.sendMessageDelayed(msg,ConstantValues.DELAY_TIME);
                            return;
                        }
                        mAttitudes=list;
                        Log.i("==>",list.size()+"<==抓取了");
                        mHandler.sendEmptyMessageDelayed(GET_DATA_SUCCESS
                                ,ConstantValues.DELAY_TIME);
                    }
                    @Override
                    public void onError(int i, String s) {
                        Message msg=new Message();
                        msg.arg1=i;
                        msg.what=GET_DATA_FAILURE;
                        mHandler.sendMessageDelayed(msg,ConstantValues.DELAY_TIME);
                    }
                });
    }
    private void attitudeBeans2CardItemBeans() {
        if (mAttitudes!=null&&!mAttitudes.isEmpty()){
            beanList.clear();
            for (AttitudeBean mAttitudeBean:mAttitudes) {
                CardItemBean mBean=new CardItemBean();
                if (mAttitudeBean.getAuthor()!=null){
                    mBean.avatarUrl=mAttitudeBean.getAuthor().getAvatarUrl();//头像url
                    mBean.nickname=mAttitudeBean.getAuthor().getNickName();
                }else{
                    mBean.avatarUrl="default";
                    mBean.nickname="匿名用户";
                }
                mBean.aDes=mAttitudeBean.getaDescription();
                mBean.bDes=mAttitudeBean.getbDescription();
                mBean.contentTxt=mAttitudeBean.getContentTxt();
                mBean.objectId=mAttitudeBean.getObjectId();
                Log.i("==>","contentTxt="+mBean.contentTxt);
                beanList.add(mBean);
            }
            //全部填充好后，将Data填充到Panel中
            slidePanel.fillData(beanList);
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_attitude;
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {
        prepareDataList();
    }
}