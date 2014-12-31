package com.rajaraman.playerprofile.ui;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rajaraman.playerprofile.R;


public class PlayerProfileActivity extends ActionBarActivity implements
    PlayerProfileBattingAndFieldingAvgFragment.
                OnPlayerProfileBattingAndFieldingAvgFragmentInteractionListener,
    PlayerProfileBowlingAvgFragment.
                OnPlayerProfileBowlingAvgFragmentInteractionListener {

    ActionBar.Tab playerProfileBattingAndFieldingAvgTab,
                                                playerProfileBowlingTab;
    PlayerProfileBattingAndFieldingAvgFragment playerProfileBattingAndFieldingAvgFragment =
                                            new PlayerProfileBattingAndFieldingAvgFragment();
    PlayerProfileBowlingAvgFragment playerProfileBowlingAvgFragment =
                                            new PlayerProfileBowlingAvgFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        //if (savedInstanceState == null) {
        //}

        Button buttonBattingFieldingAvg = (Button)findViewById(R.id.button_batting);

        buttonBattingFieldingAvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.player_profile_avg_fragment_container, new PlayerProfileBattingAndFieldingAvgFragment())
                        .commit();
            }
        });

        Button buttonBowlingAvg = (Button)findViewById(R.id.button_bowling);

        buttonBowlingAvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.player_profile_avg_fragment_container, new PlayerProfileBowlingAvgFragment())
                        .commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlayerProfileBattingAndFieldingAvgFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPlayerProfileBowlingAvgFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_player_profile, container, false);
            return rootView;
        }
    }
}
