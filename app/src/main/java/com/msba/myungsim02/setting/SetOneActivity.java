package com.msba.myungsim02.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.NotiActivity;
import com.msba.myungsim02.R;
import com.msba.myungsim02.point.DisDBHelper;
import com.msba.myungsim02.record.RecordDBHelper;
import com.niwattep.materialslidedatepicker.SlideDatePickerDialog;
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SetOneActivity extends AppCompatActivity implements SlideDatePickerDialogCallback{

    private String yy3,mm3,dd3, yy2, mm2, dd2, date1, date2, date3, getTime;
    long selectedDate=0, now;
    TextView tv1, tv2;
    int age, age2;
    EditText et1, et2;

    SQLiteDatabase db, db1, db2,db3,db4,db5;
    InfoDBHelper helper,helper1,helper2,helper3, helper4;
    RecordDBHelper helper5;
    int recordCount;

    RadioButton rb_1, rb_2;
    RadioGroup rg1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_one);

        Button btn1= (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.recordbtn);


        tv1 = findViewById(R.id.tv_2);

        //
        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getTime = dateFormat.format(date);

        rg1 = findViewById(R.id.rg_1);
        rb_1 = (RadioButton) findViewById(R.id.rb_1);
        rb_2 = (RadioButton) findViewById(R.id.rb_2);


        //
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);

        helper = new InfoDBHelper(SetOneActivity .this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select sex, MAX(Date) from tb_sex", null);
        while(cursor.moveToNext()) {
            String sex = cursor.getString(0);
            if(cursor.isNull(0)||sex.equals("남성")){
                rb_1.setChecked(true);

                setBirth();

            } else {
                rb_2.setChecked(true);

                setBirth();
            }
        }
        db.close();

        btn1.setOnClickListener(this::onClick);
        btn3.setOnClickListener(this::onClick);

               //Floating btn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetOneActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);

            }
        });

    } //onCreate

    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.button2:

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

                if(rg1.getCheckedRadioButtonId()==-1){
                    Toast.makeText(SetOneActivity.this, "성별을 체크해주세요", Toast.LENGTH_SHORT).show();
                }else if(rg1.getCheckedRadioButtonId()==R.id.rb_1) {
                    helper1 = new InfoDBHelper(SetOneActivity .this);
                    db1 = helper1.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("sex", "남성"); //column, value
                    values.put("Date", now);
                    values.put("Date1", getTime);
                    db1.insert("tb_sex", null, values);
                    db1.close();

                    birthDate();
                } else {
                    helper1 = new InfoDBHelper(SetOneActivity .this);
                    db1 = helper1.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("sex", "여성"); //column, value
                    values.put("Date", now);
                    values.put("Date1", getTime);
                    db1.insert("tb_sex", null, values);
                    db1.close();

                    birthDate();
                }
               break;


        }

    }

    private void birthDate() {

        if(tv1.getText().equals("저장된 생년월일이 없습니다")){

            Toast.makeText(SetOneActivity.this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();

           /*
            helper2 = new InfoDBHelper(SetOneActivity .this);

            db2 = helper2.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put("birth","0" ); //column, value
            values1.put("Date", now);
            values1.put("Date1", getTime);
            values1.put("age","0" ); //column, value
            db2.insert("tb_birth", null, values1);
            db2.close();
           */

        } else {
            helper2 = new InfoDBHelper(SetOneActivity .this);
            db2 = helper2.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put("birth",tv1.getText().toString()); //column, value
            values1.put("Date", now);
            values1.put("Date1", getTime);
            values1.put("age","0" ); //column, value
            db2.insert("tb_birth", null, values1);
            db2.close();

            height();

        }

    }

    private void height() {
        if(et1.getText().toString().equals("")){

            Toast.makeText(SetOneActivity.this, "키를 입력해주세요", Toast.LENGTH_SHORT).show();

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
            helper4 = new InfoDBHelper(SetOneActivity .this);
            db4= helper4.getWritableDatabase();
            ContentValues values2 = new ContentValues();
            values2.put("height",Float.parseFloat(et1.getText().toString())); //column, value
            values2.put("Date", now);
            values2.put("Date1", getTime);
            db4.insert("tb_height", null, values2);
            db4.close();

            weight();
        }
    }

    private void weight() {

        if(et2.getText().toString().equals("")){

            Toast.makeText(SetOneActivity.this, "체중을 입력해주세요", Toast.LENGTH_SHORT).show();
            /*
            helper5 = new RecordDBHelper(SetOneActivity .this);
            db5= helper5.getWritableDatabase();
            ContentValues values4 = new ContentValues();
            values4.put("WEIGHT", "0"); //column, value
            values4.put("Date", now);
            values4.put("Date1", getTime);
            db5.insert("tb_weight", null, values4);
            db5.close();
             */
        } else {
            helper5 = new RecordDBHelper(SetOneActivity .this);
            db5= helper5.getWritableDatabase();
            ContentValues values4 = new ContentValues();
            values4.put("Date", now);
            values4.put("Date1", getTime);
            values4.put("WEIGHT", Float.parseFloat(et2.getText().toString())); //column, valu
            db5.insert("tb_weight", null, values4);
            db5.close();

            Toast.makeText(SetOneActivity.this, "성공적으로 저장되었습니다!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setBirth() {
        helper = new InfoDBHelper(SetOneActivity .this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select birth, MAX(Date) from tb_birth", null);
        while(cursor.moveToNext()) {
            String birth = cursor.getString(0);
            if(cursor.isNull(0)){

                tv1.setText("저장된 생년월일이 없습니다");

                setHeight();

            } else {

                tv1.setText(birth);

                setHeight();
            }
        }
        db.close();

    }

    private void setHeight() {
        helper = new InfoDBHelper(SetOneActivity .this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select height, MAX(Date) from tb_height", null);
        while(cursor.moveToNext()) {
            float height = cursor.getFloat(0);
            if(cursor.isNull(0)){

                et1.setText("");
                setWeight();

            } else {
                et1.setText(String.valueOf(height));

                setWeight();
            }
        }
        db.close();
    }

    private void setWeight() {
        helper5 = new RecordDBHelper(SetOneActivity .this);
        db = helper5.getWritableDatabase();

        Cursor cursor = db.rawQuery("select weight, MAX(Date) from tb_weight", null);
        while(cursor.moveToNext()) {
            float weight = cursor.getFloat(0);
            if(cursor.isNull(0)){

                et2.setText("");

            } else {
                et2.setText(String.valueOf(weight));

            }
        }
        db.close();
    }

    @Override
    public void onPositiveClick(int i, int i1, int i2, @NonNull Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MM");
        SimpleDateFormat format3 = new SimpleDateFormat("dd");
        tv1.setText(format.format(calendar.getTime()));
        selectedDate = calendar.getTimeInMillis();

        int birthYear = Integer.parseInt(format1.format(calendar.getTime()));
        int birthMonth = Integer.parseInt(format2.format(calendar.getTime()));
        int birthDay = Integer.parseInt(format3.format(calendar.getTime()));

        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);

        age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) {
            age2= age--;
        }else{
            age2 = age;
        }

    }
}
