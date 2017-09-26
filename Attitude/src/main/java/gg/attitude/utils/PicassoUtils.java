package gg.attitude.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gg.attitude.R;

/**
 * Created by ImGG on 2016/6/29.
 * Email:gu.yuepeng@foxmail.com
 */
public final class PicassoUtils {
    private PicassoUtils(){}

    private static final int size=125;
    public static void setAvatar2ImageView(Context context,String url, ImageView imageView){
        if ("default".equals(url)){
            Picasso.with(context)
                    .load(R.drawable.default_avatar)
                    .into(imageView);
            return;
        }
        Picasso.with(context)
                .load(url)
                .centerInside()
                .resize(size,size)
                .placeholder(R.drawable.loading_avatar)//默认情况会没有设置头像会传入-default-此时error显示出默认头像
                .error(R.drawable.default_avatar)
                .into(imageView);
    }
    public static void setAvatar2ImageView(Context context,String url, ImageView imageView ,boolean forCard){
        int size=100;
        if ("default".equals(url)){
            Picasso.with(context)
                    .load(R.drawable.default_avatar)
                    .into(imageView);
            return;
        }
        Picasso.with(context)
                .load(url)
                .centerInside()
                .resize(size,size)
                .placeholder(R.drawable.loading_avatar)//默认情况会没有设置头像会传入-default-此时error显示出默认头像
                .error(R.drawable.default_avatar)
                .into(imageView);
    }
}
