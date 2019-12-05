package jack.com.servicekeep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jack.com.servicekeep.model.TimeRequest;


public class AdsRequestDatabase {

    private SQLiteDatabase db;
    private AdsRequestHelper setupInstallHelper;

    public AdsRequestDatabase(Context context) {
        AdsRequestHelper openHelper = new AdsRequestHelper(context, AdsRequestHelper.DATABASE_NAME, null, AdsRequestHelper.DATABASE_VERSION);
        this.setupInstallHelper = openHelper;
        db = openHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
    }

    public int addConfigTime(TimeRequest timeRequest) {
        ContentValues values = new ContentValues();
        values.put(AdsRequestHelper.KEY_ID, timeRequest.id);
        values.put(AdsRequestHelper.KEY_TIME, timeRequest.time);
        values.put(AdsRequestHelper.KEY_ACTIVE, timeRequest.active);
        if (!db.isOpen()) {
            db = setupInstallHelper.getWritableDatabase();
        }
        long ID = db.insert(AdsRequestHelper.TABLE_ADS_REQUEST_APP, null, values);
        db.close();
        return (int) ID;
    }

    public int updateAlarm(TimeRequest timeRequest) {
        ContentValues values = new ContentValues();
        values.put(AdsRequestHelper.KEY_TIME, timeRequest.time);
        values.put(AdsRequestHelper.KEY_ACTIVE, timeRequest.active);
        if (!db.isOpen()) {
            db = setupInstallHelper.getWritableDatabase();
        }
        db.enableWriteAheadLogging();
        db.beginTransactionNonExclusive();
        try {
            //do some insertions or whatever you need
            db.update(AdsRequestHelper.TABLE_ADS_REQUEST_APP, values, AdsRequestHelper.KEY_ID + "=?",
                    new String[]{String.valueOf(timeRequest.id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return -1;
    }

    public boolean findItemWithTitle(int id) {
        if (!db.isOpen()) {
            db = setupInstallHelper.getWritableDatabase();
        }

        Cursor cursor = db.query(AdsRequestHelper.TABLE_ADS_REQUEST_APP, null, AdsRequestHelper.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        TimeRequest timeConfigActive = null;
        if (cursor != null && cursor.moveToFirst()) {
            timeConfigActive = new TimeRequest();
            timeConfigActive.id = cursor.getInt(0);
            timeConfigActive.time = cursor.getLong(1);
            timeConfigActive.active = cursor.getInt(2);
            cursor.close();

        }
        return timeConfigActive != null;

    }

    public TimeRequest getTimeRequest(int id) {
        if (!db.isOpen()) {
            db = setupInstallHelper.getWritableDatabase();
        }

        Cursor cursor = db.query(AdsRequestHelper.TABLE_ADS_REQUEST_APP, null, AdsRequestHelper.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        TimeRequest timeConfigActive = null;
        if (cursor != null && cursor.moveToFirst()) {
            timeConfigActive = new TimeRequest();
            timeConfigActive.id = cursor.getInt(0);
            timeConfigActive.time = cursor.getLong(1);
            timeConfigActive.active = cursor.getInt(2);
            cursor.close();

        }
        return timeConfigActive;

    }


}
