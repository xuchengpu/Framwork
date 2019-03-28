package com.xcp.framework.eventbus;

/**
 * Created by 许成谱 on 2019/3/21 17:54.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class MessageBean {
    private int num;
    private String message;

    public MessageBean() {
    }

    public MessageBean(int num, String message) {
        this.num = num;
        this.message = message;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
