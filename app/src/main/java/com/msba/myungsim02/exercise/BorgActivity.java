package com.msba.myungsim02.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.R;

public class BorgActivity extends AppCompatActivity {

    SeekBar seekbar;
    private Button upload;
    //private long start,end;
    private TextView textView, textView2;
    private int borg;
    //private int borg, getInt;
    ExerciseDBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borg);


        seekbar = findViewById(R.id.borg);
        textView = findViewById(R.id.tv_intensity);
        textView2 = findViewById(R.id.tv_intensity_result);
        upload = (Button) findViewById(R.id.btn_upload);

        //
        String data = textView.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(data);
        int start=data.indexOf("좌측 빨간색 동그라미");
        int end=start+"좌측 빨간색 동그라미".length();
        builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 90, 0)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);

        //database
        helper = new ExerciseDBHelper(BorgActivity.this);
        db = helper.getWritableDatabase();

        setborg();


        //저장
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("crkim",String.valueOf(borg));

                if(borg==0) {

                    Toast.makeText(BorgActivity.this, "운동자각도를 체크 해주세요",Toast.LENGTH_SHORT).show();

                }else {

                    ContentValues values = new ContentValues();
                    values.put("borg", borg);
                    db.delete("tb_borg", null, null);
                    db.insert("tb_borg", null, values);
                    // db.update("tb_borg", values, "_id = ?", new String[]{"1"});
                    //db.close();


                    Intent intent = new Intent(BorgActivity.this, ExResultActivity.class);
                    startActivity(intent);

                    //
                    finish();
                }
            }
        });


    }
    private void setborg() {


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                borg = progress;
                textView2.setText("이번 운동 강도는 " + String.format("%02d", progress)  + "점이었습니다.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(BorgActivity.this, "오늘의 운동강도를 선택해주세요!",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고");
        builder.setMessage("운동을 저장하지 않고 종료하시겠습니끼?");
        builder.setPositiveButton("네", dialoglistner);
        builder.setNegativeButton("아니오", null);
        builder.show();

        // super.onBackPressed();
    }

    DialogInterface.OnClickListener dialoglistner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            //화면전
            Intent intent = new Intent(BorgActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);


        }
    };



}
