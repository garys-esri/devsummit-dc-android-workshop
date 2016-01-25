package com.esri.devsummit.dc.year2016.networkanalysttasks.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.esri.devsummit.dc.year2016.networkanalysttasks.R;

public class MainActivity extends AppCompatActivity {

    private class FabOnClickListener implements View.OnClickListener {

        private boolean isShowing = false;

        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

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

}
