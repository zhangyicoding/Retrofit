package com.estyle.retrofit.httpservice;

import com.estyle.retrofit.bean.LoginStateBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

// post请求
// 使用@Body不需要添加额外注解
public interface LoginService {

    // 服务器接收Form表单数据
    @FormUrlEncoded
    @POST("webtest/login/form")
    Call<LoginStateBean> getLoginCall(@Field("account") String account, @Field("password") String password);

    // 服务器接收Json字符串
//    @POST("webtest/login/json")
//    Call<LoginStateBean> getLoginCall(@Body LoginBean login);

}
