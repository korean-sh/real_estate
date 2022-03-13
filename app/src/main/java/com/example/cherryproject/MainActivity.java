package com.example.cherryproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;
    DatabasHelper dbHelper;
    int recordCount;

    Type1Fragment type1Fragment;
    Type2Fragment type2Fragment;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectDatabase();
        countExecuteQuery();

        type1Fragment = new Type1Fragment();
        type2Fragment = new Type2Fragment();

        //결과 값 따라서 분기
        if(recordCount > 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, type1Fragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.container, type2Fragment).commit();
        }

         closeDatabase();

    }

    private void connectDatabase(){  //데이터 베이스 연결
        dbHelper = new DatabasHelper(this);
        database = dbHelper.getWritableDatabase();
//        Snackbar.make(getWindow().getDecorView().getRootView(),"데이터베이스 생성이 완료되었습니다.", Snackbar.LENGTH_LONG).show();
    }

    private void countExecuteQuery(){
        if(database != null){
            Cursor cursor = database.rawQuery("select _id from informationTable",null);

            recordCount = cursor.getCount();
            Log.d("Sql","rawCount = " + recordCount);
        }else{
            Snackbar.make(getWindow().getDecorView().getRootView(),"Database is null.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void closeDatabase(){   //데이터 베이스 연결 해제
         database.close();
         dbHelper = null;
    }
}
