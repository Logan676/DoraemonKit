package com.didichuxing.doraemonkit.kit.health.cpu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.frame.FrameListAdapter;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;

import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;

public class CPUListView extends LinearLayout {
    private RecyclerView mFrameList;
    private FrameListAdapter mListAdapter;

    public CPUListView(Context context) {
        this(context, null);
    }

    public CPUListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CPUListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_list, this);
        initView();
        initData();
    }


    private void initView() {
        mFrameList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFrameList.setLayoutManager(layoutManager);
        mListAdapter = new FrameListAdapter(getContext(), TYPE_CPU);
        mFrameList.setAdapter(mListAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mFrameList.addItemDecoration(decoration);

        ((EditText) findViewById(R.id.network_list_filter)).setVisibility(GONE);
    }

    private void initData() {
        synchronized (this) {

            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null || info.getData().getCpu() == null) {
                return;
            }

            List<PerformanceBean> cpus = info.getData().getCpu();
            mListAdapter.setData(cpus);
        }
    }

}
