
package gg.attitude.bean;

/**
 * Created by ImGG on 2016/6/14.
 * Email:gu.yuepeng@foxmail.com
 */
public class FeedbackBean {
    private int id;//用来标识在AttitudeList中的位置
    private UserBean author;//作者
    private String contentTxt;
    private String aDes;//a选项的描述
    private String bDes;
    private int aCount;//a选项的支持数
    private int bCount;
    private boolean isAnonymity;//是否匿名
    private boolean hasFeedback;//是否已经有反馈了
    private String AttitudeID;//记录原AttitudeBean的id
    public FeedbackBean(){}

    public FeedbackBean(UserBean author, String contentTxt, String aDes, String bDes, int aCount, int bCount, boolean isAnonymity) {
        this.author = author;
        this.contentTxt = contentTxt;
        this.aDes = aDes;
        this.bDes = bDes;
        this.aCount = aCount;
        this.bCount = bCount;
        this.isAnonymity = isAnonymity;
    }

    public UserBean getAuthor() {
        return author;
    }

    public void setAuthor(UserBean author) {
        this.author = author;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    public String getaDes() {
        return aDes;
    }

    public void setaDes(String aDes) {
        this.aDes = aDes;
    }

    public String getbDes() {
        return bDes;
    }

    public void setbDes(String bDes) {
        this.bDes = bDes;
    }

    public int getaCount() {
        return aCount;
    }

    public void setaCount(int aCount) {
        this.aCount = aCount;
    }

    public int getbCount() {
        return bCount;
    }

    public void setbCount(int bCount) {
        this.bCount = bCount;
    }

    public boolean isAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(boolean anonymity) {
        isAnonymity = anonymity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasFeedback() {
        return hasFeedback;
    }

    public void setHasFeedback(boolean hasFeedback) {
        this.hasFeedback = hasFeedback;
    }

    public String getAttitudeID() {
        return AttitudeID;
    }

    public void setAttitudeID(String attitudeID) {
        AttitudeID = attitudeID;
    }
}
