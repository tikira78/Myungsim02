package com.msba.myungsim02.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //firebase 인증처리
    private DatabaseReference mDatabaseRef; //실시간 database
    private EditText mEtEmail, mEtPwd;
    private CheckBox autoLogin;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    private TextView textView1;
    View mainLayout;
    Snackbar snackbar;
    Button btn_register2,btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtEmail = findViewById(R.id.et_email2);
        mEtPwd = findViewById(R.id.et_pass2);
        btn_register2 = findViewById(R.id.btn_register2);
        btn_login = findViewById(R.id.btn_login);
        autoLogin = findViewById(R.id.checkBox);

        btn_login.setEnabled(false);
        btn_register2.setEnabled(false);

        mainLayout = findViewById(R.id.main_layout);
        LinearLayoutCompat linearLayoutCompat = (LinearLayoutCompat) findViewById(R.id.login);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConnectivityStatus(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getConnectivityStatus(this);


        setting = getSharedPreferences("setting",Context.MODE_PRIVATE);
        editor = setting.edit();


        if(setting.getBoolean("autoLogin",false)){
            mEtEmail.setText(setting.getString("id",""));
            mEtPwd.setText(setting.getString("pw",""));
            autoLogin.setChecked(true);
        }

        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEtEmail.getText().toString().equals("")||mEtPwd.getText().toString().equals("")){

                    Toast.makeText(MainActivity.this, "이메일과 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();

                }else {


                    if (autoLogin.isChecked()) {

                        String strEmail = mEtEmail.getText().toString();
                        String strPwd = mEtPwd.getText().toString();

                        editor.putString("id", strEmail);
                        editor.putString("pw", strPwd);
                        editor.putBoolean("autoLogin", true);
                        editor.commit();

                        mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "로그인 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    getConnectivityStatus(getApplicationContext());
                                }
                            }
                        });


                    } else {
                        String strEmail = mEtEmail.getText().toString();
                        String strPwd = mEtPwd.getText().toString();

                        mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "로그인 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    getConnectivityStatus(getApplicationContext());
                                }
                            }
                        });

                        editor.clear();
                        editor.commit();
                    }
                }
            }

        });

    }



    public void getConnectivityStatus(Context context) {
        // 네트워크 연결 상태 확인하기 위한 ConnectivityManager 객체 생성(https://develop-writing.tistory.com/1)
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {

            // 기기가 마시멜로우 버전인 Andorid 6 이상인 경우
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                // 활성화된 네트워크의 상태를 표현하는 객체
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

                if (nc != null) {

                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Toast.makeText(context, "와이파이 연결됨", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth = FirebaseAuth.getInstance();
                        btn_login.setEnabled(true);
                        btn_register2.setEnabled(true);
                        if(snackbar!=null){
                            snackbar.dismiss();
                        }

                    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Toast.makeText(context, "셀룰러 통신 사용", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth = FirebaseAuth.getInstance();
                        btn_login.setEnabled(true);
                        btn_register2.setEnabled(true);
                        if(snackbar!=null){
                            snackbar.dismiss();
                        }
                    }
                } else {

                    btn_login.setEnabled(false);
                    btn_register2.setEnabled(false);

                    snackbar = Snackbar.make(mainLayout, "인터넷에 연결되지 않았습니다.\n연결 후 화면을 위에서 아래로 쓸어주세요.", Snackbar.LENGTH_INDEFINITE);
                   /* snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });

                    */
                    snackbar.show();


                }

            } else {

                // 기기 버전이 마시멜로우 버전보다 아래인 경우
                // getActiveNetworkInfo -> API level 29에 디플리케이트 됨
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // 연결된 네트워크 확인
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        Toast.makeText(context, "와이파이 연결됨", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth = FirebaseAuth.getInstance();
                        btn_login.setEnabled(true);
                        btn_register2.setEnabled(true);
                        if(snackbar!=null){
                            snackbar.dismiss();
                        }
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Toast.makeText(context, "셀룰러 통신 사용", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth = FirebaseAuth.getInstance();
                        btn_login.setEnabled(true);
                        btn_register2.setEnabled(true);
                        if(snackbar!=null){
                            snackbar.dismiss();
                        }
                    }
                } else {

                    btn_login.setEnabled(false);
                    btn_register2.setEnabled(false);
                    snackbar = Snackbar.make(mainLayout, "인터넷에 연결되지 않았습니다.\n연결 후 화면을 위에서 아래로 쓸어주세요.", Snackbar.LENGTH_INDEFINITE);
                   /*snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });

                    */
                    snackbar.show();


                }
            }
        }
    }

}