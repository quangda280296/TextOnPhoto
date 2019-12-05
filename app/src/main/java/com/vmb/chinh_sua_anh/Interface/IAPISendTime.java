package com.vmb.chinh_sua_anh.Interface;

import com.vmb.chinh_sua_anh.Config;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAPISendTime {

    @POST(Config.URL_API_LOG_TIME_LOAD_IMG)
    Call<ResponseBody> logTime(@Body RequestBody body);
}