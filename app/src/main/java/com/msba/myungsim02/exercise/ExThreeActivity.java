package com.msba.myungsim02.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msba.myungsim02.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExThreeActivity extends AppCompatActivity {

    boolean boo = true;
    Spinner spinner;
    int sbValue;
    TextView tvresult;

    private ExdaAdapter exdaAdapter = null;
    private TextView textView1;
    private String getTime, getTime2, getTime3, getTime4,getTime5,getTime6,getTime7, yy, mm, dd;
    private ExdaDatabase database;

    long now, now2;
    boolean isOpen;
    private AlertDialog alertDialog;
    NumberPicker picker1,picker2;
    SeekBar seekBar;
    boolean isExist;

    EditText et;

    int hh,mm2;

    private FirebaseUser firebaseUser;
    DocumentReference docR;
    FirebaseFirestore db4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_three);

        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = ExdaDatabase.getInstance(this);
        boolean isOpen = database.open();
        if (isOpen) {
            Log.i("crkim", " database is open.");
        } else {
            Log.i("crkim", "database is not open.");
        }

        exdaAdapter = new ExdaAdapter();

        Button dateBtn = (Button) findViewById(R.id.button3);
        Button regBtn = (Button) findViewById(R.id.recordBtn1);
        Button resultBtn = (Button) findViewById(R.id.resultBtn1);
        dateBtn.setOnClickListener(this::onClick);
        regBtn.setOnClickListener(this::onClick);
        resultBtn.setOnClickListener(this::onClick);

        et = findViewById(R.id.editTextTextMultiLine);

        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd aa");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormat5 = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormat6 = new SimpleDateFormat("mm");

        getTime = dateFormat.format(date);
        //getTime2 = dateFormat1.format(date);
        getTime3 = dateFormat2.format(date);
        getTime4 = dateFormat3.format(date);
        getTime5 = dateFormat4.format(date);
        getTime6 = dateFormat5.format(date);
        getTime7 = dateFormat6.format(date);


        now2 = Long.parseLong(getTime3);

        textView1 = findViewById(R.id.upDate_ed);
        //textView1.setText(getTime);
        tvresult = findViewById(R.id.seebar_result);


        picker1 = (NumberPicker) findViewById(R.id.np_hour);
        picker1.setMinValue(0);
        picker1.setMaxValue(5);
        //picker1.setWrapSelectorWheel(false);

        picker2 = (NumberPicker) findViewById(R.id.np_min);
        picker2.setMinValue(0);
        picker2.setMaxValue(59);

        spinner = findViewById(R.id.spinner_ed);
        ArrayAdapter aa = ArrayAdapter.createFromResource(this, R.array.ed, R.layout.spinner_style);
        aa.setDropDownViewResource(R.layout.spin_dropdown);
        spinner.setAdapter(aa);
        spinner.setPrompt("운동을 선택해주세요");
        spinner.setDropDownVerticalOffset(80);
        //spinner.setDropDownHorizontalOffset(20);

        //        spinner.getSelectedItem().toString(); // 선택된 스피너의 String 값 영화1
        //spinner.setSelection(3);              // 특정 번호의 아이템을 임의 지정할때
        // final ImageView imageView1 = findViewById(R.id.imageView1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),items[position],Toast.LENGTH_SHORT).show();
