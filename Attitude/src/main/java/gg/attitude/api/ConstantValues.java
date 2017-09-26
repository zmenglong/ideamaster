package gg.attitude.api;

/**
 * Created by ImGG on 2016/6/14.
 * Email:gu.yuepeng@foxmail.com
 */
public interface ConstantValues {
    //加载得到结果后默认延时
    int DELAY_TIME=1200;
    //SharePreference 的文件名称
    String SP_CONFIG = "attitude_";
    String File_Path_Key = "file_path_key";
    //获取图片的标识符
    int SELECT_PIC_KITKAT = 100;
    int SELECT_PIC = 200;
    //Attitude默认加载Bean的条数
    int LOAD_ATTITUDE_COUNT=16;

    //正则Email
    String EMAIL_CHECK="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";


    //AccountUtils--try2GetCode
    int REGISTER=1;
    int RESET=0;
    String SMS_CODE_REGISTER="IdeaMaster";
    String SMS_CODE_RESET="resetPSW";

    //Main
    int ATTITUDE_FRAGMENT=0;
    int FEEDBACK_FRAGMENT=1;
    int SETTINGS_FRAGMENT=2;
    int SQUARE_FRAGMENT=3;
    //Blank
    int NO_DATA_FRAGMENT=4;
}
