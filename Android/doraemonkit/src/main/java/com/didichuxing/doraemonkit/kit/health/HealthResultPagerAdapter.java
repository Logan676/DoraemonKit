package com.didichuxing.doraemonkit.kit.health;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HealthResultPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> tabs = new ArrayList<>();

    public HealthResultPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

//    public HealthResultPagerAdapter(Context context, List<Fragment> views) {
//        tabs.add(context.getString(R.string.dk_net_monitor_title_summary));
//        tabs.add(context.getString(R.string.dk_net_monitor_list));
//    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
