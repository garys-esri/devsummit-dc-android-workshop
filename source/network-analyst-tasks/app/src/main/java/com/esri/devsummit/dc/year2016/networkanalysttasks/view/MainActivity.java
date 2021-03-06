package com.esri.devsummit.dc.year2016.networkanalysttasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.devsummit.dc.year2016.networkanalysttasks.R;

public class MainActivity extends AppCompatActivity {

    private class FabOnClickListener implements View.OnClickListener {

        private boolean isShowing = false;

        @Override
        public void onClick(View view) {
            FloatingActionButton fab_closestFacility = (FloatingActionButton) findViewById(R.id.fab_closestFacility);
            FloatingActionButton fab_serviceArea = (FloatingActionButton) findViewById(R.id.fab_serviceArea);
            FloatingActionButton fab_route = (FloatingActionButton) findViewById(R.id.fab_route);

            FrameLayout.LayoutParams layoutParams;
            if (!isShowing) {
                //Show the FAB menu
                layoutParams = (FrameLayout.LayoutParams) fab_closestFacility.getLayoutParams();
                layoutParams.bottomMargin += (int) (fab_closestFacility.getHeight() * 1.25);
                fab_closestFacility.setLayoutParams(layoutParams);
                fab_closestFacility.startAnimation(show_fab_closestFacility);
                fab_closestFacility.setClickable(true);

                layoutParams = (FrameLayout.LayoutParams) fab_serviceArea.getLayoutParams();
                layoutParams.bottomMargin += (int) (fab_serviceArea.getHeight() * 2.5);
                fab_serviceArea.setLayoutParams(layoutParams);
                fab_serviceArea.startAnimation(show_fab_serviceArea);
                fab_serviceArea.setClickable(true);

                layoutParams = (FrameLayout.LayoutParams) fab_route.getLayoutParams();
                layoutParams.bottomMargin += (int) (fab_route.getHeight() * 3.75);
                fab_route.setLayoutParams(layoutParams);
                fab_route.startAnimation(show_fab_route);
                fab_route.setClickable(true);

                isShowing = true;
            } else {
                //Code to hide the FAB menu
                layoutParams = (FrameLayout.LayoutParams) fab_closestFacility.getLayoutParams();
                layoutParams.bottomMargin -= (int) (fab_closestFacility.getHeight() * 1.25);
                fab_closestFacility.setLayoutParams(layoutParams);
                fab_closestFacility.startAnimation(hide_fab_closestFacility);
                fab_closestFacility.setClickable(false);

                layoutParams = (FrameLayout.LayoutParams) fab_serviceArea.getLayoutParams();
                layoutParams.bottomMargin -= (int) (fab_serviceArea.getHeight() * 2.5);
                fab_serviceArea.setLayoutParams(layoutParams);
                fab_serviceArea.startAnimation(hide_fab_serviceArea);
                fab_serviceArea.setClickable(false);

                layoutParams = (FrameLayout.LayoutParams) fab_route.getLayoutParams();
                layoutParams.bottomMargin -= (int) (fab_route.getHeight() * 3.75);
                fab_route.setLayoutParams(layoutParams);
                fab_route.startAnimation(hide_fab_route);
                fab_route.setClickable(false);

                isShowing = false;
            }
        }

    }

    private Animation show_fab_closestFacility = null;
    private Animation hide_fab_closestFacility = null;
    private Animation show_fab_serviceArea = null;
    private Animation hide_fab_serviceArea = null;
    private Animation show_fab_route = null;
    private Animation hide_fab_route = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show_fab_closestFacility = AnimationUtils.loadAnimation(this, R.anim.fab_closest_facility_show);
        hide_fab_closestFacility = AnimationUtils.loadAnimation(this, R.anim.fab_closest_facility_hide);
        show_fab_serviceArea = AnimationUtils.loadAnimation(this, R.anim.fab_service_area_show);
        hide_fab_serviceArea = AnimationUtils.loadAnimation(this, R.anim.fab_service_area_hide);
        show_fab_route = AnimationUtils.loadAnimation(this, R.anim.fab_route_show);
        hide_fab_route = AnimationUtils.loadAnimation(this, R.anim.fab_route_hide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new FabOnClickListener());
    }

    public void fab_route_onClick(View view) {
        showNetworkAnalystForm(0);
    }

    public void fab_serviceArea_onClick(View view) {
        showNetworkAnalystForm(1);
    }

    public void fab_closestFacility_onClick(View view) {
        showNetworkAnalystForm(2);
    }

    private void showNetworkAnalystForm(int index) {
        View view_networkAnalystForms = findViewById(R.id.view_networkAnalysisForms);
        if (null != view_networkAnalystForms) {
            ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator_networkAnalystForms);
            viewAnimator.setDisplayedChild(index);
            view_networkAnalystForms.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, NetworkAnalystFormActivity.class);
            intent.putExtra(NetworkAnalystFormActivity.EXTRA_NETWORK_ANALYST_FORM_INDEX, index);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final MapView mapView = (MapView) findViewById(R.id.map);
        mapView.unpause();

        if (NetworkAnalystFormFragment.ACTION_TAP_POINT.equals(getIntent().getAction())) {
            mapView.setOnSingleTapListener(new OnSingleTapListener() {
                @Override
                public void onSingleTap(float x, float y) {
                    mapView.setOnSingleTapListener(null);
                    Point pt = mapView.toMapPoint(x, y);
                    Intent intent = NetworkAnalystFormFragment.createResponseIntentFromPoint(
                            pt,
                            mapView.getSpatialReference().getID()
                    );
                    setResult(NetworkAnalystFormFragment.RESULT_TAP_POINT_OK, intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MapView) findViewById(R.id.map)).pause();
    }

}
