package com.esri.idt.messageinabottle;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.ags.geoprocessing.GPDouble;
import com.esri.core.tasks.ags.geoprocessing.GPFeatureRecordSetLayer;
import com.esri.core.tasks.ags.geoprocessing.GPParameter;
import com.esri.core.tasks.ags.geoprocessing.GPResultResource;
import com.esri.core.tasks.ags.geoprocessing.Geoprocessor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final Geoprocessor gp = new Geoprocessor("http://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_Currents_World/GPServer/MessageInABottle");
    private final ArrayList<GPParameter> params = new ArrayList<>(1);
    private final GPFeatureRecordSetLayer gpLayer = new GPFeatureRecordSetLayer("Input_Point") {
        @Override
        public String generateValueParams() throws Exception {
            return super.generateValueParams().replace("\"esriGeometryPoint\",\"spatialReference\":", "\"esriGeometryPoint\",\"sr\":");
        }
    };
    private final ArrayList<Graphic> graphics = new ArrayList<>(1);
    private final GraphicsLayer graphicsLayer = new GraphicsLayer();
    private final SimpleMarkerSymbol startPointSymbol = new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
    private final SimpleMarkerSymbol endPointSymbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
    private final SimpleLineSymbol pathSymbol = new SimpleLineSymbol(Color.YELLOW, 5, SimpleLineSymbol.STYLE.SOLID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MapView mapView = (MapView) findViewById(R.id.mapView);

        mapView.addLayer(graphicsLayer);

        gpLayer.setGeometryType(Geometry.Type.POINT);
        gpLayer.setGraphics(graphics);
        params.add(gpLayer);
        GPDouble days = new GPDouble("Days");
        days.setValue(180);
        params.add(days);

        final OnSingleTapListener tapListener = new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                mapView.setOnSingleTapListener(null);

                final Snackbar snackbar_calculating = Snackbar.make(
                        findViewById(R.id.coordinatorLayout_main),
                        R.string.calculating,
                        Snackbar.LENGTH_INDEFINITE
                );
                snackbar_calculating.show();

                graphicsLayer.removeAll();

                Point pt = (Point) GeometryEngine.normalizeCentralMeridian(mapView.toMapPoint(x, y), mapView.getSpatialReference());
                Graphic graphic = new Graphic(pt, startPointSymbol, 1);
                graphicsLayer.addGraphic(graphic);
                if (0 == graphics.size()) {
                    graphics.add(graphic);
                } else {
                    graphics.set(0, graphic);
                }
                new AsyncTask<ArrayList<GPParameter>, Void, GPParameter[]>() {

                    @Override
                    protected GPParameter[] doInBackground(ArrayList<GPParameter>... params) {
                        GPResultResource result = null;
                        try {
                            result = gp.execute(params[0]);
                            return result.getOutputParameters();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(GPParameter[] results) {
                        snackbar_calculating.dismiss();
                        if (null != results) {
                            for (GPParameter result : results) {
                                GPFeatureRecordSetLayer resultRecordSet = (GPFeatureRecordSetLayer) results[0];
                                if (0 < resultRecordSet.getGraphics().size()) {
                                    Graphic resultGraphic = resultRecordSet.getGraphics().get(0);
                                    Graphic displayGraphic = new Graphic(
                                            GeometryEngine.project(resultGraphic.getGeometry(), resultRecordSet.getSpatialReference(), mapView.getSpatialReference()),
                                            pathSymbol,
                                            resultGraphic.getAttributes(),
                                            0);
                                    graphicsLayer.addGraphic(displayGraphic);

                                    if (displayGraphic.getGeometry() instanceof Polyline) {
                                        Polyline line = (Polyline) displayGraphic.getGeometry();
                                        Graphic endPointGraphic = new Graphic(line.getPoint(line.getPointCount() - 1), endPointSymbol, 1);
                                        graphicsLayer.addGraphic(endPointGraphic);
                                    }
                                }
                            }
                        } else {
                            Snackbar.make(findViewById(R.id.coordinatorLayout_main), R.string.error, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }.execute(params);

                mapView.setOnSingleTapListener(this);
            }
        };

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (STATUS.INITIALIZED.equals(status)) {
                    mapView.setOnSingleTapListener(tapListener);
                    gp.setOutSR(mapView.getSpatialReference());
                    gpLayer.setSpatialReference(mapView.getSpatialReference());
                    mapView.enableWrapAround(true);
                }
            }
        });
    }
}
