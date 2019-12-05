package com.vmb.chinh_sua_anh.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.chinh_sua_anh.Interface.IOnGetFont;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

// File download process from URL
public class DownloadFileFromURL extends AsyncTask {

    private Context context;
    private IOnGetFont listener;
    private String baseURL;
    private List<String> fontNames;

    public DownloadFileFromURL(Context context, IOnGetFont listener, String baseURL, List<String> fontNames) {
        this.context = context;
        this.listener = listener;
        this.baseURL = baseURL;
        this.fontNames = fontNames;
    }

    private AlertDialog dialog;
    private TextView lbl_title;
    private NumberProgressBar progressBar;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (context == null)
            return;

        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(R.layout.dialog_dowloading_font, null);
        lbl_title = alertLayout.findViewById(R.id.lbl_title);
        progressBar = alertLayout.findViewById(R.id.number_progress_bar);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        dialog = alert.create();
        //dialog.setCancelable(false);
        dialog.show();
        dialog.setCancelable(false);
    }

    private int currentFile;

    @Override
    protected Object doInBackground(Object[] objects) {
        String TAG = "downloadFont()";
        int count;
        for (int i = 0; i < fontNames.size(); i++) {
            currentFile = i + 1;
            try {
                URL url = new URL(String.format(baseURL, new Object[]{fontNames.get(i)}));
                Log.i(TAG, "url = " + url);
                URLConnection conection = url.openConnection();
                conection.connect();

                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                //OutputStream output = new FileOutputStream("/mnt/sdcard/" + Config.Directory.SAVE_DIRECTORY + "/" + s);
                OutputStream output = context.openFileOutput(fontNames.get(i), Activity.MODE_PRIVATE);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (context == null)
            return;

        lbl_title.setText(context.getString(R.string.downloading_font) + " " + currentFile + "/" + fontNames.size());
        progressBar.setProgress((int) values[0]);
    }

    @Override
    protected void onPostExecute(Object object) {
        // Display the custom font after the File was downloaded !
        if (listener != null)
            listener.onGetFont(fontNames);

        if (context == null)
            return;

        dialog.dismiss();
        ToastUtil.longToast(context, context.getString(R.string.font_downloaded));
    }
}