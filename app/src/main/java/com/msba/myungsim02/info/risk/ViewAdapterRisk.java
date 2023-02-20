package com.msba.myungsim02.info.risk;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAdapterRisk extends FragmentStateAdapter {
    public int mCount;
    public ViewAdapterRisk(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragmentR0();
        else if(index==1) return new FragmentR1();
        else if(index==2) return new FragmentR2();
        else if(index==3) return new FragmentR3();
        else if(index==4) return new FragmentR4();
        else if(index==5) return new FragmentR5();
        else if(index==6) return new FragmentR6();
        else if(index==7) return new FragmentR7();
        else if(index==8) return new FragmentR8();
        else if(index==9) return new FragmentR9();
        else if(index==10) return new FragmentR10();
        else if(index==11) return new FragmentR11();
        else if(index==12) return new FragmentR12();
        else if(index==13) return new FragmentR13();
        else if(index==14) return new FragmentR14();
        else if(index==15) return new FragmentR15();
        else return new FragmentR16();


    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}

