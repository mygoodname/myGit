package com.tangbing.admin.calendercheckdemo.model;

/**
 * Created by tangbing on 2019/3/20.
 * Describe :
 */

public class SignDateModel {
    public static final int STATUS_NO_SIGN = 0; //0 未签到的日期
    public static final int STATUS_HAVE_SIGN = 1; //1 已签到的日期
    public static final int STATUS_SAT_OR_SUN = 2; //2 周六或周日
    public static final int STATUS_CURRENT_DAY = 3; //3 今天日期
    private int status; //签到状态
    private String content;
    private String day; //要展示的文字
    public SignDateModel() {
        this.status = STATUS_NO_SIGN;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public SignDateModel(int status, String content) {
        this.status = status;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
