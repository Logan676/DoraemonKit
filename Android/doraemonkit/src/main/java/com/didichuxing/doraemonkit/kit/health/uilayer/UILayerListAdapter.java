package com.didichuxing.doraemonkit.kit.health.uilayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.UiLevelBean;

public class UILayerListAdapter extends AbsRecyclerAdapter<AbsViewBinder<UiLevelBean>, UiLevelBean> {
    private int mType = TYPE_FRAME;

    public UILayerListAdapter(Context context, int type) {
        super(context);
        mType = type;
    }

    @Override
    protected AbsViewBinder<UiLevelBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view, mType);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<UiLevelBean> {
        private TextView className;
        private TextView frame;
        private TextView detail;

        private int mType;

        public ItemViewHolder(View view, int type) {
            super(view);
            mType = type;
        }

        @Override
        protected void getViews() {
            getView(R.id.traffic_style1).setVisibility(View.GONE);
            getView(R.id.traffic_style2).setVisibility(View.VISIBLE);
            className = getView(R.id.traffic_url);
            frame = getView(R.id.traffic_amount);
            getView(R.id.request_amount).setVisibility(View.GONE);
            getView(R.id.traffic_view_detail).setVisibility(View.GONE);
            detail = getView(R.id.detail_info);
            detail.setVisibility(View.VISIBLE);
        }

        @Override
        public void bind(final UiLevelBean bean) {
            if (bean == null) {
                return;
            }

            className.setText("类名：" + bean.getPage());

            String level = bean.getLevel();
            frame.setText("层级：" + level);
            detail.setText(bean.getDetail());

        }
    }

    @Override
    public void setData(Collection<UiLevelBean> items) {
        super.setData(items);
    }
}

