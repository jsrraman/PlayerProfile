// Created by rajaraman on Dec 28, 2014
package com.rajaraman.playerprofile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.network.data.entity.CountryEntity;
import com.rajaraman.playerprofile.network.data.entity.PlayerEntity;
import com.rajaraman.playerprofile.network.data.provider.VolleySingleton;

import java.util.ArrayList;

public class PlayerListAdapter extends ArrayAdapter<PlayerEntity> {
    private Context context;
    private ArrayList<PlayerEntity> playerEntityList = null;
    private ImageLoader imageLoader;

    public PlayerListAdapter(Context context, ArrayList<PlayerEntity> playerEntityList) {
        super(context, R.layout.fragment_playerlist_list, playerEntityList);
        this.context = context;
        this.playerEntityList = playerEntityList;
        this.imageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.fragment_countrylist_list, parent, false);

        // Get the thumbnail url from network using NetworkImageView
        NetworkImageView imageView = (NetworkImageView)
                              rowView.findViewById(R.id.fragment_countrylist_list_icon_country);

        imageView.setImageUrl(playerEntityList.get(position).thumbnailUrl, this.imageLoader);

        TextView textView = (TextView) rowView.
                              findViewById(R.id.fragment_countrylist_list_textview_country_name);

        textView.setText(playerEntityList.get(position).name);

        return rowView;
    }
}