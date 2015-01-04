package com.rajaraman.playerprofile.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.ui.MainActivity;
import com.rajaraman.playerprofile.ui.adapters.CountryListAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.FragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    MainActivity activity;

    @Before
    public void setup()  {
        // To redirect Robolectric to stdout
        System.setProperty("robolectric.logging", "stdout");

        //activity = Robolectric.buildActivity(MainActivity.class).create().get();

        // Simulate activity's onCreate, onStart, onResume life cycle callbacks. This may be
        // needed if the fragment is not created in Activity.onCreate(). Just keep this for
        // reference. visible() call is for making sure Activity is visible
        activity = Robolectric.buildActivity(MainActivity.class).create().
                                                            visible().start().resume().get();
    }

    @Test
    public void testMainActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void testWhetherNavigationDrawerIsShown() throws Exception {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment drawerFragment = fragmentManager.findFragmentById(R.id.navigation_drawer);

        assertNotNull(drawerFragment);

        assertTrue(drawerFragment.isInLayout());

        // Get the root view (i.e) list view
        // Note: Ideally getView itself should have returned ListView because that is the
        // only view inflated in this fragment but that gives ClassCastException, similar to the
        // one mentioned here - http://stackoverflow.com/questions/7318050/classcastexception-when-casting-to-viewpager
        // So when I created an unique id for this view (navigation_drawer_listview) and then
        // retrieve as below it could return ListView.
        ListView drawerListView = (ListView) drawerFragment.getView().
                                                  findViewById(R.id.listview_navigation_drawer);
        assertNotNull(drawerListView);

//        CountryListAdapter drawerList = (CountryListAdapter) drawerListView.getAdapter();
//
//        assertNotNull(drawerList);
//
//        // Check whether the drawer list has at least one item
//        assertTrue(drawerList.getCount() > 0);
    }

//    @Test
//    public void testWhetherPlayerProfileActivityIsStarted() throws Exception {
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        Fragment playerListFragment = fragmentManager.findFragmentById(R.id.player_list_fragment_container);
//
//        assertNotNull(playerListFragment);
//    }
}