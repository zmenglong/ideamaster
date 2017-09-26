package gg.attitude.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public class UserBean extends BmobUser {
    private String nickName;//昵称
    private String avatarUrl;//头像url
    private String sign;//个性签名
    private String psw;//密码
    private String phoneNum;//电话号码
    private String age="0";//年龄--初始化为0
    private Boolean sex=true;//性别    男性true,女性false--初始化为男性
    private String place;//所在地
    private Boolean isNew=true;//是否为新用户--默认为是新用户
    private Integer exp=0;//经验--初始化为0经验
    private BmobRelation cares;//我想要得到反馈的列表

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }
    
    public Integer getExp(){
        return exp;
    }

    public BmobRelation getCares() {
        return cares;
    }

    public void setCares(BmobRelation cares) {
        this.cares = cares;
    }
    public void add2Cares(AttitudeBean mAttitude){
        BmobRelation relation = new BmobRelation();
        relation.add(mAttitude);
        setCares(relation);
    }
    public void remove2Cares(AttitudeBean mAttitude){
        BmobRelation relation = new BmobRelation();
        relation.remove(mAttitude);
        setCares(relation);
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
