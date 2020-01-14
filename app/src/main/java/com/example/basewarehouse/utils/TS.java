package com.example.basewarehouse.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.basewarehouse.R;
import com.example.basewarehouse.app.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class TS {
    private static long lastShowTime = 0l;
    private static String lastShowMsg = null;
    private static String curShowMsg = null;
    private static final int TOAST_DURATION = 5000;
    public static void customShowToast(Context context, CharSequence s) {
        curShowMsg = s.toString();
        long curShowTime = System.currentTimeMillis();
        if (curShowMsg.equals(lastShowMsg)) {
            if (curShowTime - lastShowTime > TOAST_DURATION) {
//                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                ToastUtil.makeTextShort(context,s);
                lastShowTime = curShowTime;
                lastShowMsg = curShowMsg;
            }
        } else {
//            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            ToastUtil.makeTextShort(context,s);
            lastShowTime = curShowTime;
            lastShowMsg = curShowMsg;
        }
    }

    public static void show(CharSequence s) {
        curShowMsg = s.toString();
        long curShowTime = System.currentTimeMillis();
        if (curShowMsg.equals(lastShowMsg)) {
            if (curShowTime - lastShowTime > TOAST_DURATION) {
//                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                ToastUtil.makeTextShort( com.example.basewarehouse.app.Constants.context,s);
                lastShowTime = curShowTime;
                lastShowMsg = curShowMsg;
            }
        } else {
//            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            ToastUtil.makeTextShort( com.example.basewarehouse.app.Constants.context,s);
            lastShowTime = curShowTime;
            lastShowMsg = curShowMsg;
        }
    }

    public static void showMyToast(final Toast toast, final int cnt, final OnDissListener onDissListener) {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(onDissListener!=null){
                    onDissListener.onDissListenr();
                }
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }

    /**
     * 规定toast 弹出的时间显示
     * @param text
     * @param cnt
     */
    public static void showTimeText(String text, int cnt, OnDissListener onDissListener){
        Toast toast = new Toast(Constants.context);
        View view = LayoutInflater.from(Constants.context).inflate(R.layout.toast, null);
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
        iv_image.setVisibility(View.GONE);
        tv_text.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
        showMyToast(toast,cnt,onDissListener);
    }

    public  interface OnDissListener{
        void onDissListenr();
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
