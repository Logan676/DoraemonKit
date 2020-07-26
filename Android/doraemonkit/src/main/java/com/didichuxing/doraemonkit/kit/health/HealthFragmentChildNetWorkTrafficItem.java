package com.didichuxing.doraemonkit.kit.health;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficItemAdapter;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficListAdapter;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean.NetworkValuesBean;

public class HealthFragmentChildNetWorkTrafficItem extends BaseFragment {
    private RecyclerView mNetworkList;
    private NetworkTrafficItemAdapter mNetworkListAdapter;

    private String mClassName;

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_health_child_net_traffic_item;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        final TitleBar mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightClick() {

            }
        });

        mNetworkList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mNetworkList.setLayoutManager(layoutManager);
        mNetworkListAdapter = new NetworkTrafficItemAdapter(getContext());
        mNetworkList.setAdapter(mNetworkListAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mNetworkList.addItemDecoration(decoration);
    }

    private void initData() {
        synchronized (this) {
            if (getArguments() != null) {
                mClassName = getArguments().getString("className");
            }

            NetworkBean info = AppHealthInfoUtil.getInstance().getNetWorkInfo(mClassName);
            if (info == null || info.getValues() == null) {
                return;
            }

            List<NetworkValuesBean> network = info.getValues();

            mNetworkListAdapter.setData(network);
        }
    }
}
