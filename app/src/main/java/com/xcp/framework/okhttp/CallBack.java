package com.xcp.framework.okhttp;

import java.io.IOException;

/**
 * Created by 许成谱 on 2019/3/28 12:12.
 * qq:1550540124
 * 热爱生活每一天！
 */
public interface CallBack {
    /**
     * Called when the request could not be executed due to cancellation, a connectivity problem or
     * timeout. Because networks can fail during an exchange, it is possible that the remote server
     * accepted the request before the failure.
     */
    void onFailure(Call call, IOException e);

    /**
     *
     * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
     * not necessarily indicate application-layer success: {@code response} may still indicate an
     * unhappy HTTP response code like 404 or 500.
     */
    void onResponse(Call call, Response response) throws IOException;
}
