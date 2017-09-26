package gg.attitude.listener.attitude;

import java.util.List;

import gg.attitude.bean.AttitudeBean;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public interface OnQueryAttitudeListener {
    void onSuccess(List<AttitudeBean> list);
    void onError(int i, String s);
}
