package com.esri.devsummit.dc.year2016.networkanalysttasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;

public class NetworkAnalystTasksPagerAdapter extends FragmentPagerAdapter {

    public static class FormFragment extends Fragment {
        public static final String ARG_VIEW_ID = "view_id";

        private final HashSet<CompoundButton> locationChooserToggleButtons = new HashSet<>();

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int viewId = args.getInt(ARG_VIEW_ID);
            View rootView = inflater.inflate(viewId, container, false);

            //Add dynamic components
            switch (viewId) {
                case R.layout.fragment_route_form:
                    if (rootView instanceof ViewGroup) {
                        ViewGroup rootViewGroup = (ViewGroup) rootView;
                        for (int i = 1; i <= 2; i++) {
                            final ViewGroup locationChooser = (ViewGroup) inflater.inflate(R.layout.location_chooser, rootViewGroup, false);
                            TextView textView_locationText = (TextView) locationChooser.findViewById(R.id.textView_locationText);
                            textView_locationText.setHint("Stop " + i);
                            CompoundButton toggleButton_choosePoint = (CompoundButton) locationChooser.findViewById(R.id.toggleButton_choosePoint);
                            locationChooserToggleButtons.add(toggleButton_choosePoint);
                            toggleButton_choosePoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        Iterator<CompoundButton> buttons = locationChooserToggleButtons.iterator();
                                        while (buttons.hasNext()) {
                                            CompoundButton button = buttons.next();
                                            if (!button.equals(buttonView)) {
                                                button.setChecked(false);
                                            }
                                        }
                                    }
                                }
                            });
                            rootViewGroup.addView(locationChooser);
                        }
                    }
                    break;
            }

            return rootView;
        }
    }

    public NetworkAnalystTasksPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FormFragment();
        Bundle args = new Bundle();
        int viewId = 0;
        switch (position) {
            case 0:
                viewId = R.layout.fragment_route_form;
                break;

            case 1:
                viewId = R.layout.fragment_service_area_form;
                break;

            case 2:
                viewId = R.layout.fragment_closest_facility_form;
                break;
        }
        args.putInt(FormFragment.ARG_VIEW_ID, viewId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
