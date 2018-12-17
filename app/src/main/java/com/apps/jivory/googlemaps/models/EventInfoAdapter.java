package com.apps.jivory.googlemaps.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.jivory.googlemaps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class EventInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View view;

    public EventInfoAdapter(Context context) {
        this.view = LayoutInflater.from(context).inflate(R.layout.marker, null);
        this.context = context;
    }
    

    public void initView(Marker marker) {
        String title = marker.getTitle();
        TextView textViewTitle = view.findViewById(R.id.textView_Post_Title);

        String snippet = marker.getSnippet();
        TextView textViewSnippet = view.findViewById(R.id.textView_Snippet);

        textViewTitle.setText(title);
        textViewSnippet.setText(snippet);


    }

    @Override
    public View getInfoWindow(Marker marker) {
        initView(marker);

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        initView(marker);
        return view;
    }
}
