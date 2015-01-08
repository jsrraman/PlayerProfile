package com.rajaraman.playerprofile.ui;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.network.data.entities.CountryEntity;
import com.rajaraman.playerprofile.network.data.entities.PlayerEntity;
import com.rajaraman.playerprofile.network.data.provider.DataProvider;
import com.rajaraman.playerprofile.network.data.provider.PlayerProfileApiDataProvider;
import com.rajaraman.playerprofile.ui.adapters.CountryListAdapter;
import com.rajaraman.playerprofile.ui.adapters.PlayerListAdapter;
import com.rajaraman.playerprofile.utils.AppUtil;

import java.util.ArrayList;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements
                                                        DataProvider.OnDataReceivedListener {

    private static final String TAG = NavigationDrawerFragment.class.getCanonicalName();

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        // Get the country names list
        if (false == AppUtil.isNetworkAvailable(getActivity())) {
            AppUtil.showDialog(getActivity(),
                    getActivity().getString(R.string.network_not_available));

            return null;
        }

        PlayerProfileApiDataProvider.getInstance().getCountryList(getActivity(), this);

        AppUtil.showProgressDialog(getActivity());

        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onDataFetched(int playerProfileApiId, Object responseData) {
//        // We have received the notification from PlayerProfileDataProvider so
//        // dismiss the progress dialog
//        AppUtil.logDebugMessage(TAG, "onDataFetched callback");
//
//        AppUtil.dismissProgressDialog();
//
//        if (null == responseData) {
//            // The app has failed to get a response from webservice. There is no point in
//            // proceeding further as this is the starting point in the app, so show the error
//            // and quit the app.
//            String message = getActivity().getString(R.string.quit_application);
//
//            AppUtil.showErrorDialogAndQuitApp(getActivity(), message);
//
//            return;
//        }
//
//        switch (playerProfileApiId) {
//            default:
//            case PlayerProfileApiDataProvider.GET_COUNTRY_LIST_API : {
//                // Get the data for the country list and show the list
//                ArrayList<CountryEntity> countryEntityList = (ArrayList<CountryEntity>)responseData;
//
//                CountryListAdapter countryListAdapter = new CountryListAdapter(getActivity(),
//                        countryEntityList);
//                mDrawerListView.setAdapter(countryListAdapter);
//                mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
//
//                // Select either the default item (0) or the last selected item.
//                selectItem(mCurrentSelectedPosition);
//            }
//        }

        AppUtil.logDebugMessage(TAG, "onDataFetched callback");

        AppUtil.dismissProgressDialog();

        boolean showErrorMessage = false;

        if (null == responseData) {
            showErrorMessage = true;
        }

        // Some APIs return boolean as responseData, so check for that as well
        if (false == showErrorMessage) {
            // Even though the service would have sent it as boolean, the value would be
            // autoboxed to Boolean, so it is safe to check like this
            if ( responseData instanceof Boolean) {
                boolean status = ((Boolean)responseData).booleanValue();
                showErrorMessage = !status; // status = true means the API had succeeded
            }
        }

        if (showErrorMessage) {
            // The app has failed to get a response from webservice. Show appropriate error message
            String message = getActivity().getString(R.string.quit_application);
            AppUtil.showDialog(getActivity(), message);
            return;
        }

        switch (playerProfileApiId) {
            case PlayerProfileApiDataProvider.GET_PLAYER_LIST_FOR_COUNTRY_ID_API: {
                HandleGetCountryListResponse(responseData);
                break;
            }

            case PlayerProfileApiDataProvider.SCRAPE_PLAYER_LIST_FOR_COUNTRY_ID_API: {
                HandleScrapeCountryListResponse(responseData);
                break;
            }

            default: break;
        }
    }

    // Handles the get player list API response
    private void HandleGetCountryListResponse(Object responseData) {

        // Try getting the data for the country list and show the list
        ArrayList<CountryEntity> countryEntityList = (ArrayList<CountryEntity>)responseData;

        if (null == countryEntityList) {
            AppUtil.logDebugMessage(TAG, "Country entity list is null. This is unexpected !!!");
            AppUtil.showDialog(getActivity(), getActivity().getString(R.string.response_failed));
            return;
        }

        // If there is no country list available yet, first scrape the data and then try
        // getting the data again.
        if ( 0 == countryEntityList.size() ) {
            PlayerProfileApiDataProvider.getInstance().scrapeCountryList(getActivity(), this);

            AppUtil.showProgressDialog(getActivity());

            return;
        }

        CountryListAdapter countryListAdapter = new CountryListAdapter(getActivity(),
                                                                            countryEntityList);
        mDrawerListView.setAdapter(countryListAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    // Country list scrapped successfully, so try getting the country list again
    private void HandleScrapeCountryListResponse(Object responseData) {

        PlayerProfileApiDataProvider.getInstance().getCountryList(getActivity(), this);

        AppUtil.showProgressDialog(getActivity());
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
