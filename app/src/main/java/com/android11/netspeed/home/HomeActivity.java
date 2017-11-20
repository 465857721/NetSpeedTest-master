package com.android11.netspeed.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android11.netspeed.R;
import com.android11.netspeed.floatspeed.SpeedFloatFragment;
import com.android11.netspeed.main.BaseActivity;
import com.android11.netspeed.main.adapter.HomeFragmentPagerAdapter;
import com.android11.netspeed.netspeed.TestSpeedFragment;
import com.android11.netspeed.setting.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.container)
    LinearLayout container;

    private List<Fragment> list_fragment = new ArrayList<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    vp.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    vp.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    vp.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SpeedFloatFragment f2 = new SpeedFloatFragment();
        TestSpeedFragment f1 = new TestSpeedFragment();
        SettingFragment f3 = new SettingFragment();
        list_fragment.add(f1);
        list_fragment.add(f2);
        list_fragment.add(f3);

        vp.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(), list_fragment));

        vp.setCurrentItem(0);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
