package com.xty.slidingtab;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xty.slidingtablayout.view.SlidingTabLayout;

/**
 * @author xty
 *         Created by xty on 2015/9/6.
 */
public class TabViewPagerAdapter extends PagerAdapter implements SlidingTabLayout.TabIconProvider {


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        //Log.i("TabViewPagerAdapter", "destroyItem() [position: " + position + "]");
    }


    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        // Retrieve a TextView from the inflated View, and update it's text
        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(String.valueOf(position + 1));

    //    Log.i("TabViewPagerAdapter", "instantiateItem() [position: " + position + "]");

        // Return the View
        return view;
    }


    @Override
    public int getPageIconResId(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_tab_alarms;
            case 2:
                return R.drawable.ic_tab_stats;
            case 1:
                return R.drawable.ic_tab_leaderboard;
        }
        return 0;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "Item " + (position + 1);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }
}
