package com.msba.myungsim02.info.ex;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.msba.myungsim02.info.cr.FragmentCR0;
import com.msba.myungsim02.info.cr.FragmentCR1;
import com.msba.myungsim02.info.cr.FragmentCR2;
import com.msba.myungsim02.info.cr.FragmentCR3;

public class ViewAdapterEx extends FragmentStateAdapter {
    public int mCount;
    public ViewAdapterEx(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragmentEX0();
        else if(index==1) return new FragmentEX1();
        else if(index==2) return new FragmentEX2();
        else if(index==3) return new FragmentEX3();
        else if(index==4) return new FragmentEX4();
        else if(index==5) return new FragmentEX5();
        else return new FragmentEX6();


    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}

