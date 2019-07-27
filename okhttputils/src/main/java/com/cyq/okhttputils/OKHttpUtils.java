package com.cyq.okhttputils;

import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtils {
    //设置设置超时时间和缓存
    private static OkHttpClient client = new OkHttpClient.Builder().build();

    /**
     * 同步调用，需要开启子线程
     * response的body有很多种输出方法，string()只是其中之一，注意是string()不是toString()。如果是下载文件就是response.body().bytes()。
     * 另外可以根据response.code()获取返回的状态码。
     * @param url
     */
    public void httpSyncGet(String url,SyncCallback callback) {
        this.callback = callback;
        try {
            Request request = new Request.Builder().url(url).get().build();
            Call call = client.newCall(request);
            Response response = call.execute();

            this.callback.onCompleted(response);//回调请求结果
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步调用
     * response的body有很多种输出方法，string()只是其中之一，注意是string()不是toString()。如果是下载文件就是response.body().bytes()。
     * 另外可以根据response.code()获取返回的状态码。
     * @param url
     */
    public void httpAsynGet(String url,Callback callback) {
        try {
            Request request = new Request.Builder().url(url).get().build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * response的body有很多种输出方法，string()只是其中之一，注意是string()不是toString()。如果是下载文件就是response.body().bytes()。
     * 另外可以根据response.code()获取返回的状态码。
     * @param url
     * @param map
     * @param callback
     */
    public static void httpAsynGet(String url, Map<String, String> map, Callback callback) {
        try {
            StringBuffer sb = new StringBuffer();
            //拼接URL的参数
            for (Map.Entry<String, String> m : map.entrySet()) {
                String key = m.getKey();
                String value = m.getValue();
                if (sb.length() == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key + "=" + value);
            }
            //得到最终的URL
            String resultUrl = "";
            if (!TextUtils.isEmpty(sb.toString())) {
                resultUrl = url + sb.toString();
            }
            //请求调用
            Request request = new Request.Builder().get().url(resultUrl).build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求参数方法
     * @param url
     * @param map
     * @param callback
     */
    public static void httpAsynPost(String url, Map<String, String> map,Callback callback){
        try {
            FormBody.Builder builder = new FormBody.Builder();

            if (map != null && map.isEmpty()) {
                for (Map.Entry<String, String> m : map.entrySet()) {
                    builder.add(m.getKey(),m.getValue());
                }
            }

            FormBody formBody = builder.build();
            Request request = new Request.Builder().post(formBody).url(url).build();
            Call call = client.newCall(request);
            call.enqueue(callback);//异步请求方式
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MultipartBody：用来提交包涵文件的参数
     * @param url
     * @param map 普通参数
     * @param mediaType eg:image/png
     * @param fileName 提交文件的关键字
     * @param filePath 路径
     * @param callback
     */
    public static void httpAsynPost(String url, Map<String, String> map,String mediaType,String fileName,String filePath,Callback callback){
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            if (map != null && map.isEmpty()) {
                for (Map.Entry<String, String> m : map.entrySet()) {
                    builder.addFormDataPart(m.getKey(),m.getValue());
                }
            }
            File file = new File(filePath);
            builder.addFormDataPart(fileName,filePath, RequestBody.create(MediaType.parse(mediaType),file));
            MultipartBody body = builder.build();

            Request request = new Request.Builder().post(body).url(url).build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MultipartBody：用来提交包涵文件的参数
     * @param url
     * @param map 普通参数
     * @param mediaType eg:image/png
     * @param fileKey 提交文件的关键字
     * @param paths 路径集合
     * @param callback
     */
    public static void httpAsynPost(String url, Map<String, String> map, String mediaType, String fileKey, List<String> paths, Callback callback){
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            if (map != null && map.isEmpty()) {
                for (Map.Entry<String, String> m : map.entrySet()) {
                    builder.addFormDataPart(m.getKey(),m.getValue());
                }
            }

            if (paths != null && paths.size() > 0) {
                for (String path : paths) {
                    File file = new File(path);
                    builder.addFormDataPart(fileKey,path, RequestBody.create(MediaType.parse(mediaType),file));
                }
            }
            MultipartBody body = builder.build();

            Request request = new Request.Builder().post(body).url(url).build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SyncCallback callback;
    public interface SyncCallback{
        void onCompleted(Response response);
    }

    public void setSyncCallback(SyncCallback callback){
        this.callback = callback;
    }
}
