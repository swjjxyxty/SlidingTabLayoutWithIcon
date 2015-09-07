# SlidingTabLayoutWithIcon#
## Use ImageView instead of TextView##
![](https://github.com/swjjxyxty/SlidingTabLayoutWithIcon/blob/master/preview.gif)

## How to use : ##
## Gradle##
	compile 'com.xty:library:1.0.0'
## activity_main.xml##

	<android.support.design.widget.CoordinatorLayout
    	xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:fitsSystemWindows="true"
    	>

	    <android.support.design.widget.AppBarLayout
	        android:layout_width="match_parent"
	        android:layout_height="?actionBarSize">

        <android.support.v7.widget.Toolbar
            android:id="@+id/id_toolbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="?attr/colorPrimary"
            app:popupTheme="@style/OverflowPopupMenu"
            app:theme="@style/ToolBarTheme">

            <com.xty.slidingtablayout.view.SlidingTabLayout
                android:id="@+id/id_tablayout"
                android:layout_width="250.0dip"
                android:layout_height="?actionBarSize"
                />
        </android.support.v7.widget.Toolbar>
    </android.support.design.widget.AppBarLayout>

    <android.support.v4.view.ViewPager
        android:id="@+id/id_viewpager"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

	</android.support.design.widget.CoordinatorLayout>

Add `com.xty.slidingtablayout.view.SlidingTabLayout` to layout file

## ViewPageAdapter ##
Let you adpater class implements TabIconProvider

```java
public class TabViewPagerAdapter extends PagerAdapter implements SlidingTabLayout.TabIconProvider{
	
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        ......
    }

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		......
		return view;
	}

		@Override
    public boolean isViewFromObject(View view, Object o) {
        return .......;
    }
}
```


##License
    Copyright 2015 xty

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
