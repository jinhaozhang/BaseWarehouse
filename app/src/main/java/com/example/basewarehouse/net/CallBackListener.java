package com.example.basewarehouse.net;


import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CallBackListener<T> {
    public abstract void onSucessData(int code, String responsejson, T t);
    public abstract void onFailure(int code, String msg);
    public abstract void onBeforeResponse();
    public void onLogin(int code, String result){

    }

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public CallBackListener() {
        mType = getSuperclassTypeParameter(getClass());
    }
}
