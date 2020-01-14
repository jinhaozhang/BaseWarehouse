package com.example.basewarehouse.utils;

import android.app.Activity;

import java.util.HashSet;

/**
 * 项目名    yqfoodec
 * 包名      com.yqfoodec.app.yqfoodec.utils
 * 文件名    ActivityManager
 * 创建者    飞翔的大阳阳
 * 创建时间  2018/8/22/022  14:48
 * 描述      TODO
 */
public class ActivityManager {

    /**
     *  定义HashSet集合来装Activity，是可以防止Activity不被重复
     */
    private static HashSet<Activity> hashSet = new HashSet<Activity>();

    private static ActivityManager instance = new ActivityManager();;

    private ActivityManager() {}

    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 每一个Activity 在 onCreate 方法的时候，可以装入当前this
     * @param activity
     */
    public void addActivity(Activity activity) {
        try {
            hashSet.add(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭所有的activity
     */
    public void finishActivity() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用此方法用于退出整个Project
     */
    public void exit() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    // 此方法用于扩展使用
    /*
    public void onLowMemory() {
      super.onLowMemory();
      System.gc();
    }
    */
}
