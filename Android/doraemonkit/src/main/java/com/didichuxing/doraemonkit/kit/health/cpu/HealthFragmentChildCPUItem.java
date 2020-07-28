package com.didichuxing.doraemonkit.kit.health.cpu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_CLASS_NAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_TYPE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean.ValuesBean;


public class HealthFragmentChildCPUItem extends BaseFragment {
    private TitleBar mTitleBar;
    private CPUChartItemView mCPUChartItemView;
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
        initView();
        initData();
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
        mCPUChartItemView = findViewById(R.id.cpu_chart_item);

    }

    private void initData() {
        synchronized (this) {
            if (getArguments() != null) {
                mClassName = getArguments().getString(KEY_CLASS_NAME);
                mType = getArguments().getInt(KEY_TYPE);
            }

            mTextViewClass.setText("类：" + mClassName);

            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null) {
                return;
            }

            DataBean data = info.getData();
            List<PerformanceBean> list = null;
            if (TYPE_FRAME == mType) {
                mTitleBar.setTitle(getString(R.string.dk_frame_chart_detail));
                list = data.getFps();
            } else if (TYPE_CPU == mType) {
                mTitleBar.setTitle(getString(R.string.dk_cpu_chart_detail));
                list = data.getCpu();
            }

            if (list == null) return;

            List<ValuesBean> values = new ArrayList<>();
            for (PerformanceBean b : list) {
                if (b.getPage().equals(mClassName)) {
                    List<ValuesBean> tmpList = b.getValues();
                    values.addAll(tmpList);
                    break;
                }
            }
            mCPUChartItemView.setData(values);
        }
    }
}
