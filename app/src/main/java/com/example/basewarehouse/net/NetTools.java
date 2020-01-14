package com.example.basewarehouse.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetTools {
    private static String TAG = "NetTools";
    private static OkHttpClient client = null;
    private static volatile NetTools instance=null;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private NetTools(){
        client = getClientInstance();
    }

    public static NetTools getInstance(){
        if(instance==null){
            synchronized(NetTools.class){
                if(instance==null){
                    instance=new NetTools();
                }
            }
        }
        return instance;
    }

    private OkHttpClient getClientInstance() {
        if (client == null) {
            synchronized (NetTools.class) {
                if (client == null)
                    client = new OkHttpClient().newBuilder()
                        .connectTimeout(30, TimeUnit.SECONDS) //请求超时设置
                        .readTimeout(30, TimeUnit.SECONDS).build();
            }
        }
        return client;
    }


    /**
     * 取消指定的请求
     * @param tag
     */
    public void cancelTag(String tag){
        for(Call call : client.dispatcher().queuedCalls()) {
            if(call.request().tag().equals(tag))
                call.cancel();
        }
        for(Call call : client.dispatcher().runningCalls()) {
            if(call.request().tag().equals(tag))
                call.cancel();
        }
    }

    /**
     * Post请求发送JSON数据
     * @param url
     * @param callBackListener
     */
    public void get(String tag, String url, final CallBackListener callBackListener) {
        Log.d(TAG,"url=get="+baseUrl()+url);
        if(callBackListener!=null){
            callBackListener.onBeforeResponse();
        }
        Request request = new Request.Builder()
                .url(baseUrl()+url)
                .tag(tag)
//                .addHeader("accessToken", API.Token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackError(callBackListener,0,"网络链接失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                analysisJson(json,response,callBackListener);
            }
        });

    }

    /**
     * Post请求发送JSON数据
     * @param url
     * @param jsonParams
     * @param callBackListener
     */
    public void post(String tag, String url, String jsonParams, final CallBackListener callBackListener) {
        Log.d(TAG,"jsonParams=="+jsonParams);
        Log.d(TAG,"url=="+baseUrl()+url);
        if(callBackListener!=null){
            callBackListener.onBeforeResponse();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(baseUrl()+url)
//                .addHeader("ot-login-terminal","ANDROID")
//                .addHeader("accessToken", API.Token)
                .post(body).tag(tag)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure=="+e.getMessage());
                callbackError(callBackListener,0,"网络链接失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                analysisJson(json,response,callBackListener);
            }
        });
    }
    /**
     * Post请求发送JSON数据
     * @param url
     * @param callBackListener
     */
    public void get(String url, final CallBackListener callBackListener) {
        Log.d(TAG,"url=get="+baseUrl()+url);
        if(callBackListener!=null){
            callBackListener.onBeforeResponse();
        }
        Request request = new Request.Builder()
                .url(baseUrl()+url)
                .tag(url)
//                .addHeader("accessToken", API.Token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure=="+e.getMessage());
                callbackError(callBackListener,0,"网络链接失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                analysisJson(json,response,callBackListener);
            }
        });

    }

    /**
     * Post请求发送JSON数据
     * @param url
     * @param jsonParams
     * @param callBackListener
     */
    public void post(String url, String jsonParams, final CallBackListener callBackListener) {
        Log.d(TAG,"jsonParams=="+jsonParams);
        Log.d(TAG,"url=="+baseUrl()+url);
        if(callBackListener!=null){
            callBackListener.onBeforeResponse();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(baseUrl()+url)
//                .addHeader("ot-login-terminal","ANDROID")
//                .addHeader("accessToken", API.Token)
                .post(body).tag(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure=="+e.getMessage());
                callbackError(callBackListener,0,"网络链接失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                analysisJson(json,response,callBackListener);
            }
        });
    }

    /**
     * Post请求发送JSON数据
     * @param url
     * @param jsonParams
     * @param callBackListener
     */
    public void login(String url, String jsonParams, final CallBackListener callBackListener) {
        Log.d(TAG,"jsonParams=="+jsonParams);
        Log.d("Net","url=="+ baseUrl()+url);
        if(callBackListener!=null){
            callBackListener.onBeforeResponse();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(baseUrl()+url)
//                .addHeader("ot-login-terminal","ANDROID")
//                .addHeader("accessToken", API.Token)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .post(body).tag(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure=="+e.getMessage());
                callbackError(callBackListener,0,"网络链接失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                analysisJson(json,response,callBackListener);
            }
        });
    }

    /**
     * 解析网络层的json数据
     * @param json
     * @param response
     * @param callBackListener
     */
    private void analysisJson(String json, Response response, final CallBackListener callBackListener){
        Log.d(TAG,"RequestReslut=="+json);
        int code =response.code();
        final String msg;
        String data;
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getInt("code");
            msg = jsonObject.getString("msg");
            data = jsonObject.getString("data");
            if(code==200){
                if(TextUtils.isEmpty(data)){
                    callbackSuccess(callBackListener, json,code, null);
                }else {
                    if (callBackListener.mType == String.class) {
                        callbackSuccess(callBackListener,data, code, data);
                    }else {
                        Object obj = new Gson().fromJson(data, callBackListener.mType);
                        if (obj != null) {
                            callbackSuccess(callBackListener,data, code, obj);
                        } else {
                            callbackError(callBackListener, code, "数据解析失败！");
                        }
                    }
                }
            }else if(code==401){
                callbackLogin(callBackListener,data,code,msg);
            }else {
                callbackError(callBackListener, code, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callbackError(callBackListener, code, "数据解析失败！");
        }
    }

    private void callbackLogin(final CallBackListener callBackListener, final String result, final int code, final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,msg);
                callBackListener.onLogin(code,result);
            }
        });
    }

    private void callbackSuccess(final CallBackListener callBackListener, final String result, final int code, final Object object){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBackListener.onSucessData(code,result,object);

            }
        });
    }

    private void callbackError(final CallBackListener callBackListener, final int code, final String error){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,error);
                callBackListener.onFailure(code,error);

            }
        });
    }




    public static String baseUrl(){
        return "https://www.baidu.com/";
    }


}
