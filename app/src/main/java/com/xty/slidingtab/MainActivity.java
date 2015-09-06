package com.xty.slidingtab;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.xty.slidingtablayout.view.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setSupportActionBar(mToolbar);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new TabViewPagerAdapter());

        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setTabHighlight();
//        mTabLayout.setShowDivider(true);
        mToolbar.inflateMenu(R.menu.menu_main);

        mToolbar.setContentInsetsRelative(0, 0);
    }


}
