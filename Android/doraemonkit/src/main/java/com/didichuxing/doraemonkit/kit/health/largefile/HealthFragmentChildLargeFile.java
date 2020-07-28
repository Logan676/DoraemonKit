package com.didichuxing.doraemonkit.kit.health.largefile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.kit.health.traffic.NetworkTrafficItemAdapter;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.BigFileBean;

import java.util.List;

public class HealthFragmentChildLargeFile extends BaseFragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private LargeFileListAdapter mLargeFileListAdapter;


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
        mTitleBar.setTitle(getResources().getString(R.string.dk_kit_health_large_file));
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightClick() {

            }
        });

        mRecyclerView = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mLargeFileListAdapter = new LargeFileListAdapter(getContext());
        mRecyclerView.setAdapter(mLargeFileListAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mRecyclerView.addItemDecoration(decoration);
    }

    private void initData() {
        synchronized (this) {

            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null || info.getData().getBigFile() == null) {
                return;
            }

            List<BigFileBean> list = info.getData().getBigFile();
            mLargeFileListAdapter.setData(list);
        }
    }

    @Override
    public void onClick(View v) {
    }

}
