package com.example.user.dfinal2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 2017/12/21.
 */

public class DBsqlite extends SQLiteOpenHelper {

    // 資料庫名稱
    public static final String DATABASE_NAME = "mydata";
//    public String Sql="CREATE TABLE DFinal(Index  INTEGER PRIMARY KEY AUTOINCREMENT ,U_Num TEXT, U_Name TEXT) ";

// 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一


    // 資料庫物件，固定的欄位變數

    public DBsqlite(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Sql="CREATE TABLE 'DFinal'('Index' INTEGER PRIMARY KEY AUTOINCREMENT , 'U_Num' TEXT, 'U_Name' TEXT, 'Keep' TEXT,'Email' TEXT,'PWD' TEXT); ";
        sqLiteDatabase.execSQL(Sql);
//        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DFinal" );
        onCreate(sqLiteDatabase);
    }

    public void insert(DBsqlite dop, Map<String,String>data){
        SQLiteDatabase db=dop.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("Index", "");
        for (Map.Entry<String,String> a:data.entrySet()){
            values.put( a.getKey(),a.getValue());
        }
        db.insert("DFinal",null,values);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getAll(DBsqlite dop,Map<String,String>map) {
        SQLiteDatabase db = dop.getReadableDatabase();
        String selectQuery =  "SELECT  * FROM DFinal";
//        String selectQuery =  "SELECT  * FROM DFinal";
        int count=map.entrySet().size();
        int i=1;
        for(Map.Entry<String,String> Entry:map.entrySet()){
            if (i==1){
                selectQuery+=" WHERE ";
            }
            selectQuery+=Entry.getKey()+" = '"+Entry.getValue()+"'";
            if(i++<count){
                selectQuery+=" AND ";
            }
        }
        System.out.println("SQL=\t"+selectQuery);
        ArrayList<HashMap<String, String>> ItemGPSList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> checkpoint = new HashMap<>();
                checkpoint.put("Index", cursor.getString(cursor.getColumnIndex("Index")));
                checkpoint.put("U_Num",cursor.getString(cursor.getColumnIndex("U_Num")));
                checkpoint.put("U_Name",cursor.getString(cursor.getColumnIndex("U_Name")));
                checkpoint.put("Keep",cursor.getString(cursor.getColumnIndex("Keep")));
                checkpoint.put("Email",cursor.getString(cursor.getColumnIndex("Email")));
                checkpoint.put("PWD",cursor.getString(cursor.getColumnIndex("PWD")));
//                System.out.print("Email"+cursor.getString(cursor.getColumnIndex("Email")));
//                System.out.print("PWD"+cursor.getString(cursor.getColumnIndex("PWD")));
                ItemGPSList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ItemGPSList;
    }
    public void delete(DBsqlite dop,int i){
        SQLiteDatabase db=dop.getWritableDatabase();
//        String sql="DELETE FROM DFinal WHERE 'Index' = "+i+" ;";
        int dFinal = db.delete("DFinal", "Email = " + "'example@example.com'",null);
    }

}
