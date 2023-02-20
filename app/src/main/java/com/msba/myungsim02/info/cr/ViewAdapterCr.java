package com.msba.myungsim02.info.cr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAdapterCr extends FragmentStateAdapter {
    public int mCount;
    public ViewAdapterCr(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragmentCR0();
        else if(index==1) return new FragmentCR1();
        else if(index==2) return new FragmentCR2();
        else return new FragmentCR3();


    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}

