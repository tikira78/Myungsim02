package com.msba.myungsim02.record;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;


    public RecordDBHelper(Context context) {
        super(context,"recorddb",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sbpSQL ="create table tb_sbp" +
                "(_id integer primary key autoincrement,"
                +"Date long,"
                +"Date1 String,"
                +"SBP integer)";
        db.execSQL(sbpSQL);

        String dbpSQL ="create table tb_dbp" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"DBP integer)";
        db.execSQL(dbpSQL);

        String hrSQL ="create table tb_hr" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"HR integer)";
        db.execSQL(hrSQL);

        String bstSQL ="create table tb_bst" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"BST integer)";
        db.execSQL(bstSQL);

        String weightSQL ="create table tb_weight" +
                "(_id integer primary key,"
                +"Date long,"
                +"Date1 String,"
                +"WEIGHT float)";
        db.execSQL(weightSQL);

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            onCreate(db);
        }
    }

}
