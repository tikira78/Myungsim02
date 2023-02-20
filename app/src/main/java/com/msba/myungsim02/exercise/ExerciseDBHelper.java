package com.msba.myungsim02.exercise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;


    public ExerciseDBHelper(Context context) {
        super(context,"kind.db",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String kindSQL ="create table tb_kind" +
                "(_id integer primary key,"+
                "kind)";

        String borgSQL ="create table tb_borg" +
                "(_id integer primary key,"+
                "borg integer)";

        String timeSQL ="create table tb_time" +
                "(_id integer primary key,"+
                "starttime long,"+
                "pausetime long,"+
                "restarttime long,"+
                "endtime long)";

        String pausetimeSQL ="create table tb_pausetime" +
                "(_id integer primary key autoincrement,"+
                "intervaltime long)";

        String stepsSQL ="create table tb_steps" +
                "(_id integer primary key autoincrement,"+
                "steps int)";

        String firstSQL ="create table tb_first" +
                "(_id integer primary key autoincrement,"+
                "first int)";

        db.execSQL(kindSQL);
        db.execSQL(borgSQL);
        db.execSQL(timeSQL);
        db.execSQL(pausetimeSQL);
        db.execSQL(stepsSQL);
        db.execSQL(firstSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_kind");
           db.execSQL("drop table tb_pausetime");
            db.execSQL("drop table tb_time");
           db.execSQL("drop table tb_borg");
            db.execSQL("drop table tb_steps");
            db.execSQL("drop table tb_first");
            onCreate(db);
        }


    }
}
