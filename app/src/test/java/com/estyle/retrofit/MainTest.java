package com.estyle.retrofit;

import com.estyle.retrofit.bean.DishBean;
import com.estyle.retrofit.bean.LoginBean;
import com.estyle.retrofit.bean.LoginStateBean;
import com.estyle.retrofit.bean.UploadBean;
import com.estyle.retrofit.httpservice.DishService;
import com.estyle.retrofit.httpservice.LoginService;
import com.estyle.retrofit.httpservice.UploadService;
import com.estyle.retrofit.util.MyLog;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangyi
 * <p>
 * Retrofit使用流程：
 * 1、构建Retrofit实例
 * 2、创建接口，定义请求方法，返回Retrofit的Call或RxJava的Observable
 * 3、使用Retrofit创建接口实例，获取Call或Observable
 * 4、使用Call或Observable
 * <p>
 * url路径拆分规则：主机地址+固定部分(+变化参数)
 * http://www.qubaobei.com/ + ios/cf/dish_list.php?stage_id=1&limit=10 + &page=1
 */
public class MainTest {

    // 创建Retrofit，全局只需要配置一次，一般在Application中初始化
    private Retrofit createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.qubaobei.com/")// 配置一次host地址即可，结尾必须"/"
                .addConverterFactory(GsonConverterFactory.create())// 配置使用Gson自动解析json
//                .addConverterFactory(MyJsonConverterFactory.create())// 自定义json转换器
                .build();
        return retrofit;
    }

    // get请求
    @Test
    public void test1() {
        Retrofit retrofit = createRetrofit();

        // 通过动态代理模式，实现回调方法：
        // 1、解析参数，注解等配置信息，生成完整url地址
        // 2、通过适配器模式，当我们调用使用Retrofit的call时，实际就是在操作OkHttpCall
        DishService httpService = retrofit.create(DishService.class);
        // 获取接口中的call
        Call<DishBean> call = httpService.getDishCall(1);
        // 执行异步请求。Retrofit的call的响应会自动切换主线程，OkHttp的call不会
        call.enqueue(new Callback<DishBean>() {

            @Override
            public void onResponse(Call<DishBean> call, Response<DishBean> response) {
                MyLog.e("当前线程", Thread.currentThread().getName());// 主线程

                DishBean dishBean = response.body();
                List<DishBean.DataBean> datas = dishBean.getData();
                datas.forEach(data -> {
                    MyLog.e(data.getTitle());
                });
            }

            @Override
            public void onFailure(Call<DishBean> call, Throwable t) {

            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // post请求
    @Test
    public void test2() {
        // 如果场景二，封装要提交的参数
        LoginBean login = new LoginBean("admin", "123456");

        Retrofit retrofit = createRetrofit();
        retrofit.create(LoginService.class)
                .getLoginCall("admin", "123456")// 场景一：直接提交参数作为form表单
//                .getLoginCall(login)// 场景二：将参数进行封装作为json
                .enqueue(new Callback<LoginStateBean>() {
                    @Override
                    public void onResponse(Call<LoginStateBean> call,
                                           Response<LoginStateBean> response) {
                        LoginStateBean loginState = response.body();
                    }

                    @Override
                    public void onFailure(Call<LoginStateBean> call, Throwable t) {

                    }
                });

    }

    // post请求：上传多文件(图片)，场景1：使用MultiPartBody.Part
    // 一个file对应一个请求体，一个请求体对应一个part
    @Test
    public void test3() {
        List<File> files = getFiles();

        Retrofit retrofit = createRetrofit();
        // 封装file为part
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        files.forEach(file -> {
            // 创建请求体，包含文件和文件类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            // part通常要求一个"file"标记和文件名(后台不一定用得上)，最后封装请求体
            MultipartBody.Part part = MultipartBody.Part.createFormData("file",
                    file.getName(),
                    requestBody);
            parts.add(part);
        });

        retrofit.create(UploadService.class)
                .getUploadCallWithPart(parts)
                .enqueue(new Callback<UploadBean>() {
                    @Override
                    public void onResponse(Call<UploadBean> call, Response<UploadBean> response) {

                    }

                    @Override
                    public void onFailure(Call<UploadBean> call, Throwable t) {

                    }
                });
    }

    // post请求：上传多文件(图片)，场景1：使用MultiPartBody
    // 一个file对应一个请求体，构建者添加全部请求体并构建MultiPartBody
    @Test
    public void test4() {
        List<File> files = getFiles();

        Retrofit retrofit = createRetrofit();

        MultipartBody.Builder builder = new MultipartBody.Builder();

        files.forEach(file -> {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        });

        MultipartBody multipartBody = builder.setType(MultipartBody.FORM).build();

        retrofit.create(UploadService.class)
                .getUploadCallWithBody(multipartBody)
                .enqueue(new Callback<UploadBean>() {
                    @Override
                    public void onResponse(Call<UploadBean> call, Response<UploadBean> response) {

                    }

                    @Override
                    public void onFailure(Call<UploadBean> call, Throwable t) {

                    }
                });
    }

    private List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            File file = new File("xxx");
            files.add(file);
        }
        return files;
    }

    // 自定义OkHttpClient
    @Test
    public void test5() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.qubaobei.com/")
                .client(okHttpClient)// 自定义OkHttpClient
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // ...
    }

    // 配合RxJava2
    @Test
    public void test6() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.qubaobei.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 将Call转换为Observable
                .build();

        retrofit.create(DishService.class)
                .getDishObservable(1)// 获取接口返回的Observable
                .map(DishBean::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(datas -> {
                            datas.forEach(data -> {
                                MyLog.e(data.getTitle());
                            });
                        },
                        throwable -> {

                        });
    }

}
