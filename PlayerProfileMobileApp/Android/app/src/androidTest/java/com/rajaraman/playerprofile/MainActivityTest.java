package com.rajaraman.playerprofile;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        activity = Robolectric.buildActivity(MainActivity.class).create().get();

        // To redirect Robolectric to stdout
        System.setProperty("robolectric.logging", "stdout");

        // Simulate activity's onCreate, onStart, onResume life cycle callbacks. This may be
        // needed if the fragment is not created in Activity.onCreate(). Just keep this for
        // reference
        //activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void checkWhetherNavigationDrawerListsAllCountries() throws Exception {
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
                                                  findViewById(R.id.navigation_drawer_listview);
        assertNotNull(drawerListView);

        ArrayAdapter drawerList = (ArrayAdapter) drawerListView.getAdapter();

        assertNotNull(drawerList);

        // Check whether the list has 3 items
        assertEquals(3, drawerList.getCount());

        System.out.println("List Item 0->" + drawerList.getItem(2));
    }
}