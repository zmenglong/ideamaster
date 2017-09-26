package gg.attitude.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by ImGG on 2016/6/11.
 * Email:gu.yuepeng@foxmail.com
 */
public class AttitudeBean extends BmobObject {
    private UserBean author=null;//作者
    private String when2Feedback;//最晚反馈时间
    private String times2Feedback;//达到多少条即做出反馈
    private BmobRelation who2Feedback;//反馈给的用户列表，在AttitudeHandleUtils的save方法中初始化过
    private Boolean hasFeedback=false;//是否已经反馈过了
    private Integer aCount=0;//选a的人数
    private Integer bCount=0;//选b的人数
    private Boolean isAnonymity=false;//是否为匿名
    private String contentTxt;//Idea内容--文字
    private String aDescription;//a选项的描述
    private String bDescription;//b选项的描述
    private BmobFile contentPic;//Idea内容--图片--通过getFileUrl获取url
    private BmobFile contentRec;//Idea内容--音频
    private List<String> handledUserIDs =new ArrayList<String>();//处理过该条Attitude的User列表

    public UserBean getAuthor() {
        return author;
    }

    public void setAuthor(UserBean author) {
        this.author = author;
    }

    public String getWhen2Feedback() {
        return when2Feedback;
    }

    public void setWhen2Feedback(String when2Feedback) {
        this.when2Feedback = when2Feedback;
    }

    public String getTimes2Feedback() {
        return times2Feedback;
    }

    public void setTimes2Feedback(String times2Feedback) {
        this.times2Feedback = times2Feedback;
    }

    public Boolean getHasFeedback() {
        return hasFeedback;
    }

    public void setHasFeedback(Boolean hasFeedback) {
        this.hasFeedback = hasFeedback;
    }

    public Integer getaCount() {
        return aCount;
    }

    public void setaCount(Integer aCount) {
        this.aCount = aCount;
    }

    public Integer getbCount() {
        return bCount;
    }

    public void setbCount(Integer bCount) {
        this.bCount = bCount;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    public BmobFile getContentPic() {
        return contentPic;
    }

    public void setContentPic(BmobFile contentPicUrl) {
        this.contentPic = contentPicUrl;
    }

    public BmobFile getContentRec() {
        return contentRec;
    }

    public void setContentRec(BmobFile contentRec) {
        this.contentRec = contentRec;
    }

    public BmobRelation getWho2Feedback() {
        return who2Feedback;
    }

    public void setWho2Feedback(BmobRelation who2Feedback) {
        this.who2Feedback = who2Feedback;
    }
    public void add2Feedback(UserBean mUser){
        BmobRelation relation = new BmobRelation();
        relation.add(mUser);
        setWho2Feedback(relation);
    }
    public void remove2Feedback(UserBean mUser){
        //从反馈列表中移除某个用户
        BmobRelation relation = new BmobRelation();
        relation.remove(mUser);
        setWho2Feedback(relation);
    }

    public List<String> getHandledUserIDs() {
        return handledUserIDs;
    }
    public void addHandledUser(UserBean mUser){
        handledUserIDs.add(mUser.getObjectId());
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaDescription(String aDescription) {
        this.aDescription = aDescription;
    }

    public String getbDescription() {
        return bDescription;
    }

    public void setbDescription(String bDescription) {
        this.bDescription = bDescription;
    }
}
