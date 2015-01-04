// Created by rajaraman on Dec 28, 2014
package com.rajaraman.playerprofile.ui.adapters;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.network.data.entities.PlayerEntity;
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
        String thumbnailUrl = this.playerEntityList.get(position).getThumbnailUrl();

        NetworkImageView imageView = (NetworkImageView)
                rowView.findViewById(R.id.fragment_countrylist_list_icon_country);

        if ((thumbnailUrl == null) || thumbnailUrl.isEmpty()) {
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setImageUrl(thumbnailUrl, this.imageLoader);
        }

        TextView textView = (TextView) rowView.
                            findViewById(R.id.fragment_countrylist_list_textview_country_name);

        textView.setText(this.playerEntityList.get(position).getName());

        return rowView;
    }
}