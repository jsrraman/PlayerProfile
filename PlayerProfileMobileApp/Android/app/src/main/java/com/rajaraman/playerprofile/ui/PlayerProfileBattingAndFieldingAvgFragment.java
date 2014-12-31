package com.rajaraman.playerprofile.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajaraman.playerprofile.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.rajaraman.playerprofile.ui.PlayerProfileBattingAndFieldingAvgFragment.onPlayerProfileBattingAndFieldingAvgFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link com.rajaraman.playerprofile.ui.PlayerProfileBattingAndFieldingAvgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerProfileBattingAndFieldingAvgFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPlayerProfileBattingAndFieldingAvgFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerProfileBattingAndFieldingAvg.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerProfileBattingAndFieldingAvgFragment newInstance(String param1, String param2) {
        PlayerProfileBattingAndFieldingAvgFragment fragment = new PlayerProfileBattingAndFieldingAvgFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerProfileBattingAndFieldingAvgFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_profile_batting_and_fielding_avg, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPlayerProfileBattingAndFieldingAvgFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPlayerProfileBattingAndFieldingAvgFragmentInteractionListener) activity;
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
    public interface OnPlayerProfileBattingAndFieldingAvgFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onPlayerProfileBattingAndFieldingAvgFragmentInteraction(Uri uri);
    }

}
