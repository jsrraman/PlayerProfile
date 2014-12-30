package com.rajaraman.playerprofile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.network.data.entity.CountryEntity;
import com.rajaraman.playerprofile.network.data.entity.PlayerEntity;
import com.rajaraman.playerprofile.network.data.provider.DataProvider;
import com.rajaraman.playerprofile.network.data.provider.PlayerProfileApiDataProvider;
import com.rajaraman.playerprofile.ui.adapters.CountryListAdapter;
import com.rajaraman.playerprofile.ui.adapters.PlayerListAdapter;
import com.rajaraman.playerprofile.utils.AppUtil;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class PlayerListFragment extends Fragment implements
                                        AbsListView.OnItemClickListener,
                                        DataProvider.OnDataReceivedListener {

    private static final String TAG = PlayerListFragment.class.getCanonicalName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "countryId";
    private static final String ARG_PARAM2 = "countryName";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private PlayerProfileApiDataProvider playerProfileApiDataProvider;

    // TODO: Rename and change types of parameters
    public static PlayerListFragment newInstance(CountryEntity countryEntity) {
        PlayerListFragment fragment = new PlayerListFragment();

        Bundle args = new Bundle();

        args.putInt(ARG_PARAM1, countryEntity.countryId);
        args.putString(ARG_PARAM2, countryEntity.name);

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayerListFragment() {
        this.playerProfileApiDataProvider = new PlayerProfileApiDataProvider();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//        }

        // TODO: Change Adapter to display your content
        //mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);

//        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_playerlist, container, false);

//        // Set the adapter
//        mListView = (AbsListView) view.findViewById(android.R.id.list);
//        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
//
//        // Set OnItemClickListener so we can be notified on item clicks
//        mListView.setOnItemClickListener(this);

        // Get the player list

        // Get the country id passed as a parameter during this fragment's creation
        int countryId = getArguments().getInt(ARG_PARAM1);

        this.playerProfileApiDataProvider.getPlayerListForCountry(getActivity(), this, countryId);

        AppUtil.showProgressDialog(getActivity());

        return mListView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    // Callback for the backend to let this fragment know the player list from the web service
    public void onDataFetched(String status, int playerProfileApiId, Object responseData) {
        AppUtil.logDebugMessage(TAG, "onDataFetched callback");

        AppUtil.dismissProgressDialog();

        if (status.equals("failure")) {
            // The app has failed to get a response from webservice. There is no point in
            // proceeding further as this is the starting point in the app, so show the error
            // and quit the app.
            String message = getActivity().getString(R.string.response_failed);
            AppUtil.showDialog(getActivity(), message);

            return;
        }

        switch (playerProfileApiId) {
            case PlayerProfileApiDataProvider.GET_PLAYER_LIST_FOR_COUNTRY_ID_API: {
                HandleGetPlayerListResponse(responseData);
                break;
            }

            case PlayerProfileApiDataProvider.SCRAPE_PLAYER_LIST_FOR_COUNTRY_ID_API: {
                HandleScrapePlayerListResponse(responseData);
                break;
            }

            default: break;
        }
    }

    private void HandleGetPlayerListResponse(Object responseData) {

        // Try getting the data for the country list and show the list
        ArrayList<PlayerEntity> playerEntityList = (ArrayList<PlayerEntity>)responseData;

        // If there is no player list yet available for this country, first scrape the data
        // and then try getting the data again.
        if (null != playerEntityList) {
            if ( 0 == playerEntityList.size() ) {
                int countryId = getArguments().getInt(ARG_PARAM1);
                String countryName = getArguments().getString(ARG_PARAM2);

                this.playerProfileApiDataProvider.
                        scrapePlayerListForCountry(getActivity(), this, countryId, countryName);

                AppUtil.showProgressDialog(getActivity());

                return;
            }
        }

        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getActivity(),
                                                                            playerEntityList);

        mListView.setAdapter(playerListAdapter);
    }

    // Player list scrapped successfully, so try getting the player list for the country again
    private void HandleScrapePlayerListResponse(Object responseData) {

        // Get the country id passed as a parameter during this fragment's creation
        int countryId = getArguments().getInt(ARG_PARAM1);

        this.playerProfileApiDataProvider.getPlayerListForCountry(getActivity(), this, countryId);

        AppUtil.showProgressDialog(getActivity());
    }
}