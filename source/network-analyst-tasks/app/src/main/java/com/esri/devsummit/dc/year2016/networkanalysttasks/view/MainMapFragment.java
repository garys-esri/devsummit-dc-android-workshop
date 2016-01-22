package com.esri.devsummit.dc.year2016.networkanalysttasks.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.devsummit.dc.year2016.networkanalysttasks.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainMapFragment extends Fragment {

    public MainMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_map, container, false);
    }
}
