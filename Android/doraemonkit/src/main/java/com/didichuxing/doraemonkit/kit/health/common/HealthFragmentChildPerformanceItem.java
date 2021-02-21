package com.didichuxing.doraemonkit.kit.health.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_CLASS_NAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_TYPE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY_ITEM;

/**
 * 体检结果二级展示页面
 * <p>
 * 支持如下类型数据的展示
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME },
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE}
 */
public class HealthFragmentChildPerformanceItem extends BaseFragment {
    private TitleBar mTitleBar;
    private PerformanceChartView mChartView;
    private TextView mTextViewClass;

    private String mClassName;

    private int mType;

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_health_child_cpu_item;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightClick() {
            }
        });

        mTextViewClass = findViewById(R.id.cpu_class_name);
        mChartView = new PerformanceChartView(getContext(), mType, mClassName);
        LinearLayout linearLayout = findViewById(R.id.health_child_item_container);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mChartView.setLayoutParams(layoutParams);
        linearLayout.addView(mChartView);

        mTextViewClass.setText("类：" + mClassName);

        if (TYPE_FRAME_ITEM == mType) {
            mTitleBar.setTitle(getString(R.string.dk_frame_chart_detail));
        } else if (TYPE_CPU_ITEM == mType) {
            mTitleBar.setTitle(getString(R.string.dk_cpu_chart_detail));
        } else if (TYPE_MEMORY_ITEM == mType) {
            mTitleBar.setTitle(getString(R.string.dk_memory_list));
        }
    }

    private void initData() {
        synchronized (this) {
            if (getArguments() != null) {
                mClassName = getArguments().getString(KEY_CLASS_NAME);
                mType = getArguments().getInt(KEY_TYPE);
            }
        }
    }
}
