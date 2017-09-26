package gg.attitude.listener.login;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public interface OnGetCodeListener {
    void onSuccess(Integer smsId, BmobException arg1);
    void onFailure(Integer smsId, BmobException arg1);
}
