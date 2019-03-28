package com.xcp.framework.okhttp;

/**
 * Created by 许成谱 on 2019/3/28 11:36.
 * qq:1550540124
 * 热爱生活每一天！
 */
public enum Method {
    GET, POST, PUT, DELETE;

    public boolean doOutput() {
        switch (this) {
            case POST:
            case PUT:
                return true;
        }
        return false;
    }
}
