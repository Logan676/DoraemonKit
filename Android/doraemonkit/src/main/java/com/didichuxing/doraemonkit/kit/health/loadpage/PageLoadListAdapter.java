package com.didichuxing.doraemonkit.kit.health.loadpage;

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

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_CLASS_NAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_TYPE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PageLoadBean;

/**
 * @desc: 页面打开时长
 */
public class PageLoadListAdapter extends AbsRecyclerAdapter<AbsViewBinder<PageLoadBean>, PageLoadBean> {
    private int mType = TYPE_LOAD_PAGE;

    public PageLoadListAdapter(Context context, int type) {
        super(context);
        mType = type;
    }

    @Override
    protected AbsViewBinder<PageLoadBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view, mType);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<PageLoadBean> {
        private TextView className;
        private TextView frame;
        private TextView stackTrace;

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
            stackTrace = getView(R.id.request_amount);
            getView(R.id.traffic_view_detail).setVisibility(View.GONE);
        }

        @Override
        public void bind(final PageLoadBean bean) {
            if (bean == null) {
                return;
            }

            className.setText("类名：" + bean.getPage());

            String time = bean.getTime();
            frame.setText(time + "ms");

            String trace = bean.getTrace();
            stackTrace.setText(trace);

            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_CLASS_NAME, bean.getPage());
                    bundle.putInt(KEY_TYPE, mType);
                    startUniversalActivity(
                            getContext(),
                            bundle,
                            getFragmentIndex());
                }
            });
        }

        private int getFragmentIndex() {
            if (TYPE_LOAD_PAGE == mType) {
                return FragmentIndex.FRAGMENT_HEALTH_LOAD_PAGE_ITEM;
            }
            return FragmentIndex.FRAGMENT_HEALTH_FRAME_ITEM;
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
    public void setData(Collection<PageLoadBean> items) {
        super.setData(items);
    }
}

