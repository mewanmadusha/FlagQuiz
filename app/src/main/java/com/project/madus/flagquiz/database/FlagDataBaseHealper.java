package com.project.madus.flagquiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FlagDataBaseHealper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "flagquiz.db";
    private static final String TABLE_NAME = "flag_table";
    public static final String FLAG_TABLE_COLUMN_1 = "id";
    public static final String FLAG_TABLE_COLUMN_2 = "flag_code";
    public static final String FLAG_TABLE_COLUMN_3 = "flag_name";





    public FlagDataBaseHealper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //checking db working
//        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     /*
     * creating table flag_table
     * */
        db.execSQL("CREATE table " +TABLE_NAME+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, flag_code TEXT ,flag_name TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        * in case having any issue with db you can drop your table
        *
        * */

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    /**
     * @param flagcode
     * @param flagname
     * @return
     *
     * insert data in to sqllite db
     */
    public boolean insertFlagData(String flagcode,String flagname){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(FLAG_TABLE_COLUMN_2,flagcode);
        contentValues.put(FLAG_TABLE_COLUMN_3,flagname);

        /*
        * incase of error inserting value here it returns -1 value thats why we return boolean value
        * */
       long insertingValue=  db.insert(TABLE_NAME,null,contentValues);

       if(insertingValue == -1)
           return false;
       else
           return true;

    }

    public Cursor getAllFlagData(){

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select * from "+TABLE_NAME,null);
        return result;

    }

}
