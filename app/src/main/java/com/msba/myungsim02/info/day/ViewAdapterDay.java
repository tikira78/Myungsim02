package com.msba.myungsim02.info.day;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAdapterDay extends FragmentStateAdapter {
    public int mCount;
    public ViewAdapterDay(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragmentD0();
        else if(index==1) return new FragmentD1();
        else if(index==2) return new FragmentD2();
        else if(index==3) return new FragmentD3();
        else return new FragmentD4();


    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}

