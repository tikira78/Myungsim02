package com.msba.myungsim02;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.msba.myungsim02.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WatchActivity extends FragmentActivity implements SensorEventListener, DataClient.OnDataChangedListener,
        AmbientModeSupport.AmbientCallbackProvider{

    TextView textView1, textView2;
    ImageButton lap, pause, restart;
    long MillisecondTime, now, StartTime, EndTime, PauseTime = 0, RestartTime, sumPauseTime, sumRestartTime;
    String getTime;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;

    SQLiteDatabase db1, db2;
    WatchDBHelper helper1, helper2;

    private SensorManager sensorManager;
    private Sensor sensor;

    Vibrator vibrator;

    private final static String TAG = "Wear MainActivity";

    int num = 1;
    int i;
    String datapath = "/data_path";

    private AmbientModeSupport.AmbientController ambientController;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        // Enables Always-on
        ambientController = AmbientModeSupport.attach(this);
        i = 1;

    textView1 = (TextView) findViewById(R.id.tv2);
    textView2 = findViewById(R.id.text);
    //send a message from the wear.  This one will not have response.

    pause =  findViewById(R.id.wrbutton1);
    restart =  findViewById(R.id.wrbutton2);
    lap = findViewById(R.id.wrbutton3);



        //dbase
        helper2 = new WatchDBHelper(WatchActivity.this);
        db2 = helper2.getWritableDatabase();
        db2.delete("tb_time", null, null);
        Log.i("crkim","db deletede");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

    handler = new Handler();

    //
    handler.postDelayed(runnable, 0);
    StartTime = System.currentTimeMillis();
    Log.i("crkim", "Start time:" + String.valueOf(StartTime));
     ContentValues values = new ContentValues();
    values.put("starttime", StartTime);
    db2.insert("tb_time", null, values);

    pause.setEnabled(true);
    restart.setEnabled(false);
    lap.setEnabled(false);

    pause.setVisibility(View.VISIBLE);
    restart.setVisibility(View.INVISIBLE);
    lap.setVisibility(View.INVISIBLE);


    //일시정지
    pause.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            PauseTime = System.currentTimeMillis();
            Log.i("crkim", "pause time1:" + String.valueOf(PauseTime));
            handler.removeCallbacks(runnable);

            ContentValues values = new ContentValues();
            values.put("pausetime", PauseTime);
            db2.insert("tb_time", null, values);

            pause.setEnabled(false);
            restart.setEnabled(true);
            lap.setEnabled(true);

            pause.setVisibility(View.INVISIBLE);
            restart.setVisibility(View.VISIBLE);
            lap.setVisibility(View.VISIBLE);

            sensorManager.unregisterListener(WatchActivity.this);
            Wearable.getDataClient(WatchActivity.this).removeListener(WatchActivity.this);

        }
    });

    //restart
    restart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RestartTime = System.currentTimeMillis();
            Log.i("crkim", "restart time1:" + String.valueOf(RestartTime));

            ContentValues values = new ContentValues();
            values.put("restarttime", RestartTime);
            db2.insert("tb_time", null, values);
            handler.postDelayed(runnable, 0);

            pause.setEnabled(true);
            restart.setEnabled(false);
            lap.setEnabled(false);

            pause.setVisibility(View.VISIBLE);
            restart.setVisibility(View.INVISIBLE);
            lap.setVisibility(View.INVISIBLE);

            sensorManager.registerListener(WatchActivity.this, sensor, 3);
            Wearable.getDataClient(WatchActivity.this).addListener(WatchActivity.this);

        }
    });

    //시작
    lap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        String message = "Hello device " + num;
        //Requires a new thread to avoid blocking the UI
        sendData(message);

        db1.close();
        db2.close();

        handler.removeCallbacks(runnable);

        sensorManager.unregisterListener(WatchActivity.this);
        Wearable.getDataClient(WatchActivity.this).removeListener(WatchActivity.this);

        }
        });



        }//oncreate end




    public Runnable runnable = new Runnable() {
        public void run() {

            Cursor c1 = db2.rawQuery("select SUM(pausetime) FROM tb_time", null);
            Cursor c2 = db2.rawQuery("select SUM(restarttime) FROM tb_time", null);

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
                textView2.setText("" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds) + "." + String.format("%03d", MilliSeconds));

                handler.postDelayed(this, 0);


            } else {

                MillisecondTime = System.currentTimeMillis() - StartTime;
                Seconds = (int) (MillisecondTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (MillisecondTime % 1000);
                textView2.setText("" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds) + "." + String.format("%03d", MilliSeconds));

                handler.postDelayed(this, 0);

            }
        }

    };

