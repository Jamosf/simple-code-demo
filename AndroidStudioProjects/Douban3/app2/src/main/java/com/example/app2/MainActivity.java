package com.example.app2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    PagerSlidingTabStrip tab;
    public static int NUM_pages = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //初始化pager
        pager = (ViewPager) findViewById(R.id.page_view);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        DisplayMetrics dm = getResources().getDisplayMetrics();
        tab = (PagerSlidingTabStrip) findViewById(R.id.page_tab);
        tab.setViewPager(pager);
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);

    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0){

            super.onBackPressed();

        }else{
            pager.setCurrentItem(0);
        }

    }

    private class MyPagerAdapter extends FragmentPagerAdapter{

        private final String[] TITLE = getResources().getStringArray(R.array.tab_name);

        private MyPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            Fragment pagerFragment;

            if(position == 0){
                pagerFragment = new listRefresh();
            }else{
                pagerFragment = new PagerFragment();
            }

            bundle.putInt("page_num", position);
            pagerFragment.setArguments(bundle);
            return pagerFragment;
        }

        @Override
        public int getCount() {
            return NUM_pages;
        }
    }
}



