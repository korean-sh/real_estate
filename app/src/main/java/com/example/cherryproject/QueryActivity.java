package com.example.cherryproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QueryActivity extends AppCompatActivity {

    EditText addressTxt1;   //기본주소
    EditText addressTxt2;   //상세주소
    EditText phoneNumber;   //집주인 연락처
    TextView possibleDate;  //입주가능일

    RadioGroup payType;      //전세,월세 라디오 그룹

    TextView monthlyPayTxt; //월세
    TextView yealyPayTxt;   //전세
    TextView savePayTxt;    //보증금

    EditText monthlyPayEdit;    //월세
    EditText yealyPayEdit;      //전세
    EditText savePayEdit;       //보증금
    EditText usePayEdit;    //관리비
    EditText memoEdit;      //메모

    File file1;
    File file2;
    File file3;
    File file4;
    File file5;

    int imageSeq = 0;   //imageView 순서

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;

    String fileName1 = "NoImage";
    String fileName2 = "NoImage";
    String fileName3 = "NoImage";
    String fileName4 = "NoImage";
    String fileName5 = "NoImage";

    //데이터 베이스
    SQLiteDatabase database = null;
    DatabasHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        addressTxt1 = findViewById(R.id.defaultEdit1);  //기본주소
        addressTxt2 = findViewById(R.id.defaultEdit2);  //상세주소
        phoneNumber = findViewById(R.id.defaultTxt3);   //집 주인 연락처
        possibleDate = findViewById(R.id.defaultTxt4);  //입주 가능일
        memoEdit = findViewById(R.id.editText9);    //메모

        possibleDate.setOnClickListener(new View.OnClickListener() {    //달력 출력
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(QueryActivity.this,listener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        //전세 관련
        yealyPayTxt = findViewById(R.id.economicalTxt2);
        yealyPayEdit = findViewById(R.id.editText3);

        //월세 관련
        savePayTxt = findViewById(R.id.economicalTxt3);
        monthlyPayTxt = findViewById(R.id.economicalTxt4);
        savePayEdit = findViewById(R.id.editText4);
        monthlyPayEdit = findViewById(R.id.editText5);

        //관리비
        usePayEdit = findViewById(R.id.editText6);

        //계약 타입
        payType = findViewById(R.id.payGroup);
        payType.setOnCheckedChangeListener(onCheckedChangeListener);

        imageView1 = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSeq = 1;
                imageMethod(imageView1, imageSeq);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSeq = 2;
                imageMethod(imageView2, imageSeq);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSeq = 3;
                imageMethod(imageView3, imageSeq);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSeq = 4;
                imageMethod(imageView4, imageSeq);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSeq = 5;
                imageMethod(imageView5, imageSeq);
            }
        });

        connectDatabase();

        Button saveButton = findViewById(R.id.button2);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // DB 저장 후 연결 해제

                saveDefault();
                saveEconomical();
                saveOption();
                saveLifeStyle();
                saveDistance();
                closeDatabase();
            }
        });

    }

    private void imageMethod(ImageView imageView, final int seq){
        ImageView publicImage = imageView;
        publicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(seq);
            }
        });
    }

    //계약에 따른 항목 제거 및 나타내기
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.radioButton){ //전세 체크
                savePayTxt.setVisibility(View.GONE);
                monthlyPayTxt.setVisibility(View.GONE);
                savePayEdit.setVisibility(View.GONE);
                monthlyPayEdit.setVisibility(View.GONE);

                yealyPayTxt.setVisibility(View.VISIBLE);
                yealyPayEdit.setVisibility(View.VISIBLE);
            }else if(checkedId == R.id.radioButton2){ //월세 체크
                monthlyPayTxt.setVisibility(View.VISIBLE);
                savePayTxt.setVisibility(View.VISIBLE);
                savePayEdit.setVisibility(View.VISIBLE);
                monthlyPayEdit.setVisibility(View.VISIBLE);

                yealyPayTxt.setVisibility(View.GONE);
                yealyPayEdit.setVisibility(View.GONE);
            }
        }
    };

    //달력 팝업
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            possibleDate.setText(i + "년" + (i1+1) + "월" + i2 + "일");
        }
    };

    private void connectDatabase(){ //데이터 베이스 연결
        dbHelper = new DatabasHelper(this);
        database = dbHelper.getWritableDatabase();
        //database2 = dbHelper.getWritableDatabase();
        //database3 = dbHelper.getWritableDatabase();
        //database4 = dbHelper.getWritableDatabase();
        //database5 = dbHelper.getWritableDatabase();
        Log.d("Sql","데이터 베이스 연결 성공");
        Snackbar.make(getWindow().getDecorView().getRootView(),"데이터베이스 연결이 완료되었습니다.", Snackbar.LENGTH_LONG).show();
    }

    private void closeDatabase(){   //데이터 베이스 연결 해제
        database.close();
        dbHelper = null;
    }

    //기본 정보
    private void saveDefault(){
        //습도, 온도 추가 예정

        if(database == null){
            Log.d("Sql","데이터베이스 null");
            return;
        }else if(dbHelper == null){
            Log.d("Sql","헬퍼 null");
            return;
        }

        String address1 = addressTxt1.getText().toString();
        String address2 = addressTxt2.getText().toString();
        String p_number = phoneNumber.getText().toString();
        String p_date = possibleDate.getText().toString();

        String memo = "NoMemo";   //메모
        if(memoEdit.getText().toString().length() > 0)
            memo = memoEdit.getText().toString();

        String sql = "insert into informationTable (address1, address2, phonenumber, possibledate, memo, picturepath1, picturepath2, picturepath3, picturepath4, picturepath5) values ("
                    + "'" + address1 + "', '" + address2 + "', '" + p_number + "', '" + p_date + "', '" + memo + "', '"  + fileName1 + "', '" + fileName2 + "', '" + fileName3 + "', '" + fileName4 + "', '" + fileName5 + "');";
        Log.d("Sql","Dafault >> " + sql);
        database.execSQL(sql);
    }

    //경제적 질문
    private void saveEconomical(){
        int contractType = 0;   //1=전세, 2=월세

        RadioButton contractType1 = findViewById(R.id.radioButton);
        RadioButton contractType2 = findViewById(R.id.radioButton2);
        if(contractType1.isChecked()){
            contractType = 1;
        }else if(contractType2.isChecked()){
            contractType = 2;
        }else if(!contractType1.isChecked() && !contractType2.isChecked()){
            contractType = 9;
        }

        int yearlyPay = 99999;
        int savePay = 99999;
        int monthlyPay = 99999;
        int maintenance = 99999;

        if(yealyPayEdit.getText().toString().length() > 0)
            yearlyPay = Integer.parseInt(yealyPayEdit.getText().toString());

        if(savePayEdit.getText().toString().length() > 0)
            savePay = Integer.parseInt(savePayEdit.getText().toString());

        if(monthlyPayEdit.getText().toString().length() > 0)
            monthlyPay = Integer.parseInt(monthlyPayEdit.getText().toString());

        if(usePayEdit.getText().toString().length() > 0)
            maintenance = Integer.parseInt(usePayEdit.getText().toString());

        //전기세 1. 포함 / 0. 미포함 / 9. 선택 안함
        int electronicPay = 0;
        RadioButton electronicRBT_ON = findViewById(R.id.radioButton3);
        RadioButton electronicRBT_OFF = findViewById(R.id.radioButton4);
        if(electronicRBT_ON.isChecked()){
            electronicPay = 1;
        }else if(electronicRBT_OFF.isChecked()){
            electronicPay = 0;
        }else if(!electronicRBT_ON.isChecked() && !electronicRBT_OFF.isChecked()){
            electronicPay = 9;
        }

        //수도세 1. 포함 / 0. 미포함 / 9. 미표시
        int waterPay = 0;
        RadioButton waterRBT_ON = findViewById(R.id.radioButton5);
        RadioButton waterRBT_OFF = findViewById(R.id.radioButton6);
        if(waterRBT_ON.isChecked()){
            waterPay = 1;
        }else if(waterRBT_OFF.isChecked()){
            waterPay = 0;
        }else if(!waterRBT_ON.isChecked() && !waterRBT_OFF.isChecked()){
            waterPay = 9;
        }

        //도시가스세 1. 포함 / 0. 미포함 / 9. 미표시
        int gasPay = 0;
        RadioButton gasRBT_ON = findViewById(R.id.radioButton7);
        RadioButton gasRBT_OFF = findViewById(R.id.radioButton8);
        if(gasRBT_ON.isChecked()){
            gasPay = 1;
        }else if(gasRBT_OFF.isChecked()){
            gasPay = 0;
        }else if(!gasRBT_ON.isChecked() && !gasRBT_OFF.isChecked()){
            gasPay = 9;
        }

        //인터넷 사용료 1. 포함 / 0. 미포함 / 9. 미표시
        int internetPay = 0;
        RadioButton internetRBT_ON = findViewById(R.id.radioButton9);
        RadioButton internetRBT_OFF = findViewById(R.id.radioButton10);
        if(internetRBT_ON.isChecked()){
            internetPay = 1;
        }else if(internetRBT_OFF.isChecked()){
            internetPay = 0;
        }else if(!internetRBT_ON.isChecked() && !internetRBT_OFF.isChecked()){
            internetPay = 9;
        }

        //가스종류 1. 도시가스 / 0. LPG / 9. 미표시
        int gasKind = 0;
        RadioButton cityGas_ON = findViewById(R.id.radioButton11);
        RadioButton lpgGas_ON = findViewById(R.id.radioButton12);
        if(cityGas_ON.isChecked()){
            gasKind = 1;
        }else if(lpgGas_ON.isChecked()){
            gasKind = 0;
        }else if(!cityGas_ON.isChecked() && !lpgGas_ON.isChecked()){
            gasKind = 9;
        }

        String sql = "insert into moneyTable (contracttype, year, deposit, month, maintenance, electronic, water, gas, internet, gastype) values ("
                    + "'" + contractType + "', '" +  yearlyPay + "', '" + savePay + "', '" + monthlyPay + "', '" + maintenance + "', '" + electronicPay + "', '" + waterPay + "', '" + gasPay
                    + "', '" + internetPay + "', '" + gasKind + "');";
        Log.d("Sql","경제 >> " + sql);
        database.execSQL(sql);
    }

    //있다, 없다 저장 [옵션 데이터 베이스]
    private void saveOption(){

        // 1. 포함, 0. 미포함

        int eDroorLock = 0;     //보안
        int shoesContainer = 0;
        int fireTool = 0;
        int kitchenWind = 0;
        int desk = 0;
        int chair = 0;
        int closet = 0;
        int bed = 0;
        int washingMachine = 0;
        int airconditional = 0;
        int toiletWind = 0;
        int refrigerator = 0;
        int elevator = 0;
        int parkingArea = 0;
        int cctv = 0;       //보안

        RadioButton eDroorLockHave = findViewById(R.id.checkHaveItemRbtn1);
        RadioButton eDroorLockNoHave = findViewById(R.id.checkHaveItemRbtn2);
        RadioButton shoesContainterHave = findViewById(R.id.checkHaveItemRbtn3);
        RadioButton shoesContainterNoHave = findViewById(R.id.checkHaveItemRbtn4);
        RadioButton kitchenWindHave = findViewById(R.id.checkHaveItemRbtn5);
        RadioButton kitchenWindNoHave = findViewById(R.id.checkHaveItemRbtn6);
        RadioButton fireToolHave = findViewById(R.id.checkHaveItemRbtn7);
        RadioButton fireToolNoHave = findViewById(R.id.checkHaveItemRbtn8);
        RadioButton deskHave = findViewById(R.id.checkHaveItemRbtn9);
        RadioButton deskNoHave = findViewById(R.id.checkHaveItemRbtn10);
        RadioButton chairHave = findViewById(R.id.checkHaveItemRbtn11);
        RadioButton chairNoHave = findViewById(R.id.checkHaveItemRbtn12);
        RadioButton closetHave = findViewById(R.id.checkHaveItemRbtn13);
        RadioButton closetNoHave = findViewById(R.id.checkHaveItemRbtn14);
        RadioButton bedHave = findViewById(R.id.checkHaveItemRbtn15);
        RadioButton bedNoHave = findViewById(R.id.checkHaveItemRbtn16);
        RadioButton washingMachineHave = findViewById(R.id.checkHaveItemRbtn17);
        RadioButton washingMachineNoHave = findViewById(R.id.checkHaveItemRbtn18);
        RadioButton airconditionalHave = findViewById(R.id.checkHaveItemRbtn19);
        RadioButton airconditionalNoHave = findViewById(R.id.checkHaveItemRbtn20);
        RadioButton toiletWindHave = findViewById(R.id.checkHaveItemRbtn21);
        RadioButton toiletWindNoHave = findViewById(R.id.checkHaveItemRbtn22);
        RadioButton refrigeratorHave = findViewById(R.id.checkHaveItemRbtn23);
        RadioButton refrigeratorNoHave = findViewById(R.id.checkHaveItemRbtn24);
        RadioButton elevatorHave = findViewById(R.id.checkHaveItemRbtn25);
        RadioButton elevatorNoHave = findViewById(R.id.checkHaveItemRbtn26);
        RadioButton parkingAreaHave = findViewById(R.id.checkHaveItemRbtn27);
        RadioButton parkingAreaNoHave = findViewById(R.id.checkHaveItemRbtn28);
        RadioButton cctvHave = findViewById(R.id.checkHaveItemRbtn29);
        RadioButton cctvNoHave = findViewById(R.id.checkHaveItemRbtn30);

        if(eDroorLockHave.isChecked()){
            eDroorLock = 1;
        }else if(eDroorLockNoHave.isChecked()){
            eDroorLock = 0;
        }else if(!eDroorLockHave.isChecked() && !eDroorLockNoHave.isChecked()){
            eDroorLock = 9;
        }

        if(shoesContainterHave.isChecked()){
            shoesContainer = 1;
        }else if(shoesContainterNoHave.isChecked()){
            shoesContainer = 0;
        }else if(!shoesContainterHave.isChecked() && !shoesContainterNoHave.isChecked()){
            shoesContainer = 9;
        }

        if(kitchenWindHave.isChecked()){
            kitchenWind = 1;
        }else if(kitchenWindNoHave.isChecked()){
            kitchenWind = 0;
        }else if(!kitchenWindHave.isChecked() && !kitchenWindNoHave.isChecked()){
            kitchenWind = 9;
        }

        if(fireToolHave.isChecked()){
            fireTool = 1;
        }else if(fireToolNoHave.isChecked()){
            fireTool = 0;
        }else if(!fireToolHave.isChecked() && !fireToolNoHave.isChecked()){
            fireTool = 9;
        }

        if(deskHave.isChecked()){
            desk = 1;
        }else if(deskNoHave.isChecked()){
            desk = 0;
        }else if(!deskHave.isChecked() && !deskNoHave.isChecked()){
            desk = 9;
        }

        if(chairHave.isChecked()){
            chair = 1;
        }else if(chairNoHave.isChecked()){
            chair = 0;
        }else if(!chairHave.isChecked() && !chairNoHave.isChecked()){
            chair = 9;
        }

        if(closetHave.isChecked()){
            closet = 1;
        }else if(closetNoHave.isChecked()){
            closet = 0;
        }else if(!closetHave.isChecked() && !closetNoHave.isChecked()){
            closet = 9;
        }

        if(bedHave.isChecked()){
            bed = 1;
        }else if(bedNoHave.isChecked()){
            bed = 0;
        }else if(!bedHave.isChecked() && !bedNoHave.isChecked()){
            bed = 9;
        }

        if(washingMachineHave.isChecked()){
            washingMachine = 1;
        }else if(washingMachineNoHave.isChecked()){
            washingMachine = 0;
        }else if(!washingMachineHave.isChecked() && !washingMachineNoHave.isChecked()){
            washingMachine = 9;
        }

        if(airconditionalHave.isChecked()){
            airconditional = 1;
        }else if(airconditionalNoHave.isChecked()){
            airconditional = 0;
        }else if(!airconditionalHave.isChecked() && !airconditionalNoHave.isChecked()){
            airconditional = 9;
        }

        if(toiletWindHave.isChecked()){
            toiletWind = 1;
        }else if(toiletWindNoHave.isChecked()){
            toiletWind = 0;
        }else if(!toiletWindHave.isChecked() && !toiletWindNoHave.isChecked()){
            toiletWind = 9;
        }

        if(refrigeratorHave.isChecked()){
            refrigerator = 1;
        }else if(refrigeratorNoHave.isChecked()){
            refrigerator = 0;
        }else if(!refrigeratorHave.isChecked() && !refrigeratorNoHave.isChecked()){
            refrigerator = 9;
        }

        if(elevatorHave.isChecked()){
            elevator = 1;
        }else if(elevatorNoHave.isChecked()){
            elevator = 0;
        }else if(!elevatorHave.isChecked() && !elevatorNoHave.isChecked()){
            elevator = 9;
        }

        if(parkingAreaHave.isChecked()){
            parkingArea = 1;
        }else if(parkingAreaNoHave.isChecked()){
            parkingArea = 0;
        }else if(!parkingAreaHave.isChecked() && !parkingAreaNoHave.isChecked()){
            parkingArea = 9;
        }

        if(cctvHave.isChecked()){
            cctv = 1;
        }else if(cctvNoHave.isChecked()){
            cctv = 0;
        }else if(!cctvHave.isChecked() && !cctvNoHave.isChecked()){
            cctv = 9;
        }

        String sql = "insert into optionTable (e_doorlock, shoescontainer, fire, kitchenfan, desk, chair, closet, bed, washingmashine, airconditional, toiletfan, refrigerator, elevator, parkingarea, cctv) values ("
                + "'" + eDroorLock + "', '" + shoesContainer + "', '" + fireTool + "', '" + kitchenWind + "', '" + desk + "', '" + chair + "', '" + closet
                + "', '" + bed + "', '"+ washingMachine + "', '" + airconditional + "', '" + toiletWind + "', '" + refrigerator + "', '" +elevator + "', '" + parkingArea + "', '" + cctv + "');";
        database.execSQL(sql);
        Log.d("Sql","옵션 >> " + sql);
    }

    private void saveLifeStyle(){
        int wallpapaer = 1;
        int linoleum = 1;
        int pressure = 1;
        int warmwater = 1;
        int sound = 1;
        int clean = 1;
        int mold = 1;
        int funiture = 1;

        RadioButton omgBbt1 = findViewById(R.id.omrRbt1);
        RadioButton omgBbt2 = findViewById(R.id.omrRbt2);
        RadioButton omgBbt3 = findViewById(R.id.omrRbt3);
        RadioButton omgBbt4 = findViewById(R.id.omrRbt4);
        RadioButton omgBbt5 = findViewById(R.id.omrRbt5);

        RadioButton omgBbt6 = findViewById(R.id.omrRbt6);
        RadioButton omgBbt7 = findViewById(R.id.omrRbt7);
        RadioButton omgBbt8 = findViewById(R.id.omrRbt8);
        RadioButton omgBbt9 = findViewById(R.id.omrRbt9);
        RadioButton omgBbt10 = findViewById(R.id.omrRbt10);

        RadioButton omgBbt11 = findViewById(R.id.omrRbt11);
        RadioButton omgBbt12 = findViewById(R.id.omrRbt12);
        RadioButton omgBbt13 = findViewById(R.id.omrRbt13);
        RadioButton omgBbt14 = findViewById(R.id.omrRbt14);
        RadioButton omgBbt15 = findViewById(R.id.omrRbt15);

        RadioButton omgBbt16 = findViewById(R.id.omrRbt16);
        RadioButton omgBbt17 = findViewById(R.id.omrRbt17);
        RadioButton omgBbt18 = findViewById(R.id.omrRbt18);
        RadioButton omgBbt19 = findViewById(R.id.omrRbt19);
        RadioButton omgBbt20 = findViewById(R.id.omrRbt20);

        RadioButton omgBbt21 = findViewById(R.id.omrRbt21);
        RadioButton omgBbt22 = findViewById(R.id.omrRbt22);
        RadioButton omgBbt23 = findViewById(R.id.omrRbt23);
        RadioButton omgBbt24 = findViewById(R.id.omrRbt24);
        RadioButton omgBbt25 = findViewById(R.id.omrRbt25);

        RadioButton omgBbt26 = findViewById(R.id.omrRbt26);
        RadioButton omgBbt27 = findViewById(R.id.omrRbt27);
        RadioButton omgBbt28 = findViewById(R.id.omrRbt28);
        RadioButton omgBbt29 = findViewById(R.id.omrRbt29);
        RadioButton omgBbt30 = findViewById(R.id.omrRbt30);

        RadioButton omgBbt31 = findViewById(R.id.omrRbt31);
        RadioButton omgBbt32 = findViewById(R.id.omrRbt32);
        RadioButton omgBbt33 = findViewById(R.id.omrRbt33);
        RadioButton omgBbt34 = findViewById(R.id.omrRbt34);
        RadioButton omgBbt35 = findViewById(R.id.omrRbt35);

        RadioButton omgBbt36 = findViewById(R.id.omrRbt36);
        RadioButton omgBbt37 = findViewById(R.id.omrRbt37);
        RadioButton omgBbt38 = findViewById(R.id.omrRbt38);
        RadioButton omgBbt39 = findViewById(R.id.omrRbt39);
        RadioButton omgBbt40 = findViewById(R.id.omrRbt40);

        if(omgBbt1.isChecked()){
            wallpapaer = 1;
        }else if(omgBbt2.isChecked()){
            wallpapaer = 2;
        }else if(omgBbt3.isChecked()){
            wallpapaer = 3;
        }else if(omgBbt4.isChecked()){
            wallpapaer = 4;
        }else if(omgBbt5.isChecked()){
            wallpapaer = 5;
        }

        if(omgBbt6.isChecked()){
            linoleum = 1;
        }else if(omgBbt7.isChecked()){
            linoleum = 2;
        }else if(omgBbt8.isChecked()){
            linoleum = 3;
        }else if(omgBbt9.isChecked()){
            linoleum = 4;
        }else if(omgBbt10.isChecked()){
            linoleum = 5;
        }

        if(omgBbt11.isChecked()){
            pressure = 1;
        }else if(omgBbt12.isChecked()){
            pressure = 2;
        }else if(omgBbt13.isChecked()){
            pressure = 3;
        }else if(omgBbt14.isChecked()){
            pressure = 4;
        }else if(omgBbt15.isChecked()){
            pressure = 5;
        }

        if(omgBbt16.isChecked()){
            warmwater = 1;
        }else if(omgBbt17.isChecked()){
            warmwater = 2;
        }else if(omgBbt18.isChecked()){
            warmwater = 3;
        }else if(omgBbt19.isChecked()){
            warmwater = 4;
        }else if(omgBbt20.isChecked()){
            warmwater = 5;
        }

        if(omgBbt21.isChecked()){
            sound = 1;
        }else if(omgBbt22.isChecked()){
            sound = 2;
        }else if(omgBbt23.isChecked()){
            sound = 3;
        }else if(omgBbt24.isChecked()){
            sound = 4;
        }else if(omgBbt25.isChecked()){
            sound = 5;
        }

        if(omgBbt26.isChecked()){
            clean = 1;
        }else if(omgBbt27.isChecked()){
            clean = 2;
        }else if(omgBbt28.isChecked()){
            clean = 3;
        }else if(omgBbt29.isChecked()){
            clean = 4;
        }else if(omgBbt30.isChecked()){
            clean = 5;
        }

        if(omgBbt31.isChecked()){
            mold = 1;
        }else if(omgBbt32.isChecked()){
            mold = 2;
        }else if(omgBbt33.isChecked()){
            mold = 3;
        }else if(omgBbt34.isChecked()){
            mold = 4;
        }else if(omgBbt35.isChecked()){
            mold = 5;
        }

        if(omgBbt36.isChecked()){
            funiture = 1;
        }else if(omgBbt37.isChecked()){
            funiture = 2;
        }else if(omgBbt38.isChecked()){
            funiture = 3;
        }else if(omgBbt39.isChecked()){
            funiture = 4;
        }else if(omgBbt40.isChecked()){
            funiture = 5;
        }

        String sql = "insert into statusTable (wallpaper, linoleum, pressure, warmwater, sound, clean, mold, funiture) values ("
                    + "'" + wallpapaer + "', '" + linoleum + "', '" + pressure + "', '" + warmwater + "', '" + sound + "', '" + clean + "', '" + mold + "', '" + funiture + "');";
        Log.d("Sql","라이프스타일 >> " + sql);
        database.execSQL(sql);
    }

    private void saveDistance(){

        EditText convenient1 = findViewById(R.id.editConvient1);
        EditText convenient2 = findViewById(R.id.editConvient2);
        EditText convenient3 = findViewById(R.id.editConvient3);
        EditText convenient4 = findViewById(R.id.editConvient4);
        EditText convenient5 = findViewById(R.id.editConvient5);
        EditText convenient6 = findViewById(R.id.editConvient6);
        EditText convenient7 = findViewById(R.id.editConvient7);

        //공란
        int trashPlace = 99999;
        int convientStore = 99999;
        int mart = 99999;
        int washingShop = 99999;
        int hospital = 99999;
        int subway = 99999;
        int busStop = 99999;

        //유효성 검사
        if(convenient1.getText().toString().length() > 0)
            trashPlace = Integer.parseInt(convenient1.getText().toString());

        if(convenient2.getText().toString().length() > 0)
            convientStore = Integer.parseInt(convenient2.getText().toString());

        if(convenient3.getText().toString().length() > 0)
            mart = Integer.parseInt(convenient3.getText().toString());

        if(convenient4.getText().toString().length() > 0)
            washingShop = Integer.parseInt(convenient4.getText().toString());

        if(convenient5.getText().toString().length() > 0)
            hospital = Integer.parseInt(convenient5.getText().toString());

        if(convenient6.getText().toString().length() > 0)
            subway = Integer.parseInt(convenient6.getText().toString());

        if(convenient7.getText().toString().length() > 0)
            busStop = Integer.parseInt(convenient7.getText().toString());

        String sql = "insert into facilityTable (trash, covientstore, mart, washingshop, hospital, subway, busstop) values ("
                    + "'" + trashPlace + "', '" + convientStore + "', '" + mart + "', '" + washingShop + "', '" + hospital + "', '" + subway + "', '" + busStop + "');";
        database.execSQL(sql);
        Log.d("Sql","거리 >> " + sql);

    }

    private String makeFileName(){
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String getTime = simpleDateFormat.format(mDate);
        String name = getTime+".jpg";

        return name;
    }

    private File createFile(){
        String fileName = makeFileName();

        if(imageSeq == 1)
            fileName1 = fileName;
        else if(imageSeq == 2)
            fileName2 = fileName;
        else if(imageSeq == 3)
            fileName3 = fileName;
        else if(imageSeq == 4)
            fileName4 = fileName;
        else if(imageSeq == 5)
            fileName5 = fileName;

        File storgeDir = getBaseContext().getFilesDir();
        File outFile = new File(storgeDir, fileName);
        return outFile;
    }

    private void takePicture(int seq){
        Uri fileUri = null;

        if(seq == 1){
            if(file1 == null){
                file1 = createFile();
            }
            fileUri = FileProvider.getUriForFile(this, "com.example.cherryproject.fileprovider",file1);
        }else if(seq == 2){
            if(file2 == null){
                file2 = createFile();
            }
            fileUri = FileProvider.getUriForFile(this, "com.example.cherryproject.fileprovider",file2);
        }else if(seq == 3){
            if(file3 == null){
                file3 = createFile();
            }
            fileUri = FileProvider.getUriForFile(this, "com.example.cherryproject.fileprovider",file3);
        }else if(seq == 4){
            if(file4 == null){
                file4 = createFile();
            }
            fileUri = FileProvider.getUriForFile(this, "com.example.cherryproject.fileprovider",file4);
        }else if(seq == 5){
            if(file5 == null){
                file5 = createFile();
            }
            fileUri = FileProvider.getUriForFile(this, "com.example.cherryproject.fileprovider",file5);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        if(intent.resolveActivity((getPackageManager())) != null){
            startActivityForResult(intent, 101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap bitmap;

            if(imageSeq == 1){
                bitmap = BitmapFactory.decodeFile(file1.getAbsolutePath(), options);
                imageView1.setImageBitmap(bitmap);
            }else if(imageSeq == 2){
                bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath(), options);
                imageView2.setImageBitmap(bitmap);
            }else if(imageSeq == 3){
                bitmap = BitmapFactory.decodeFile(file3.getAbsolutePath(), options);
                imageView3.setImageBitmap(bitmap);
            }else if(imageSeq == 4){
                bitmap = BitmapFactory.decodeFile(file4.getAbsolutePath(), options);
                imageView4.setImageBitmap(bitmap);
            }else if(imageSeq == 5){
                bitmap = BitmapFactory.decodeFile(file5.getAbsolutePath(), options);
                imageView5.setImageBitmap(bitmap);
            }
        }
    }
}
