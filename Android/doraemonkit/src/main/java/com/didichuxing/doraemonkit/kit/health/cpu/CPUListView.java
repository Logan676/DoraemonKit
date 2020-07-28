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
import com.didichuxing.doraemonkit.kit.health.uilayer.UILayerListAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;

import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.UiLevelBean;

public class CPUListView extends LinearLayout {
    private RecyclerView mFrameList;
    private FrameListAdapter mListAdapter;
    private UILayerListAdapter mUILayerListAdapter;

    private int mType;

    public CPUListView(Context context, int type) {
        this(context, null, type);
    }

    public CPUListView(Context context, @Nullable AttributeSet attrs, int type) {
        this(context, attrs, 0, type);
    }

    public CPUListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int type) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_list, this);
        mType = type;
        initView();
        initData();
    }


    private void initView() {
        mFrameList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFrameList.setLayoutManager(layoutManager);

        if (TYPE_CPU == mType || TYPE_FRAME == mType) {
            mListAdapter = new FrameListAdapter(getContext(), mType);
            mFrameList.setAdapter(mListAdapter);
        } else if (TYPE_UI_LAYER == mType) {
            mUILayerListAdapter = new UILayerListAdapter(getContext(), mType);
            mFrameList.setAdapter(mUILayerListAdapter);
        }

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mFrameList.addItemDecoration(decoration);

        ((EditText) findViewById(R.id.network_list_filter)).setVisibility(GONE);
    }

    private void initData() {
        synchronized (this) {

            if (TYPE_CPU == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getCpu() == null) {
                    return;
                }

                List<PerformanceBean> cpus = info.getData().getCpu();
                mListAdapter.setData(cpus);
            } else if (TYPE_UI_LAYER == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getUiLevel() == null) {
                    return;
                }

                List<UiLevelBean> uiLevels = info.getData().getUiLevel();
                mUILayerListAdapter.setData(uiLevels);
            }

        }
    }

}
