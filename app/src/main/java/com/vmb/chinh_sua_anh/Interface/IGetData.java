package com.vmb.chinh_sua_anh.Interface;

import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.handler.Font;
import com.vmb.chinh_sua_anh.handler.Image;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetData {

    /*@GET(Config.UrlData.URL_DATA_BACKGROUND)
    Call<Background> getListBackground();

    @GET(Config.UrlData.URL_DATA_STICKER)
    Call<Icon> getListSticker();*/

    @GET(Config.UrlData.URL_IMAGES)
    Call<Image> getListImage();

    @GET(Config.UrlData.URL_FONTS)
    Call<Font> getListFont();
}