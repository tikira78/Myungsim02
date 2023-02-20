package com.msba.myungsim02.CPET;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.msba.myungsim02.R;
import java.io.File;

public class CPETresultActivity extends AppCompatActivity {
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
      SubsamplingScaleImageView imageView;
    CPETdatabase database;
    Long image;
    String image2,getString,getString1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpetresult);

        //
        Intent intent =getIntent();
        image = intent.getLongExtra("image",-1);
        image2 = String.valueOf(image);
        Log.i("crkim","image2:" + image2);

        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = CPETdatabase.getInstance(this);
        boolean isOpen = database.open();
        if (isOpen) {
            Log.d("crkim", " database is open.");
        } else {
            Log.d("crkim", "database is not open.");
        }

        imageView = findViewById(R.id.iv_loadCPET);
        textView = findViewById(R.id.tv_cpet);

        File storageDir = new File(getFilesDir() + "/capture");
        if(!storageDir.exists()){

            Toast.makeText(this,"저장된 결과가 아직 없습니다.",Toast.LENGTH_SHORT).show();

        }else {



            loadImage();
        }




    }

    private void loadImage() {


        Cursor cursor = database.rawQuery("select IMAGE from cpet where DATE2 =" + image2 );
        while(cursor.moveToNext()) {
            Log.i("crkim", cursor.getString(0));
            getString = cursor.getString(0);
        }
        cursor.close();

        Cursor cursor1 = database.rawQuery("select DATE from cpet where DATE2 =" + image2 );
        while(cursor1.moveToNext()) {
            Log.i("crkim", cursor1.getString(0));
            getString1 = cursor1.getString(0);
        }
        cursor1.close();


        imageView.setImage(ImageSource.uri("/data/data/com.msba.myungsim02/files/capture/"+getString));
        imageView.setDoubleTapZoomScale(0.6f);

        textView.setText(getString1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

}