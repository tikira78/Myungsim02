package com.msba.myungsim02.record.hr;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.msba.myungsim02.R;
import com.msba.myungsim02.record.RecordDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHR1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHR1 extends Fragment {
    private static final String TAG = "crkim";
    LineChart linechart;
    SimpleDateFormat transFormat;
    XAxis xAxis;
    String[] date;
    int recordCount=3;
    SQLiteDatabase db;
    RecordDBHelper dbHelper;

    public static FragmentHR1 newInstance() {
        return new FragmentHR1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_graph, container, false);

        linechart = rootView.findViewById(R.id.chart);

        //
        dbHelper = new RecordDBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        initUI();
        addData();

        return rootView;
    }

    private void initUI() {
        xAxis = linechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(3, 24, 0);
        xAxis.setAxisLineWidth(2);
        xAxis.setTextSize(10f);
        xAxis.setLabelCount(recordCount+2, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("MM월dd일", Locale.KOREA);
            @Override
            public String getFormattedValue(float value) {
                // long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(value);
            }
        });

        LimitLine ll1 = new LimitLine(110f, "110회/분");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        YAxis yLAxis = linechart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setAxisLineWidth(2);
        yLAxis.setTextSize(14f);
        yLAxis.addLimitLine(ll1);
        yLAxis.setAxisMinimum(40f);
        yLAxis.setAxisMaximum(200f);
        yLAxis.setLabelCount(10, true);

        YAxis yRAxis = linechart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("안정시 맥박수 (최고)");
        description.setTextSize(30f);
        description.setTextColor(Color.RED);

        linechart.setDoubleTapToZoomEnabled(false);
        linechart.setPinchZoom(false);
        linechart.setDrawGridBackground(false);
        linechart.setDescription(description);
        linechart.setScaleXEnabled(false);
        linechart.setScaleYEnabled(true);
        //linechart.animateY(2000, Easing.EaseInOutSine);
        linechart.invalidate();
    }

    private void addData(){

        //오늘
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String toDay = date.format(today);
        //일주일전
        Calendar week = Calendar.getInstance();
        week.add(Calendar.DATE , -7);
        String beforeWeek = new SimpleDateFormat("yyyy-MM-dd").format(week.getTime());
        //한달전
        Calendar mon = Calendar.getInstance();
        mon.add(Calendar.MONTH , -1);
        String beforeMonth = new SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());
        System.out.println(beforeMonth);

        //최고
        Cursor cursor = db.rawQuery("select MAX(HR), Date from tb_hr" +" WHERE Date1 >= '" + beforeMonth + "' " +"GROUP BY Date1", null);

        recordCount = cursor.getCount();
        println("recordCount: " + recordCount);

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i<recordCount; i++){
            cursor.moveToNext();

            int BP = cursor.getInt(0);
            long DATE = cursor.getLong(1);

            entries.add(new Entry(DATE, BP));
        }

        cursor.close();
        println("completed!!" );

        LineDataSet lineDataSet = new LineDataSet(entries, "일최고 ");
        lineDataSet.setLineWidth(3);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setValueTextSize(24);
        lineDataSet.setCircleColor(Color.parseColor("#eb34e5"));
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setColor(Color.parseColor("#eb34e5"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        linechart.setData(lineData);
    }

    private void println(String msg) {
        Log.i(TAG, msg);
    }
}