package com.msba.myungsim02.CPET;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.msba.myungsim02.R;

import me.relex.circleindicator.CircleIndicator3;

public class FragmentCPET0 extends Fragment {
   CPETActivity cpetActivity;

    VideoView vv;

    public static FragmentCPET0 newInstance() {
        return new FragmentCPET0();
    }

    // 메인 액티비티 위에 올린다.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cpetActivity = (CPETActivity) getActivity();

        requireActivity().getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
    }

    // 메인 액티비티에서 내려온다.
    @Override
    public void onDetach() {
        super.onDetach();
        cpetActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_cpet0, container, false);

       //child fragment
       //getChildFragmentManager().beginTransaction().add(R.id.child_fragment, new FragmentCR0()).commit();

       //video
       vv = (VideoView) view.findViewById(R.id.vv);

       Uri videoUri = Uri.parse("https://tikira78.github.io/creduguide/CPET2_1.mp4");

        //VideoView가 보여줄 동영상의 경로 주소(Uri) 설정하기
        vv.setVideoURI(videoUri);
        MediaController m_mediaController = new MediaController(getContext());

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController m_mediaController = new MediaController(getContext());
                        vv.setMediaController(m_mediaController);
                        m_mediaController.setAnchorView(vv);
                    }
                });
            }
        });

        vv.start();
        vv.seekTo(3000);
        vv.pause();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.child_fragment, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }


    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed(){

            // Create new fragment and transaction
            Fragment newFragment = new FragmentCPET0();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.content_layout, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }
    };

}