package com.example.basewarehouse.utils;

import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basewarehouse.R;


/**
 * 自定义的Toast
 * Created by XiaoPu on 2017/3/8.
 */

public class ToastUtil {
    private Toast mToast=null;
    private String mTempStr = "";
    private long time = 5000;
    private android.os.Handler  handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    private ToastUtil(Context context, CharSequence text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
        iv_image.setVisibility(View.GONE);
//        iv_image.setImageDrawable(context.getResources().getDrawable(imageId));
        tv_text.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(view);
        mToast.show();
    }

    public static ToastUtil makeTextLong(Context context, CharSequence text) {
        return new ToastUtil(context, text,  Toast.LENGTH_LONG);
    }

    public static ToastUtil makeTextShort(Context context, CharSequence text) {
        return new ToastUtil(context, text, Toast.LENGTH_SHORT);
    }
//    public void show() {
//        if (mToast != null) {
//            mToast.show();
//        }
//    }
//    public void setGravity(int gravity, int xOffset, int yOffset) {
//        if (mToast != null) {
//            mToast.setGravity(gravity, xOffset, yOffset);
//        }
//    }

}
