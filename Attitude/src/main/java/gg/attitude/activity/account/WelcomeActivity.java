package gg.attitude.activity.account;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;
import java.util.List;

import gg.attitude.R;
import gg.attitude.base.BaseActivity;
import gg.attitude.holder.LocalImageHolderView;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ImGG on 2016/6/13.
 * Email:gu.yuepeng@foxmail.com
 */
public class WelcomeActivity extends BaseActivity {
    private ConvenientBanner mConvenientBanner;
    private ImageView first,second,third;//作为指示器用的

    private FancyButton register,login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initButton();
        initImageView();
        initConvenientBanner();
    }

    private void initButton() {
        register= (FancyButton) findViewById(R.id.register);
        login= (FancyButton) findViewById(R.id.login);
        register.setOnClickListener(mClickListener);
        login.setOnClickListener(mClickListener);
    }


    private void initConvenientBanner() {
        List<Integer> localImages=new ArrayList<>();
        mConvenientBanner = (ConvenientBanner)findViewById(R.id.convenientBanner);
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        localImages.clear();
        localImages.add(R.drawable.first);
        localImages.add(R.drawable.second);
        localImages.add(R.drawable.third);
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }
                    @Override
                    public void onPageSelected(int position) {
                        setSelectedImage(position);
                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

    }

View.OnClickListener mClickListener=new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                showActivity(WelcomeActivity.this,RegisterActivity.class);
                finish();
                break;
            case R.id.login:
                showActivity(WelcomeActivity.this,LoginActivity.class);
                finish();
                break;
        }
    }
};


    @Override
    protected void initViews() {
        //这个Activity中没有Toolbar，所以这个方法没有被调用
    }

    @Override
    protected void reLoadDataEventAfterLoadError() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(2400);
    }

    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }
    @Override
    protected void doWhenLeftBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenRightBtnIsOnClick_Toolbar() {

    }

    @Override
    protected void doWhenFeedbackBtnIsOnClick_Toolbar() {

    }

    //存储指示器的ImageView集合--用最笨的方法做指示器:)
    private List<ImageView> mImages=new ArrayList<>();
    private void initImageView() {
        mImages.clear();
        first= (ImageView) findViewById(R.id.first);
        second= (ImageView) findViewById(R.id.second);
        third= (ImageView) findViewById(R.id.third);
        mImages.add(first);
        mImages.add(second);
        mImages.add(third);
        resetImages(mImages);
        setSelectedImage(0);
    }
    private void resetImages(List<ImageView> mImages) {
        for (int i=0;i<mImages.size();i++){
            mImages.get(i).setImageResource(R.drawable.ic_page_indicator);
        }
    }
    private void setSelectedImage(int position) {
        if (mImages.size()==0) initImageView();
        resetImages(mImages);
        mImages.get(position).setImageResource(R.drawable.ic_page_indicator_focused);

    }
}
