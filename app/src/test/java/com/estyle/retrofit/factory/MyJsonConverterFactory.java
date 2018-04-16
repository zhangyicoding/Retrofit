package com.estyle.retrofit.factory;

import android.support.annotation.Nullable;

import com.estyle.retrofit.bean.DishBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MyJsonConverterFactory extends Converter.Factory {

    private MyJsonConverterFactory() {
    }

    public static MyJsonConverterFactory create() {
        return new MyJsonConverterFactory();
    }

    // 请求体转换器，将请求参数转换为RequestBody，一般不用重写
    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    // 响应体转换器，将响应体转换为实体bean
    @Nullable
    @Override
    public Converter<ResponseBody, DishBean> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, DishBean>() {

            // 转换器中自定义转换规则
            @Override
            public DishBean convert(ResponseBody responseBody) throws IOException {
                String json = responseBody.string();
                return new Gson().fromJson(json, DishBean.class);
            }
        };
    }
}
