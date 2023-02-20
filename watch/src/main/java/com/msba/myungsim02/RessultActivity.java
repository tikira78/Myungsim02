package com.msba.myungsim02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.msba.myungsim02.databinding.ActivityRessultBinding;

public class RessultActivity extends Activity {

    private TextView mTextView;
    private ActivityRessultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRessultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTextView = binding.text;
    }
}