package com.xcp.framework.okhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 许成谱 on 2019/3/28 13:03.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class Response {
    private final InputStream inputStream;

    public Response(InputStream inputStream) {
        this.inputStream=inputStream;
    }

    public String getString(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream!=null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
