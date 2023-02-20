package com.msba.myungsim02.CPET;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.msba.myungsim02.R;
import com.msba.myungsim02.point.DisDBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class FragmentCPET1 extends Fragment implements View.OnClickListener{

    private ExpandableListView listView;
    ImageView resultImageView;
    Button btnCamera, btnSave, btnDate;
    String mCurrentPhotoPath;
    String imageFileName,imageFileName2;
    LinearLayout layout;
    DisDBHelper helper;
    SQLiteDatabase db;
    boolean boo = true;
    Spinner spinner;
    TextView textView,textView2;
    String yy,mm,dd;
    private CPETdatabase database;
    ImageButton btnCamera1,btnSave1;

    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_cpet1, container, false);

        init();

        layout = rootView.findViewById(R.id.la_cpet);

        resultImageView = (ImageView) rootView.findViewById(R.id.imageView13);
        btnCamera1 = (ImageButton) rootView.findViewById(R.id.btn_photo);
        btnSave1 = (ImageButton)  rootView.findViewById(R.id.btn_saveCPET);
        btnDate = (Button) rootView.findViewById(R.id.btn_seldate);
        btnCamera1.setOnClickListener(this);
        btnSave1.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnSave1.setEnabled(false);
        layout.setVisibility(View.INVISIBLE);
        textView2 = rootView.findViewById(R.id.textView41);

        //
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext().getApplicationContext(),
                                "com.msba.myungsim02.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 0);


                    }
                }
            }
        });

        //resultImageView.setVisibility(View.INVISIBLE);

        //
        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = CPETdatabase.getInstance(getContext());
        boolean isOpen = database.open();
        if (isOpen) {
            Log.i("crkim", " database is open.");
        } else {
            Log.i("crkim", "database is not open.");
        }


        //
        String time_cpet = new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date());
        textView = rootView.findViewById(R.id.tv_date);
        textView.setText(time_cpet);

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("crkim", "권한 설정 완료");
            } else {
                Log.d("crkim", "권한 설정 요청");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        return rootView;
    }
    //
    private void init(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {

               Log.i("crkim","photo taken!!");
                try {
                    // 카메라 촬영을 하면 이미지뷰에 사진 삽입
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            Bitmap rotatedBitmap = null;
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }

                            //resultImageView.setVisibility(View.VISIBLE);
                            resultImageView.setImageBitmap(rotatedBitmap);
                            btnSave1.setEnabled(true);//저장하기 켜기
                            layout.setVisibility(View.VISIBLE);
                            textView2.setVisibility(View.INVISIBLE);

                        }


                } catch (Exception error) {
                    error.printStackTrace();
                }

    });
    }

   /* // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("crkim", "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("crkim", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
    @Override
    public void handleOnBackPressed(){

        // Create new fragment and transaction
        Fragment newFragment = new FragmentCPET0();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
       // Replace whatever is in the fragment_container view with this fragment,
       // and add the transaction to the back stack
        transaction.replace(R.id.content_layout, newFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_photo:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(),
                                "com.msba.myungsim02.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activityResultLauncher.launch(takePictureIntent);
                        Log.i("crkim","camera ready");

                    }
                }
                break;
            case R.id.btn_saveCPET:

                saveImg();
                upload();

                break;
            case R.id.btn_seldate:

                Calendar calendar = new GregorianCalendar();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        yy = String.valueOf(i);
                        mm = String.valueOf(i1+1);
                        dd = String.valueOf(i2);

                        textView.setText(yy+"년 "+mm+"월 " + dd +"일");


                    }
                }, mYear, mMonth,mDay);
                datePickerDialog.show();

                break;
        }
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // 카메라 촬영을 하면 이미지뷰에 사진 삽입
            if (requestCode == 0 ) {
                File file = new File(mCurrentPhotoPath);
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }

                    //resultImageView.setVisibility(View.VISIBLE);
                    resultImageView.setImageBitmap(rotatedBitmap);
                    btnSave1.setEnabled(true);//저장하기 켜기
                    layout.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);

                }

            }
        } catch (Exception error) {
            error.printStackTrace();
        }

 */

    // ImageFile의 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void saveImg() {

        try{
            File storageDir = new File(getActivity().getFilesDir() + "/capture");
            if(!storageDir.exists()){
                storageDir.mkdirs();
            }

            imageFileName2 = imageFileName + ".jpg";

            Log.i("crkim",getActivity().getFilesDir().toString());

            File file = new File(storageDir,imageFileName2);
            boolean deleted = file.delete();
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable) resultImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }finally {
                try {
                    assert output != null;
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("crkim", "Capture saved");
            Toast.makeText(getActivity(),"결과지를 저장하였습니다!",Toast.LENGTH_SHORT).show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void upload() {
        helper = new DisDBHelper(getContext());
        db = helper.getWritableDatabase();

        long date = System.currentTimeMillis();
        insert(imageFileName2, date, textView.getText().toString());

    }

    public void insert(String IMAGE, long DATE2, String DATE) {
        database.insertRecord(IMAGE, DATE2, DATE);
    }
}