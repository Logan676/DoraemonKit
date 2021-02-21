package com.didichuxing.doraemonkit.kit.health.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.loadpage.PageLoadListAdapter;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.kit.health.uilayer.UILayerListAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PageLoadBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.UiLevelBean;

/**
 * 展示列表
 * <p>
 * 支持如下类型数据的展示
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE}
 */
public class PerformanceListView extends LinearLayout {
    private RecyclerView mPerformanceList;
    private PerformanceListAdapter mListAdapter;
    private UILayerListAdapter mUILayerListAdapter;
    private PageLoadListAdapter mLoadListAdapter;

    private int mType;

    public PerformanceListView(Context context, int type) {
        this(context, null, type);
    }

    public PerformanceListView(Context context, @Nullable AttributeSet attrs, int type) {
        this(context, attrs, 0, type);
    }

    public PerformanceListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int type) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_list, this);
        mType = type;
        initView();
        initData();
    }


    private void initView() {
        mPerformanceList = findViewById(R.id.network_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mPerformanceList.setLayoutManager(layoutManager);

        if (TYPE_CPU == mType || TYPE_FRAME == mType || TYPE_MEMORY == mType) {
            mListAdapter = new PerformanceListAdapter(getContext(), mType);
            mPerformanceList.setAdapter(mListAdapter);
        } else if (TYPE_UI_LAYER == mType) {
            mUILayerListAdapter = new UILayerListAdapter(getContext(), mType);
            mPerformanceList.setAdapter(mUILayerListAdapter);
        } else if (TYPE_LOAD_PAGE == mType) {
            mLoadListAdapter = new PageLoadListAdapter(getContext(), mType);
            mPerformanceList.setAdapter(mLoadListAdapter);
        }

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mPerformanceList.addItemDecoration(decoration);

        ((EditText) findViewById(R.id.network_list_filter)).setVisibility(GONE);
    }

    private void initData() {
        synchronized (this) {
            if (TYPE_CPU == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getCpu() == null) {
                    return;
                }

                List<PerformanceBean> cpus = info.getData().getCpu();
                Collections.sort(cpus, new Comparator<PerformanceBean>() {
                    @Override
                    public int compare(PerformanceBean o1, PerformanceBean o2) {
                        if (o1 == null || o1.values == null || o2 == null || o2.values == null)
                            return 0;
                        float sum = 0f;
                        for (PerformanceBean.ValuesBean b : o1.values) {
                            if (b == null) continue;
                            if (b.value == null || TextUtils.isEmpty(b.value)) continue;
                            sum += Float.parseFloat(b.value);
                        }

                        float sum2 = 0f;
                        for (PerformanceBean.ValuesBean b : o2.values) {
                            if (b == null) continue;
                            if (b.value == null || TextUtils.isEmpty(b.value)) continue;
                            sum2 += Float.parseFloat(b.value);
                        }

                        float average1 = sum / o1.values.size();
                        float average2 = sum2 / o2.values.size();
                        return Float.compare(average2, average1);
                    }
                });
                mListAdapter.setData(cpus);
            } else if (TYPE_FRAME == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getFps() == null) {
                    return;
                }

                List<PerformanceBean> fps = info.getData().getFps();
                Collections.sort(fps, new Comparator<PerformanceBean>() {
                    @Override
                    public int compare(PerformanceBean o1, PerformanceBean o2) {
                        if (o1 == null || o1.values == null || o2 == null || o2.values == null) return 0;

                        float sum = 0f;
                        for (PerformanceBean.ValuesBean b : o1.values) {
                            if (b == null) continue;
                            if (b.value == null || TextUtils.isEmpty(b.value)) continue;
                            sum += Float.parseFloat(b.value);
                        }

                        float sum2 = 0f;
                        for (PerformanceBean.ValuesBean b : o2.values) {
                            if (b == null) continue;
                            if (b.value == null || TextUtils.isEmpty(b.value)) continue;
                            sum2 += Float.parseFloat(b.value);
                        }

                        float average1 = sum / o1.values.size();
                        float average2 = sum2 / o2.values.size();
                        return Float.compare(average1, average2);
                    }
                });
                mListAdapter.setData(fps);
            } else if (TYPE_MEMORY == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getMemory() == null) {
                    return;
                }

                List<PerformanceBean> memoryList = info.getData().getMemory();
                Collections.sort(memoryList, new Comparator<PerformanceBean>() {
                    @Override
                    public int compare(PerformanceBean o1, PerformanceBean o2) {
                        if (o1 == null || o1.getValues() == null || o2 == null || o2.getValues() == null)
                            return 0;

                        float sum = 0f;
                        for (PerformanceBean.ValuesBean b : o1.getValues()) {
                            if (b == null) continue;
                            if (b.getValue() == null || TextUtils.isEmpty(b.getValue())) continue;
                            sum += Float.parseFloat(b.getValue());
                        }

                        float sum2 = 0f;
                        for (PerformanceBean.ValuesBean b : o2.getValues()) {
                            if (b == null) continue;
                            if (b.getValue() == null || TextUtils.isEmpty(b.getValue())) continue;
                            sum2 += Float.parseFloat(b.getValue());
                        }

                        return Float.compare(sum2, sum);
                    }
                });
                mListAdapter.setData(memoryList);
            } else if (TYPE_UI_LAYER == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getUiLevel() == null) {
                    return;
                }

                List<UiLevelBean> uiLevels = info.getData().getUiLevel();
                Collections.sort(uiLevels);
                mUILayerListAdapter.setData(uiLevels);
            } else if (TYPE_LOAD_PAGE == mType) {
                AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
                if (info == null || info.getData() == null || info.getData().getPageLoad() == null) {
                    return;
                }

                List<PageLoadBean> pageLoadBeanList = info.getData().getPageLoad();
                Collections.sort(pageLoadBeanList);
                mLoadListAdapter.setData(pageLoadBeanList);
            }

        }
    }

}
