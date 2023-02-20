package com.msba.myungsim02.wear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

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


public class WearActivity extends AppCompatActivity implements DataClient.OnDataChangedListener,
        View.OnClickListener {

    String datapath = "/data_path";
    Button mybutton;
    TextView logger;
    String TAG = "crkim";
    int num = 1;

    private AlertDialog alertDialog;
    NumberPicker picker1,picker2,picker3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.msba.myungsim02.R.layout.activity_wear);
        //get the widgets
        mybutton = findViewById(R.id.sendbtn);
        mybutton.setOnClickListener(this);
        logger = findViewById(R.id.logger);

       // init();

        //
        picker1 = (NumberPicker) findViewById(R.id.np_bpm);
        picker1.setMinValue(50);
        picker1.setMaxValue(200);
        //picker1.setWrapSelectorWheel(false);

        picker2 = (NumberPicker) findViewById(R.id.np_hour);
        picker2.setMinValue(0);
        picker2.setMaxValue(12);

        picker3 = (NumberPicker) findViewById(R.id.np_min);
        picker3.setMinValue(0);
        picker3.setMaxValue(60);
        //picker1.setWrapSelectorWheel(false);


    }

    // add data listener
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    //remove data listener
    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }

    /**
     * simple method to add the log TextView.
     */
    public void logthis(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            logger.setText("\n" + newinfo);
        }
    }

    //button listener
    @Override
    public void onClick(View v) {
        String message = "Hello wearable " + num;
        //Requires a new thread to avoid blocking the UI
        sendData(message);
        num++;
    }

    /**
     * Receives the data, note since we are using the same data base, we will also receive
     * data that we sent as well.  Likely should add some kind of id number to datamap, so it
     * can tell between mobile device and wear device.  or this maybe the functionality you want.
     */
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "onDataChanged: " + dataEventBuffer);
        for (com.google.android.gms.wearable.DataEvent event : dataEventBuffer) {
            if (event.getType() == com.google.android.gms.wearable.DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (datapath.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String message = dataMapItem.getDataMap().getString("message");
                    Log.v(TAG, "Wear activity received message: " + message);
                    // Display message in UI
                    logthis(message);
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

    private void init(){
        String message = "스마트워치와 연결되었습니다.";
        //Requires a new thread to avoid blocking the UI
       // sendData0(message);

    }
    /**
     * Sends the data.  Since it specify a client, everyone who is listening to the path, will
     * get the data.
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
                        Toast.makeText(WearActivity.this, "성공적으로 전달되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Sending message failed: " + e);
                        Toast.makeText(WearActivity.this, "스마트폰과 스마트워치의 연결을 확인하시도 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
    private void sendData0(String message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(datapath);
        dataMap.getDataMap().putString("message", message);
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();

        Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
        dataItemTask
                .addOnSuccessListener(new OnSuccessListener<DataItem>() {
                    @Override
                    public void onSuccess(DataItem dataItem) {
                        Log.d(TAG, "connection was successful: " + dataItem);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "connection failed: " + e);
                        sendData0(message);
                    }
                });
    }

     */

}