//                imageView1.setImageResource(imageList[position]);

                if (boo) {
                    boo = false;
                    return;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                sbValue = i + 5;

                if (sbValue == 5) {

                    tvresult.setText("");
                } else if (sbValue < 8) {
                    tvresult.setText(String.valueOf(sbValue) + "점 (매우 약한)");
                    tvresult.setTextColor(Color.BLUE);
                } else if (sbValue < 12) {
                    tvresult.setText(String.valueOf(sbValue) + "점 (약한)");
                    tvresult.setTextColor(Color.parseColor("#009933"));
                } else if (sbValue < 15) {
                    tvresult.setText(String.valueOf(sbValue) + "점 (약간 힘든)");
                    tvresult.setTextColor(Color.parseColor("#996633"));
                } else if (sbValue < 17) {
                    tvresult.setText(String.valueOf(sbValue) + "점 (힘든)");
                    tvresult.setTextColor(Color.MAGENTA);
                } else {
                    tvresult.setText(String.valueOf(sbValue) + "점 (매우 힘든)");
                    tvresult.setTextColor(Color.RED);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.button3:

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        yy = String.valueOf(i);
                        mm = String.valueOf(i1+1);
                        dd = String.valueOf(i2);
                        timeselect();

                    }
                }, Integer.parseInt(getTime3), Integer.parseInt(getTime4)-1, Integer.parseInt(getTime5));
                datePickerDialog.show();



            case R.id.recordBtn1:
                regRecord();
                break;

            case R.id.resultBtn1:
                Intent intent= new Intent(ExThreeActivity.this,ExdaResultActivity.class);
                startActivity(intent);
                finish();
                break;


        }

    }

    public boolean getPackageList() {
        isExist = false;

        PackageManager pkgMgr = this.getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith("com.xiaomi.hm.health")){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }

    private void regRecord() {

        if(textView1.getText().toString().equals("")) {
            Toast.makeText(this, "운동을 시작한 시각을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (spinner.getSelectedItem().toString().equals("여기를 클릭해서 선택하세요")) {
            Toast.makeText(this, "운동을 선택해주세요", Toast.LENGTH_SHORT).show();
        }else if(picker1.getValue()==0 && picker2.getValue() == 0) {
            Toast.makeText(this, "운동시간을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (sbValue==5||sbValue==0) {
            Toast.makeText(this, "운동강도를 입력해주세요", Toast.LENGTH_SHORT).show();
        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("저장");
            builder.setMessage("운동종류: " + spinner.getSelectedItem().toString()+ "\n운동시간: " + picker1.getValue() +
                    "시간" + picker2.getValue() + "분 \n운동강도: " + sbValue + "점 \n으로 저장하시겠습니까?");
            builder.setPositiveButton("OK", dialoglistner);
            builder.setNegativeButton("NO", null);
            builder.show();

        }
    }


    DialogInterface.OnClickListener dialoglistner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            getTime2 = textView1.getText().toString();
            //Toast.makeText(getActivity(), "정보를 추가했습니다.", Toast.LENGTH_SHORT).show();
            insert(spinner.getSelectedItem().toString(), picker1.getValue(), picker2.getValue(), sbValue, now, getTime2);

            spinner.setPrompt("걷기(천천히)");
            picker1.setValue(0);
            picker2.setValue(0);
            seekBar.setProgress(7);

            upload();

            Intent intent= new Intent(ExThreeActivity.this,ExdaResultActivity.class);
            startActivity(intent);
            finish();


        }
    };

    public void upload() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db4 = FirebaseFirestore.getInstance();

        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // HH=24h, hh=12h
        String now2 = df.format(now);

        Map<String, Object> member = new HashMap<>();
        member.put("1.운동종류",spinner.getSelectedItem().toString());
        member.put("2.운동시간",picker1.getValue() + "시간" + picker2.getValue() + "분");
        member.put("3.rpe",sbValue);
        member.put("7.문의",et.getText().toString());
        db4.collection(firebaseUser.getEmail())
                .document(now2 + "기록만")
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("crkim", "success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("crkim", "failure");
                    }
                });
    }

    public void insert(String TYPE, int HOUR, int MIN, int RPE, long DATE2, String DATE) {
        database.insertRecord(TYPE, HOUR, MIN, RPE, DATE2, DATE);

    }

    public void timeselect() {
        TimePickerDialog timepickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hh=i;
                mm2=i1;

                textView1.setText(yy + "년" +  mm +"월" + dd +"일 " + String.valueOf(hh) + "시" + String.valueOf(mm2) + "분"  );
            }
        }, Integer.parseInt(getTime6), Integer.parseInt(getTime7), true);
        timepickerDialog.show();


    }


}