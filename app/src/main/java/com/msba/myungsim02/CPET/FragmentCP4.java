package com.msba.myungsim02.CPET;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.msba.myungsim02.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCP1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCP4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCP4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCR0.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCP4 newInstance(String param1, String param2) {
        FragmentCP4 fragment = new FragmentCP4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_cp, container, false);

        SubsamplingScaleImageView imageView1 = (SubsamplingScaleImageView) rootView.findViewById(R.id.cr);
        imageView1.setImage(ImageSource.resource(R.drawable.cpet4));
        imageView1.setDoubleTapZoomScale(0.6f);

        return rootView;
    }
}