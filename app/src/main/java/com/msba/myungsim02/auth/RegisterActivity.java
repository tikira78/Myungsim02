package com.msba.myungsim02.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msba.myungsim02.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth; //firebase 인증처리
    private DatabaseReference mDatabaseRef; //실시간 database
    private EditText mEtEmail, mEtPwd, mEtName, mEtAge;
    private Button mBtnRegister;

    private FirebaseUser firebaseUser;
    DocumentReference docR;
    FirebaseFirestore db;

    int length, length2;

    String strName,strEmail,strPwd, strAge;

    Calendar cal;
    SimpleDateFormat formats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("crehab");

        mEtName = findViewById(R.id.et_name);
        mEtAge = findViewById(R.id.et_age);
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pass);
        mBtnRegister = findViewById(R.id.btn_register);

        Pattern pattern = Patterns.EMAIL_ADDRESS;

        mBtnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                strName = mEtName.getText().toString();
                strAge = mEtAge.getText().toString();
                strEmail = mEtEmail.getText().toString();
                strPwd = mEtPwd.getText().toString();


                length = strPwd.length();
                length2 = strAge.length();
                Log.i("crkim", String.valueOf(length));
                if(strName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요", Toast.LENGTH_LONG).show();
                } else if(strAge.equals("")) {
                    Toast.makeText(RegisterActivity.this, "출생년도 4자리 입력해주세요", Toast.LENGTH_LONG).show();
                } else if(length2 != 4) {
                    Toast.makeText(RegisterActivity.this, "출생년도는 4자리로 입력해주세요(예:1966)", Toast.LENGTH_LONG).show();
                } else if(!pattern.matcher(strEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "이메일을 정확히 입력해주세요", Toast.LENGTH_LONG).show();
                } else if(length != 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 6자리 숫자입니다", Toast.LENGTH_LONG).show();
                } else {
                    register();
                }

            }
        });
    }


    private void register() {


        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> member = new HashMap<>();
                    member.put("name", strName);
                    member.put("birth",strAge);
                    member.put("email", strEmail);
                    member.put("pw", strPwd);
                    db.collection(firebaseUser.getEmail())
                            .document(strName)
                            .set(member)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    name();

                                    Log.i("crkim", "success");
                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("crkim", "failure");
                                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });


                          /*
                          FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account); //데이터베이스에 삽입

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            */

                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void name(){

        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy");
        String getTime = dateFormat.format(date);
        int time = Integer.parseInt(getTime);
        int age = time - Integer.parseInt(strAge);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Map<String, Object> member = new HashMap<>();
        member.put("name", strName);
        member.put("age",age);
        db.collection(firebaseUser.getEmail())
                .document("account")
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

}