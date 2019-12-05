package com.vmb.chinh_sua_anh.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.util.PermissionUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.MainActivity;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.handler.Font;
import com.vmb.chinh_sua_anh.handler.Image;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static int getLineCount(String text) {
        return text.split("[\n|\r]").length;
    }

    public static Bitmap scaleDownImage(Bitmap sourceBitmap) {
        int compare = Data.getInstance().getCompare();

        float decrease = 1.0f;
        if (sourceBitmap.getWidth() >= sourceBitmap.getHeight()) {
            if (sourceBitmap.getWidth() > compare)
                decrease = (float) sourceBitmap.getWidth() / (float) compare;
        } else {
            if (sourceBitmap.getHeight() > compare)
                decrease = (float) sourceBitmap.getHeight() / (float) compare;
        }

        return Bitmap.createScaledBitmap(sourceBitmap,
                Math.round((float) sourceBitmap.getWidth() / decrease),
                Math.round((float) sourceBitmap.getHeight() / decrease), true);
    }

    /*public static Bitmap fixOrientation(Bitmap sourceBitmap, String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0) {
                matrix.preRotate(rotationInDegrees);
            }
            Bitmap bitmap = Bitmap.createBitmap
                    (sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static void startCamera(Activity activity) {
        if (PermissionUtil.requestPermission(activity, Config.RequestCode.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider",
                    getCameraFile(activity));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, photoUri));

            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, Config.RequestCode.REQUEST_CODE_CAMERA);
        }
    }

    public static File getCameraFile(Context context) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        Calendar myCalendar = Calendar.getInstance();
        String date = df.format(myCalendar.getTime());
        String fileName = Config.FileName.FILE_NAME_TEMP_PHOTO + "_" + date + ".jpg";
        Data.getInstance().setFilename(fileName);

        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        return new File(dir, fileName);
    }

    public static List<File> getPhotoInDirectory(String path) {
        List<File> allFiles = null;
        File folder = new File(path);
        if (folder.exists()) {
            try {
                allFiles = new LinkedList(Arrays.asList(folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".JPG")
                                || name.endsWith(".JPEG")
                                || name.endsWith(".PNG")
                                || name.endsWith(".jpg")
                                || name.endsWith(".jpeg")
                                || name.endsWith(".png"));
                    }
                })));

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("getImageInDirectory()", "Exception e: " + e.getMessage());
            }
        }
        return allFiles;
    }

    public static List<File> getImageInDirectory(String path) {
        List<File> allFiles = null;
        File folder = new File(path);
        if (folder.exists()) {
            try {
                allFiles = new LinkedList(Arrays.asList(folder.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return ((file.getPath().endsWith(".JPG")
                                || file.getPath().endsWith(".JPEG")
                                || file.getPath().endsWith(".PNG")
                                || file.getPath().endsWith(".jpg")
                                || file.getPath().endsWith(".jpeg")
                                || file.getPath().endsWith(".png")) && file.length() > 0);
                    }
                })));

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("getImageInDirectory()", "Exception e: " + e.getMessage());
            }
        }
        return allFiles;
    }

    public static void setTag(Context context, View v) {
        if (v instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) v;
            for (int i = 0; i < parentView.getChildCount(); i++) {
                View view = parentView.getChildAt(i);
                view.setTag(context.getString(R.string.root));
            }
        }
    }

    /*public static void setBackground(MainActivity mainActivity, int id, int color) {
        ViewGroup parent = mainActivity.findViewById(id);
        setColor(mainActivity, parent, color);
    }

    public static void setColor(Context context, ViewGroup viewGroup, int color) {
        if (viewGroup.getChildCount() > 0) {
            View v = viewGroup.getChildAt(0);
            if (v instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) v;
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        if (view.getTag() == null)
                            setColor(context, (ViewGroup) view, color);
                        else if (view.getTag().toString().equalsIgnoreCase(context.getString(R.string.root)))
                            view.setBackgroundColor(color);
                    }
                }
            }
        }
    }*/

    public static void setBackgroundBlack(MainActivity mainActivity, int id, int color) {
        ViewGroup parent = mainActivity.findViewById(id);
        setColorBlack(mainActivity, parent, color);
    }

    public static void setColorBlack(Context context, ViewGroup viewGroup, int color) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                if (view.getTag() == null)
                    setColorBlack(context, (ViewGroup) view, color);
                else if (view.getTag().toString().equalsIgnoreCase(context.getString(R.string.root)))
                    view.setBackgroundColor(color);
            }
        }
    }

    public static void turnOffBrushMode() {
        if (PhotoHandler.getInstance().getPhotoEditor() != null)
            PhotoHandler.getInstance().getPhotoEditor().setBrushDrawingMode(false);

        PhotoHandler.getInstance().setEraserMode(false);
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        return (int) px;
    }

    public static int convertPixelToDP(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px * DisplayMetrics.DENSITY_DEFAULT / metrics.densityDpi;
        return (int) dp;
    }

    public static boolean isFullHD(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int widthDP = convertPixelToDP(widthPixels, context);
        if (widthDP < 480)
            return false;
        else
            return true;
    }

    /*public static void caculate(Context context) {
        String TAG = "caculate()";

        // set size image_view in grid_layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        int widthPixels = displayMetrics.widthPixels;
        Log.i(TAG, "widthPixels = " + widthPixels);

        int spacing_grid_Pixels = context.getResources().getDimensionPixelSize(R.dimen.spacing_grid);
        Log.i(TAG, "spacing_grid_Pixels = " + spacing_grid_Pixels);

        int sizePixels = (widthPixels - 4 * spacing_grid_Pixels) / 3;
        Log.i(TAG, "sizePixels = " + sizePixels);

        int tab_2_title_layout_size_Pixels = context.getResources().getDimensionPixelSize(R.dimen.tab_2_title_layout_size);
        Log.i(TAG, "tab_2_title_layout_size_Pixels = " + tab_2_title_layout_size_Pixels);
        int sizeSmallPixels = (widthPixels - tab_2_title_layout_size_Pixels - 4 * spacing_grid_Pixels / 2) / 3;
        Log.i(TAG, "sizeSmallPixels = " + sizeSmallPixels);

        Data.getInstance().setSize(sizePixels);
        Data.getInstance().setSizeSmall(sizeSmallPixels);

        if (Utils.isFullHD(context))
            Data.getInstance().setCompare(1920);
        else
            Data.getInstance().setCompare(1280);
    }*/

    public static boolean initImagesWhileOffline(Context context) {
        String TAG = "initImagesWhileOffline";
        Log.i(TAG, "init()");

        boolean success = false;
        Data.getInstance().setOfflineMode(true);

        // Read from a file
        try {
            FileInputStream fin = context.openFileInput(Config.FileName.FILE_IMAGES_DATA);
            InputStreamReader isr = new InputStreamReader(fin);

            char[] inputBuffer = new char[10];
            int charRead;
            String readString = "";

            //---Start reading from file---
            while ((charRead = isr.read(inputBuffer)) > 0) {
                readString += String.copyValueOf(inputBuffer, 0, charRead);
                inputBuffer = new char[10];
            }

            Log.i(TAG, "readString = " + readString);

            Gson gson = new Gson();
            Image config = gson.fromJson(readString, Image.class);
            Image.getInstance().setData(config.getData());
            success = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
        return success;
    }

    public static boolean initFontsWhileOffline(Context context) {
        String TAG = "initFontsWhileOffline";
        Log.i(TAG, "init()");

        boolean success = false;
        Data.getInstance().setOfflineMode(true);

        // Read from a file
        try {
            FileInputStream fin = context.openFileInput(Config.FileName.FILE_FONTS_DATA);
            InputStreamReader isr = new InputStreamReader(fin);

            char[] inputBuffer = new char[10];
            int charRead;
            String readString = "";

            //---Start reading from file---
            while ((charRead = isr.read(inputBuffer)) > 0) {
                readString += String.copyValueOf(inputBuffer, 0, charRead);
                inputBuffer = new char[10];
            }

            Log.i(TAG, "readString = " + readString);

            Gson gson = new Gson();
            Font font = gson.fromJson(readString, Font.class);
            Font.getInstance().setInstance(font);
            success = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
        return success;
    }

    public static Image handleVersionImages(Activity activity) {
        String TAG = "handleVersionImages";

        // Read from a file
        try {
            FileInputStream fin = activity.openFileInput(Config.FileName.FILE_IMAGES_DATA);
            InputStreamReader isr = new InputStreamReader(fin);

            char[] inputBuffer = new char[10];
            int charRead;
            String readString = "";

            //---Start reading from file---
            while ((charRead = isr.read(inputBuffer)) > 0) {
                readString += String.copyValueOf(inputBuffer, 0, charRead);
                inputBuffer = new char[10];
            }

            Log.i(TAG, "readString = " + readString);

            Gson gson = new Gson();
            Image config = gson.fromJson(readString, Image.class);
            return config;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
        return null;
    }

    public static int rand(int min, int max) {
        try {
            Random rn = new Random();
            int range = max - min + 1;
            int randomNum = min + rn.nextInt(range);
            return randomNum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void addPhotoToGallery(Context context, String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        context.sendBroadcast(intent);
    }
}