protected void onResume() {
    super.onResume();
    sensorManager.registerListener(WatchActivity.this, sensor, 3);
    Wearable.getDataClient(WatchActivity.this).addListener(WatchActivity.this);


    if (pause.isEnabled()) {

        handler.postDelayed(runnable, 0);

    }
}

@Override
protected void onPause() {
    super.onPause();

    if (pause.isEnabled()) {
        handler.removeCallbacks(runnable);

    }
}

@Override
public void onSensorChanged(SensorEvent event) {
        int i = (int) event.values[0];
        textView1.setText("" + i + "회/분");
        sendData(String.valueOf(i));

        //db_bpm
        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getTime = dateFormat.format(date);

        helper1 = new WatchDBHelper(WatchActivity .this);
        db1= helper1.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("BPM",i); //column, value
        values1.put("Date", now);
        values1.put("Date1", getTime);
        db1.insert("tb_bpm", null, values1);

        Log.i("crkim","bpm: " +i);

        //bpm check
        checkbpm(i);

        }

  public void checkbpm(int i) {

        if(i>100){
            textView1.setTextColor(Color.RED);
            // 1초 진동
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));


        }else{
            textView1.setTextColor(Color.GREEN);
        }

        }

@Override
public void onAccuracyChanged(Sensor sensor, int i) {

        }
@Override
public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "onDataChanged: " + dataEventBuffer);
        for (DataEvent event : dataEventBuffer) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
        String path = event.getDataItem().getUri().getPath();
        if (datapath.equals(path)) {
        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
        String message = dataMapItem.getDataMap().getString("message");
        Log.v(TAG, "Wear activity received message: " + message);
        // Display message in UI
        //mTextView.setText(message);

        } else {
        Log.e(TAG, "Unrecognized path: " + path);
        }
        } else if (event.getType() == DataEvent.TYPE_DELETED) {
        Log.v(TAG, "Data deleted : " + event.getDataItem().toString());
        } else {
        Log.e(TAG, "Unknown data event Type = " + event.getType());
        }
        }
        }

    /**
    * Sends the data, note this is a broadcast, so we will get the message as well.
    */
    private void sendData(String message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(datapath);
        dataMap.getDataMap().putString("message", message);
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();

        Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
        dataItemTask
        .addOnSuccessListener(new OnSuccessListener<DataItem>() {
    @Override
    public void onSuccess(DataItem dataItem) {
        Log.d(TAG, "Sending message was successful: " + dataItem);
        }
        })
        .addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Sending message failed: " + e);
        }
        })
        ;
        }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
        }


    private class MyAmbientCallback extends AmbientModeSupport.AmbientCallback {
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        // Handle entering ambient mode
        super.onEnterAmbient(ambientDetails);

        textView1.setTextColor(Color.RED);
        textView1.setBackgroundColor(Color.WHITE);
        textView1.getPaint().setAntiAlias(false);
    }

    @Override
    public void onExitAmbient() {
        // Handle exiting ambient mode
        textView1.setBackgroundColor(Color.CYAN);
        textView1.getPaint().setAntiAlias(true);
        textView1.setTextColor(Color.BLACK);
        super.onExitAmbient();
    }

    @Override
    public void onUpdateAmbient() {
        // Update the content
        super.onUpdateAmbient();
        //mTextView.setText("Hello Square World! " + i);
        i++;
    }
}



}
