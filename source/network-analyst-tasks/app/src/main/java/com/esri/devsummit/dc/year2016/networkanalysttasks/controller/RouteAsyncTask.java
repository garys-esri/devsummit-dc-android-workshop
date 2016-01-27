package com.esri.devsummit.dc.year2016.networkanalysttasks.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;

public class RouteAsyncTask extends AsyncTask<Point, Integer, RouteResult> {

    private static final String TAG = RouteAsyncTask.class.getSimpleName();

    private int outSrWkid = 3857;
    private RouteTask routeTask = null;

    public RouteAsyncTask(int outSrWkid, RouteTask routeTask) {
        this.outSrWkid = outSrWkid;
        this.routeTask = routeTask;
    }

    @Override
    protected RouteResult doInBackground(Point... stops) {
        SpatialReference outSr = SpatialReference.create(outSrWkid);
        RouteParameters rp = null;
        try {
            rp = routeTask.retrieveDefaultRouteTaskParameters();
            NAFeaturesAsFeature rfaf = new NAFeaturesAsFeature();
            SpatialReference serviceSr = rp.getOutSpatialReference();
            Graphic[] stopGraphics = new Graphic[stops.length];
            for (int i = 0; i < stops.length; i++) {
                Point stop = (Point) GeometryEngine.project(stops[i], outSr, serviceSr);
                StopGraphic stopGraphic = new StopGraphic(stop);
                stopGraphics[i] = stopGraphic;
            }
            rfaf.setFeatures(stopGraphics);
            rfaf.setCompressedRequest(true);
            rp.setStops(rfaf);
            rp.setOutSpatialReference(outSr);

            return routeTask.solve(rp);
        } catch (Exception e) {
            Log.e(TAG, null, e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(RouteResult routeResult) {
        System.out.println("Got it!");
    }
}
