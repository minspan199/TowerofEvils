package com.michael.pan.eviltower.fragments;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.michael.pan.eviltower.R;

// COMPLETED (1) Implement OnPreferenceChangeListener
public class SettingsFragment extends PreferenceFragmentCompat implements
	OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    public SettingsFragment(){}

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.user_pref);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        String[] preferenceKeys = new String[]{getString(R.string.pref_fps_key), getString(R.string.pref_lan_key), getString(R.string.pref_size_key), getString(R.string.pref_game_panel_key), getString(R.string.pref_user_icon_key)};
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        // Go through all of the preferences, and set up their preference summary.
        for (String key: preferenceKeys) {
            Preference p = preferenceScreen.findPreference(key);
            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(key, "");
                setPreferenceSummary(p, value);
            }
        }

        // COMPLETED (3) Add the OnPreferenceChangeListener specifically to the EditTextPreference
        // Add the preference listener which checks that the size is correct to the size preference
        Preference fontSizePreference = findPreference(getString(R.string.pref_size_key));
        Preference fpsPreference = findPreference(getString(R.string.pref_fps_key));
        fontSizePreference.setOnPreferenceChangeListener(this);
        fpsPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference preference = findPreference(key);
        if (null != preference) {
            // Updates the summary for the preference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }

    // COMPLETED (2) Override onPreferenceChange. This method should try to convert the new preference value
    // to a float; if it cannot, show a helpful error message and return false. If it can be converted
    // to a float check that that float is between 0 (exclusive) and 3 (inclusive). If it isn't, show
    // an error message and return false. If it is a valid number, return true.

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // In this context, we're using the onPreferenceChange listener for checking whether the
        // size setting was set to a valid value.
        String fpsError = getString(R.string.fps_error);
        Toast error;
//        System.out.println(preference.getKey());
        // Double check that the preference is the size preference
        if (preference.getKey().equals(getString(R.string.pref_fps_key))){
            String stringSize = (String) newValue;
            error = Toast.makeText(getContext(), fpsError, Toast.LENGTH_SHORT);
            try {
                float size = Float.parseFloat(stringSize);
                // If the number is outside of the acceptable range, show an error.
                if (size < 10 || size > 30) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                // If whatever the user entered can't be parsed to a number, show an error
                error.show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}