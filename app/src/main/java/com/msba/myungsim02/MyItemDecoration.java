package com.msba.myungsim02;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration{

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int index=parent.getChildAdapterPosition(view)+1;

        if (index % 10 ==0)
            outRect.set(20,2,20,20);
        else
            outRect.set(20,2,20,2);

        view.setBackgroundColor(0xFFEC9E9);
        ViewCompat.setElevation(view,1.0f);
    }

}
