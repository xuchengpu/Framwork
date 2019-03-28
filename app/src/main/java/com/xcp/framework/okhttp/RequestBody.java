package com.xcp.framework.okhttp;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 许成谱 on 2019/3/28 11:39.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class RequestBody {
    public static final String FORM = "multipart/form-data";
    private Map<String, Object> params;
    private String type;//请求类型 ：表单、文件等
    private String boundary =createBoundary();
    private String startBoundary = "--"+boundary;
    private String endBoundary = startBoundary+"--";

    private String createBoundary() {
        return "OkHttp"+ UUID.randomUUID().toString();
    }

    public RequestBody() {
        params = new HashMap<>();
    }


    public RequestBody addParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestBody type(String type) {
        this.type=type;
        return this;
    }

    public String getContentType() {
        // 都是一些规范
        return type + ";boundary = "+boundary;
    }

    public long getContentLength() {
        // 多少个字节要给过去，写的内容做一下统计
        long length = 0;
        for(Map.Entry<String,Object> entry:params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                String postTextStr = getText(key,(String)value);
                Log.e("TAG",postTextStr);
                length += postTextStr.getBytes().length;
            }

//            if(value instanceof Bindry){
//                Bindry bindry = (Bindry) value;
//                String postTextStr = getText(key,bindry);
//                Log.e("TAG",postTextStr);
//                length += postTextStr.getBytes().length;
//                length += bindry.fileLength()+"\r\n".getBytes().length;
//            }
        }

        if(params.size()!=0){
            length += endBoundary.getBytes().length;
        }
        return length;
    }
    private String getText(String key, String value) {
        return startBoundary+"\r\n"+
                "Content-Disposition: form-data; name = \""+key+"\"\r\n"+
                "Context-Type: text/plain\r\n"+
                "\r\n"+
                value+
                "\r\n";
    }

    public void onWriteBody(OutputStream outputStream) throws IOException {
        for(Map.Entry<String,Object> entry:params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                String postTextStr = getText(key,(String)value);
                outputStream.write(postTextStr.getBytes());
            }
//            if(value instanceof Bindry){
//                Bindry bindry = (Bindry) value;
//                String postTextStr = getText(key,bindry);
//                outputStream.write(postTextStr.getBytes());
//                bindry.onWrite(outputStream);
//                outputStream.write("\r\n".getBytes());
//            }
        }

        if(params.size()!=0){
            outputStream.write(endBoundary.getBytes());
        }
    }
//    private String getText(String key, Bindry value) {
//        return startBoundary+"\r\n"+
//                "Content-Disposition: form-data; name = \""+key+"\" filename = \""+value.fileName()+"\""+
//                "Context-Type: "+value.mimType()+"\r\n"+
//                "\r\n";
//    }
}
