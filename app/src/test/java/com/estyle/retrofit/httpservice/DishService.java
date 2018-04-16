package com.estyle.retrofit.httpservice;

import com.estyle.retrofit.bean.DishBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// get请求
public interface DishService {

    @GET("ios/cf/dish_list.php?stage_id=1&limit=10")// 固定部分
    Call<DishBean> getDishCall(@Query("page") int page);// 变化参数

    // Restful风格的url
//    @GET("ios/cf/dish_list.php?stage_id/1/limit/10/page/${page}")
//    Call<DishBean> getDishCall(@Path("page") int page);

    // 使用RxJava
    @GET("ios/cf/dish_list.php?stage_id=1&limit=10")
    Observable<DishBean> getDishObservable(@Query("page") int page);

}
