package com.didichuxing.doraemonkit.kit.health.traffic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;
import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean.NetworkValuesBean;

/**
 * @desc: 流量分析
 */
public class NetworkTrafficListAdapter extends AbsRecyclerAdapter<AbsViewBinder<List<NetworkValuesBean>>, List<NetworkValuesBean>> {
    public NetworkTrafficListAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbsViewBinder<List<NetworkValuesBean>> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<List<NetworkValuesBean>> {
        private TextView url;
        private TextView trafficMB;
        private TextView category;

        public ItemViewHolder(View view) {
            super(view);
        }

        @Override
        protected void getViews() {
            getView(R.id.traffic_style1).setVisibility(View.VISIBLE);
            getView(R.id.traffic_style2).setVisibility(View.GONE);

            url = getView(R.id.traffic_url);
            trafficMB = getView(R.id.traffic_up_down_mb);
            category = getView(R.id.traffic_category);
        }

        @Override
        public void bind(final List<NetworkValuesBean> list) {
            if (list == null || list.isEmpty()) {
                return;
            }

            NetworkValuesBean bean = list.get(0);
            int size = list.size();
            url.setText(bean.getUrl());
            category.setText("请求过多");
            String note = "总共请求" + size + "次";
            trafficMB.setText(note);
        }
    }

    @Override
    public void setData(Collection<List<NetworkValuesBean>> items) {
        super.setData(items);
    }
}

