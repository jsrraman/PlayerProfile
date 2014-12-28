// Created by rajaraman on Dec 28, 2014
package com.rajaraman.playerprofile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.network.data.entity.CountryEntity;
import com.rajaraman.playerprofile.network.data.provider.VolleySingleton;

import java.util.ArrayList;

public class CountryListAdapter extends ArrayAdapter<CountryEntity> {
    private Context context;
    private ArrayList<CountryEntity> countryEntityList = null;
    private ImageLoader imageLoader;

    public CountryListAdapter(Context context, ArrayList<CountryEntity> countryEntityList) {
        super(context, R.layout.list_country, countryEntityList);
        this.context = context;
        this.countryEntityList = countryEntityList;
        this.imageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_country, parent, false);

        // Get the thumbnail url from network using NetworkImageView
        NetworkImageView imageView = (NetworkImageView)
                                        rowView.findViewById(R.id.list_country_icon_country);

        imageView.setImageUrl(countryEntityList.get(position).thumbnailUrl, this.imageLoader);

        TextView textView = (TextView) rowView.findViewById(R.id.list_country_textview_country_name);

        textView.setText(countryEntityList.get(position).name);

        return rowView;
    }
}