package gg.attitude.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.listener.GetListener;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import gg.attitude.R;
import gg.attitude.api.ConstantValues;
import gg.attitude.api.ErrorImageType;
import gg.attitude.api.GuideInfo;
import gg.attitude.base.BaseActivity;
import gg.attitude.bean.AttitudeBean;
import gg.attitude.bean.UserBean;
import gg.attitude.utils.AttitudeUtils;
import gg.attitude.utils.GuideUtils;
import gg.attitude.utils.PicassoUtils;
import gg.attitude.views.CircleImageView;
import gg.attitude.views.MyScrollView;
import gg.attitude.views.chart.PieGraph;
import gg.attitude.views.chart.PieSlice;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class FeedbackDetailActivity extends BaseActivity {

    private PieGraph mGraph;

    private MyScrollView contentSL;

    private String mAttitudeID;
    private AttitudeBean mAttitude;
    private CircleImageView avatar;
    private CircleImageView iv_hasFeedback;
    private TextView nickname,contentTxt,aDes,bDes;

    private LinearLayout feedback_detail_ll;
    private TextView aDes_graph,bDes_graph;

    private LinearLayout card_ll;
    private LinearLayout graph_txt_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);
        initToolBar("反馈详情",true,false,false);
        mAttitudeID=getIntent().getStringExtra("mAttitude.getObjectId");
        queryAttitude(mAttitudeID);
    }

    private void queryAttitude(String id){
        showLoadingDialog();
        AttitudeUtils.queryAttitudeById(FeedbackDetailActivity.this, id, new GetListener<AttitudeBean>() {
            @Override
            public void onSuccess(AttitudeBean attitudeBean) {
                mAttitude=attitudeBean;
                mHandler.sendEmptyMessageDelayed(QUERY_SUCCESS, ConstantValues.DELAY_TIME);
            }

            @Override
            public void onFailure(int i, String s) {
                Message msg=new Message();
                msg.what=QUERY_FAILED;
                msg.arg1=i;
                mHandler.sendMessageDelayed(msg,ConstantValues.DELAY_TIME);
            }
        });
    }
    @Override
    protected void initViews() {
        contentSL = (MyScrollView) findViewById(R.id.contentSL);
        card_ll= (LinearLayout) findViewById(R.id.card_ll);
        avatar= (CircleImageView) findViewById(R.id.avatar);
        iv_hasFeedback= (CircleImageView) findViewById(R.id.iv_hasFeedback);
        nickname= (TextView) findViewById(R.id.nickname);
        contentTxt= (TextView) findViewById(R.id.contentTxt);
        aDes= (TextView) findViewById(R.id.aDes);
        bDes= (TextView) findViewById(R.id.bDes);
        feedback_detail_ll= (LinearLayout) findViewById(R.id.feedback_detail_ll);
        graph_txt_ll= (LinearLayout) findViewById(R.id.graph_txt_ll);
        aDes_graph= (TextView) findViewById(R.id.aDes_graph);
        bDes_graph= (TextView) findViewById(R.id.bDes_graph);
    }

    /**
     * 初始化Graph
     * @param aCount
     * @param bCount
     */
    private void initGraph(final int aCount, final int bCount) {
        //对于尚未有人表态的情况，初始化为a:b=1:1
        float aPer=0.5f;
        float bPer=0.5f;
        if(aCount+bCount!=0){
            aPer=(float) aCount/(aCount+bCount);
            bPer=(float) bCount/(aCount+bCount);//计算出a,b所占的支持率百分比
        }
        mGraph= (PieGraph) findViewById(R.id.graph);
        PieSlice a = new PieSlice();
        a.setColor(getResources().getColor(R.color.color_chart_a));
        a.setValue(0.1f);
        a.setGoalValue(aPer);
        PieSlice b = new PieSlice();
        b.setColor(getResources().getColor(R.color.color_chart_b));
        b.setValue(0.1f);
        b.setGoalValue(bPer);
        //先添加b，再添加a为了在显示时能让a选项在图表的左边显示出来
        mGraph.addSlice(b);
        mGraph.addSlice(a);
        mGraph.setDuration(1600);//default if unspecified is 300 ms
//        mGraph.setInterpolator(new AnticipateOvershootInterpolator());//default if unspecified is linear; constant speed
        mGraph.animateToGoalValues();
        mGraph.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
            @Override
            public void onClick(int index) {
                switch (index){
                    case 0:
                        Snackbar.make(contentSL,"共有"+bCount+"人支持:"+mAttitude.getbDescription(), Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(contentSL,"共有"+aCount+"人支持:"+mAttitude.getaDescription(), Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        aDes_graph.setText("A:共有"+aCount+"人支持");
        bDes_graph.setText("B:共有"+bCount+"人支持");
    }
    /**
     * 初始化底部的Card布局
     */
    private void initCard() {
        UserBean author=mAttitude.getAuthor();
        PicassoUtils.setAvatar2ImageView(FeedbackDetailActivity.this
                ,author.getAvatarUrl(),avatar);
        nickname.setText(author.getNickName());
        contentTxt.setText(mAttitude.getContentTxt());
        aDes.setText(mAttitude.getaDescription());
        bDes.setText(mAttitude.getbDescription());
        if (!mAttitude.getHasFeedback()){
            //若该问题尚未反馈，则设置隐藏
            iv_hasFeedback.setVisibility(View.GONE);
        }

        GuideUtils.showGuide2View(FeedbackDetailActivity.this,card_ll, GuideInfo.FEEDBACKDETAIL_CARD_LL,materialIntroListener);
    }
    MaterialIntroListener materialIntroListener=new MaterialIntroListener() {
        @Override
        public void onUserClicked(String materialIntroViewId) {
            if (GuideInfo.FEEDBACKDETAIL_CARD_LL.equals(materialIntroViewId)){
                GuideUtils.showGuide2View(FeedbackDetailActivity.this,mGraph,GuideInfo.FEEDBACKDETAIL_GRAPH,materialIntroListener);
            }else if(GuideInfo.FEEDBACKDETAIL_GRAPH.equals(materialIntroViewId)){
                GuideUtils.showGuide2View(FeedbackDetailActivity.this,graph_txt_ll,GuideInfo.FEEDBACKDETAIL_GRAPH_txt,materialIntroListener);
            }
        }
    };
    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {
        finish();
    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }
    @Override
    protected void reLoadDataEventAfterLoadError() {
        queryAttitude(mAttitudeID);
    }


    private static final int QUERY_SUCCESS=1;
    private static final int QUERY_FAILED=0;

    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case QUERY_SUCCESS:
                    dismissErrorView(contentSL);
                    dismissLoadingDialog();
                    if (mAttitude!=null){
                        //正常获取到数据时，初始化图表，初始化Attitude信息
                        initGraph(mAttitude.getaCount(),mAttitude.getbCount());
                        initCard();
                    }else{
                        //当mAttitude为空时应该显示网络异常，请点击返回重试；
                        showErrorView("网络异常，请退出重试",ErrorImageType.ANGRY, contentSL);
                    }
                    break;
                case QUERY_FAILED:
                    showErrorView(msg.arg1,ErrorImageType.SAD, contentSL);
                    dismissLoadingDialog();
                    break;
            }
            return false;
        }
    });
}
