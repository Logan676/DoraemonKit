package com.didichuxing.doraemonkit.kit.health.traffic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.BundleKey;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.network.utils.ByteUtil;
import com.didichuxing.doraemonkit.ui.DoraemonActivity;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_CLASS_NAME;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean.NetworkValuesBean;

/**
 * @desc: 网络统计
 */
public class NetworkTrafficStatisticAdapter extends AbsRecyclerAdapter<AbsViewBinder<NetworkBean>, NetworkBean> {

    public NetworkTrafficStatisticAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbsViewBinder<NetworkBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<NetworkBean> {
        private TextView className;
        private TextView trafficAmount;
        private TextView requestAmount;
        private TextView viewDetailBtn;

        public ItemViewHolder(View view) {
            super(view);
        }

        @Override
        protected void getViews() {
            getView(R.id.traffic_style1).setVisibility(View.GONE);
            getView(R.id.traffic_style2).setVisibility(View.VISIBLE);
            className = getView(R.id.traffic_url);
            trafficAmount = getView(R.id.traffic_amount);
            requestAmount = getView(R.id.request_amount);
            viewDetailBtn = getView(R.id.traffic_view_detail);
        }

        @Override
        public void bind(final NetworkBean networkBean) {
            if (networkBean == null) {
                return;
            }

            className.setText("类名：" + networkBean.getPage());

            List<NetworkValuesBean> list = networkBean.getValues();
            if (list != null) {
                requestAmount.setText("请求数：" + networkBean.getValues().size());

                int sum = 0;
                for (NetworkValuesBean b : list) {
                    int downSize = Integer.parseInt(b.getDown());
                    int upSize = Integer.parseInt(b.getUp());
                    sum += downSize;
                    sum += upSize;
                }
                String trafficSize = "总流量：" + ByteUtil.getPrintSize(sum);
                trafficAmount.setText(trafficSize);
            }


            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_CLASS_NAME, networkBean.getPage());
                    startUniversalActivity(
                            getContext(),
                            bundle,
                            FragmentIndex.FRAGMENT_HEALTH_NETWORK_RESULT_ITEM);
                }
            });
        }

        public void startUniversalActivity(Context context, Bundle bundle, int fragmentIndex) {
            Intent intent = new Intent(context, DoraemonActivity.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BundleKey.FRAGMENT_INDEX, fragmentIndex);
            context.startActivity(intent);
        }
    }

    @Override
    public void setData(Collection<NetworkBean> items) {
        super.setData(items);
    }
}

