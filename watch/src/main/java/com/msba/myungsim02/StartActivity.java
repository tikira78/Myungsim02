package com.msba.myungsim02;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.msba.myungsim02.databinding.ActivityStartBinding;

public class StartActivity extends Activity {

    private TextView mTextView;
    private ActivityStartBinding binding;

    private RadioButton rb_1,rb_2,rb_3,rb_4,rb_5;
    private RadioGroup rg;
    private Button btn_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mTextView = binding.text;

        //permission
        checkAndRequestPermissions ();

        //
        WatchDBHelper helper = new WatchDBHelper(StartActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        rb_1 = (RadioButton) findViewById(R.id.ex1);
        rb_2 = (RadioButton) findViewById(R.id.ex2);
        rb_3 = (RadioButton) findViewById(R.id.ex3);
        rb_4 = (RadioButton) findViewById(R.id.ex4);
        rb_5 = (RadioButton) findViewById(R.id.ex5);

        rg = findViewById(R.id.rg_exercise);
        btn_1 = (Button) findViewById(R.id.btn_select);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(StartActivity.this,"운동을 하나 선택해 주세요!", Toast.LENGTH_SHORT).show();

                }else {

                    ContentValues values = new ContentValues();
                    values.put("kind",rg.getCheckedRadioButtonId());
                    db.delete("tb_kind", null, null);
                    db.insert("tb_kind", "0", values);
                    //   db.update("tb_kind", values, "_id = ?", new String[]{"1"});
                    db.close();

                    Intent intent = new Intent(StartActivity.this, CountActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    public void checkAndRequestPermissions (){

        if ( checkSelfPermission(Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("crkim", "권한 설정 완료");
        } else {
            Log.d("crkim", "권한 설정 요청");
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.BODY_SENSORS}, 1);
        }

    }


    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("crkim", "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.i("crkim", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }
}