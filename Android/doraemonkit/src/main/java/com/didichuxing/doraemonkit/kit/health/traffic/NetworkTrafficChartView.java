package com.didichuxing.doraemonkit.kit.health.traffic;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.chart.NetworkTrafficValueFormatter;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.kit.network.utils.ByteUtil;
import com.didichuxing.doraemonkit.util.StringUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.NetworkBean.NetworkValuesBean;
import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;

public class NetworkTrafficChartView extends LinearLayout implements OnChartValueSelectedListener {

    public static final int COLOR_REQUEST_COUNT = 0xff56A6EA;
    public static final int COLOR_TRAFFIC = 0xff2CCD9E;

    private LineChart chart;

    public NetworkTrafficChartView(Context context) {
        this(context, null);
    }

    public NetworkTrafficChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkTrafficChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_network_monitor_chart, this);
        initView();
        initData();
    }


    private void initView() {

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.setDescription("网络请求总流量/请求数");

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
//        chart.setBackgroundColor(Color.LTGRAY);


        chart.animateX(300);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setPosition(BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setGranularity(2000);
//        xAxis.setValueFormatter(custom);
//        xAxis.setLabelCount(4);

        YAxisValueFormatter trafficValueFormatter = new NetworkTrafficValueFormatter("");
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(COLOR_TRAFFIC);
//        leftAxis.setAxisMaximum(1000f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setValueFormatter(trafficValueFormatter);

        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setTypeface(tfLight);
        rightAxis.setTextColor(COLOR_REQUEST_COUNT);
//        rightAxis.setAxisMaximum(1000);
//        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularity(20);
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

        List<NetworkBean> networkBeanList = info.getData().getNetwork();
        count = networkBeanList.size();

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            if (networkBeanList.get(i) == null) continue;
            String className = networkBeanList.get(i).getPage();
            String name = StringUtil.getSimpleClassName(className);
            if (!TextUtils.isEmpty(name)) {
                xVals.add(name);
            } else {
                xVals.add(i + "");
            }
        }

        ArrayList<Entry> trafficAmountList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            NetworkBean bean = networkBeanList.get(i);
            List<NetworkValuesBean> values = bean.getValues();
            int sum = 0;
            for (NetworkValuesBean b : values) {
                if (b == null) continue;
                int downSize = Integer.parseInt(b.getDown());
                int upSize = Integer.parseInt(b.getUp());
                sum += downSize;
                sum += upSize;
            }
            float val = sum;
            trafficAmountList.add(new Entry(val, i, bean));
        }

        ArrayList<Entry> requestCountList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            NetworkBean bean = networkBeanList.get(i);
            if (bean != null && bean.getValues() != null) {
                float val = bean.getValues().size();
                requestCountList.add(new Entry(val, i, bean));
            }
        }

        LineDataSet set1, set2;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
//            set1.setValues(trafficAmountList);
//            set2.setValues(requestCountList);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(trafficAmountList, "总流量");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(COLOR_TRAFFIC);
            set1.setCircleColor(COLOR_TRAFFIC);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(COLOR_TRAFFIC);
            set1.setDrawValues(false);
//            set1.setDrawCircleHole(true);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setFillFormatter(new MyFillFormatter(900f));
            set1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return ByteUtil.getPrintSize((long) value);
                }
            });

            // create a dataset and give it a type
            set2 = new LineDataSet(requestCountList, "请求数");

            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(COLOR_REQUEST_COUNT);
            set2.setCircleColor(COLOR_REQUEST_COUNT);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(COLOR_REQUEST_COUNT);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawValues(true);
//            set2.setDrawCircleHole(true);
            //set2.setFillFormatter(new MyFillFormatter(0f));
            //set2.setDrawHorizontalHighlightIndicator(false);
            //set2.setVisible(false);
            //set2.setCircleHoleColor(Color.WHITE);


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            // create a data object with the data sets
            LineData data = new LineData(xVals, dataSets);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            chart.setData(data);
        }
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
