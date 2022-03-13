package com.example.cherryproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabasHelper extends SQLiteOpenHelper {
    public static String NAME = "cherry.db";
    public static  int VERSION = 1;
    public DatabasHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //기본정보
        String sql1 = "create table if not exists informationTable (_id integer PRIMARY KEY autoincrement,"
                    + "address1 varchar,"
                    + "address2 varchar,"
                    + "phonenumber varchar,"
                    + "possibledate varcahr,"
                    + "memo varchar,"
                    + "picturepath1 varchar,"
                    + "picturepath2 varchar,"
                    + "picturepath3 varchar,"
                    + "picturepath4 varchar,"
                    + "picturepath5 varchar);";


        //경제
        String sql2 = "create table if not exists moneyTable (_id integer PRIMARY KEY autoincrement, "
                    + "contracttype integer,"
                    + "year integer,"
                    + "deposit integer,"
                    + "month integer,"
                    + "maintenance integer,"
                    + "electronic integer,"
                    + "water integer,"
                    + "gas integer,"
                    + "internet integer,"
                    + "gastype integer);";


        //옵션
        String sql3 = "create table if not exists optionTable (_id integer PRIMARY KEY autoincrement, "
                    + "e_doorlock integer,"
                    + "shoescontainer integer,"
                    + "fire integer,"
                    + "kitchenfan integer,"
                    + "desk integer,"
                    + "chair integer,"
                    + "closet integer,"
                    + "bed integer,"
                    + "washingmashine integer,"
                    + "airconditional integer,"
                    + "toiletfan integer,"
                    + "refrigerator integer,"
                    + "elevator integer,"
                    + "parkingarea integer,"
                    + "cctv integer);";

        //집상태
        String sql4 = "create table if not exists statusTable (_id integer PRIMARY KEY autoincrement, "
                    + "wallpaper integer,"
                    + "linoleum integer,"
                    + "pressure integer,"
                    + "warmwater integer,"
                    + "sound integer,"
                    + "clean integer,"
                    + "mold integer,"
                    + "funiture integer);";

        //편의시설
        String sql5 = "create table if not exists facilityTable (_id integer PRIMARY KEY autoincrement, "
                + "trash integer,"
                + "covientstore integer,"
                + "mart integer,"
                + "washingshop integer,"
                + "hospital integer,"
                + "subway integer,"
                + "busstop integer);";

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        sqLiteDatabase.execSQL(sql4);
        sqLiteDatabase.execSQL(sql5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 > 1){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS informationTable");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS moneyTable");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS optionTable");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS statusTable");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS facilityTable");
        }
    }
}
