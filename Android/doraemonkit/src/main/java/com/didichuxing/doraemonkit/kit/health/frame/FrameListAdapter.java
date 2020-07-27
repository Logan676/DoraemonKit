package com.didichuxing.doraemonkit.kit.health.frame;

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
import com.didichuxing.doraemonkit.ui.UniversalActivity;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;
import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean.ValuesBean;

/**
 * @desc: 帧率
 */
public class FrameListAdapter extends AbsRecyclerAdapter<AbsViewBinder<PerformanceBean>, PerformanceBean> {

    public FrameListAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbsViewBinder<PerformanceBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<PerformanceBean> {
        private TextView className;
        private TextView frame;

        public ItemViewHolder(View view) {
            super(view);
        }

        @Override
        protected void getViews() {
            getView(R.id.traffic_style1).setVisibility(View.GONE);
            getView(R.id.traffic_style2).setVisibility(View.VISIBLE);
            className = getView(R.id.traffic_url);
            frame = getView(R.id.traffic_amount);
            getView(R.id.request_amount).setVisibility(View.GONE);
        }

        @Override
        public void bind(final PerformanceBean bean) {
            if (bean == null) {
                return;
            }

            className.setText("类名：" + bean.getPage());

            List<ValuesBean> values = bean.getValues();

            if (values != null) {

                float averageFrame = 0;

                for (ValuesBean b : values) {
                    averageFrame += Float.parseFloat(b.getValue());
                }

                float average = averageFrame * 1.0f / values.size();

                frame.setText("帧率平均值：" + average);
            }

            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("className", bean.getPage());
                    startUniversalActivity(
                            getContext(),
                            bundle,
                            FragmentIndex.FRAGMENT_HEALTH_FRAME_ITEM);
                }
            });
        }

        public void startUniversalActivity(Context context, Bundle bundle, int fragmentIndex) {
            Intent intent = new Intent(context, UniversalActivity.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BundleKey.FRAGMENT_INDEX, fragmentIndex);
            context.startActivity(intent);
        }
    }

    @Override
    public void setData(Collection<PerformanceBean> items) {
        super.setData(items);
    }
}

