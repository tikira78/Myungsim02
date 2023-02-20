package com.msba.myungsim02.CPET;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msba.myungsim02.MyItemDecoration;
import com.msba.myungsim02.R;

import java.util.ArrayList;

public class FragmentCPET2 extends Fragment {


    private ArrayList<CPETitem> items = null;
    private CPETadapter CPETadapter = null;
    CPETdatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_cpet2, container, false);

        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = CPETdatabase.getInstance(getActivity());
        boolean isOpen = database.open();
        if (isOpen) {
            Log.d("crkim", " database is open.");
        } else {
            Log.d("crkim", "database is not open.");
        }

        //
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        RecyclerView recyclerView = view.findViewById(R.id.recordList3);
        recyclerView.setLayoutManager(layoutManager);



        CPETadapter = new CPETadapter();
        recyclerView.setAdapter(CPETadapter);
        recyclerView.addItemDecoration(new MyItemDecoration());

        items = selectAll();
        CPETadapter.setItems(items);
        CPETadapter.notifyDataSetChanged();
        //recyclerView.setHasFixedSize(true);
        layoutManager.scrollToPosition(items.size() - 1); // 제일 최근거에 맞추기

        return view;
    }

    public ArrayList<CPETitem> selectAll() {
        ArrayList<CPETitem> result = database.selectAll();
        //Toast.makeText(getApplicationContext(), "정보를 조회했습니다.", Toast.LENGTH_LONG).show();
        return result;
    }
}