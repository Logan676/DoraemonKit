package com.didichuxing.doraemonkit.kit.health.block;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.BlockBean;

/**
 * @desc: 卡顿
 */
public class BlockListAdapter extends AbsRecyclerAdapter<AbsViewBinder<BlockBean>, BlockBean> {
    public BlockListAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbsViewBinder<BlockBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<BlockBean> {
        private TextView className;
        private TextView methodBlockMs;

        public ItemViewHolder(View view) {
            super(view);
        }

        @Override
        protected void getViews() {
            getView(R.id.traffic_style1).setVisibility(View.GONE);
            getView(R.id.traffic_style2).setVisibility(View.VISIBLE);

            className = getView(R.id.traffic_url);
            methodBlockMs = getView(R.id.traffic_amount);
            getView(R.id.request_amount).setVisibility(View.GONE);
        }

        @Override
        public void bind(final BlockBean bean) {
            if (bean == null) {
                return;
            }
            className.setText(bean.getPage());
            methodBlockMs.setText("耗时：" + bean.getBlockTime() + " ms");

        }
    }

    @Override
    public void setData(Collection<BlockBean> items) {
        super.setData(items);
    }
}

