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
import com.didichuxing.doraemonkit.kit.network.utils.ByteUtil;
import com.didichuxing.doraemonkit.ui.UniversalActivity;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_CLASS_NAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_TYPE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean.ValuesBean;

/**
 * @desc: 帧率
 */
public class FrameListAdapter extends AbsRecyclerAdapter<AbsViewBinder<PerformanceBean>, PerformanceBean> {
    private int mType = TYPE_FRAME;

    public FrameListAdapter(Context context, int type) {
        super(context);
        mType = type;
    }

    @Override
    protected AbsViewBinder<PerformanceBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view, mType);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_network_traffic, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<PerformanceBean> {
        private TextView className;
        private TextView frame;

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
        }

        @Override
        public void bind(final PerformanceBean bean) {
            if (bean == null) {
                return;
            }

            className.setText("类名：" + bean.getPage());

            List<ValuesBean> values = bean.getValues();

            if (values != null) {

                if (TYPE_FRAME == mType) {
                    frame.setText("帧率平均值：" + getAverage(values));
                } else if (TYPE_CPU == mType) {
                    String format = String.format("CPU平均值：%.2f%%", getAverage(values));
                    frame.setText(format);
                } else if (TYPE_MEMORY == mType) {
                    float sum = getSum(values);
                    String size = ByteUtil.getPrintSize((long) sum);
                    String format = "内存泄露：" + size;
                    frame.setText(format);
                }
            }


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
            if (TYPE_FRAME == mType) {
                return FragmentIndex.FRAGMENT_HEALTH_FRAME_ITEM;
            } else if (TYPE_CPU == mType) {
                return FragmentIndex.FRAGMENT_HEALTH_CPU_ITEM;
            } else if (TYPE_MEMORY == mType) {
                return FragmentIndex.FRAGMENT_HEALTH_MEMORY_ITEM;
            }
            return FragmentIndex.FRAGMENT_HEALTH_FRAME_ITEM;
        }

        private float getAverage(List<ValuesBean> values) {
            float averageFrame = 0;

            for (ValuesBean b : values) {
                averageFrame += Float.parseFloat(b.getValue());
            }
            return averageFrame * 1.0f / values.size();
        }

        private float getSum(List<ValuesBean> valuesBeans) {
            float sum = 0;
            for (ValuesBean b : valuesBeans) {
                sum += Float.parseFloat(b.getValue());
            }
            return sum;
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

