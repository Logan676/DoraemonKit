package com.didichuxing.doraemonkit.kit.health;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.network.ui.NetWorkMainPagerFragment;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HealthResultFragment extends BaseFragment {

    private ViewPager mViewPager;
    private HealthResultPagerAdapter mPagerAdapter;
    private NetWorkMainPagerFragment mNetWorkFragment;

    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_health_result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = findViewById(R.id.view_pager_health_result);

        mNetWorkFragment = new NetWorkMainPagerFragment();
        mFragmentList.add(mNetWorkFragment);

        mPagerAdapter = new HealthResultPagerAdapter(getFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mPagerAdapter);
    }


}
