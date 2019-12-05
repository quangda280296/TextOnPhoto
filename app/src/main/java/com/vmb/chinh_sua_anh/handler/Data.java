package com.vmb.chinh_sua_anh.handler;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.model.FileFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private static Data data;

    private boolean offlineMode = false;

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    private List<String> listFontNames = new ArrayList<>();
    private List<Typeface> listFonts = new ArrayList<>();
    private List<FileFolder> alFolders = new ArrayList<>();
    private boolean isScanDone = true;

    private ScanPhotoListener listener;

    public interface ScanPhotoListener {
        void onInsert(int position, FileFolder fileFolder);

        void onChange(int position, FileFolder fileFolder);

        void onDone();
    }

    public ScanPhotoListener getListener() {
        return listener;
    }

    public void setListener(ScanPhotoListener listener) {
        this.listener = listener;
    }

    private String Filename;
    private int compare = 1920;

    private boolean isSendTime;
    private int typeSendTime = 1;
    private long start_time_load_img;

    public static Data getInstance() {
        if (data == null) {
            synchronized (Data.class) {
                data = new Data();
            }
        }
        return data;
    }

    public void destroy() {
        this.data = null;
    }

    public List<String> getListFontNames() {
        return listFontNames;
    }

    public List<Typeface> getListFonts(Context context) {
        if (listFonts.size() == 0) {
            addOriginalFont(context);
            addFontFromDownloadedFile(context);
        }
        return listFonts;
    }

    public void addFont(Typeface typeface) {
        listFonts.add(typeface);
    }

    public void addOriginalFont(Context context) {
        try {
            String origins[] = context.getAssets().list("fonts/Original Fonts");
            String foots[] = context.getAssets().list("fonts/Fonts With Foot");
            String withoutFoots[] = context.getAssets().list("fonts/Fonts Without Foot");
            String hands[] = context.getAssets().list("fonts/Hand Fonts");

            Log.i("test_data_font", "origins.length = " + origins.length);
            Log.i("test_data_font", "foots.length = " + foots.length);
            Log.i("test_data_font", "withoutFoots.length = " + withoutFoots.length);
            Log.i("test_data_font", "hands.length = " + hands.length);

            for (int i = 0; i < origins.length; i++) {
                String test[] = context.getAssets().list("fonts/Original Fonts/" + origins[i]);
                Log.i("test_data_font", "origins.test.length = " + test.length);
                for (int j = 0; j < test.length; j++) {
                    Log.i("test_data_font", test[j]);
                    listFontNames.add(test[j]);
                    this.listFonts.add(Typeface.createFromAsset(context.getAssets(), "fonts/Original Fonts/" + origins[i] + "/" + test[j]));
                }
            }

            for (int i = 0; i < foots.length; i++) {
                String test[] = context.getAssets().list("fonts/Fonts With Foot/" + foots[i]);
                Log.i("test_data_font", "foots.test.length = " + test.length);
                for (int j = 0; j < test.length; j++) {
                    Log.i("test_data_font", test[j]);
                    listFontNames.add(test[j]);
                    this.listFonts.add(Typeface.createFromAsset(context.getAssets(), "fonts/Fonts With Foot/" + foots[i] + "/" + test[j]));
                }
            }

            for (int i = 0; i < withoutFoots.length; i++) {
                String test[] = context.getAssets().list("fonts/Fonts Without Foot/" + withoutFoots[i]);
                Log.i("test_data_font", "withoutFoots.test.length = " + test.length);
                for (int j = 0; j < test.length; j++) {
                    Log.i("test_data_font", test[j]);
                    listFontNames.add(test[j]);
                    this.listFonts.add(Typeface.createFromAsset(context.getAssets(), "fonts/Fonts Without Foot/" + withoutFoots[i] + "/" + test[j]));
                }
            }

            for (int i = 0; i < hands.length; i++) {
                String test[] = context.getAssets().list("fonts/Hand Fonts/" + hands[i]);
                Log.i("test_data_font", "hands.test.length = " + test.length);
                for (int j = 0; j < test.length; j++) {
                    Log.i("test_data_font", test[j]);
                    listFontNames.add(test[j]);
                    this.listFonts.add(Typeface.createFromAsset(context.getAssets(), "fonts/Hand Fonts/" + hands[i] + "/" + test[j]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFontFromDownloadedFile(Context context) {
        /*String listFontName = getListFontName(context);
        if (TextUtils.isEmpty(listFontName))
            return;

        String fontNames[] = listFontName.split("]");*/
        List<String> fontNames = new ArrayList<>();

        FontSQLHelper helper = new FontSQLHelper(context);
        Cursor cursor = helper.getAll();
        if (cursor.getCount() <= 0)
            return;

        cursor.moveToFirst();
        do {
            fontNames.add(helper.getName(cursor));
        } while (cursor.moveToNext());

        String pathFolder = Environment.getExternalStorageDirectory() + "/" + Config.Directory.SAVE_DIRECTORY + "/";
        File create_folder = new File(pathFolder);
        if (!create_folder.exists())
            create_folder.mkdirs();

        for (String nameFile : fontNames) {
            Log.i("test_data_font", nameFile);
            File file = new File("/mnt/sdcard/" + Config.Directory.SAVE_DIRECTORY + "/" + nameFile);
            if (!file.exists()) {
                try {
                    FileInputStream fin = context.openFileInput(nameFile);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = fin.read(bytes)) != -1)
                        outputStream.write(bytes, 0, read);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                // Display font from Storage
                Typeface typeFace = Typeface.createFromFile(file);
                if (typeFace != null) {
                    this.listFonts.add(typeFace);
                    this.listFontNames.add(nameFile);
                }
            } catch (Exception e) {
                helper.delete(nameFile);
            }
        }
    }

    public void addFontJustDownloaded(Context context, List<String> fontNames) {
        if (listFonts.size() == 0)
            addOriginalFont(context);

        FontSQLHelper helper = new FontSQLHelper(context);

        String pathFolder = Environment.getExternalStorageDirectory() + "/" + Config.Directory.SAVE_DIRECTORY + "/";
        File create_folder = new File(pathFolder);
        if (!create_folder.exists())
            create_folder.mkdirs();

        for (String nameFile : fontNames) {
            Log.i("test_data_font", nameFile);
            File file = new File("/mnt/sdcard/" + Config.Directory.SAVE_DIRECTORY + "/" + nameFile);
            if (!file.exists()) {
                try {
                    FileInputStream fin = context.openFileInput(nameFile);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = fin.read(bytes)) != -1)
                        outputStream.write(bytes, 0, read);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                // Display font from Storage
                Typeface typeFace = Typeface.createFromFile(file);
                if (typeFace != null) {
                    this.listFonts.add(typeFace);
                    this.listFontNames.add(nameFile);
                }
            } catch (Exception e) {
                helper.delete(nameFile);
            }
        }
    }

    /*public static String getListFontName(Context context) {
        String TAG = "getListFontName";
        Log.i(TAG, "getListFontName()");

        // Read from a file
        try {
            FileInputStream fin = context.openFileInput(Config.FileName.FILE_FONTS_NAME);
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
            return readString;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
        return null;
    }*/

    public List<FileFolder> getAlFolders() {
        return alFolders;
    }

    public boolean isScanDone() {
        return isScanDone;
    }

    public void setScanDone(boolean scanDone) {
        isScanDone = scanDone;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public int getCompare() {
        return compare;
    }

    public boolean isSendTime() {
        return isSendTime;
    }

    public void setSendTime(boolean sendTime) {
        isSendTime = sendTime;
    }

    public int getTypeSendTime() {
        return typeSendTime;
    }

    public void setTypeSendTime(int typeSendTime) {
        this.typeSendTime = typeSendTime;
    }

    public long getStart_time_load_img() {
        return start_time_load_img;
    }

    public void setStart_time_load_img(long start_time_load_img) {
        this.start_time_load_img = start_time_load_img;
    }

    public void scanPhoto(Context context) {
        this.isScanDone = false;
        List<String> parentFolders = new ArrayList();
        alFolders = new ArrayList<>();
        alFolders.clear();

        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_MODIFIED};
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        if (query != null && query.getCount() > 0) {
            int columnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA);
            if (columnIndex != -1 && query.moveToFirst()) {
                do {
                    try {
                        String path = query.getString(columnIndex);
                        File file = new File(path);

                        if (file.exists() && file.length() > 0) {
                            String pathParentFile = file.getParent();
                            if (!isFolderDuplicated(pathParentFile, parentFolders)) {
                                parentFolders.add(pathParentFile);
                                FileFolder fileFolder = new FileFolder(file.getParentFile().getName(), pathParentFile, path);
                                alFolders.add(fileFolder);
                                if (alFolders.size() == 1)
                                    if (listener != null)
                                        listener.onInsert(alFolders.size() - 1, fileFolder);
                            }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } while (query.moveToNext());
                query.close();

                /*for (int i = 0; i < alFolders.size(); i++) {
                    FileFolder fileFolder = alFolders.get(i);
                    if (fileFolder != null) {
                        List<File> files = Utils.getPhotoInDirectory(fileFolder.getPathFolder());
                        if (files != null && files.size() > 0) {
                            fileFolder.setListPhoto(files);
                            if (listener != null)
                                listener.onChange(i, fileFolder);
                        }
                    }
                }*/

                /*// sort
                if (alFolders.size() > 0) {
                    Comparator comparator = new Compare();
                    sortList(alFolders, comparator);
                }*/
            }
        }

        this.isScanDone = true;
        if (listener != null)
            listener.onDone();
    }

    /*private boolean checkFile(File file) {
        boolean z = false;
        if (file == null) {
            return false;
        }
        if (file.isFile()) {
            String name = file.getName();
            if (!name.startsWith(".")) {
                if (file.length() != 0) {
                    String[] tail = Config.FORMAT_IMG;
                    int length = tail.length;
                    int i = 0;
                    while (i < length) {
                        if (!name.endsWith(tail[i])) {
                            i++;
                        }
                    }
                }
            }
            return z;
        }
        z = true;
        return z;
    }*/

    public boolean isFolderDuplicated(String pathParentFile, List<String> arrayList) {
        if (arrayList.contains(pathParentFile)) {
            return true;
        }
        return false;
    }

    /*public <T> void sortList(List<T> list, Comparator<? super T> comparator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(comparator);
        } else {
            Collections.sort(list, comparator);
        }
    }

    public class Compare implements Comparator<FileFolder> {

        @Override
        public int compare(FileFolder item_1, FileFolder item_2) {
            String name_1 = item_1.getName();
            String name_2 = item_2.getName();

            if (name_1.length() >= name_2.length()) {
                for (int i = 0; i < name_2.length(); i++) {
                    if (name_1.charAt(i) > name_2.charAt(i))
                        return 1;

                    if (name_1.charAt(i) < name_2.charAt(i))
                        return -1;
                }
                return 0;

            } else {
                for (int i = 0; i < name_1.length(); i++) {
                    if (name_1.charAt(i) > name_2.charAt(i))
                        return 1;

                    if (name_1.charAt(i) < name_2.charAt(i))
                        return -1;
                }
                return 0;
            }
        }
    }*/
}