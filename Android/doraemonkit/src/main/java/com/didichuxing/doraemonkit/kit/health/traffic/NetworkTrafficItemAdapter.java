package com.didichuxing.doraemonkit.kit.health.traffic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.network.utils.ByteUtil;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean.NetworkValuesBean;

/**
 * @desc: 流量分析-查看详情
 */
public class NetworkTrafficItemAdapter extends AbsRecyclerAdapter<AbsViewBinder<NetworkValuesBean>, NetworkValuesBean> {
    public NetworkTrafficItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbsViewBinder<NetworkValuesBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic_item, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<NetworkValuesBean> {
        private TextView url;
        private TextView method;
        private TextView trafficUpMB;
        private TextView trafficDownMB;

        public ItemViewHolder(View view) {
            super(view);
        }

        @Override
        protected void getViews() {
            url = getView(R.id.traffic_url);
            method = getView(R.id.traffic_method);
            trafficUpMB = getView(R.id.traffic_up_mb);
            trafficDownMB = getView(R.id.traffic_down_mb);
        }

        @Override
        public void bind(final NetworkValuesBean bean) {
            url.setText(bean.getUrl());
            method.setText("网络请求类型：" + bean.getMethod());
            trafficUpMB.setText("上行流量：" + ByteUtil.getPrintSize(Long.parseLong(bean.getUp())));
            trafficDownMB.setText("下行流量：" + ByteUtil.getPrintSize(Long.parseLong(bean.getDown())));
        }
    }

    @Override
    public void setData(Collection<NetworkValuesBean> items) {
        super.setData(items);
    }
}

