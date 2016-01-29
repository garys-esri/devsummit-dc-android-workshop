package com.esri.devsummit.dc.year2016.networkanalysttasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.devsummit.dc.year2016.networkanalysttasks.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to manage location choosers.
 */
public abstract class NetworkAnalystFormFragment extends Fragment implements View.OnClickListener {

    public static final int RESULT_TAP_POINT_OK = 1;

    public static final String ACTION_TAP_POINT = NetworkAnalystFormFragment.class.getName() + ".ACTION_TAP_POINT";

    public static final String EXTRA_XY = "EXTRA_XY";
    public static final String EXTRA_SR_WKID = "EXTRA_SR_WKID";

    private final ArrayList<Point> stops = new ArrayList<>();
    private final ArrayList<Integer> stopSrWkids = new ArrayList<>();

    protected abstract View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected List<Point> getStops() {
        return stops;
    }

    protected List<Integer> getStopSpatialReferenceWkids() {
        return stopSrWkids;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateView(inflater, container, savedInstanceState);

        if (view instanceof ViewGroup) {
            List<View> locationChoosers = getLocationChoosers((ViewGroup) view);
            for (View chooser : locationChoosers) {
                View button = chooser.findViewById(R.id.toggleButton_choosePoint);
                if (null != button) {
                    button.setOnClickListener(this);
                }
                stops.add(null);
                stopSrWkids.add(null);
            }
        }

        return view;
    }

    protected List<View> getLocationChoosers(ViewGroup viewGroup) {
        return getLocationChoosers(viewGroup, new ArrayList<View>());
    }

    private List<View> getLocationChoosers(ViewGroup viewGroup, ArrayList<View> locationChoosers) {
        if (getString(R.string.location_chooser_tag).equals(viewGroup.getTag())) {
            locationChoosers.add(viewGroup);
            return locationChoosers;
        } else {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof ViewGroup) {
                    locationChoosers.addAll(getLocationChoosers((ViewGroup) childView));
                }
            }
            return locationChoosers;
        }
    }

    @Override
    public void onClick(View v) {
        MainMapFragment mapFragment = (MainMapFragment) getFragmentManager().findFragmentById(R.id.fragment_mainMap);
        //The requestCode is the index of the location chooser
        final int requestCode = getLocationChoosers((ViewGroup) getView()).indexOf(v.getParent());
        if (null != mapFragment && null != mapFragment.getView()) {
            final MapView mapView = (MapView) mapFragment.getView().findViewById(R.id.map);
            if (null != mapView) {
                mapView.setOnSingleTapListener(new OnSingleTapListener() {
                    @Override
                    public void onSingleTap(float x, float y) {
                        Point pt = mapView.toMapPoint(x, y);
                        onActivityResult(
                                requestCode,
                                RESULT_TAP_POINT_OK,
                                createResponseIntentFromPoint(pt, mapView.getSpatialReference().getID())
                        );
                    }
                });
            }
        } else {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setAction(ACTION_TAP_POINT);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_TAP_POINT_OK == resultCode) {
            double[] xy = data.getExtras().getDoubleArray(EXTRA_XY);
            int wkid = data.getExtras().getInt(EXTRA_SR_WKID);
            View chooser = getLocationChoosers((ViewGroup) getView()).get(requestCode);
            TextView textView = (TextView) chooser.findViewById(R.id.textView_locationText);
            textView.setText(xy[0] + ", " + xy[1]);
            stops.remove(requestCode);
            stops.add(requestCode, new Point(xy[0], xy[1]));
            stopSrWkids.remove(requestCode);
            stopSrWkids.add(requestCode, wkid);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static Intent createResponseIntentFromPoint(Point pt, int srWkid) {
        Intent intent = new Intent();
        intent.putExtra(NetworkAnalystFormFragment.EXTRA_XY, new double[]{pt.getX(), pt.getY()});
        intent.putExtra(NetworkAnalystFormFragment.EXTRA_SR_WKID, srWkid);
        return intent;
    }

}
