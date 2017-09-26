package gg.attitude.listener;

import java.util.List;

/**
 * Created by ImGG on 2016/6/15.
 * Email:gu.yuepeng@foxmail.com
 */
public interface OnQueryListener<E> {
    void onSuccess(List<E> mList);
    void onFailure(int i, String s);
}
