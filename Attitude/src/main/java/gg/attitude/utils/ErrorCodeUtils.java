package gg.attitude.utils;

import gg.attitude.fragments.main.AttitudeFragment;
import gg.attitude.fragments.main.FeedbackFragment;

/**
 * Created by ImGG on 2016/6/14.
 * Email:gu.yuepeng@foxmail.com
 */
public final class ErrorCodeUtils {
    private ErrorCodeUtils(){}
    public static String getErrorMsgFromCode(int code){
        switch (code){
            case AttitudeFragment.NO_DATA://-2
                return "数据库中暂时没有可以处理的数据了，休息一会吧";
            case FeedbackFragment.NO_DATA_2_FEEDBACK://-100
                return "您还没有关注过任何问题，去试试吧";
            case -999:
                //自己写的错误码
                return "手机号已注册过，请尝试登陆或找回密码";
            case -998:
                //自己写的错误码
                return "该手机号尚未注册，请核实后重试或前往注册";
            case 207:
                //返回的错误码
                return "验证码输入有误";
            case 210:
                //返回的错误码
                return "原密码错误，请核实后重试";
            case 101:
                return "账号或密码错误:(";
            case 9001:
                return "Application Id为空，请初始化";
            case 9002:
                return "解析返回数据出错";
            case 9003:
                return "上传文件出错";
            case 9004:
                return "文件上传失败";
            case 9005:
                return "批量操作只支持最多50条";
            case 9006:
                return "objectId为空";
            case 9007:
                return "文件大小超过10M";
            case 9008:
                return "上传文件不存在";
            case 9009:
                return "没有缓存数据";
            case 9010:
                return "网络超时";
            case 9011:
                return "User类不支持批量操作";
            case 9012:
                return "上下文为空";
            case 9013:
                return "BmobObject（数据表名称）格式不正确";
            case 9014:
                return "第三方账号授权失败";
            case 9015:
                return "未知错误";
            case 9016:
                return "无网络连接，请检查您的手机网络";
            case 9017:
                return "与第三方登录有关的错误，具体请看对应的错误描述";
            case 9018:
                return "参数不能为空";
            case 9019:
                return "格式不正确：手机号码、邮箱地址、验证码";
        }
        return "未知错误:(  请重试";
    }
}
