package com.msba.myungsim02.risk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.msba.myungsim02.R;

public class RiskActivity extends AppCompatActivity {

    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.msba.myungsim02.R.layout.activity_risk);

        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cb6 = findViewById(R.id.checkBox6);
        cb7 = findViewById(R.id.checkBox7);
        cb8 = findViewById(R.id.checkBox8);
        cb9 = findViewById(R.id.checkBox9);
        cb10 = findViewById(R.id.checkBox10);
        cb11 = findViewById(R.id.checkBox11);

        btn1 = findViewById(R.id.btn_check);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result ="";
                if(!cb1.isChecked()&&!cb2.isChecked()&&!cb3.isChecked()&&!cb4.isChecked()&&!cb5.isChecked()&&!cb6.isChecked()&&!cb7.isChecked()&&!cb8.isChecked()&&!cb9.isChecked()&&!cb10.isChecked()&&!cb11.isChecked()){

                    Toast.makeText(getApplicationContext(), "위험인자가 없습니다.", Toast.LENGTH_SHORT).show();

                }else {

                    if (cb1.isChecked()) result += 1;
                    if (cb2.isChecked()) result += 2;
                    if (cb3.isChecked()) result += 3;
                    if (cb4.isChecked()) result += 4;
                    if (cb5.isChecked()) result += 5;
                    if (cb6.isChecked()) result += 6;
                    if (cb7.isChecked()) result += 7;
                    if (cb8.isChecked()) result += 8;
                    if (cb9.isChecked()) result += 9;
                    if (cb10.isChecked()) result += "a";
                    if (cb11.isChecked()) result += "b";


                    Log.i("crkim", "check: " + result);

                    Intent intent = new Intent(RiskActivity.this, RiskResultActivity.class);
                    intent.putExtra("check", result);
                    startActivity(intent);
                }
            }
        });

        }


}