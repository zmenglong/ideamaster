package gg.attitude.listener.login;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public interface OnLoginListener {
    void onSuccess();
    void onFailure(int code, String msg);
}
