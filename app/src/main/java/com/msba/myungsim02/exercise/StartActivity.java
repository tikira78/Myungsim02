package com.msba.myungsim02.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.msba.myungsim02.R;

public class StartActivity extends AppCompatActivity {

    private TextView tv_1;
    private CountDownTimer countDownTimer;
    private int count = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.count);


        Button btn_1 = (Button) findViewById(R.id.btn_start);
        tv_1 = findViewById(R.id.tv_count);



        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_1.setVisibility(View.GONE);
                countDownTimer();
                countDownTimer.start();

            }
        });


    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tv_1.setText(String.valueOf(count));
                count--;

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(StartActivity.this, ExerciseActivity.class);
                startActivity(intent);
                finish();

            }

        };
    }


    @Override
    public void onBackPressed() {

        /*
        countDownTimer.cancel();

        Intent intent = new Intent(StartActivity.this, ExTwoActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();

         */
    }
}

