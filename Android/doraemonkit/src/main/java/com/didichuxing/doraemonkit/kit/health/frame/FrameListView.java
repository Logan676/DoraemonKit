package com.didichuxing.doraemonkit.kit.health.frame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.common.PerformanceListAdapter;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;

public class FrameListView extends LinearLayout {
    private RecyclerView mFrameList;
    private PerformanceListAdapter mPerformanceListAdapter;

    public FrameListView(Context context) {
        this(context, null);
    }

    public FrameListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_list, this);
        initView();
        initData();
    }


    private void initView() {
        mFrameList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFrameList.setLayoutManager(layoutManager);
        mPerformanceListAdapter = new PerformanceListAdapter(getContext(), TYPE_FRAME);
        mFrameList.setAdapter(mPerformanceListAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mFrameList.addItemDecoration(decoration);

        ((EditText) findViewById(R.id.network_list_filter)).setVisibility(GONE);
    }

    private void initData() {
        synchronized (this) {

            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null || info.getData().getFps() == null) {
                return;
            }

            List<PerformanceBean> fps = info.getData().getFps();
            Collections.sort(fps, new Comparator<PerformanceBean>() {
                @Override
                public int compare(PerformanceBean o1, PerformanceBean o2) {
                    if (o1 == null || o1.values == null || o2 == null || o2.values == null)
                        return 0;

                    float sum = 0f;
                    for (PerformanceBean.ValuesBean b : o1.values) {
                        if (b == null) continue;
                        if (b.value == null || TextUtils.isEmpty(b.value)) continue;
                        sum += Float.parseFloat(b.value);
                    }

                    float sum2 = 0f;
                    for (PerformanceBean.ValuesBean b : o2.values) {
                        if (b == null) continue;
                        if (b.value == null || TextUtils.isEmpty(b.value)) continue;
                        sum2 += Float.parseFloat(b.value);
                    }

                    float average1 = sum / o1.values.size();
                    float average2 = sum2 / o2.values.size();
                    return Float.compare(average1, average2);
                }
            });

            mPerformanceListAdapter.setData(fps);
        }
    }

}
