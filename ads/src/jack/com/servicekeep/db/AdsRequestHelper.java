package jack.com.servicekeep.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdsRequestHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = " AdsRequestDatabase";
    public static final String TABLE_ADS_REQUEST_APP = " AdsRequestTable";
    public static final String KEY_ID = "id";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_TIME = "time";

    String CREATE_ADS_REQUEST_TABLE = "CREATE TABLE" + TABLE_ADS_REQUEST_APP +
            "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TIME + " LONG,"
            + KEY_ACTIVE + " INTEGER"
            + ")";


    public AdsRequestHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADS_REQUEST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS_REQUEST_APP);
        onCreate(db);
    }
}
