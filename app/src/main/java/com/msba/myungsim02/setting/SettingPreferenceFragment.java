package com.msba.myungsim02.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.msba.myungsim02.R;


public class SettingPreferenceFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preference, s);



}}






