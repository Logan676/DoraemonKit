package com.didichuxing.doraemonkit.kit.health.frame;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.util.StringUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean.ValuesBean;
import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;

public class FrameChartView extends LinearLayout implements OnChartValueSelectedListener {

    public static final int COLOR_FRAME = 0xff2CCD9E;

    private LineChart chart;

    public FrameChartView(Context context) {
        this(context, null);
    }

    public FrameChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_chart, this);
        initView();
        initData();
    }


    private void initView() {

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        // no description text
        chart.setDescription("页面帧率");

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
        xl.setAxisMinValue(0f);
        xl.setPosition(BOTTOM);
//        xl.setTextColor(COLOR_FRAME);

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setInverted(true);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        leftAxis.setTextColor(COLOR_FRAME);
        leftAxis.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf((int) value);
            }
        });

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
        l.setTextSize(14f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);

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

        AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
        if (info == null || info.getData() == null) {
            return;
        }

        List<PerformanceBean> fps = info.getData().getFps();
        count = fps.size();

        ArrayList<Entry> entries = new ArrayList<>();

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            if (fps.get(i) == null) continue;
            String className = fps.get(i).getPage();
            String name = StringUtil.getSimpleClassName(className);
            if (!TextUtils.isEmpty(name)) {
                xVals.add(name);
            } else {
                xVals.add(i + "");
            }
        }
        for (int i = 0; i < count; i++) {
            PerformanceBean bean = fps.get(i);
            List<ValuesBean> values = bean.getValues();
            float fpsSum = 0f;
            for (ValuesBean b : values) {
                fpsSum += Float.parseFloat(b.getValue());
            }
            float averageFps = fpsSum / values.size();

            entries.add(new Entry(averageFps, i, bean.getPage()));
        }

        // sort by x-value
        //Collections.sort(entries, new EntryXComparator());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(entries, "帧率");

        set1.setLineWidth(1.5f);
        set1.setCircleRadius(4f);

        // create a data object with the data sets
        LineData data = new LineData(xVals, set1);

        // set data
        chart.setData(data);

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());

        chart.centerViewToAnimated(e.getXIndex(), e.getVal(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
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
