
package gg.attitude.views.card;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gg.attitude.R;
import gg.attitude.bean.CardItemBean;
import gg.attitude.utils.PicassoUtils;
import gg.attitude.views.CircleImageView;

/**
 * 卡片View项
 * @author ImGG
 */
public class CardItemView extends LinearLayout {

    public CircleImageView avatar;
    private TextView nickname;
    private TextView contentTxt;
    private TextView aDes,bDes;
    private Context mContext;
    public ImageView x;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.item_cardview, this);
        mContext=context;
        avatar = (CircleImageView) findViewById(R.id.avatar);
        nickname = (TextView) findViewById(R.id.nickname);
        contentTxt = (TextView) findViewById(R.id.contentTxt);
        aDes = (TextView) findViewById(R.id.aDes);
        bDes = (TextView) findViewById(R.id.bDes);
        x= (ImageView) findViewById(R.id.x);
    }

    public void fillData(CardItemBean itemData) {
        PicassoUtils.setAvatar2ImageView(mContext,itemData.avatarUrl,avatar,true);
        nickname.setText(itemData.nickname);
        contentTxt.setText(itemData.contentTxt + "");
        aDes.setText(itemData.aDes);
        bDes.setText(itemData.bDes);
    }
}
