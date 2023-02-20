package com.msba.myungsim02.exercise;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ExdaDatabase {
    private static final String TAG = "crkim";

    private static ExdaDatabase database;
    public static String TABLE_NAME= "exda";
    public static String DATABASE_NAME ="exda.db";
    public static int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private ExdaDatabase(Context context) {
        this.context =context;
    }

    public static ExdaDatabase getInstance(Context context){
        if (database == null){
            database = new ExdaDatabase(context);
        }
        return database;
    }

    public boolean open() {
        println("opening database [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;

    }

    public void close() {
        println("closing dtabase [" + DATABASE_NAME + "].");
        db.close();

        database = null;
    }

    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQL, null);
            println("cursor count : " + cursor.getCount());
        } catch (Exception ex) {
            Log.i(TAG, "Exception in exectueQuery", ex);
        }

        return cursor;
    }

    // 데이터베이스 명령문
    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.i(TAG, "SQL: " + SQL);
            db.execSQL(SQL);
        } catch (Exception ex) {
            Log.i(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {//앱이 설치 될때 한번만 실행 됨. getWritableDatabase() 를통해서 접근한것임
            println("creating table [" + TABLE_NAME + "].");

            String DROP_SQL = "drop table if exists " + TABLE_NAME;
            try {
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.i(TAG, "Exception in DROP_SQL", ex);
            }

            //create table
            String CREATE_SQL = "create table " + TABLE_NAME + "("
                    + "  _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "DATE string,"
                    + "TYPE string,"
                    + "HOUR integer,"
                    + "MIN integer,"
                    + "RPE integer,"
                    + "DATE2 long,"
                    + "CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            //
            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.i(TAG, "Exception in CREAT_SQL", ex);
            }

            //insertRecord(db,0,0,0, 0,null);

           /*String CREATE_INDEX_SQL = "create index " + TABLE_DAILY + "_IDX ON " + TABLE_DAILY + "("
                   + "CREATE_DATE"
                   + ")";
           try {
               db.execSQL(CREATE_INDEX_SQL);
           } catch (Exception ex) {
               Log.i(TAG, "Exception in CREATE_INDEX_SQL", ex);
           }

            */
        }

        public void onOpen(SQLiteDatabase db) {//getWritableDatabase() 를통해서 접근한것이지만,onCreate()접근안하고 onopen을 통하여 db를 연다
            println("opened database [" + DATABASE_NAME + "].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");

            if(newVersion==DATABASE_VERSION){
                db.execSQL("drop table " + TABLE_NAME);
                onCreate(db);
            }


        }


        private void insertRecord(SQLiteDatabase db,String type, int hour, int min, int rpe, long date2, String date){
            try{
                db.execSQL("insert into " + TABLE_NAME + "(TYPE, HOUR, MIN, RPE, DATE2, DATE) values ('" + type + "', '" + hour + "', '" + min + "', '" + rpe + "', '"  + date2 + "', '"+ date + "');");
            }catch (Exception ex){
                Log.i(TAG,"Exception in execution insert SQL.",ex);
            }
        }
    }

    public void insertRecord(String type, int hour, int min, int rpe, long date2, String date) {
        try{
            db.execSQL("insert into " + TABLE_NAME + "(TYPE, HOUR, MIN, RPE, DATE2, DATE) values ('" + type + "', '" + hour + "', '" + min + "', '" + rpe + "', '"  + date2 + "', '"+ date + "');");
        }catch (Exception ex){
            Log.i(TAG,"Exception in execution insert SQL.",ex);
        }
    }

    public ArrayList<ExdaItem> selectAll()
    {
        ArrayList<ExdaItem>result = new ArrayList<ExdaItem>();
        try{
            Cursor cursor = db.rawQuery("select TYPE, HOUR, MIN, RPE, DATE2, DATE from " + TABLE_NAME,null);
            for(int i = 0; i< cursor.getCount(); i++){
                cursor.moveToNext();
                String type = cursor.getString(0);
                int hour = cursor.getInt(1);
                int min = cursor.getInt(2);
                int rpe = cursor.getInt(3);
                long date2 = cursor.getLong(4);
                String date = cursor.getString(5);


                ExdaItem info = new ExdaItem(type,hour,min,rpe,date2,date);
                result.add(info);
            }

        }catch(Exception ex) {
            Log.i(TAG,"Exception in executing insert SQL.", ex);
        }
        return result;
    }


    private void println(String msg) {
        Log.i(TAG, msg);
    }
}





