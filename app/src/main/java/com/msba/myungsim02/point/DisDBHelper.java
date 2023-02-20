package com.msba.myungsim02.point;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DisDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    SQLiteDatabase db;


    public DisDBHelper(Context context) {
        super(context,"dischargedb",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String disSQL ="create table tb_discharge" +
                "(_id integer primary key autoincrement,"+
                "time String)";
        db.execSQL(disSQL);

        String hrSQL ="create table tb_restinghr" +
                "(_id integer primary key,"+
                "restinghr integer)";
        db.execSQL(hrSQL);

        String feedbackSQL ="create table tb_feedback" +
                "(_id integer primary key,"+
                "feedback String)";
        db.execSQL(feedbackSQL);

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_discharge");
            db.execSQL("drop table tb_restinghr");
            db.execSQL("drop table tb_feedback");
            onCreate(db);
        }
    }

}
