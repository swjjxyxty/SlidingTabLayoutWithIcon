package com.xty.slidingtablayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xty.slidingtablayout.R;

/**
 * @author xty
 *         Created by xty on 2015/9/6.
 */
public class SlidingTabLayout extends HorizontalScrollView {

    /**
     * title offset dip
     */
    private static final int TITLE_OFFSET_DIPS = 24;

    /**
     * default TabViewLayout id .
     * if not call {@link #setCustomTabView(int, int)}, will use this default.
     */
    private static final int mDefaultTabViewLayoutId = R.layout.default_tabview;
    /**
     * default ImageView id
     */
    private static final int mDefaultImageViewId = R.id.tab_icon;

    /**
     * real title offset .<br>
     *
     * @<code> TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density</code>
     */
    private int mTitleOffset;
    /**
     * tab view layout resource id
     */
    private int mTabViewLayoutId;

    /**
     * tab icon image view id
     */
    private int mTabViewImageViewId;

    /**
     * TabView viewPager
     */
    private ViewPager mViewPager;
    /**
     * ViewPager listener
     */
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    /**
     * Whether display divider,default value false
     */
    private boolean showDivider = false;

    private final SlidingTabStrip mTabStrip;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //disable horizontal scroll bar
        setHorizontalScrollBarEnabled(false);

