package com.msba.myungsim02.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.R;

import java.security.Permissions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExResultActivity extends AppCompatActivity {
    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    private static final int REQUEST_OAUTH_REQUEST_CODE = 100;
    private Button btn_s, btn_e;
    private long start, end, timeInterval;
    private int totalSteps = 0;
    private float totalDistance = 0;
    private float totalCalories = 0;
    private String totalStepTime,totalStepTime2,totalStepTime3;
    private String ex;
    private int borg;
    private int getInt;

    ExerciseDBHelper helper;
    SQLiteDatabase db, db2, db3;

    private FirebaseUser firebaseUser;
    FirebaseFirestore db4;

    private ExdaDatabase database;
    private ExdaAdapter exdaAdapter = null;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    private AlertDialog alertDialog;
    EditText et;

    Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_result);

        textView1 = findViewById(R.id.tv_today);
        textView2 = findViewById(R.id.tv_today2);
        textView3 = findViewById(R.id.tv_today3);
        textView4 = findViewById(R.id.tv_today4);
        textView5 = findViewById(R.id.tv_today5);
        textView6 = findViewById(R.id.tv_today6);

        btn_s = findViewById(R.id.button7);
        btn_e = findViewById(R.id.button5);

        et = findViewById(R.id.editTextTextMultiLine);

        //
        database = ExdaDatabase.getInstance(this);
        boolean isOpen = database.open();
        if (isOpen) {
            Log.i("crkim", " database is open.");
        } else {
            Log.i("crkim", "database is not open.");
        }

        exdaAdapter = new ExdaAdapter();

        //
        helper = new ExerciseDBHelper(ExResultActivity.this);

        //type of exercise
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select kind from tb_kind where _id=1", null);
        while(cursor.moveToNext()) {
            Log.i("crkim", String.valueOf(cursor.getInt(0)));
            getInt = cursor.getInt(0);
        }
        cursor.close();

        if (getInt == R.id.ex1) {
            ex = "걷기";
        } else if (getInt == R.id.ex2) {
            ex = "달리기";
        } else if (getInt == R.id.ex3) {
            ex = "러닝머신";
        } else if (getInt == R.id.ex4) {
            ex = "실외자전거";
            textView3.setVisibility(View.INVISIBLE);
            textView4.setVisibility(View.INVISIBLE);
            textView5.setVisibility(View.INVISIBLE);
        } else {
            ex = "기타";
            textView3.setVisibility(View.INVISIBLE);
            textView4.setVisibility(View.INVISIBLE);
            textView5.setVisibility(View.INVISIBLE);
        }

        textView1.setText("운동종류: " + ex);


        //
        db3 = helper.getWritableDatabase();
        Cursor cursor3 = db3.rawQuery("select borg from tb_borg where _id=1", null);
        while(cursor3.moveToNext()) {
            borg = cursor3.getInt(0);
        }
        cursor3.close();
        textView6.setText("운동자각도: " + borg +"점");

        //
        db2 = helper.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery("select starttime, endtime from tb_time where _id=1", null);
        while(cursor2.moveToNext()) {
            start = cursor2.getLong(0);
            end = cursor2.getLong(1);
            Log.i("crkim", String.valueOf(start));
            Log.i("crkim", String.valueOf(end));
        }
        cursor2.close();

        timeInterval = end - start;
        totalStepTime = String.format("%02d시간 %02d분 %02d초", TimeUnit.MILLISECONDS.toHours(timeInterval),
                TimeUnit.MILLISECONDS.toMinutes(timeInterval) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInterval)),
                TimeUnit.MILLISECONDS.toSeconds(timeInterval) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInterval)));
        totalStepTime2 = String.format("%02d", TimeUnit.MILLISECONDS.toHours(timeInterval));
        totalStepTime3 = String.format("%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInterval) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInterval)));

        textView2.setText("운동시간: " + totalStepTime);

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .addDataType(DataType.TYPE_DISTANCE_DELTA)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED)
                .addDataType(DataType.TYPE_HEART_RATE_BPM)
                .addDataType(DataType.AGGREGATE_HEART_RATE_SUMMARY)
                .build();

        if(!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            Fitness.getRecordingClient(this,
                            GoogleSignIn.getLastSignedInAccount(this))
                    .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("crkim", "Successfully subscribed!");
                                        readData();
                                    } else {
                                        Log.w("crkim", "There was a problem subscribing.", task.getException());
                                    }
                                }
                            });
        }

        //
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload();
                save();
                Toast.makeText(ExResultActivity.this, "저장되었습니다!!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ExResultActivity.this, ExdaResultActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
            }
        });

        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("알림");
                builder.setMessage("결과를 저장하지 않으시겠습니까?");
                builder.setPositiveButton("네", dialogListener);
                builder.setNegativeButton("아니오", null);
                builder.show();
           }
        });

    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            Toast.makeText(ExResultActivity.this, "저장하지 않고 운동종료합니다", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(ExResultActivity.this, HomeActivity.class);
            startActivity(intent2);
            finish();
            overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        }
    };

    private void readData() {

        Log.i("crkim", "read DATA!!");

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(new DataReadRequest.Builder()
                        .read(DataType.TYPE_CALORIES_EXPENDED)
                        .read(DataType.TYPE_STEP_COUNT_DELTA)
                        .read(DataType.TYPE_DISTANCE_DELTA)
                        .setTimeRange(start, end, TimeUnit.MILLISECONDS)
                        .build())
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse response) {

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // HH=24h, hh=12h
                        String str3 = df.format(start);
                        String str4 = df.format(end);
                        Log.i("crkim", "cal start: " + str3);
                        Log.i("crkim", "cal end: " + str4);

                        DataSet dataSet = response.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
                        DataSet dataSet2 = response.getDataSet(DataType.TYPE_DISTANCE_DELTA);
                        DataSet dataSet3 = response.getDataSet(DataType.TYPE_CALORIES_EXPENDED);

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            Log.i("crkim", dp.getDataType().getName());

                            for (Field field : dp.getDataType().getFields()) {

                                Log.i("crkim", field.getName());
                                Log.i("crkim", dp.getValue(field).toString());
                                totalSteps += dp.getValue(field).asInt();
                            }
                        }

                        textView4.setText("총걸음수: " + totalSteps + " 걸음");

                        for (DataPoint dp2 : dataSet2.getDataPoints()) {
                            Log.i("crkim", dp2.getDataType().getName());
                            for (Field field : dp2.getDataType().getFields()) {
                                Log.i("crkim", dp2.getValue(field).toString());

                                totalDistance += dp2.getValue(field).asFloat();
                            }
                        }

                        textView5.setText("총운동거리: " + (int) Math.floor(totalDistance) + " m");


                        for (DataPoint dp3 : dataSet3.getDataPoints()) {
                            Log.i("crkim", dp3.getDataType().getName());
                            for (Field field : dp3.getDataType().getFields()) {

                                totalCalories += dp3.getValue(field).asFloat();

                            }
                        }

                        textView3.setText("소모열량: " + (int) Math.floor(totalCalories) + " 칼로리");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Status", "Failure", e);
                    }
                });
    }

    public void upload() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db4 = FirebaseFirestore.getInstance();

        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // HH=24h, hh=12h
        String now2 = df.format(now);

        Map<String, Object> member = new HashMap<>();
        member.put("1.운동종류",ex);
        member.put("2.운동시간",totalStepTime);
        member.put("3.rpe",borg);
        member.put("4.걸움수",totalSteps);
        member.put("5.소모열량",totalCalories);
        member.put("6.운동거리", totalDistance);
        member.put("7.문의",et.getText().toString());
        db4.collection(firebaseUser.getEmail())
                .document(now2 + "앱운동")
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("crkim", "success");

                      /*  ResultItem item = new ResultItem();
                        item.setEmail(firebaseUser.getEmail());
                        item.setDate(now2);
                        item.setType(ex);
                        item.setSteps(totalSteps);
                        item.setTime(totalStepTime);
                        item.setRpe(borg);
                        item.setComment(et.getText().toString());
                        databaseReference.child("data").push().setValue(item);

                       */

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("crkim", "failure");
                    }
                });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 로그인 성공시
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {


            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ExResultActivity.this, BorgActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();
    }

    public void save(){

        DateFormat df2 = new SimpleDateFormat("yyyy년MM월dd일 HH시mm분");
        String start2 = df2.format(start);

        insert(ex, Integer.parseInt(totalStepTime2), Integer.parseInt(totalStepTime3), borg, start, start2);
    }

    public void insert(String TYPE, int HOUR, int MIN, int RPE, long DATE2, String DATE) {
        database.insertRecord(TYPE, HOUR, MIN, RPE, DATE2, DATE);

    }
}