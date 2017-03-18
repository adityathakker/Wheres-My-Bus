package com.adityathakker.wmb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.adityathakker.wmb.R;
import com.adityathakker.wmb.custom.CustomViewPager;
import com.adityathakker.wmb.ui.fragment.RoutingFragment;
import com.adityathakker.wmb.ui.fragment.LiveTrackingFragment;
import com.adityathakker.wmb.utils.AppConst;

import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private CustomViewPager viewPager;

    private String sourceName;
    private String destinationName;
    private int sourceId;
    private int destinationId;
    private String busNumber;
    private int stops;

    private String middleName;
    private int middleId;
    private String startingBusNumber;
    private String endingBusNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Boolean isSingle = intent.getBooleanExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_IS_SINGLE, true);
        if(isSingle){
            sourceName = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_NAME);
            destinationName = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_NAME);
            sourceId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_ID, -1);
            destinationId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_ID, -1);
            busNumber = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_NUMBER);
            stops = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STOPS, -1);
        }else{
            sourceName = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_NAME);
            destinationName = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_NAME);
            sourceId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_ID, -1);
            destinationId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_ID, -1);
            busNumber = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_NUMBER);
            stops = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STOPS, -1);
            middleName = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_MIDDLE_NAME);
            middleId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_MIDDLE_ID, -1);
            startingBusNumber = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_BUS_NUMBER);
            endingBusNumber = intent.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_BUS_NUMBER);
        }


        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        LiveTrackingFragment liveTrackingFragment = new LiveTrackingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_STOP_ID, sourceId);
        bundle.putString(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_NUMBER, busNumber);
        liveTrackingFragment.setArguments(bundle);

        adapter.addFragment(liveTrackingFragment, "Tracking");
        adapter.addFragment(new RoutingFragment(), "Route");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
