package gg.attitude.listener.attitude;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public interface OnHandleListener {
    void onSuccess();
    void onFailure(int i, String s);
    void onFinished();
}
