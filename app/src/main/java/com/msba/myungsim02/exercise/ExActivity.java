package com.msba.myungsim02.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.msba.myungsim02.R;
import com.msba.myungsim02.setting.InfoDBHelper;
import com.niwattep.materialslidedatepicker.SlideDatePickerDialog;
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback;

import java.security.Permissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExActivity extends AppCompatActivity implements SlideDatePickerDialogCallback {

    private String getTime;
    long now;
    TextView tv1, tv2;
    EditText et1, et2;
    Button btn1, btn2,btn3;

    SQLiteDatabase db, db1, db2,db3,db4,db5;
    InfoDBHelper helper,helper1,helper2,helper3, helper4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex);

        ImageButton btn1 =  findViewById(R.id.recordbtn);
        btn2= findViewById(R.id.button1);
        btn3= findViewById(R.id.button2);

        tv1 = findViewById(R.id.tv1);
        et1 = findViewById(R.id.et2);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ExActivity.this, ExdaResultActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        //
        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getTime = dateFormat.format(date);


        //database
        helper = new InfoDBHelper(ExActivity .this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select intervention, MAX(Date) from tb_intervention", null);
        while(cursor.moveToNext()) {
            String intervention = cursor.getString(0);
            if(cursor.isNull(0)){
                tv1.setText("저장된 시술일자가 없습니다");

            } else {
                tv1.setText(intervention);
                setHR();
            }
        }
        db.close();

        btn1.setOnClickListener(this::onClick);
        btn2.setOnClickListener(this::onClick);

    } //onCreate

    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.button1:

                Calendar endDate = Calendar.getInstance();
                endDate.set(Calendar.YEAR, 2100);

                Calendar startDate = Calendar.getInstance();
                startDate.set(Calendar.YEAR, 1900);

                Calendar today = Calendar.getInstance();
                SlideDatePickerDialog.Builder builder = new SlideDatePickerDialog.Builder();

                builder.setStartDate(startDate)
                        .setEndDate(endDate)
                        .setPreselectedDate(today)
                        .setLocale(Locale.KOREAN)
                        .setThemeColor(Color.rgb(100,200,255))
                        .setShowYear(true)
                        .setHeaderDateFormat("MM월 dd일")
                        .setCancelText("취소")
                        .setConfirmText("확인");

                SlideDatePickerDialog dialog = builder.build();
                dialog.show(getSupportFragmentManager(), "TAG");

                break;


            case R.id.recordbtn:

                if(tv1.getText().equals("저장된 시술일자가 없습니다")){

                    Toast.makeText(ExActivity.this, "시술일자를 입력해주세요", Toast.LENGTH_SHORT).show();


                } else {


                    restingHr();

                }


                break;


        }

    }


    private void restingHr() {
        if(et1.getText().toString().equals("")){

            Toast.makeText(ExActivity.this, "안정시 맥박수를 입력해주세요", Toast.LENGTH_SHORT).show();

         /*   helper4 = new InfoDBHelper(SetOneActivity .this);
            db4= helper4.getWritableDatabase();
            ContentValues values2 = new ContentValues();
            values2.put("height","0"); //column, value
            values2.put("Date", now);
            values2.put("Date1", getTime);
            db4.insert("tb_height", null, values2);
            db4.close();

          */
        } else {
            helper4 = new InfoDBHelper(ExActivity .this);
            db4= helper4.getWritableDatabase();
            ContentValues values2 = new ContentValues();
            values2.put("restinghr",Integer.parseInt(et1.getText().toString())); //column, value
            values2.put("Date", now);
            values2.put("Date1", getTime);
            db4.insert("tb_restinghr", null, values2);
            db4.close();

            Toast.makeText(ExActivity.this, "정보가 저장되었습니다", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ExActivity.this, ExOneActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);

        }
    }


    private void setHR() {
        helper = new InfoDBHelper(ExActivity.this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select restinghr, MAX(Date) from tb_restinghr", null);
        while(cursor.moveToNext()) {
            int restinghr = cursor.getInt(0);
            if(cursor.isNull(0)){

                et1.setText("");

            } else {
                et1.setText(String.valueOf(restinghr));

            }
        }
        db.close();
    }


    @Override
    public void onPositiveClick(int i, int i1, int i2, @NonNull Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        tv1.setText(format.format(calendar.getTime()));

        helper2 = new InfoDBHelper(ExActivity .this);
        db2 = helper2.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("intervention",tv1.getText().toString()); //column, value
        values1.put("Date", now);
        values1.put("Date1", getTime);
        db2.insert("tb_intervention", null, values1);
        db2.close();

    }
}
