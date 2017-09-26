package gg.attitude.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import gg.attitude.R;
import gg.attitude.activity.FeedbackDetailActivity;
import gg.attitude.base.BaseActivity;
import gg.attitude.bean.FeedbackBean;
import gg.attitude.listener.feedback.OnFeedbackDeleteListener;
import gg.attitude.utils.PicassoUtils;
import gg.attitude.views.CircleImageView;

/**
 * Created by ImGG on 2016/6/14.
 * Email:gu.yuepeng@foxmail.com
 */
public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {
    private List<FeedbackBean> mFeedbackBeans;
    private BaseActivity mActivity;

    private OnFeedbackDeleteListener mListener;

    public FeedBackAdapter(BaseActivity mActivity, List<FeedbackBean> mFeedbackBeans) {
        this.mActivity=mActivity;
        this.mFeedbackBeans = mFeedbackBeans;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycleview_feedback, null);
        ViewHolder vh = new ViewHolder(view);
        vh.swipe= (SwipeLayout) view.findViewById(R.id.swipe);
        vh.avatar= (CircleImageView) view.findViewById(R.id.avatar);
        vh.authorNickname= (TextView) view.findViewById(R.id.authorNickname);
        vh.iv_hasFeedback= (CircleImageView) view.findViewById(R.id.iv_hasFeedback);
        vh.contentTxt= (TextView) view.findViewById(R.id.contentTxt);
        vh.delete_ll= (LinearLayout) view.findViewById(R.id.delete_ll);
        vh.contentLL= (LinearLayout) view.findViewById(R.id.contentLL);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //强制关闭复用
        holder.setIsRecyclable(false);
        final FeedbackBean mFeedbackBean=mFeedbackBeans.get(position);
        PicassoUtils.setAvatar2ImageView(mActivity
                ,mFeedbackBean.getAuthor().getAvatarUrl(),holder.avatar);
        holder.authorNickname.setText(mFeedbackBean.getAuthor().getNickName());
        holder.contentTxt.setText(mFeedbackBean.getContentTxt());

        //若有反馈，则显示
        if (!mFeedbackBean.isHasFeedback()){
            holder.iv_hasFeedback.setVisibility(View.GONE);
        }

        holder.delete_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onDelete(position);
            }
        });
        holder.contentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent detail=new Intent(mActivity, FeedbackDetailActivity.class);
                    detail.putExtra("mAttitude.getObjectId",mFeedbackBean.getAttitudeID());
                    mActivity.startIntent(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeedbackBeans.isEmpty() ? 0 : mFeedbackBeans.size();
    }

    public void setOnFeedBackBeanDeleteListener(OnFeedbackDeleteListener mListener){
        this.mListener=mListener;
    }

    /**
     * 传入一个noDataView，当检测数据为空时，显示noDataView
     * @param noDataView
     */
    public void notifyOnDataChange(View noDataView){
        if (mFeedbackBeans.size()==0){
            //若数据源为空，则显示空数据layout
            noDataView.setVisibility(View.VISIBLE);
            return;
        }else {
            noDataView.setVisibility(View.GONE);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipe;
        private CircleImageView avatar;
        private TextView authorNickname;
        private LinearLayout delete_ll,contentLL;
        private TextView contentTxt;
        private CircleImageView iv_hasFeedback;
        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