        setFillViewport(true);
        // calculate real title offset
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);


        mTabStrip = new SlidingTabStrip(context);
        // set gravity center
        mTabStrip.setGravity(Gravity.CENTER);
        // add mTabStrip in View
        addView(mTabStrip, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    /**
     * Whether display divider<br>
     * default not show
     *
     * @return whether show divider
     */
    public boolean isShowDivider() {
        return showDivider;
    }

    /**
     * set show divider
     *
     * @param showDivider
     */
    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
    }

    /**
     * set custom tabColorizer
     *
     * @param tabColorizer {@link com.xty.slidingtablayout.view.SlidingTabLayout.TabColorizer}
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * set selected indicator color<br>
     * if ready call {@link #setCustomTabColorizer(TabColorizer)},please not call this.
     * because will make custom tabcolorizer fail.
     *
     * @param colors int color value
     */
    public void setSelectedIndicatorColors(@NonNull int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * set divider color
     * if ready call {@link #setCustomTabColorizer(TabColorizer)},please not call this.
     * because will make custom tabcolorizer fail.
     *
     * @param colors int color value
     */
    public void setDividerColors(@NonNull int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    /**
     * set viewpager listener
     *
     * @param listener
     * @see android.support.v4.view.ViewPager.OnPageChangeListener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * set custom tab view resId and imageViewId
     *
     * @param layoutResId layout file resId
     * @param imageViewId imageview resId
     */
    public void setCustomTabView(@LayoutRes int layoutResId, @IdRes int imageViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewImageViewId = imageViewId;
    }

    /**
     * set viewpager
     *
     * @param viewPager
     * @see ViewPager
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStripAndIcon();
        }
    }


    private void populateTabStripAndIcon() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        try {
            final TabIconProvider iconProvider = (TabIconProvider) mViewPager.getAdapter();

            final View.OnClickListener tabClickListener = new TabClickListener();

            for (int i = 0; i < adapter.getCount(); i++) {
                View tabView = null;
                ImageView tabIconView = null;

                if (mTabViewLayoutId != 0) {
                    tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                            false);
                    tabIconView = (ImageView) tabView.findViewById(mTabViewImageViewId);
                }
                if (tabView == null) {
                    tabView = LayoutInflater.from(getContext()).inflate(mDefaultTabViewLayoutId, mTabStrip, false);
                }
                if (tabIconView == null) {
                    tabIconView = (ImageView) tabView.findViewById(mDefaultImageViewId);
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                layoutParams.width = 0;
                layoutParams.weight = 1.0f;
                tabIconView.setImageResource(iconProvider.getPageIconResId(i));
                tabView.setOnClickListener(tabClickListener);

                mTabStrip.addView(tabView);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(e.getMessage()
                    + adapter.getClass().getSimpleName() + " must implement TabIconProvider!");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    /**
     * set tab high light
     */
    public void setTabHighlight() {
        View child;
        float alpha;
        for (int i = 0; i < mTabStrip.getChildCount(); ++i) {
            child = mTabStrip.getChildAt(i);
            alpha = i == 0 ? 1.0f : 0.5f;
            child.setAlpha(alpha);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
            View child;
            float alpha;
            for (int i = 0; i < mTabStrip.getChildCount(); ++i) {
                mTabStrip.getChildAt(i).setSelected(position == i);
                child = mTabStrip.getChildAt(i);
                alpha = position == i ? 1.0f : 0.5f;

                child.setAlpha(alpha);
            }
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    private class SlidingTabStrip extends LinearLayout {


        private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 38;
        private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
        private static final int DEFAULT_SELECTED_INDICATOR_COLOR = -1;
        private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;


        private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
        private static final byte DEFAULT_DIVIDER_COLOR_ALPHA = 0x20;
        private static final float DEFAULT_DIVIDER_HEIGHT = 0.5f;

        private final int mBottomBorderThickness;
        private final Paint mBottomBorderPaint;

        private final int mSelectedIndicatorThickness;
        private final Paint mSelectedIndicatorPaint;

        private final int mDefaultBottomBorderColor;

        private int mSelectedPosition;
        private float mSelectionOffset;

        private final Paint mDividerPaint;
        private final float mDividerHeight;

        private SlidingTabLayout.TabColorizer mCustomTabColorizer;
        private final SimpleTabColorizer mDefaultTabColorizer;

        public SlidingTabStrip(Context context) {
            this(context, null);
        }

        public SlidingTabStrip(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public SlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setWillNotDraw(false);
            float density = getResources().getDisplayMetrics().density;
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.colorForeground, value, true);
            int themeForegroundColor = value.data;

            mDefaultBottomBorderColor = setColorAlpha(themeForegroundColor,
                    DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);

            mDefaultTabColorizer = new SimpleTabColorizer();
            mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
            mDefaultTabColorizer.setDividerColors(setColorAlpha(themeForegroundColor,
                    DEFAULT_DIVIDER_COLOR_ALPHA));

            mBottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
            mBottomBorderPaint = new Paint();
            mBottomBorderPaint.setColor(mDefaultBottomBorderColor);

            mSelectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
            mSelectedIndicatorPaint = new Paint();

            mDividerHeight = DEFAULT_DIVIDER_HEIGHT;
            mDividerPaint = new Paint();
            mDividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));
        }

        void setCustomTabColorizer(SlidingTabLayout.TabColorizer customTabColorizer) {
            mCustomTabColorizer = customTabColorizer;
            invalidate();
        }

        void setSelectedIndicatorColors(int... colors) {
            // Make sure that the custom colorizer is removed
            mCustomTabColorizer = null;
            mDefaultTabColorizer.setIndicatorColors(colors);
            invalidate();
        }

        void setDividerColors(int... colors) {
            // Make sure that the custom colorizer is removed
            mCustomTabColorizer = null;
            mDefaultTabColorizer.setDividerColors(colors);
            invalidate();
        }

        void onViewPagerPageChanged(int position, float positionOffset) {
            mSelectedPosition = position;
            mSelectionOffset = positionOffset;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            final int height = getHeight();
            final int childCount = getChildCount();
            final int dividerHeightPx = (int) (Math.min(Math.max(0f, mDividerHeight), 1f) * height);
            final SlidingTabLayout.TabColorizer tabColorizer = mCustomTabColorizer != null
                    ? mCustomTabColorizer
                    : mDefaultTabColorizer;

            if (childCount > 0) {
                View selectedTitle = getChildAt(mSelectedPosition);
                int left = selectedTitle.getLeft();
                int right = selectedTitle.getRight();
                int color = tabColorizer.getIndicatorColor(mSelectedPosition);

                if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                    int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1);
                    if (color != nextColor) {
                        color = blendColors(nextColor, color, mSelectionOffset);
                    }

                    // Draw the selection partway between the tabs
                    View nextTitle = getChildAt(mSelectedPosition + 1);
                    left = (int) (mSelectionOffset * nextTitle.getLeft() +
                            (1.0f - mSelectionOffset) * left);
                    right = (int) (mSelectionOffset * nextTitle.getRight() +
                            (1.0f - mSelectionOffset) * right);
                }

                mSelectedIndicatorPaint.setColor(color);

                canvas.drawRect(left, height - mSelectedIndicatorThickness, right,
                        height, mSelectedIndicatorPaint);
            }

            canvas.drawRect(0, height - mBottomBorderThickness, getWidth(), height, mBottomBorderPaint);

            if (showDivider) {

                // divider
                int separatorTop = (height - dividerHeightPx) / 2;
                for (int i = 0; i < childCount - 1; i++) {
                    View child = getChildAt(i);
                    mDividerPaint.setColor(tabColorizer.getDividerColor(i));
                    canvas.drawLine(child.getRight(), separatorTop, child.getRight(),
                            separatorTop + dividerHeightPx, mDividerPaint);
                }
            }
        }


    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private static int setColorAlpha(int color, byte alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    private static class SimpleTabColorizer implements TabColorizer {

        private int[] mIndicatorColors;
        private int[] mDividerColors;

        @Override
        public int getIndicatorColor(int position) {
            return mIndicatorColors[position % mIndicatorColors.length];
        }

        @Override
        public int getDividerColor(int position) {
            return mDividerColors[position % mDividerColors.length];
        }

        public void setIndicatorColors(int... colors) {
            mIndicatorColors = colors;
        }

        public void setDividerColors(int... colors) {
            mDividerColors = colors;
        }
    }

    public interface TabColorizer {

        int getIndicatorColor(int position);

        int getDividerColor(int position);
    }

    /**
     * provide tab icon resId
     */
    public interface TabIconProvider {
        int getPageIconResId(int position);
    }
}
