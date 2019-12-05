package com.vmb.chinh_sua_anh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.vmb.ads_in_app.util.NotificationPushOpenStore;
import com.vmb.chinh_sua_anh.Interface.IOnGetBitmap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadBitmapFromUrl extends AsyncTask<String, Integer, Bitmap> {

    private Context context;
    private IOnGetBitmap listener;

    public LoadBitmapFromUrl(Context context, IOnGetBitmap listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            //Tiến hành tạo đối tượng URL
            URL urlConnection = new URL(urls[0]);

            //Mở kết nối
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();

            //Đọc dữ liệu
            InputStream input = connection.getInputStream();

            //Convert
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Log.i("LoadBitmapFromURL", "onPostExecute");

        if(listener != null)
            listener.onGetBitmap(bitmap);
    }
}