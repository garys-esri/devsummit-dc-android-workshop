package com.esri.devsummit.dc.year2016.networkanalysttasks.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ViewAnimator;

import com.esri.devsummit.dc.year2016.networkanalysttasks.R;

public class NetworkAnalystFormActivity extends AppCompatActivity {

    public static final String EXTRA_NETWORK_ANALYST_FORM_INDEX = "EXTRA_NETWORK_ANALYST_FORM_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_analyst_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_networkAnalystForm);
        setSupportActionBar(toolbar);

        ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator_networkAnalystForms);
        if (null != viewAnimator) {
            int index = getIntent().getExtras().getInt(EXTRA_NETWORK_ANALYST_FORM_INDEX, 0);
            viewAnimator.setDisplayedChild(index);
        }
    }

}
