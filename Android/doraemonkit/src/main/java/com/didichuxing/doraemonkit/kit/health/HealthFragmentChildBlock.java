package com.didichuxing.doraemonkit.kit.health;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.block.BlockListView;
import com.didichuxing.doraemonkit.kit.health.frame.FrameChartView;
import com.didichuxing.doraemonkit.kit.health.frame.FrameListView;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficChartView;
import com.didichuxing.doraemonkit.kit.network.ui.NetWorkMainPagerAdapter;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class HealthFragmentChildBlock extends BaseFragment implements View.OnClickListener {
    private ViewPager mViewPager;
    private BlockListView mBlockListView;
    private FrameListView mFrameListView;
    private FrameChartView mFrameChartView;

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
        mTitleBar.setTitle("卡顿、帧率监控摘要");
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
        mBlockListView = new BlockListView(getContext());
        mFrameListView = new FrameListView(getContext());
        mFrameChartView = new FrameChartView(getContext());
        List<View> views = new ArrayList<>();
        views.add(mBlockListView);
        views.add(mFrameListView);
        views.add(mFrameChartView);
        mViewPager.setAdapter(new NetWorkMainPagerAdapter(getContext(), views));

        final View tabList = findViewById(R.id.tab_list);
        ((TextView) tabList.findViewById(R.id.tab_text)).setText(R.string.dk_block_list);
        ((ImageView) tabList.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_list_selector);
        tabList.setOnClickListener(this);

        final View tabSummary = findViewById(R.id.tab_summary);
        ((TextView) tabSummary.findViewById(R.id.tab_text)).setText(R.string.dk_frame_list);
        ((ImageView) tabSummary.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_summary_selector);
        tabSummary.setSelected(true);
        tabSummary.setOnClickListener(this);

        final View tabChart = findViewById(R.id.tab_chart);
        ((TextView) tabChart.findViewById(R.id.tab_text)).setText(R.string.dk_frame_chart);
        ((ImageView) tabChart.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_chart_selector);
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
