package com.didichuxing.doraemonkit.kit.health.traffic;

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

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean;

public class NetworkTrafficStatisticView extends LinearLayout {
    private RecyclerView mNetworkList;
    private NetworkTrafficStatisticAdapter mStatisticAdapter;

    public NetworkTrafficStatisticView(Context context) {
        this(context, null);
    }

    public NetworkTrafficStatisticView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkTrafficStatisticView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_list, this);
        initView();
        initData();
    }


    private void initView() {
        mNetworkList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mNetworkList.setLayoutManager(layoutManager);
        mStatisticAdapter = new NetworkTrafficStatisticAdapter(getContext());
        mNetworkList.setAdapter(mStatisticAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mNetworkList.addItemDecoration(decoration);

        ((EditText) findViewById(R.id.network_list_filter)).setVisibility(GONE);
    }

    private void initData() {
        synchronized (this) {

            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null || info.getData().getNetwork() == null) {
                return;
            }

            List<NetworkBean> network = info.getData().getNetwork();
            mStatisticAdapter.setData(network);
        }
    }

}
