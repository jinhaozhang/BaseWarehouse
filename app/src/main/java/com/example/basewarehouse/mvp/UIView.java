package com.example.basewarehouse.mvp;

public interface UIView extends IView {
    /**
     * 数据获取失败
     */
    void showFailUI(int requesCode);

    /**
     * 数据获取失败
     */
    void showBeforeNetUI(int requesCode);
}
