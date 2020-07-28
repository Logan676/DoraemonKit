package com.didichuxing.doraemonkit.kit.health.cpu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PageLoadBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean.ValuesBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.UiLevelBean;

public class CPUChartView extends LinearLayout implements OnChartValueSelectedListener {

    public static final int COLOR_FRAME = 0xff2CCD9E;

    private LineChart chart;

    private int mType;

    public CPUChartView(Context context, int type) {
        this(context, null, type);
    }

    public CPUChartView(Context context, @Nullable AttributeSet attrs, int type) {
        this(context, attrs, 0, type);
    }

    public CPUChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int type) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_chart, this);
        mType = type;
        initView();
        initData();
    }


    private void initView() {

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        // chart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(chart); // For bounds control
//        chart.setMarker(mv); // Set the marker to the chart

        XAxis xl = chart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setAxisMinimum(0f);
//        xl.setTextColor(COLOR_FRAME);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setInverted(true);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        leftAxis.setTextColor(COLOR_FRAME);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        // // restrain the maximum scale-out factor
        // chart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // chart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void initData() {
        setData();

        // redraw
        chart.invalidate();
    }

    private void setData() {
        int count;
        float range = 30;

        ArrayList<Entry> entries = buildEntries();

        if (entries == null) return;

        // sort by x-value
        Collections.sort(entries, new EntryXComparator());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(entries, getLabelByType());

        set1.setLineWidth(1.5f);
        set1.setCircleRadius(4f);

        // create a data object with the data sets
        LineData data = new LineData(set1);

        // set data
        chart.setData(data);

    }

    private String getLabelByType() {
        if (TYPE_FRAME == mType) {
            return "帧率";
        } else if (TYPE_CPU == mType) {
            return "CPU";
        } else if (TYPE_MEMORY == mType) {
            return "内存使用";
        } else if (TYPE_LOAD_PAGE == mType) {
            return "页面打开时长";
        }
        return "CPU";
    }

    private ArrayList<Entry> buildEntries() {
        int count;
        ArrayList<Entry> entries = new ArrayList<>();

        AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
        if (info == null || info.getData() == null) {
            return null;
        }

        if (TYPE_FRAME == mType || TYPE_CPU == mType || TYPE_MEMORY == mType) {
            List<PerformanceBean> performanceBeans = null;
            if (TYPE_FRAME == mType) {
                performanceBeans = info.getData().getFps();
            } else if (TYPE_CPU == mType) {
                performanceBeans = info.getData().getCpu();
            } else if (TYPE_MEMORY == mType) {
                performanceBeans = info.getData().getMemory();
            }

            if (performanceBeans == null) return null;
            count = performanceBeans.size();

            for (int i = 0; i < count; i++) {
                PerformanceBean bean = performanceBeans.get(i);
                List<ValuesBean> values = bean.getValues();
                float fpsSum = 0f;
                for (ValuesBean b : values) {
                    fpsSum += Float.parseFloat(b.getValue());
                }
                if (TYPE_FRAME == mType || TYPE_CPU == mType) {
                    float averageFps = fpsSum / values.size();
                    entries.add(new Entry(i, averageFps, bean.getPage()));
                } else if (TYPE_MEMORY == mType) {
                    // String printSize = ByteUtil.getPrintSize((long) fpsSum);
                    entries.add(new Entry(i, fpsSum, bean.getPage()));
                }

            }
        } else if (TYPE_UI_LAYER == mType) {
            List<UiLevelBean> uiLevels = info.getData().getUiLevel();
            count = uiLevels.size();

            for (int i = 0; i < count; i++) {
                UiLevelBean bean = uiLevels.get(i);
                String level = bean.getLevel();
                entries.add(new Entry(i, Integer.parseInt(level), bean.getPage()));
            }
        } else if (TYPE_LOAD_PAGE == mType) {
            List<PageLoadBean> pageLoadBeanList = info.getData().getPageLoad();
            count = pageLoadBeanList.size();

            for (int i = 0; i < count; i++) {
                PageLoadBean bean = pageLoadBeanList.get(i);
                String time = bean.getTime();
                entries.add(new Entry(i, Integer.parseInt(time), bean.getPage()));
            }
        }
        return entries;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        chart.centerViewToAnimated(e.getX(), e.getY(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
        //chart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //chart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}
