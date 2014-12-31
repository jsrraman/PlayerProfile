// Created by rajaraman on Dec 31, 2014
package com.rajaraman.playerprofile.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.rajaraman.playerprofile.R;

public class PlayerProfileAveragesTabListener implements ActionBar.TabListener {
    Fragment fragment;

    public PlayerProfileAveragesTabListener(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //ft.replace(R.id.player_profile_averages_content, fragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

