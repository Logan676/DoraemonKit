package com.didichuxing.doraemonkit.kit.health;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficChartView;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficListView;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficStatisticView;
import com.didichuxing.doraemonkit.kit.network.ui.NetWorkMainPagerAdapter;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HealthFragmentChildNetWorkTraffic extends BaseFragment implements View.OnClickListener {
    private ViewPager mViewPager;
    private NetworkTrafficListView mTrafficListView;
    private NetworkTrafficStatisticView mTrafficStatisticView;
    private NetworkTrafficChartView mTrafficChartView;

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_health_child_net_traffic;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.traffic_view_pager);
        mTrafficListView = new NetworkTrafficListView(getContext());
        mTrafficStatisticView = new NetworkTrafficStatisticView(getContext());
        mTrafficChartView = new NetworkTrafficChartView(getContext());
        List<View> views = new ArrayList<>();
        views.add(mTrafficListView);
        views.add(mTrafficStatisticView);
        views.add(mTrafficChartView);
        mViewPager.setAdapter(new NetWorkMainPagerAdapter(getContext(), views));

        final View tabList = findViewById(R.id.tab_list);
        ((TextView) tabList.findViewById(R.id.tab_text)).setText(R.string.dk_net_monitor_analyse);
        ((ImageView) tabList.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_list_selector);
        tabList.setOnClickListener(this);

        final View tabSummary = findViewById(R.id.tab_summary);
        ((TextView) tabSummary.findViewById(R.id.tab_text)).setText(R.string.dk_net_monitor_statistic);
        ((ImageView) tabSummary.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_summary_selector);
        tabSummary.setSelected(true);
        tabSummary.setOnClickListener(this);

        final View tabChart = findViewById(R.id.tab_chart);
        ((TextView) tabChart.findViewById(R.id.tab_text)).setText(R.string.dk_net_monitor_chart);
        ((ImageView) tabChart.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_summary_selector);
        tabChart.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    tabList.setSelected(true);
                    tabSummary.setSelected(false);
                    tabChart.setSelected(false);
                } else if (position == 1) {
                    tabList.setSelected(false);
                    tabSummary.setSelected(true);
                    tabChart.setSelected(false);
                } else {
                    tabList.setSelected(false);
                    tabSummary.setSelected(false);
                    tabChart.setSelected(true);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tab_list) {
            mViewPager.setCurrentItem(0, true);
        } else if (id == R.id.tab_summary) {
            mViewPager.setCurrentItem(1, true);
        } else if (id == R.id.tab_chart) {
            mViewPager.setCurrentItem(2, true);
        }
    }

}
