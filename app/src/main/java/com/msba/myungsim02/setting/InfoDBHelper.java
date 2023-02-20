package com.msba.myungsim02.setting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InfoDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    SQLiteDatabase db;


    public InfoDBHelper(Context context) {
        super(context,"infodb",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sexSQL ="create table tb_sex" +
                "(_id integer primary key autoincrement,"
                +"Date long,"
                +"Date1 String,"
                +"sex String)";
        db.execSQL(sexSQL);

        String birthSQL ="create table tb_birth" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"birth String,"
                +"age int)";
        db.execSQL(birthSQL);

        String heightSQL ="create table tb_height" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"height float)";
        db.execSQL(heightSQL);

        String interventionSQL ="create table tb_intervention" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"intervention int)";
        db.execSQL(interventionSQL);

        String restinghrSQL ="create table tb_restinghr" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"restinghr int)";
        db.execSQL(restinghrSQL);

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
           db.execSQL("drop table tb_sex");
           db.execSQL("drop table tb_birth");
           db.execSQL("drop table tb_height");
           onCreate(db);
        }
    }

}
