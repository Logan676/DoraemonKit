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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean.NetworkValuesBean;

public class NetworkTrafficChartView extends LinearLayout {
    private RecyclerView mNetworkList;

    public NetworkTrafficChartView(Context context) {
        this(context, null);
    }

    public NetworkTrafficChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkTrafficChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_list, this);
        initView();
        initData();
    }


    private void initView() {
        mNetworkList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mNetworkList.setLayoutManager(layoutManager);

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

            HashMap<String, List<NetworkValuesBean>> map = new HashMap<>();
            for (NetworkBean bean : network) {
                List<NetworkValuesBean> values = bean.getValues();
                for (NetworkValuesBean networkValuesBean : values) {
                    String url = networkValuesBean.getUrl();
                    List<NetworkValuesBean> list = map.get(url);
                    if (list == null) {
                        list = new ArrayList<>();
                        map.put(url, list);
                    }
                    list.add(networkValuesBean);

                }
            }

            TreeSet<List<NetworkValuesBean>> treeSet = new TreeSet<>(new Comparator<List<NetworkValuesBean>>() {
                @Override
                public int compare(List<NetworkValuesBean> o1, List<NetworkValuesBean> o2) {
                    return o2.size() - o1.size();
                }
            });

            for (Map.Entry<String, List<NetworkValuesBean>> entry : map.entrySet()) {
                treeSet.add(entry.getValue());
            }

        }
    }

}
