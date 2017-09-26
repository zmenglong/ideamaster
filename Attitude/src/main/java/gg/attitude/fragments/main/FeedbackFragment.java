package gg.attitude.fragments.main;


import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.twotoasters.jazzylistview.effects.TiltEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import gg.attitude.R;
import gg.attitude.adapter.FeedBackAdapter;
import gg.attitude.api.ConstantValues;
import gg.attitude.api.ErrorImageType;
import gg.attitude.api.GuideInfo;
import gg.attitude.base.BaseFragment;
import gg.attitude.bean.AttitudeBean;
import gg.attitude.bean.FeedbackBean;
import gg.attitude.listener.attitude.OnQueryAttitudeListener;
import gg.attitude.listener.attitude.OnUpdateFeedbackListListener;
import gg.attitude.listener.feedback.OnFeedbackDeleteListener;
import gg.attitude.utils.AttitudeUtils;
import gg.attitude.utils.DialogUtils;
import gg.attitude.utils.ErrorCodeUtils;
import gg.attitude.utils.GuideUtils;

/**
 * Created by ImGG on 2016/6/14.
 * Email:gu.yuepeng@foxmail.com
 */
public class FeedbackFragment extends BaseFragment {
    private static final int GET_DATA_SUCCESS=99;//获取数据成功
    private static final int GET_DATA_FAILURE=-99;
    private static final int DELETE_SUCCESS=98;
    private static final int DELETE_FAILURE=-98;

    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    dismissErrorView(feedbackLL);
                    initRecycleView();
                    dismissLoadingDialog();
                    break;
                case GET_DATA_FAILURE:
                    showErrorView(ErrorCodeUtils.getErrorMsgFromCode(msg.arg1), ErrorImageType.SAD
                            ,feedbackLL);
                    dismissLoadingDialog();
                    break;
                case DELETE_SUCCESS:
                    // 当删除掉一个item时，无论删除的item属于是否反馈的列表，均重新从网络上获取一次列表
                    // 使用isCheck记录下当前查看的是哪个列表，在获取到数据后会自动切换到该列表下
                    // 具体方法见initRecycleView(boolean)
                    // 另外需要说明的是：GifLoadingDialog在这个地方无需dismiss
                    // 因为紧接着后续仍有网络请求，等待网络请求结束后dismiss
                    // 这里使用isVisitable来获取到Dialog是否可见
                    getData();
                    break;
                case DELETE_FAILURE:
                    dismissLoadingDialog();
                    showSnackBar(feedbackLL,"取消关注失败："+msg.obj);
                    break;
            }
            return false;
        }
    });

    private View feedbackLL;
    private View noDataLL;
    private RecyclerView feedBackRV;
    private FeedBackAdapter feedBackAdapter;
    private List<FeedbackBean> mFeedbackBeans;
    public static final int NO_DATA_2_FEEDBACK=-100;

    private List<AttitudeBean> mAttitudeBeans=new ArrayList<>();//存储从网络上获取到的AttitudeBean

    private JazzyRecyclerViewScrollListener jazzyScrollListener;

    @Override
    protected void initViews(View mView) {
        feedbackLL=mView.findViewById(R.id.feedbackLL);
        noDataLL=mView.findViewById(R.id.no_data_ll);
        getData();
    }

    /**
     * 获取原始的AttitudeBean
     */
    private void getData() {
        showLoadingDialog(mActivity);
        AttitudeUtils.getAttitude4Feedback(mActivity,mUser
                , new OnQueryAttitudeListener() {
                    @Override
                    public void onSuccess(List<AttitudeBean> list) {
                        mAttitudeBeans=list;
                        mHandler.sendEmptyMessageDelayed(GET_DATA_SUCCESS, ConstantValues.DELAY_TIME);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Message msg=new Message();
                        msg.what=GET_DATA_FAILURE;
                        msg.arg1=i;
                        mHandler.sendMessageDelayed(msg,ConstantValues.DELAY_TIME);
                    }
                });
    }


    private void initRecycleView() {
        if (feedBackRV==null){
            feedBackRV=(RecyclerView) mView.findViewById(R.id.feedBackRV);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 2);
            gridLayoutManager.setAutoMeasureEnabled(true);
            feedBackRV.setLayoutManager(gridLayoutManager);
            //设置滑动监听，同时添加滑动动画
            //设置当滑动时不允许更改recycleview的数据，会引起jazzyScrollListenter的子View=null
            jazzyScrollListener=new JazzyRecyclerViewScrollListener(){
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            };
            jazzyScrollListener.setTransitionEffect(new TiltEffect());
            feedBackRV.addOnScrollListener(jazzyScrollListener);
            feedBackRV.addItemDecoration(new RecyclerView.ItemDecoration() {
                int space=8;
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                }

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.left = space;
                    outRect.right = space;
                    outRect.top = space;
                    outRect.bottom = space;
                }
            });
            feedBackRV.setHasFixedSize(true);
        }
        fillData2List();
        fillData2Adapter();


    }
    private void fillData2List() {
        attitudeBean2FeedBackBean(mAttitudeBeans);
    }
    private void fillData2Adapter() {
//        if (hasFeedBackOrNot){
        feedBackAdapter =new FeedBackAdapter(mActivity,mFeedbackBeans);
        //响应item的左滑删除事件
        feedBackAdapter.setOnFeedBackBeanDeleteListener(mItemDeleteListener);
        feedBackRV.setAdapter(feedBackAdapter);
        feedBackAdapter.notifyOnDataChange(noDataLL);
        if (feedBackRV.getAdapter().getItemCount()>0) {
            //获取不到第一个item的view，所以显示不出来引导界面，改为定位到整个recyclerView
            //提示起点
            GuideUtils.showTinyGuide2View(mActivity
                    ,feedBackRV, GuideInfo.FEEDBACK_CARD_ITEM,materialIntroListener);
        }
    }


    /**
     * 将AttitudeBean转成FeedBackBean
     * @param mAttitudes
     */
    private void attitudeBean2FeedBackBean(List<AttitudeBean> mAttitudes) {
        mFeedbackBeans=new ArrayList<>();
        for (int i=0;i<mAttitudes.size();i++) {
            FeedbackBean mFeedBackBean=new FeedbackBean();
            mFeedBackBean.setId(i);
            mFeedBackBean.setAuthor(mAttitudes.get(i).getAuthor());
            mFeedBackBean.setContentTxt(mAttitudes.get(i).getContentTxt());
            mFeedBackBean.setaDes(mAttitudes.get(i).getaDescription());
            mFeedBackBean.setbDes(mAttitudes.get(i).getbDescription());
            mFeedBackBean.setHasFeedback(mAttitudes.get(i).getHasFeedback());
            mFeedBackBean.setAttitudeID(mAttitudes.get(i).getObjectId());
            mFeedbackBeans.add(mFeedBackBean);
        }
    }


    /**
     * 有反馈的item左滑删除的回调
     * 先从mFeedBackBeans中取出Bean，根据Bean的id属性拿到在AtitudeBeans中的位置，
     * 取出来AtitudeBean请求将AttitudeBean从数据库中删除
     */
    OnFeedbackDeleteListener mItemDeleteListener =new OnFeedbackDeleteListener() {
        @Override
        public void onDelete(final int position) {
            DialogUtils.showConfirmOrCancelDialog(mActivity, "确认取消对该问题的关注", new DialogUtils.OnConfirmCallBack() {
                @Override
                public void onConfirmed(DialogInterface dialog) {
                    showLoadingDialog(mActivity);
                    FeedbackBean mBean=mFeedbackBeans.get(position);
                    AttitudeBean mAttitude=mAttitudeBeans.get(mBean.getId());
                    AttitudeUtils.removeFeedBack(mActivity, mAttitude, mUser, new OnUpdateFeedbackListListener() {
                        @Override
                        public void onSuccess() {
                            mHandler.sendEmptyMessageDelayed(DELETE_SUCCESS,ConstantValues.DELAY_TIME);
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            Message msg=new Message();
                            msg.obj=s;
                            msg.what= DELETE_FAILURE;
                            mHandler.sendMessageDelayed(msg,ConstantValues.DELAY_TIME);
                        }
                    });
                }
            });
        }
    };

    MaterialIntroListener materialIntroListener=new MaterialIntroListener() {
        @Override
        public void onUserClicked(String materialIntroViewId) {
            //do nothing
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {
        getData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFeedbackBeans=null;
    }


}
