package com.msba.myungsim02.exercise;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.msba.myungsim02.R;
import com.msba.myungsim02.record.RecordViewListener;

import java.util.ArrayList;

public class ExdaAdapter extends RecyclerView.Adapter<ExdaAdapter.ViewHolder> {

    private Context context = null;
    private RecordViewListener ExdaViewListener = null;
    private AlertDialog alertDialog;

    ExdaDatabase database;
    View v;
    Long select;
    int position;

    ArrayList<ExdaItem> items = new ArrayList<ExdaItem>();

    public void addItem(ExdaItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<ExdaItem> items) {
        this.items = items;
    }

    public ExdaItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ExdaItem item) {
        items.set(position, item);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_ex, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        ExdaItem item = items.get(i);
        viewHolder.setItem(item);
        database = ExdaDatabase.getInstance(v.getContext());

        position = i;
        Log.i("crkim",String.valueOf(position));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("crkim",String.valueOf(items.get(i).getDATE2()));
                select= items.get(i).getDATE2();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("삭제");
                builder.setMessage("삭제 하시겠습니까?");
                builder.setPositiveButton("OK", dialogListener);
                builder.setNegativeButton("NO", null);
                builder.show();
            }
        });
   }


    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            removeItem(position);
            database.execSQL("DELETE FROM exda WHERE DATE2='" + select +"'");

        }
    };


    @Override
    public int getItemCount() {

        return items.size();
    }

    public void removeItem(int position) {

        items.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleView, contentsView, bstView;
        public TextView todayView, hrView;

        int pos, pos1;

        public ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.type_ed1);
            contentsView = view.findViewById(R.id.dur_ed1);
            hrView = view.findViewById(R.id.recordHR1);
            todayView = view.findViewById(R.id.today1);

        }

        public void setItem(ExdaItem item) {

            String date = item.getDATE();

            titleView.setText("운동종류: "+ item.getTYPE() );
            contentsView.setText("운동시간: "+item.getHOUR() + "시간" + item.getMIN() + "분");
            hrView.setText("운동강도: "+ item.getRPE() + "점");
            todayView.setText(date);

        }

        @Override
        public void onClick(View view) {
            ExdaViewListener.onItemClick(getAbsoluteAdapterPosition(), view);
        }
    }
}
