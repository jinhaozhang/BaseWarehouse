package com.example.basewarehouse.mvp;

import android.app.Activity;

/**
 * 所有的view（Activity、FragmentActivity、Fragment...）都必须实现这个接口
 */
public interface IView {
    // 此方法是为了当Presenter中需要获取上下文对象时，传递上下文对象，而不是让Presenter直接持有上下  文对象
    Activity getSelfActivity();
}
