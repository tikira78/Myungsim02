package com.msba.myungsim02.CPET;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAdapterCpet3 extends FragmentStateAdapter {
    public int mCount;
    public ViewAdapterCpet3(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragmentCP1();
        else if(index==1) return new FragmentCP2();
        else if(index==2) return new FragmentCP3();
        else return new FragmentCP4();


    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}

