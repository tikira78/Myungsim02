package com.msba.myungsim02.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.msba.myungsim02.R;
import com.msba.myungsim02.setting.InfoDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExOneActivity extends AppCompatActivity {


    TextView tv1, tv2, tv3, tv4;
    FloatingActionButton btn1,btn2,btn3;
    VideoView vv;
    private FirebaseUser firebaseUser;
    private long calDateDays, calDateDays2;
    int calDateDays3;
    ProgressDialog dialog;

    InfoDBHelper helper;
    SQLiteDatabase db, db2, db3;
    String s;
    int restingHr, count1=0, count2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_one);

        //tv1 = (TextView) findViewById(R.id.tv_data);
        //tv2 = (TextView) findViewById(R.id.tv_data2);
        tv3 = (TextView) findViewById(R.id.tv_data3);
        tv4 = (TextView) findViewById(R.id.tv_data4);
        vv = (VideoView) findViewById(R.id.vv);

        Button btn4 = findViewById(R.id.button1);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExOneActivity.this, ExTwoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn5 = findViewById(R.id.button2);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ExOneActivity.this, ExThreeActivity.class);
                startActivity(intent2);
                finish();
            }
        });


        Button btn6 = findViewById(R.id.button3);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ExOneActivity.this, ExdaResultActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        btn1 = findViewById(R.id.btn_prev);
        btn2 = findViewById(R.id.btn_next);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (calDateDays3>1) {
                    calDateDays3 = calDateDays3 - 1;
                    videoPlay();
                } else {
                    calDateDays3 = 1;
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (calDateDays3 < 28 ) {
                    calDateDays3 = calDateDays3 + 1;
                    videoPlay();
                } else {
                    calDateDays3 = 28;
                }
            }
        });

         //
        helper = new InfoDBHelper(ExOneActivity.this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select intervention, MAX(DATE) from tb_intervention", null);
        while(cursor.moveToNext()) {
            Log.i("crkim", String.valueOf(cursor.getString(0)));
            s = cursor.getString(0);
        }
        cursor.close();

        Cursor cursor1 = db.rawQuery("select restinghr,MAX(DATE) from tb_restinghr", null);
        while(cursor1.moveToNext()) {
            Log.i("crkim", String.valueOf(cursor1.getInt(0)));
            restingHr = cursor1.getInt(0);
        }
        cursor1.close();

            try {
                Calendar getToday = Calendar.getInstance();
                getToday.setTime(new Date()); //금일 날짜

                SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
                Date disDate = format.parse(s);
                Calendar mdate = Calendar.getInstance();
                mdate.setTime(disDate);

                long calDate = getToday.getTimeInMillis() - mdate.getTimeInMillis();
                calDateDays = calDate / (24 * 60 * 60 * 1000);
                calDateDays2 = Math.abs(calDateDays);
                calDateDays3 = Long.valueOf(calDateDays2).intValue();

                videoPlay();

                Log.d("crkim", "일자 차 =" + calDateDays);

                if (calDateDays2 < 8) {
                    int restingHR2 = restingHr + 20;
                    tv4.setText("오늘은 퇴원 후 " + calDateDays + "일차 입니다. 아직은 무리한 활동은 하지 마시고, 10분 이내로 가볍게 걸어주세요. " +
                            "운동강도는 '쉽다(운동자각지수 9-10)'정도이며, 운동 중 적절한 맥박수 " + restingHR2 + "회 정도 입니다 (단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요).' 즐거운 운동 되세요!!");

                    String data = tv4.getText().toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(data);
                    int start=data.indexOf("쉽다(운동자각지수 9-10)");
                    int end=start+"쉽다(운동자각지수 9-10)".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start1=data.indexOf("10분 이내로 가볍게");
                    int end1=start1+"10분 이내로 가볍게".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start1,end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start2=data.indexOf(String.valueOf(restingHR2));
                    int end2=start2+String.valueOf(restingHR2).length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start2,end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start3=data.indexOf("회");
                    int end3=start3+"회".length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start3,end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start4=data.indexOf("단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요");
                    int end4=start4+"단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start4,end4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv4.setText(builder);
                    //feeback();
                } else if (15 > calDateDays2 && calDateDays2 > 7) {
                    int restingHR2 = restingHr + 25;
                    tv4.setText("오늘은 퇴원 후 " + calDateDays + "일차 입니다. 첫주보다는 운동시간을 조금 늘여보시고, 아직은 무리한 활동은 하지 말아주세요. 10~15분 이내로 가볍게 걸어주시고, (괜찮으면 20분까지) " +
                            "운동강도는 '쉬운(운동자각지수 10-11)' 정도이며, 운동 중 적절한 맥박수 " + restingHR2 + "회 정도 입니다. (단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요)' 즐거운 운동 되세요!!");

                    String data = tv4.getText().toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(data);
                    int start=data.indexOf("쉬운(운동자각지수 10-11)");
                    int end=start+"쉬운(운동자각지수 10-11)".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start1=data.indexOf("10~15분 이내로 가볍게");
                    int end1=start1+"10~15분 이내로 가볍게".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start1,end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start2=data.indexOf(String.valueOf(restingHR2));
                    int end2=start2+String.valueOf(restingHR2).length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start2,end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start3=data.indexOf("회");
                    int end3=start3+"회".length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start3,end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start4=data.indexOf("단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요");
                    int end4=start4+"단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start4,end4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv4.setText(builder);
                } else if (22 > calDateDays2 && calDateDays2 > 14) {
                    int restingHR2 = restingHr + 30 ;
                    tv4.setText("오늘은 퇴원 후 " + calDateDays + "일차 입니다. 15~20분 이내로 가볍게 걸어주시고, 괜찮으면 30분정도까지 늘여보세요. 아주 가벼운 근력운동 정도 시작해봅니다. " +
                            "운동강도는 '쉽다(운동자각지수 10-11)' 정도이며, 운동 중 적절한 맥박수 " + restingHR2 + "회 정도 입니다.(단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요)'' 즐거운 운동 되세요!!");

                    String data = tv4.getText().toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(data);
                    int start=data.indexOf("쉽다(운동자각지수 10-11)");
                    int end=start+"쉽다(운동자각지수 10-11)".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start1=data.indexOf("15~20분 이내로 가볍게");
                    int end1=start1+"15~20분 이내로 가볍게".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start1,end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start2=data.indexOf(String.valueOf(restingHR2));
                    int end2=start2+String.valueOf(restingHR2).length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start2,end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start3=data.indexOf("회");
                    int end3=start3+"회".length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start3,end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start4=data.indexOf("단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요");
                    int end4=start4+"단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start4,end4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv4.setText(builder);
                } else if(29 > calDateDays2 && calDateDays2 > 21){
                    int restingHR2 = restingHr + 40;
                    tv4.setText("오늘은 퇴원 후 " + calDateDays + "일차 입니다.  20~30분 이내로 가볍게 걸어주세요. " +
                            "운동강도는 '쉽다~약간힘들다(운동자각지수 10-12)'정도이며, 운동 중 적절한 맥박수 " + restingHR2 + "회 정도 입니다.(단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요)' 즐거운 운동 되세요!!");

                    String data = tv4.getText().toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(data);
                    int start=data.indexOf("쉽다~약간힘들다(운동자각지수 10-12)");
                    int end=start+"쉽다~약간힘들다(운동자각지수 10-12)".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start1=data.indexOf("20~30분 이내로 가볍게");
                    int end1=start1+"20~30분 이내로 가볍게".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start1,end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start2=data.indexOf(String.valueOf(restingHR2));
                    int end2=start2+String.valueOf(restingHR2).length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start2,end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start3=data.indexOf("회");
                    int end3=start3+"회".length();
                    builder.setSpan(new ForegroundColorSpan(Color.YELLOW),start3,end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int start4=data.indexOf("단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요");
                    int end4=start4+"단, 부정맥이 있으신 경우에는 맥박수보다는 자각지수로 운동하세요".length();
                    builder.setSpan(new ForegroundColorSpan(Color.WHITE),start4,end4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv4.setText(builder);
                }else {
                    tv4.setText("4주간의 운동을 마쳤습니다. 이후에는 운동부하검사에 맞추어 적절한 운동을 해주세요. 즐거운 운동 되세요!!");
                }

                //spannable
           } catch (ParseException e) {
        }
    }

// onCreate 끝

    //화면에 안보일때...
    @Override
    protected void onPause() {
        super.onPause();

        //비디오 일시 정지
        if (vv != null && vv.isPlaying()) vv.pause();
    }

    //액티비티가 메모리에서 사라질때..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        if (vv != null) vv.stopPlayback();
    }


    public void videoPlay(){
        switch (calDateDays3) {
            case 1:
                //Video
                //Video Uri
                Uri videoUri = Uri.parse("https://tikira78.github.io/creduguide/day1.mp4");
                vv.setMediaController(new MediaController(this));
                //VideoView가 보여줄 동영상의 경로 주소(Uri) 설정하기
                vv.setVideoURI(videoUri);

                break;
            case 2:
                Uri videoUri2 = Uri.parse("https://tikira78.github.io/creduguide/day2.mp4");
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(videoUri2);
                break;
            case 3:
                Uri videoUri3 = Uri.parse("https://tikira78.github.io/creduguide/day3.mp4");
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(videoUri3);
                break;
            case 4:
                Uri videoUri4 = Uri.parse("https://tikira78.github.io/creduguide/day4.mp4");
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(videoUri4);
                break;
            case 5:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day5.mp4"));
                break;
            case 6:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day6.mp4"));
                break;
            case 7:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day7.mp4"));
                break;
            case 8:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day8.mp4"));
                break;
            case 9:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day9.mp4"));
                break;
            case 10:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day10.mp4"));
                break;
            case 11:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day11.mp4"));
                break;
            case 12:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day12.mp4"));
                break;
            case 13:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day13.mp4"));
                break;
            case 14:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day14.mp4"));
                break;
            case 15:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day15.mp4"));
                break;
            case 16:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day16.mp4"));
                break;
            case 17:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day17.mp4"));
                break;
            case 18:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day18.mp4"));
                break;
            case 19:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day19.mp4"));
                break;
            case 20:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day20.mp4"));
                break;
            case 21:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day22.mp4"));
                break;
            case 23:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day23.mp4"));
                break;
            case 24:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day24.mp4"));
                break;
            case 25:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day25.mp4"));
                break;
            case 26:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day26.mp4"));
                break;
            case 27:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/day27.mp4"));
                break;
            case 28:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/cr_edu1.mp4"));
                break;
            default:
                vv.setMediaController(new MediaController(this));
                vv.setVideoURI(Uri.parse("https://tikira78.github.io/creduguide/cr_edu1.mp4"));

        }


        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController m_mediaController = new MediaController(ExOneActivity.this);
                        vv.setMediaController(m_mediaController);

                        m_mediaController.setAnchorView(vv);
                    }
                });


            }
        });
        vv.start();




    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ExOneActivity.this, ExActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();
    }
}