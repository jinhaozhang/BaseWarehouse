package com.example.basewarehouse.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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


public class NetUtils {
    private static String TAG = "NetUtils";
    private static OkHttpClient client = null;
    private static volatile NetUtils instance=null;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private NetUtils(){
        client = getClientInstance();
    }

    public static  NetUtils getInstance(){
        if(instance==null){
            synchronized(NetUtils .class){
                if(instance==null){
                    instance=new NetUtils ();
                }
            }
        }
        return instance;
    }

    private OkHttpClient getClientInstance() {
        if (client == null) {
            synchronized (NetUtils.class) {
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
     * @param httpCallBack
     */
    public void get(String tag, String url, final BaseCallBack httpCallBack) {
        Log.d(TAG,"url=get="+baseUrl()+url);
        if(httpCallBack!=null){
            httpCallBack.onBeforeResponse();
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
                backFailData(e,httpCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG,"RequestReslut=="+json);
                JsonParser parser = new JsonParser();
                int code =response.code();
                try {
                    JsonObject jo = parser.parse(json).getAsJsonObject();
                    code = jo.get("code").getAsInt();
                    String msg = jo.get("msg").getAsString();
                    if(code==200){
                        backSuccessData(json,httpCallBack );
//                        if (httpCallBack.mType == String.class) {
//                            backSuccessData(json,httpCallBack );
//                        } else {
//                            try {
//                                Object obj = mGson.fromJson(json, httpCallBack.mType);
//                                if (obj != null) {
//                                    backSuccessData(json,httpCallBack );
//                                } else {
//                                    backJsonErro(httpCallBack);
//                                }
//                            } catch (com.google.gson.JsonParseException e) { // Json解析的错误
//                                backJsonErro(httpCallBack);
//                            }
//                        }

//                        backSuccessData(list,httpCallBack );
                    }else if(code==401){
                        backLoginData(msg,httpCallBack);
                    }else {
                        backOtherErro(code,msg,httpCallBack);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    backJsonErro(code,httpCallBack);
                }
            }
        });

    }


    /**
     * Post请求发送JSON数据
     * @param url
     * @param jsonParams
     * @param httpCallBack
     */
    public void post(String tag, String url, String jsonParams, final BaseCallBack httpCallBack) {
        Log.d(TAG,"jsonParams=="+jsonParams);
        Log.d(TAG,"url=="+baseUrl()+url);
        if(httpCallBack!=null){
            httpCallBack.onBeforeResponse();
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
                backFailData(e,httpCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG,"RequestReslut=="+json);
                JsonParser parser = new JsonParser();
                int code =response.code();
                try {
                    JsonObject jo = parser.parse(json).getAsJsonObject();
                    code = jo.get("code").getAsInt();
                    String msg = jo.get("msg").getAsString();
//                    JSONObject jsonObject = new JSONObject(json);
//                    int code=jsonObject.getInt("code");
//                    String msg = jsonObject.getString("msg");
                    if(code==200){
                        backSuccessData(json,httpCallBack );
//                        if (httpCallBack.mType == String.class) {
//                            backSuccessData(json,httpCallBack );
//                        } else {
//                            try {
//                                Object obj = mGson.fromJson(json, httpCallBack.mType);
//                                if (obj != null) {
//                                    backSuccessData(json,httpCallBack );
//                                } else {
//                                    backJsonErro(httpCallBack);
//                                }
//                            } catch (com.google.gson.JsonParseException e) { // Json解析的错误
//                                backJsonErro(httpCallBack);
//                            }
//                        }

//                        backSuccessData(list,httpCallBack );
                    }else if(code==401){
                        backLoginData(msg,httpCallBack);
                    }else {
                        backOtherErro(code,msg,httpCallBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    backJsonErro(code,httpCallBack);
                }
            }
        });
    }


    /**
     * Post请求发送JSON数据
     * @param url
     * @param httpCallBack
     */
    public void get(String url, final BaseCallBack httpCallBack) {
        Log.d(TAG,"url=get="+baseUrl()+url);
        if(httpCallBack!=null){
            httpCallBack.onBeforeResponse();
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
                backFailData(e,httpCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG,"RequestReslut=="+json);
                JsonParser parser = new JsonParser();
                int code =response.code();
                try {
                    JsonObject jo = parser.parse(json).getAsJsonObject();
                    code = jo.get("code").getAsInt();
                    String msg = jo.get("msg").getAsString();
                    if(code==200){
                        backSuccessData(json,httpCallBack );
//                        if (httpCallBack.mType == String.class) {
//                            backSuccessData(json,httpCallBack );
//                        } else {
//                            try {
//                                Object obj = mGson.fromJson(json, httpCallBack.mType);
//                                if (obj != null) {
//                                    backSuccessData(json,httpCallBack );
//                                } else {
//                                    backJsonErro(httpCallBack);
//                                }
//                            } catch (com.google.gson.JsonParseException e) { // Json解析的错误
//                                backJsonErro(httpCallBack);
//                            }
//                        }

//                        backSuccessData(list,httpCallBack );
                    }else if(code==401){
                        backLoginData(msg,httpCallBack);
                    }else {
                        backOtherErro(code,msg,httpCallBack);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    backJsonErro(code,httpCallBack);
                }
            }
        });

    }


    /**
     * Post请求发送JSON数据
     * @param url
     * @param jsonParams
     * @param httpCallBack
     */
    public void post(String url, String jsonParams, final BaseCallBack httpCallBack) {
        Log.d(TAG,"jsonParams=="+jsonParams);
        Log.d(TAG,"url=="+baseUrl()+url);
        if(httpCallBack!=null){
            httpCallBack.onBeforeResponse();
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
                backFailData(e,httpCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG,"RequestReslut=="+json);
                JsonParser parser = new JsonParser();
                int code =response.code();
                try {
                    JsonObject jo = parser.parse(json).getAsJsonObject();
                    code = jo.get("code").getAsInt();
                    String msg = jo.get("msg").getAsString();
//                    JSONObject jsonObject = new JSONObject(json);
//                    int code=jsonObject.getInt("code");
//                    String msg = jsonObject.getString("msg");
                    if(code==200){
                        backSuccessData(json,httpCallBack );
//                        if (httpCallBack.mType == String.class) {
//                            backSuccessData(json,httpCallBack );
//                        } else {
//                            try {
//                                Object obj = mGson.fromJson(json, httpCallBack.mType);
//                                if (obj != null) {
//                                    backSuccessData(json,httpCallBack );
//                                } else {
//                                    backJsonErro(httpCallBack);
//                                }
//                            } catch (com.google.gson.JsonParseException e) { // Json解析的错误
//                                backJsonErro(httpCallBack);
//                            }
//                        }

//                        backSuccessData(list,httpCallBack );
                    }else if(code==401){
                        backLoginData(msg,httpCallBack);
                    }else {
                        backOtherErro(code,msg,httpCallBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    backJsonErro(code,httpCallBack);
                }
            }
        });
    }

    public static String baseUrl(){
            return "https://www.baidu.com/";
    }


    /**
     * Post请求发送JSON数据
     * @param url
     * @param jsonParams
     * @param httpCallBack
     */
    public void login(String url, String jsonParams, final BaseCallBack httpCallBack) {
        Log.d(TAG,"jsonParams=="+jsonParams);
        Log.d("Net","url=="+ baseUrl()+url);
        if(httpCallBack!=null){
            httpCallBack.onBeforeResponse();
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
                    backFailData(e,httpCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG,"RequestReslut=="+json);
                JsonParser parser = new JsonParser();
                int code =response.code();
                try {
                    JsonObject jo = parser.parse(json).getAsJsonObject();
                    code = jo.get("code").getAsInt();
                    String msg = jo.get("msg").getAsString();
//                    JSONObject jsonObject = new JSONObject(json);
//                    int code=jsonObject.getInt("code");
//                    String msg = jsonObject.getString("msg");
                    if(code==200){
                        backSuccessData(json,httpCallBack );
//                        if (httpCallBack.mType == String.class) {
//                            backSuccessData(json,httpCallBack );
//                        } else {
//                            try {
//                                Object obj = mGson.fromJson(json, httpCallBack.mType);
//                                if (obj != null) {
//                                    backSuccessData(json,httpCallBack );
//                                } else {
//                                    backJsonErro(httpCallBack);
//                                }
//                            } catch (com.google.gson.JsonParseException e) { // Json解析的错误
//                                backJsonErro(httpCallBack);
//                            }
//                        }

//                        backSuccessData(list,httpCallBack );
                    }else if(code==401){
                        backLoginData(msg,httpCallBack);
                    }else {
                        backOtherErro(code,msg,httpCallBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    backJsonErro(code,httpCallBack);
                }
            }
        });
    }

    private static void backFailData(final IOException e, final BaseCallBack httpCallBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Net","网络连接失败");
                httpCallBack.onFailure(e.getMessage());

            }
        });
    }
    private static void backSuccessData(final String data, final BaseCallBack httpCallBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                httpCallBack.onSucessData(data);
            }
        });
    }
    private static void backLoginData(final String msg, final BaseCallBack httpCallBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Net",msg);
                httpCallBack.onLogin();
            }
        });
    }
    private static void backJsonErro(final int code, final BaseCallBack httpCallBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Net","数据解析失败，请重新进入");
                httpCallBack.onParseJsonException();
            }
        });
    }
    private static void backOtherErro(final int code, final String msg, final BaseCallBack httpCallBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(code!=406){
                    if(TextUtils.isEmpty(msg)){
                        Log.i("Net",code+"  ");
                    }else {
                        Log.i("Net",msg);
                    }
                }
                httpCallBack.onOtherException(code,msg);
            }
        });
    }

}
