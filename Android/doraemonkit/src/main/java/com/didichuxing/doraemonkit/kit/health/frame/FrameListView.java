package com.didichuxing.doraemonkit.kit.health.frame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;

import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;

public class FrameListView extends LinearLayout {
    private RecyclerView mFrameList;
    private FrameListAdapter mFrameListAdapter;

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
        mFrameListAdapter = new FrameListAdapter(getContext());
        mFrameList.setAdapter(mFrameListAdapter);

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
            mFrameListAdapter.setData(fps);
        }
    }

}
