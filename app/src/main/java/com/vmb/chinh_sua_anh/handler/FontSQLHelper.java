package com.vmb.chinh_sua_anh.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FontSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "font_names.db";
    private static final int SCHEMA_VERSION = 1;

    public FontSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE font_names (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // version exists
    }

    public Cursor getAll() {
        return (getReadableDatabase().rawQuery("SELECT * FROM font_names", null));
    }

    public Cursor getData(String name) {
        return (getReadableDatabase().rawQuery
                ("SELECT * FROM font_names WHERE" + " name = '" + name + "'", null));
    }

    public void insert(String name) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        getWritableDatabase().insert("font_names", null, cv);
    }

    public void delete(String name) {
        String[] args = {name};
        getWritableDatabase().delete("font_names", "name = ?", args);
    }

    public String getName(Cursor c) {
        return (c.getString(1));
    }
}