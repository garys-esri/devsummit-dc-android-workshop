package com.esri.devsummit.dc.year2016.networkanalysttasks.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.devsummit.dc.year2016.networkanalysttasks.R;

public class MainMapFragment extends Fragment {

    private Layer[] layers = new Layer[] {
        new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/arcgis/rest/services/World_Topo_Map/MapServer")
    };
    private Point center = GeometryEngine.project(-117.195668, 34.056215, SpatialReference.create(3857));
    private double scale = 577790.554289;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_map, container, false);

        final MapView mapView = getMapView(view);
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (STATUS.INITIALIZED.equals(status)) {
                    mapView.zoomToScale(center, scale);
                }
            }
        });
        for (Layer layer : layers) {
            mapView.addLayer(layer);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        MapView mapView = getMapView(getView());
        layers = mapView.getLayers().clone();
        center = mapView.getCenter();
        scale = mapView.getScale();
        mapView.removeAll();
        mapView.recycle();

        super.onDestroyView();
    }

    private MapView getMapView(View ancestorView) {
        return (MapView) ancestorView.findViewById(R.id.map);
    }

}
