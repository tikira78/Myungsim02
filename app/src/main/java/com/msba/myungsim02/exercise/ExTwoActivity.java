package com.msba.myungsim02.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.msba.myungsim02.R;

import java.util.ArrayList;
import java.util.List;

public class ExTwoActivity extends AppCompatActivity {

    private RadioButton rb_1,rb_2,rb_3,rb_4,rb_5;
    private RadioGroup rg;
    private Button btn_1;

    boolean googleFitPermission;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int AUTH_REQUEST = 1;
    private static final int REQUEST_PERMISSION_CODE = 12345;
    private List<String> missingPermission = new ArrayList<>();
    private boolean bCheckStarted = false;
    private boolean bGoogleConnected = false;
    private String TAG = "crkim";
    private GoogleApiClient googleApiClient;
    private boolean authInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_two);

        //
        checkAndRequestPermissions();

        //database
        ExerciseDBHelper helper = new ExerciseDBHelper(ExTwoActivity.this);
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
                    Toast.makeText(ExTwoActivity.this,"운동을 하나 선택해 주세요!", Toast.LENGTH_SHORT).show();

                }else {

                    ContentValues values = new ContentValues();
                    values.put("kind",rg.getCheckedRadioButtonId());
                    db.delete("tb_kind", null, null);
                    db.insert("tb_kind", "0", values);
                    //   db.update("tb_kind", values, "_id = ?", new String[]{"1"});
                    db.close();

                    Intent intent = new Intent(ExTwoActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ExTwoActivity.this, ExOneActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    public void checkAndRequestPermissions (){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("crkim", "권한 설정 완료");
            } else {
                Log.d("crkim", "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }

    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("crkim", "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("crkim", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

}

