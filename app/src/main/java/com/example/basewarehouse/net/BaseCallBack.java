package com.example.basewarehouse.net;


public abstract class BaseCallBack {
    public abstract void onSucessData(String data);
    public abstract void onParseJsonException();
    public abstract void onOtherException(int code, String message);
    public abstract void onFailure(String content);
    public abstract void onBeforeResponse();
    public abstract void onLogin();

}
