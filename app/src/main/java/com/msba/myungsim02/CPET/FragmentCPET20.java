package com.msba.myungsim02.CPET;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.msba.myungsim02.R;

import java.io.File;

public class FragmentCPET20 extends Fragment {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    SubsamplingScaleImageView imageView;
    CPETdatabase database;
    Long image;
    String image2,getString,getString1;
    TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_cpet20, container, false);

        //
        /*
        Intent intent = getActivity().getIntent();
        image = intent.getLongExtra("image",-1);
        image2 = String.valueOf(image);
        Log.i("crkim","image2:" + image2);
         */

        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                Long image = bundle.getLong("bundleKey");
                // Do something with the result...
                image2 = String.valueOf(image);
            }
        });

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

        imageView = view.findViewById(R.id.iv_loadCPET);
        textView = view.findViewById(R.id.tv_cpet);

        File storageDir = new File(getActivity().getFilesDir() + "/capture");
        if(!storageDir.exists()){

            Toast.makeText(getActivity(),"저장된 결과가 아직 없습니다.",Toast.LENGTH_SHORT).show();

        }else {



            loadImage();
        }



        return view;
    }

    private void loadImage() {


        Cursor cursor = database.rawQuery("select IMAGE from cpet where DATE2 =" + image2 );
        while(cursor.moveToNext()) {
            Log.i("crkim", cursor.getString(0));
            getString = cursor.getString(0);
        }
        cursor.close();

        Cursor cursor1 = database.rawQuery("select DATE from cpet where DATE2 =" + image2 );
        while(cursor1.moveToNext()) {
            Log.i("crkim", cursor1.getString(0));
            getString1 = cursor1.getString(0);
        }
        cursor1.close();


        imageView.setImage(ImageSource.uri("/data/data/com.msba.myungsim02/files/capture/"+getString));
        imageView.setDoubleTapZoomScale(0.6f);

        textView.setText(getString1);

    }


}