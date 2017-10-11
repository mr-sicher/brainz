package de.sicher.brainz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "brainz.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TRACKING_TABLE_NAME = "tracking";
    public static final String TRACKING_COLUMN_ID = "_id";
    public static final String TRACKING_COLUMN_NAME = "name";
    public static final String TRACKING_COLUMN_DATE = "date";
    public static final String TRACKING_COLUMN_TIME = "time";
    public static final String TRACKING_COLUMN_STRENGTH = "strength";

//    public static final String DATE_PATTERN = "HH:mm:ss.SSSZ dd.MM.yyyy";


    public Context context;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ")
                .append(TRACKING_TABLE_NAME)
                .append("(").append(TRACKING_COLUMN_ID).append(" INTEGER PRIMARY KEY, ")
                .append(TRACKING_COLUMN_NAME).append(" TEXT, ")
                .append(TRACKING_COLUMN_DATE).append(" TEXT, ")
                .append(TRACKING_COLUMN_TIME).append(" TEXT, ")
                .append(TRACKING_COLUMN_STRENGTH).append(" INTEGER)");
        Log.d(context.getString(R.string.APP_TAG) + "_" + getClass().getName(), "raw sql statement: " + query.toString());
        db.execSQL(query.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TRACKING_TABLE_NAME;
        Log.d(context.getString(R.string.APP_TAG) + "_" + getClass().getName(), "raw sql statement: " + query);
        db.execSQL(query);
        onCreate(db);
    }


    private ContentValues makeContentValues(String name, String date, String time, int strength){
        ContentValues values = new ContentValues();

        values.put(TRACKING_COLUMN_NAME, name);
        values.put(TRACKING_COLUMN_DATE, date);
        values.put(TRACKING_COLUMN_TIME, time);
        values.put(TRACKING_COLUMN_STRENGTH, strength);

        return values;
    }

    public boolean insertData(String name, String date, String time, int strength){
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TRACKING_TABLE_NAME, null, makeContentValues(name, date, time, strength));

        return true;
    }

    public long numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TRACKING_TABLE_NAME);
    }

    public boolean updateData(Integer id, String name, String date, String time, int strength){
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TRACKING_TABLE_NAME, makeContentValues(name, date, time, strength),
                TRACKING_COLUMN_ID + " = ?", new String[] {Integer.toString(id)});

        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ")
                .append(TRACKING_TABLE_NAME)
                .append(" WHERE ")
                .append(TRACKING_COLUMN_ID)
                .append(" =?");

        Log.d(context.getString(R.string.APP_TAG) + "_" + getClass().getName(), "raw sql statement: " + query.toString());
        return db.rawQuery(query.toString(), new String[] {Integer.toString(id)});
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ")
                .append(TRACKING_TABLE_NAME);

        Log.d(context.getString(R.string.APP_TAG) + "_" + getClass().getName(), "raw sql statement: " + query.toString());
        return db.rawQuery(query.toString(), null);

    }

    public boolean deleteData(int id){
        //TODO
        Toast.makeText(context, "Lösche Daten " + id, Toast.LENGTH_LONG).show();
        return true;
    }
}