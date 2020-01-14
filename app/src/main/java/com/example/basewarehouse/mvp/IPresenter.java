package com.example.basewarehouse.mvp;

public interface IPresenter<I extends IView> {
    void detachView();//解除绑定关系
}
