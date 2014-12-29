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
    private static final String ARG_PARAM1 = "CountryId";

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

    // TODO: Rename and change types of parameters
    public static PlayerListFragment newInstance(int countryId) {
        PlayerListFragment fragment = new PlayerListFragment();

        Bundle args = new Bundle();

        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);

        args.putInt(ARG_PARAM1, countryId);

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayerListFragment() {
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
        PlayerProfileApiDataProvider playerProfileApiDataProvider =
                                                        new PlayerProfileApiDataProvider();

        // Get the country id passed as a parameter during this fragment's creation
        int countryId = getArguments().getInt(ARG_PARAM1);

        playerProfileApiDataProvider.getPlayerListForCountry(getActivity(), this, countryId);

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
    public void onDataFetched(Object obj) {
        AppUtil.logDebugMessage(TAG, "onDataFetched callback");

        AppUtil.dismissProgressDialog();

        if (obj == null) {
            AppUtil.showDialog(getActivity(),
                    getActivity().getString(R.string.webservice_failed_response));

            return;
        }

        // Get the data for the country list and show the list
        ArrayList<PlayerEntity> playerEntityList = (ArrayList<PlayerEntity>)obj;

        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getActivity(),
                                                                             playerEntityList);
        mListView.setAdapter(playerListAdapter);
    }
}