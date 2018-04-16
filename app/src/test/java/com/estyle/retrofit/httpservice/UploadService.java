package com.estyle.retrofit.httpservice;

import com.estyle.retrofit.bean.UploadBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

// post请求，上传多文件
// 使用@Body不需要添加额外注解
public interface UploadService {

    @Multipart
    @POST("webtest/parts")
    Call<UploadBean> getUploadCallWithPart(@Part List<MultipartBody.Part> parts);

    @POST("webtest/body")
    Call<UploadBean> getUploadCallWithBody(@Body MultipartBody body);

}
