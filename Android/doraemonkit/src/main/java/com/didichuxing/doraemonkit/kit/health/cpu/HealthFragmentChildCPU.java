package com.didichuxing.doraemonkit.kit.health.cpu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.network.ui.NetWorkMainPagerAdapter;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class HealthFragmentChildCPU extends BaseFragment implements View.OnClickListener {
    private ViewPager mViewPager;
    private CPUListView mCPUListView;
    private CPUChartView mCPUChartView;

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
        final TitleBar mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setTitle("CPU监控摘要");
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightClick() {

            }
        });

        mViewPager = findViewById(R.id.traffic_view_pager);
        mCPUListView = new CPUListView(getContext());
        mCPUChartView = new CPUChartView(getContext());
        List<View> views = new ArrayList<>();
        views.add(mCPUListView);
        views.add(mCPUChartView);
        mViewPager.setAdapter(new NetWorkMainPagerAdapter(getContext(), views));

        final View tabList = findViewById(R.id.tab_list);
        ((TextView) tabList.findViewById(R.id.tab_text)).setText(R.string.dk_cpu_list);
        ((ImageView) tabList.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_list_selector);
        tabList.setOnClickListener(this);

        final View tabChart = findViewById(R.id.tab_chart);
        ((TextView) tabChart.findViewById(R.id.tab_text)).setText(R.string.dk_cpu_chart);
        ((ImageView) tabChart.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_chart_selector);
        tabChart.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    tabList.setSelected(true);
                    tabChart.setSelected(false);
                } else {
                    tabList.setSelected(false);
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
        } else if (id == R.id.tab_chart) {
            mViewPager.setCurrentItem(1, true);
        }
    }

}
