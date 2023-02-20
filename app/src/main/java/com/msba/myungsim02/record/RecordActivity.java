package com.msba.myungsim02.record;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.R;
import com.msba.myungsim02.point.DisDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{


    private ArrayList<RecordItem> items =null;
    private String uid = null;
    private String itemId = null;
    private TextView textView1;
    private String getTime, getTime2, getTime3;
    private EditText SBPEdit, DBPEdit, hrEdit, BSTEdit,weightEdit;
    long now, now1;
    boolean isOpen;
    private AlertDialog alertDialog;
    SQLiteDatabase db;
    RecordDBHelper dbHelper;

    int i1;

    int sbp,dbp,hr;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        //
        SBPEdit = (EditText) findViewById(R.id.ed1);
        DBPEdit = (EditText) findViewById(R.id.ed2);
        hrEdit = (EditText)  findViewById(R.id.ed3);
        BSTEdit = (EditText) findViewById(R.id.ed4);
        weightEdit = (EditText)  findViewById(R.id.ed5);

        //focus 주기
        SBPEdit.requestFocus();

        //
        dbHelper = new RecordDBHelper(RecordActivity.this);
        db = dbHelper.getWritableDatabase();


       // recordAdapter = new RecordAdapter();

        Button regBtn =(Button) findViewById(R.id.recordBtn);
        Button resultBtn =(Button) findViewById(R.id.resultBtn);
        regBtn.setOnClickListener(this);
        resultBtn.setOnClickListener(this);

        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy년 MM월 dd일 \n HH시 mm분");
        getTime2 = dateFormat1.format(date);

        textView1 = findViewById(R.id.upDate);
        textView1.setText(getTime2);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.recordBtn:
                if (SBPEdit.getText().toString().equals("") && DBPEdit.getText().toString().equals("") && hrEdit.getText().toString().equals("")
                        && BSTEdit.getText().toString().equals("") && weightEdit.getText().toString().equals("")) {
                    Toast.makeText(RecordActivity.this, "데이터를 하나 이상 입력해주세요", Toast.LENGTH_LONG).show();
                }else{
                    regRecord();
                }
                break;

            case R.id.resultBtn:
                Intent intent = new Intent(RecordActivity.this, ResultActivity.class);
                startActivity(intent);
                finish();
                break;

            }
    }


    private void regRecord() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("저장");
            builder.setMessage("수축기혈압: " + SBPEdit.getText().toString() + "mmHg \n이완기혈압: " + DBPEdit.getText().toString() +
                "mmHg \n맥박수: " + hrEdit.getText().toString() + "회/분 \n혈당: " + BSTEdit.getText().toString() + "mg/dL \n체중: "
                    + weightEdit.getText().toString() + "kg \n으로 저장하시겠습니까?");
            builder.setPositiveButton("OK", dialoglistner);
            builder.setNegativeButton("NO", null);
            builder.show();


    }

    DialogInterface.OnClickListener dialoglistner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            getTime = dateFormat.format(date);

            if(!SBPEdit.getText().toString().equals("")){
                ContentValues values = new ContentValues();
                values.put("SBP", Integer.parseInt(SBPEdit.getText().toString())); //column, value
                values.put("Date", now);
                values.put("Date1", getTime);
                db.insert("tb_sbp", null, values);

            }

            if(!DBPEdit.getText().toString().equals("")){
                ContentValues values2 = new ContentValues();
                values2.put("DBP", Integer.parseInt(DBPEdit.getText().toString())); //column, value
                values2.put("Date", now);
                values2.put("Date1", getTime);
                db.insert("tb_dbp", null, values2);

            }

            if(!hrEdit.getText().toString().equals("")){
                ContentValues values3 = new ContentValues();
                values3.put("HR", Integer.parseInt(hrEdit.getText().toString())); //column, value
                values3.put("Date", now);
                values3.put("Date1", getTime);
                db.insert("tb_hr", null, values3);

            }

            if(!BSTEdit.getText().toString().equals("")){
                ContentValues values4 = new ContentValues();
                values4.put("BST", Integer.parseInt(BSTEdit.getText().toString())); //column, value
                values4.put("Date", now);
                values4.put("Date1", getTime);
                db.insert("tb_bst", null, values4);

            }

            if(!weightEdit.getText().toString().equals("")) {
                ContentValues values5 = new ContentValues();
                values5.put("WEIGHT", Float.parseFloat(weightEdit.getText().toString())); //column, value
                values5.put("Date", now);
                values5.put("Date1", getTime);
                db.insert("tb_weight", null, values5);

            }

            db.close();

            Toast.makeText(RecordActivity.this, "정보를 추가했습니다.", Toast.LENGTH_SHORT).show();

            SBPEdit.setText(null);
            DBPEdit.setText(null);
            hrEdit.setText(null);
            BSTEdit.setText(null);
            weightEdit.setText(null);

            Intent intent = new Intent(RecordActivity.this, ResultActivity.class);
            startActivity(intent);
            finish();
        }
    };



}