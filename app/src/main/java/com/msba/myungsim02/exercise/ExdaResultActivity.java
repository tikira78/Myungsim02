package com.msba.myungsim02.exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.MyItemDecoration;
import com.msba.myungsim02.R;
import com.msba.myungsim02.record.RecordViewListener;

import java.util.ArrayList;

public class ExdaResultActivity extends AppCompatActivity implements RecordViewListener {

        private ArrayList<ExdaItem> items =null;
        private ExdaAdapter exdaAdapter = null;
        ExdaDatabase database;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exda_result);

            //items = new ArrayList<>();

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
            RecyclerView recyclerView = findViewById(R.id.recordList_ed);
            recyclerView.setLayoutManager(layoutManager);

            // open database
            if (database != null) {
                database.close();
                database = null;
            }

            database = ExdaDatabase.getInstance(this);
            boolean isOpen = database.open();
            if (isOpen) {
                Log.d("crkim", " database is open.");
            } else {
                Log.d("crkim", "database is not open.");
            }

            exdaAdapter = new ExdaAdapter();
            recyclerView.setAdapter(exdaAdapter);
            recyclerView.addItemDecoration(new MyItemDecoration());

            items = selectAll();
            exdaAdapter.setItems(items);
            exdaAdapter.notifyDataSetChanged();
            //recyclerView.setHasFixedSize(true);
            layoutManager.scrollToPosition(items.size()-1); // 제일 최근거에 맞추기

          /*
           FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                Intent intent = new Intent(ExdaResultActivity.this, DataActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);


                }
            });

            FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2_ed);
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ExdaResultActivity.this, ExOneActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
                }
            });
        */
        }

        @Override
        protected void onStart() {
            super.onStart();
            //addChildEvent();
        }

        @Override
        public void onItemClick(int position, View view) {

        }

        public ArrayList<ExdaItem> selectAll() {
            ArrayList<ExdaItem> result = database.selectAll();
            //Toast.makeText(getApplicationContext(), "정보를 조회했습니다.", Toast.LENGTH_LONG).show();
            return result;
        }

        @Override
        public void onBackPressed(){
            super.onBackPressed();
            Intent intent = new Intent(ExdaResultActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        }

    }