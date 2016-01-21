package com.esri.devsummit.dc.year2016.networkanalysttasks;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.esri.android.map.MapView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TabLayout.Tab routeTab = null;
    private TabLayout.Tab serviceAreaTab = null;
    private TabLayout.Tab closestFacilityTab = null;

    private NetworkAnalystTasksPagerAdapter pagerAdapter = null;
    private ViewPager viewPager = null;
    private MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_supportActionBar));

//        ActionBar actionBar = getSupportActionBar();

        pagerAdapter = new NetworkAnalystTasksPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager_naTools);
        viewPager.setAdapter(pagerAdapter);
        mapView = (MapView) findViewById(R.id.mapView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.action_route:
                viewPager.setCurrentItem(0);
                viewPager.setVisibility(View.VISIBLE);
                return true;

            case R.id.action_serviceArea:
                viewPager.setCurrentItem(1);
                viewPager.setVisibility(View.VISIBLE);
                return true;

            case R.id.action_closestFacility:
                viewPager.setCurrentItem(2);
                viewPager.setVisibility(View.VISIBLE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause(){
        mapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.unpause();
    }

}
