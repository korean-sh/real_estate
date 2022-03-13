package com.example.cherryproject;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Type1Fragment extends Fragment {

    HouseAdapter adapter;
    RecyclerView recyclerView;

    SQLiteDatabase database = null;
    DatabasHelper dbHelper;

    public Type1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_type1, container, false);
        adapter = new HouseAdapter();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        loadDatabase();

        return rootView;
    }

    public void loadDatabase(){
        connectDatabase();

        String sql = "select _id, address1, address2, phonenumber, possibledate, memo, picturepath1, picturepath2, picturepath3, picturepath4, picturepath5 from informationtable order by _id asc;";

        if(database != null){
            Cursor cursor = database.rawQuery(sql, null);
            int recordCount = cursor.getCount();

            for(int i = 0; i < recordCount; i++){
                cursor.moveToNext();
                int _id = cursor.getInt(0);
                String address1 = cursor.getString(1);
                String address2 = cursor.getString(2);
                String phonenumber = cursor.getString(3);
                String possibledate = cursor.getString(4);
                String memo = cursor.getString(5);
                String picturepath1 = cursor.getString(6);
                String picturepath2 = cursor.getString(7);
                String picturepath3 = cursor.getString(8);
                String picturepath4 = cursor.getString(9);
                String picturepath5 = cursor.getString(10);

                House house = new House(_id, address1, address2, phonenumber, possibledate, memo, picturepath1, picturepath2, picturepath3, picturepath4, picturepath5);
                adapter.addItem(house);
            }
        }
    }

    private void connectDatabase(){ //데이터 베이스 연결
        dbHelper = new DatabasHelper(getContext());
        database = dbHelper.getWritableDatabase();
//        Log.d("Sql","데이터 베이스 연결 성공");
    }
}
