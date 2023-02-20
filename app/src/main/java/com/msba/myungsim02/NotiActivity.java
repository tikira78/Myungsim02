package com.msba.myungsim02;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msba.myungsim02.auth.MainActivity;
import com.msba.myungsim02.auth.UserAccount;
import com.msba.myungsim02.point.DisDBHelper;

public class NotiActivity extends AppCompatActivity {

    private TextView textView1, textView2;

    private FirebaseUser firebaseUser;
    DocumentReference docR;
    FirebaseFirestore db;

    ProgressDialog dialog;

    DisDBHelper helper,helper2;
    SQLiteDatabase db1,db2;

    String feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        //progress dialog
        dialog = new ProgressDialog(NotiActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로딩 중 ...");
        dialog.show();

        textView1 = findViewById(R.id.textView2);

        helper = new DisDBHelper(NotiActivity.this);
        helper2 = new DisDBHelper(NotiActivity.this);
        db1 = helper.getWritableDatabase();
        db2 = helper2.getWritableDatabase();


        readData();
        dialog.dismiss();

        //Floating btn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotiActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);

            }
        });

    }

    public void readData() {
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
                        textView1.setText(userAccount.getFeedback());
                        feedback =userAccount.getFeedback();

                        ContentValues values = new ContentValues();
                        values.put("feedback", feedback); //column, value
                        db1.delete("tb_feedback", null, null);
                        db1.insert("tb_feedback", null, values);
                        db1.close();


                    } else { Log.i("crkim","no document");


                    }
                }else{ Log.i("crkim","get failed with", task.getException());
                  }
            }

        });






            }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(NotiActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();
    }

}