package com.msba.myungsim02.CPET;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msba.myungsim02.R;

import java.util.ArrayList;

public class CPETadapter extends RecyclerView.Adapter<CPETadapter.ViewHolder> {

private Context context = null;
private AlertDialog alertDialog;
 CPETdatabase database;
        View v;
        Long select;
        int position;

        ArrayList<CPETitem> items = new ArrayList<CPETitem>();

public void addItem(CPETitem item) {
        items.add(item);
        }

public void setItems(ArrayList<CPETitem> items) {
        this.items = items;
        }

public CPETitem getItem(int position) {
        return items.get(position);
        }

public void setItem(int position, CPETitem item) {
        items.set(position, item);
        }


@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_cpet, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        CPETitem item = items.get(i);
        viewHolder.setItem(item);
        database = CPETdatabase.getInstance(v.getContext());

        position = i;
        Log.i("crkim",String.valueOf(position));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
       @Override
        public void onClick(View view) {
        Log.i("crkim",String.valueOf(items.get(i).getDATE2()));
        select= items.get(i).getDATE2();

         Intent intent = new Intent(v.getContext(), CPETresultActivity.class);
         intent.putExtra("image", select);
         v.getContext().startActivity(intent);

        }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                select= items.get(i).getDATE2();

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                builder.setTitle("삭제");
                builder.setMessage("삭제 하시겠습니까?");
                builder.setPositiveButton("OK", dialogListener);
                builder.setNegativeButton("NO", null);
                builder.show();

                return false;
            }
          });

        }


    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            removeItem(position);
            database.execSQL("DELETE FROM cpet WHERE DATE2='" + select +"'");

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
    public TextView todayView, hrView, dateView;

    int pos, pos1;

    public ViewHolder(View view) {
        super(view);

        todayView = view.findViewById(R.id.date);

    }

    public void setItem(CPETitem item) {

        String date = item.getDATE();
        todayView.setText(date);



    }

    @Override
    public void onClick(View view) {
        // recordViewListener.onItemClick(getAbsoluteAdapterPosition(), view);


    }
}
}


