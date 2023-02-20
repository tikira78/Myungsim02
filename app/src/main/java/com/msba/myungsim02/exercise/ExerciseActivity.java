package com.msba.myungsim02.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.msba.myungsim02.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExerciseActivity extends AppCompatActivity {

    TextView textView, textView1, textView2;
    Button lap, pause, restart;
    long MillisecondTime, StartTime, EndTime, PauseTime=0, RestartTime, IntervalTime = 0, sumPauseTime, sumRestartTime;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 100;
    private String TAG = "crkim";

    private int value3, value4;
    private FitnessOptions fitnessOptions;
    ExerciseDBHelper helper;
    SQLiteDatabase db, db3;

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    private boolean bCheckStarted = false;
    private boolean bGoogleConnected = false;

    private OnDataPointListener mListener;

    boolean googleFitPermission;

    LottieAnimationView laView;
    int val;

    int i = 0;
    private int getInt2, total,step1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        helper = new ExerciseDBHelper(ExerciseActivity.this);
        db = helper.getWritableDatabase();
        db.delete("tb_time", null, null);

        //
        lap = (Button) findViewById(R.id.button4);
        pause = (Button) findViewById(R.id.btn_pause);
        restart = (Button) findViewById(R.id.btn_restart);
        textView1 = findViewById(R.id.tv_step);
        textView = findViewById(R.id.tv_dist);

        textView1.setText("걸음수: 0 걸음");

        //lottiefile
        laView = (LottieAnimationView) findViewById(R.id.walk);

        //
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .build();

        addData(); // 선택한 운동 종류 가져오

        //
        StartTime = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put("starttime", StartTime);
        db.insert("tb_time", null, values);

        handler = new Handler();
        handler.postDelayed(runnable, 0);

        register();

        pause.setEnabled(true);
        restart.setEnabled(false);
        lap.setEnabled(false);

        restart.setVisibility(View.INVISIBLE);
        lap.setVisibility(View.INVISIBLE);

        //일시정지
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PauseTime = System.currentTimeMillis();
                ContentValues values = new ContentValues();
                values.put("pausetime", PauseTime);
                db.insert("tb_time", null, values);

                handler.removeCallbacks(runnable);

                remove();

                pause.setEnabled(false);
                restart.setEnabled(true);
                lap.setEnabled(true);

                pause.setVisibility(View.INVISIBLE);
                lap.setVisibility(View.VISIBLE);
                restart.setVisibility(View.VISIBLE);

                laView.pauseAnimation(); // lottie
            }

        });

        //restart
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RestartTime = System.currentTimeMillis();
                ContentValues values = new ContentValues();
                values.put("restarttime", RestartTime);
                db.insert("tb_time", null, values);
                handler.postDelayed(runnable, 0);

                findFitnessDataSources();

                restart.setEnabled(false);
                pause.setEnabled(true);
                lap.setEnabled(false);

                restart.setVisibility(View.INVISIBLE);
                lap.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);

                laView.playAnimation(); // lottie
            }
        });


        //운동종료
        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EndTime = System.currentTimeMillis();

                helper = new ExerciseDBHelper(ExerciseActivity.this);
                db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("endtime", EndTime);
                db.insert("tb_time", null, values);

                remove();

                handler.removeCallbacks(runnable);

                //화면전
                Intent intent = new Intent(ExerciseActivity.this, FinishActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addData() {
        helper = new ExerciseDBHelper(ExerciseActivity.this);
        db3 = helper.getWritableDatabase();

        Cursor cursor = db3.rawQuery("select kind from tb_kind where _id=1", null);
        while(cursor.moveToNext()) {
            Log.i("crkim", String.valueOf(cursor.getInt(0)));
            val = cursor.getInt(0);
        }
        cursor.close();

        if(val==R.id.ex1){
            laView.setAnimation("walk1.json");
            laView.loop(true);
            laView.playAnimation();
        } else if (val==R.id.ex2){
            laView.setAnimation("run2.json");
            laView.loop(true);
            laView.playAnimation();
        }else if (val==R.id.ex3) {
            laView.setAnimation("tread1.json");
            laView.loop(true);
            laView.playAnimation();
        }  else if (val==R.id.ex4) {
            laView.setAnimation("bike1.json");
            laView.loop(true);
            laView.playAnimation();
            textView1.setVisibility(View.INVISIBLE);
        }else if (val==R.id.ex5) {
            laView.setAnimation("others1.json");
            laView.loop(true);
            laView.playAnimation();
            textView1.setVisibility(View.INVISIBLE);
        }else{}
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            Cursor c1 = db.rawQuery("select SUM(pausetime) FROM tb_time", null);
            Cursor c2 = db.rawQuery("select SUM(restarttime) FROM tb_time", null);

            if (c1.getCount() != 0 && c2.getCount() != 0) {
                if(c1.moveToFirst()) {
                    sumPauseTime = c1.getLong(0);
                }
                if(c2.moveToFirst()) {
                    sumRestartTime = c2.getLong(0);
                }

                c1.close();
                c2.close();

                long intervalTime = sumRestartTime - sumPauseTime;

                Log.i("crkim", "interval time:" + String.valueOf(intervalTime));

                MillisecondTime = System.currentTimeMillis() - StartTime - intervalTime;
                Seconds = (int) (MillisecondTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (MillisecondTime % 1000);
                textView.setText("" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds) + "." + String.format("%03d", MilliSeconds));

                handler.postDelayed(this, 0);


            } else {

                MillisecondTime = System.currentTimeMillis() - StartTime;
                Seconds = (int) (MillisecondTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (MillisecondTime % 1000);
                textView.setText("" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds) + "." + String.format("%03d", MilliSeconds));

                handler.postDelayed(this, 0);

            }
        }

    };

    private void register() {
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(this, REQUEST_OAUTH_REQUEST_CODE, GoogleSignIn.getLastSignedInAccount(this), fitnessOptions);
        } else {
            Fitness.getRecordingClient(this,
                            GoogleSignIn.getLastSignedInAccount(this))
                    .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("crkim", "Successfully subscribed!");
                                        findFitnessDataSources();
                                    } else {
                                        Log.w("crkim", "There was a problem subscribing.", task.getException());
                                    }
                                }
                            });
        }
    }

    private void remove() {
        Fitness.getSensorsClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .remove(mListener)
                .addOnSuccessListener(unused ->
                        Log.i("crkim", "Listener was removed!"))
                .addOnFailureListener(e ->
                        Log.i("crkim", "Listener was not removed."));
    }

    private void findFitnessDataSources() {
        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .findDataSources(new DataSourcesRequest.Builder()
                        .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                        .setDataSourceTypes(com.google.android.gms.fitness.data.DataSource.TYPE_DERIVED)
                        .build())
                .addOnSuccessListener(new OnSuccessListener<List<com.google.android.gms.fitness.data.DataSource>>() {
                    @Override
                    public void onSuccess(List<com.google.android.gms.fitness.data.DataSource> dataSources) {
                        for (com.google.android.gms.fitness.data.DataSource dataSource : dataSources) {
                            Log.i("crkim", "Data source found: " + dataSource.toString());
                            Log.i("crkim", "Data Source type: " + dataSource.getDataType().getName());
                            // Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) //&& mListener == null                                    )
                            {
                                Log.i("crkim", "Data source for LOCATION_SAMPLE found!  Registering.");
                                registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
                            }
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("crkim", "Find data sources request failed", e));
    }


    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        // [START register_data_listener]
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Value val = dataPoint.getValue(field);
                    Log.i("crkim", "Detected DataPoint value3: " + val);
                    value3 += val.asInt();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("crkim", "Detected DataPoint value3: " + value3);

                            textView1.setText("걸음수: " + value3 + " 걸음");
                        }
                    });
                }//db.close();
            }
        };

        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .add(new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(1, TimeUnit.SECONDS)
                        .build(), mListener)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("crkim", "Listener registered!");
                                } else {
                                    Log.e("crkim", "Listener not registered.", task.getException());
                                }
                            }
                        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pause.isEnabled()) {

            handler.postDelayed(runnable, 0);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        remove();//step count sensor
    }

    @Override
    public void onBackPressed() {

        PauseTime = System.currentTimeMillis();
        handler.removeCallbacks(runnable);

        remove();

        laView.pauseAnimation(); // lottie

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고");
        builder.setMessage("운동을 저장하지 않고 종료하시겠습니끼?");
        builder.setPositiveButton("운동종료", dialoglistner);
        builder.setNegativeButton("운동재개", dialoglistner2);
        builder.show();

    }

    DialogInterface.OnClickListener dialoglistner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            remove();

            handler.removeCallbacks(runnable);

            //화면전
            Intent intent = new Intent(ExerciseActivity.this, ExOneActivity.class);
            startActivity(intent);
            finish();
        }
    };

    DialogInterface.OnClickListener dialoglistner2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            RestartTime = System.currentTimeMillis();
            IntervalTime = RestartTime - PauseTime;
            Log.i("crkim", String.valueOf(IntervalTime));

            ContentValues values = new ContentValues();
            values.put("intervaltime", IntervalTime);
            db.insert("tb_pausetime", null, values);
            handler.postDelayed(runnable, 0);

            findFitnessDataSources();

            restart.setEnabled(false);
            pause.setEnabled(true);
            lap.setEnabled(false);

            restart.setVisibility(View.INVISIBLE);
            lap.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);

            laView.playAnimation(); // lottie
        }
    };



}







