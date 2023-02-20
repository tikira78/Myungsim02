package com.msba.myungsim02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.msba.myungsim02.databinding.ActivityCountBinding;

public class CountActivity extends Activity {

    private TextView mTextView;
    private ActivityCountBinding binding;
    private CountDownTimer countDownTimer;
    private int count = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTextView = binding.tvCount;
        countDownTimer();
        countDownTimer.start();
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mTextView.setText(String.valueOf(count));
                count--;

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(CountActivity.this, WatchActivity.class);
                startActivity(intent);
                finish();

            }

        };
    }
}