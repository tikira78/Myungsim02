package com.msba.myungsim02.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.msba.myungsim02.R;

public class FinishActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        tv = findViewById(R.id.tv_finish);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        tv.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FinishActivity.this, BorgActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    @Override
    public void onBackPressed() {

        // super.onBackPressed();
    }



}