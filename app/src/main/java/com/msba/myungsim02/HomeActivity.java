package com.msba.myungsim02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.msba.myungsim02.CPET.CPETActivity;
import com.msba.myungsim02.CS.CSActivity;
import com.msba.myungsim02.Pace.PaceActivity;
import com.msba.myungsim02.auth.MainActivity;
import com.msba.myungsim02.auth.UserAccount;
import com.msba.myungsim02.exercise.ExActivity;
import com.msba.myungsim02.exercise.Exercise2Activity;
import com.msba.myungsim02.exercise.screen.WelcomeScreenKt;
import com.msba.myungsim02.info.InfoActivity;
import com.msba.myungsim02.point.DisDBHelper;
import com.msba.myungsim02.record.RecordActivity;
import com.msba.myungsim02.risk.RiskActivity;
import com.msba.myungsim02.wear.WearActivity;

import java.util.HashMap;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DocumentReference docR;
    FirebaseFirestore db;
    TextView textView2,textView1,tv_name;

    String feedbacklee,feedbackhong, lee, hong;
    Ringtone rt;
    LottieAnimationView laview;
    CircularSeekBar csbar;

    SQLiteDatabase db1,db2,db3,db4;
    DisDBHelper helper,helper2,helper3, helper4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FCM
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.i("crkim","Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "InstanceID Token: " + token;
                        Log.d("crkim", msg);
                    }
                });


        //Button
        Button btn1 = findViewById(R.id.btn_h1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn2 = findViewById(R.id.btn_h2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RiskActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn3 = findViewById(R.id.btn_h3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RecordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn4 = findViewById(R.id.btn_h4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ExActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn5 = findViewById(R.id.btn_h5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, WearActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn6 = findViewById(R.id.btn_h6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CPETActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn7 = findViewById(R.id.btn_h7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CSActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn8 = findViewById(R.id.btn_h8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PaceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        Button btn9 = findViewById(R.id.btn_h9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });
        //전화 button
        Button btn_phone = findViewById(R.id.phone);
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:0522508286")));
            }
        });



        //notification 알림 상태
        laview = findViewById(R.id.noti);
        laview.setAnimation("noti1.json");
        noti();

        laview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                laview.pauseAnimation();

                Intent intent = new Intent (HomeActivity.this,NotiActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.horizon_right,R.anim.not_move);
            }
        });

        //point
        csbar = (CircularSeekBar) findViewById(R.id.progressbar);
        csbar.animate();
        textView2 = findViewById(R.id.tv_score);
        point();
        tv_name =findViewById(R.id.tv_name);
        name();


        //swipe
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                point();
                //steps();
                noti();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public void noti(){

        helper3 = new DisDBHelper(HomeActivity.this);
        db3 = helper3.getWritableDatabase();

        Uri noti = Uri.parse("android.resource://com.msba.myungsim02/"+R.raw.noti);
        rt = RingtoneManager.getRingtone(this, noti);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docR = db.collection(firebaseUser.getEmail()).document("feedback");
        docR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        UserAccount userAccount = document.toObject(UserAccount.class);
                        lee = userAccount.getFeedback();

                        Cursor cursor1 = db3.rawQuery("select feedback from tb_feedback", null);
                        while(cursor1.moveToNext()) {
                            Log.i("crkim", String.valueOf(cursor1.getString(0)));
                            feedbacklee = cursor1.getString(0);
                        }
                        cursor1.close();

                        if(lee.equals(feedbacklee)){
                                                laview.pauseAnimation();
                          }else{
                                                laview.playAnimation();
                            rt.play();
                         }


                    } else {

                        Log.i("crkim","no document");

                        laview.playAnimation();
                        rt.play();

                        Map<String, Object> member = new HashMap<>();
                        member.put("feedback","운동을 시작하고 기록해 주세요!! 확인 후 조만간 피드백을 드리겠습니다.");
                        db.collection(firebaseUser.getEmail())
                                .document("feedback")
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

                        ContentValues values = new ContentValues();
                        values.put("feedback", "0"); //column, value
                        db1.delete("tb_feedback", null, null);
                        db1.insert("tb_feedback", null, values);
                        db1.close();

                    }
                }else{ Log.i("crkim","get failed with", task.getException());}
            }
        });


    }

    public void point(){

        helper = new DisDBHelper(HomeActivity.this);
        db1 = helper.getWritableDatabase();
        helper2 = new DisDBHelper(HomeActivity.this);
        db2 = helper2.getWritableDatabase();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docR = db.collection(firebaseUser.getEmail()).document("point");
        docR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserAccount userAccount = document.toObject(UserAccount.class);
                        textView2.setText(userAccount.getPoint() + "점");
                        csbar.setProgress(Integer.parseInt(userAccount.getPoint()));

                    } else {
                        Log.i("crkim", "no point document!!");
                        textView2.setText("0점");
                        csbar.setProgress(0);

                        Map<String, Object> member = new HashMap<>();
                        member.put("point","0");
                        db.collection(firebaseUser.getEmail())
                                .document("point")
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
                } else {
                    Log.i("crkim", "get failled with", task.getException());
                    textView2.setText("0점");
                    csbar.setProgress(0);
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textView2.setText("0점");
                        csbar.setProgress(0);
                    }
                });


    }

    public void name(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docR = db.collection(firebaseUser.getEmail()).document("account");
        docR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserAccount userAccount = document.toObject(UserAccount.class);
                        tv_name.setText(userAccount.getName()+ "님의 심장재활 점수는" );
                    } else {
                        Log.i("crkim", "no documetation");
                        tv_name.setText("님의 심장재활 점수는");
                    }
                } else {
                    Log.i("crkim", "get failled with", task.getException());
                    tv_name.setText("님의 심장재활 점수는");
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tv_name.setText("님의 심장재활 점수는");
                    }
                });

    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();
    }


}