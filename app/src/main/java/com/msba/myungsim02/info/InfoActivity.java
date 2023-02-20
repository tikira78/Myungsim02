package com.msba.myungsim02.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.msba.myungsim02.HomeActivity;
import com.msba.myungsim02.R;
import com.msba.myungsim02.auth.MainActivity;

public class InfoActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNV;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            BottomNavigate(menuItem.getItemId());
            return true;
        }
    });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
}
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_1) {
                fragment = new FragmentI1();
            } else if (id == R.id.navigation_2){
                fragment = new FragmentI2();
            } else if (id == R.id.navigation_3){
                fragment = new FragmentI3();
            }else {
                fragment = new FragmentI4();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.horizon_enter,R.anim.horizon_exit);
        super.onBackPressed();
    }

}