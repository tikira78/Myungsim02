package com.msba.myungsim02.record.bp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAdapterSbp extends FragmentStateAdapter {
    public int mCount;
    public ViewAdapterSbp(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragmentSBP0();
        else if(index==1) return new FragmentSBP1();
        else return new FragmentSBP2();


    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}

