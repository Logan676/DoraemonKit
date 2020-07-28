package com.didichuxing.doraemonkit.kit.health.cpu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.BundleKey;
import com.didichuxing.doraemonkit.kit.network.ui.NetWorkMainPagerAdapter;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER;

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
        int type = TYPE_CPU;
        if (getArguments() != null) {
            type = getArguments().getInt(BundleKey.KEY_TYPE);
        }

        final TitleBar mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setTitle(getTitleByType(type));
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
        mCPUListView = new CPUListView(getContext(), type);
        mCPUChartView = new CPUChartView(getContext(), type);
        List<View> views = new ArrayList<>();
        views.add(mCPUListView);
        views.add(mCPUChartView);
        mViewPager.setAdapter(new NetWorkMainPagerAdapter(getContext(), views));

        final View tabList = findViewById(R.id.tab_list);
        ((TextView) tabList.findViewById(R.id.tab_text)).setText(getLeftTabTextByType(type));
        ((ImageView) tabList.findViewById(R.id.tab_icon)).setImageResource(R.drawable.dk_net_work_monitor_list_selector);
        tabList.setOnClickListener(this);

        final View tabChart = findViewById(R.id.tab_chart);
        ((TextView) tabChart.findViewById(R.id.tab_text)).setText(getRightTabTextByType(type));
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

    private String getTitleByType(int type) {
        switch (type) {
            case TYPE_CPU:
                return "CPU监控摘要";
            case TYPE_UI_LAYER:
                return "UI层级";
            case TYPE_MEMORY:
                return getResources().getString(R.string.dk_kit_health_memory);
            default:
                return "";
        }
    }

    private int getLeftTabTextByType(int type) {
        if (TYPE_CPU == type) {
            return R.string.dk_cpu_list;
        } else if (TYPE_UI_LAYER == type) {
            return R.string.dk_ui_layer_detail;
        } else if (TYPE_MEMORY == type) {
            return R.string.dk_kit_health_memory_list;
        } else {
            return R.string.dk_cpu_list;
        }
    }

    private int getRightTabTextByType(int type) {
        if (TYPE_CPU == type) {
            return R.string.dk_cpu_chart;
        } else if (TYPE_UI_LAYER == type) {
            return R.string.dk_ui_layer_chart;
        } else if (TYPE_MEMORY == type) {
            return R.string.dk_kit_health_memory_chart;
        } else {
            return R.string.dk_cpu_chart;
        }
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
