package com.msba.myungsim02.CPET;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.msba.myungsim02.R;

import me.relex.circleindicator.CircleIndicator3;

public class FragmentCPET4 extends Fragment {

    PointF point;

    public static FragmentCPET4 newInstance() {
        return new FragmentCPET4();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_cpet4, container, false);

        SubsamplingScaleImageView imageView1 = (SubsamplingScaleImageView) view.findViewById(R.id.met);
        imageView1.setImage(ImageSource.resource(R.drawable.met2));
        imageView1.setDoubleTapZoomScale(1.5f);

        return view;
    }


}