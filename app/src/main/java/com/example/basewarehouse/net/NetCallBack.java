package com.example.basewarehouse.net;

public abstract class NetCallBack extends BaseCallBack {
    @Override
    public void onSucessData(String data) {
        onSucess(data);
    }

    @Override
    public void onParseJsonException() {
        onException(0,"");
    }

    @Override
    public void onOtherException(int code, String message) {
        onException(code,message);
    }


    @Override
    public void onFailure(String content) {
        onNetFailure(content);
    }

    @Override
    public void onBeforeResponse() {
        onBeforeRes();
    }

    @Override
    public void onLogin() {

    }
    public abstract void onSucess(String data);
    public abstract void onException(int code, String message);
    public abstract void onNetFailure(String content);
    public abstract void onBeforeRes();

}
