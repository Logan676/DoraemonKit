package com.didichuxing.doraemonkit.kit.health.common;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
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
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_BLOCK_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_FRAME_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY_ITEM;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER;
import static com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER_ITEM;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PageLoadBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.PerformanceBean.ValuesBean;
import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.UiLevelBean;
import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;

/**
 * 支持如下类型数据的展示
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_CPU},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_UI_LAYER},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_MEMORY},
 * {@code com.didichuxing.doraemonkit.constant.BundleKey.TYPE_LOAD_PAGE}
 */
public class PerformanceChartView extends LinearLayout implements OnChartValueSelectedListener {

    public static final int COLOR_FRAME = 0xff2CCD9E;

    public final static String dateFormatDHMSSSS = "dd HH:mm:ss.SSS";
    public final static String dateFormatHMSSSS = "HH:mm:ss.SSS";

    private SimpleDateFormat format1;
    private SimpleDateFormat format2;

    private LineChart chart;

    private ArrayList<Entry> entries;
    private int mType;

    private String mClassName;

    public PerformanceChartView(Context context, int type, String className) {
        super(context);
        inflate(context, R.layout.dk_fragment_network_monitor_chart, this);
        mType = type;
        mClassName = className;
        initView();
        initData();
    }

    private void initView() {

        chart = findViewById(R.id.chart1);
        changeChartStyle();

        XAxis xl = chart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        // leftAxis.setTextColor(COLOR_FRAME);
        leftAxis.setValueFormatter(mYAxisValueFormatter);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

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

        chart.animateX(300);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void changeChartStyle() {
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        // no description text
        chart.setDescription(getDescriptionByType());

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
    }

    private YAxisValueFormatter mYAxisValueFormatter = new YAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            if (TYPE_CPU == mType || TYPE_CPU_ITEM == mType) {
                return String.format("%.2f%%", value);
            } else if (TYPE_MEMORY == mType) {
                return ByteUtil.getPrintSize((long) value);
            } else if (TYPE_MEMORY_ITEM == mType) {
                return value + "mb";
            } else if (TYPE_LOAD_PAGE == mType) {
//                return String.valueOf(value);
                return ((int) value) + "ms";
            } else if (TYPE_UI_LAYER == mType) {
                return String.valueOf((int) value);
            } else if (TYPE_FRAME_ITEM == mType ||
                    TYPE_BLOCK_ITEM == mType ||
                    TYPE_UI_LAYER_ITEM == mType ||
                    TYPE_LOAD_PAGE_ITEM == mType) {
                return String.valueOf((int) value);
            } else {
                return String.valueOf(value);
            }
        }
    };

    private void initData() {
        setData();

        // redraw
        chart.invalidate();
    }

    private void setData() {
        ArrayList<String> xVals = buildXValue();

        // sort by x-value
        // Collections.sort(entries, new EntryXComparator());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(entries, getLabelByType());

        set1.setLineWidth(1.5f);
        set1.setCircleRadius(4f);

        if (TYPE_FRAME_ITEM == mType || TYPE_MEMORY_ITEM == mType) {
            set1.setDrawValues(false);
        }

        // create a data object with the data sets
        LineData data = new LineData(xVals, set1);

        // set data
        chart.setData(data);

    }

    private ArrayList<String> buildXValue() {
        ArrayList<String> xVals;
        if (TYPE_FRAME == mType ||
                TYPE_CPU == mType ||
                TYPE_MEMORY == mType ||
                TYPE_UI_LAYER == mType ||
                TYPE_LOAD_PAGE == mType) {
            xVals = performBuildXValues();
        } else {
            xVals = performBuildItemXValues();
        }
        return xVals;
    }

    private ArrayList<String> performBuildXValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        entries = buildEntries();

        if (entries == null) return xVals;
        int count = entries.size();

        for (int i = 0; i < count; i++) {
            if (entries.get(i) == null) continue;
            Entry entry = entries.get(i);
            if (entry.getData() == null) continue;
            String className = (String) entry.getData();
            String name = StringUtil.getSimpleClassName(className);
            if (!TextUtils.isEmpty(name)) {
                xVals.add(name);
            } else {
                xVals.add(i + "");
            }
        }

        return xVals;
    }

    private ArrayList<String> performBuildItemXValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        List<ValuesBean> beans = buildEntriesItem();
        int count = beans.size();

        entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ValuesBean valuesBean = beans.get(i);
            long timestamp = Long.parseLong(valuesBean.getTime());
            String formatted;
            if (TYPE_FRAME_ITEM == mType) {
                formatted = getDateDHMSSSS(timestamp);
            } else if (TYPE_CPU_ITEM == mType) {
                formatted = getDateHMSSSS(timestamp);
            } else if (TYPE_MEMORY_ITEM == mType) {
                formatted = getDateHMSSSS(timestamp);
            } else {
                formatted = getDateDHMSSSS(timestamp);
            }
            xVals.add(formatted);

            String time = valuesBean.getTime();
            String value = valuesBean.getValue();
            entries.add(new Entry(Float.parseFloat(value), i, valuesBean));
        }
        return xVals;
    }

    private String getDateDHMSSSS(long millis) {
        if (format1 == null) {
            format1 = new SimpleDateFormat(dateFormatDHMSSSS);
        }

        return format1.format(new java.util.Date(millis));
    }

    private String getDateHMSSSS(long millis) {
        if (format2 == null) {
            format2 = new SimpleDateFormat(dateFormatHMSSSS);
        }

        return format2.format(new java.util.Date(millis));
    }

    private String getLabelByType() {
        if (TYPE_FRAME == mType || TYPE_FRAME_ITEM == mType) {
            return "帧率";
        } else if (TYPE_CPU == mType || TYPE_CPU_ITEM == mType) {
            return "CPU平均使用率";
        } else if (TYPE_MEMORY == mType || TYPE_MEMORY_ITEM == mType) {
            return "内存使用";
        } else if (TYPE_UI_LAYER == mType || TYPE_UI_LAYER_ITEM == mType) {
            return "UI层级";
        } else if (TYPE_LOAD_PAGE == mType) {
            return "页面打开时长";
        } else if (TYPE_LOAD_PAGE_ITEM == mType) {
            return "加载耗时";
        }
        return "";
    }

    private String getDescriptionByType() {
        return getLabelByType();
    }

    private ArrayList<Entry> buildEntries() {
        ArrayList<Entry> entries = new ArrayList<>();

        AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
        if (info == null || info.getData() == null) {
            return null;
        }

        int count;
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
                    entries.add(new Entry(averageFps, i, bean.getPage()));
                } else if (TYPE_MEMORY == mType) {
                    // String printSize = ByteUtil.getPrintSize((long) fpsSum);
                    entries.add(new Entry(fpsSum, i, bean.getPage()));
                }

            }
        } else if (TYPE_UI_LAYER == mType) {
            List<UiLevelBean> uiLevels = info.getData().getUiLevel();
            count = uiLevels.size();

            for (int i = 0; i < count; i++) {
                UiLevelBean bean = uiLevels.get(i);
                String level = bean.getLevel();
                entries.add(new Entry(Integer.parseInt(level), i, bean.getPage()));
            }
        } else if (TYPE_LOAD_PAGE == mType) {
            List<PageLoadBean> pageLoadBeanList = info.getData().getPageLoad();
            count = pageLoadBeanList.size();

            for (int i = 0; i < count; i++) {
                PageLoadBean bean = pageLoadBeanList.get(i);
                String time = bean.getTime();
                entries.add(new Entry(Integer.parseInt(time), i, bean.getPage()));
            }
        }
        return entries;
    }

    private List<ValuesBean> buildEntriesItem() {
        List<ValuesBean> values = new ArrayList<>();

        List<PerformanceBean> list = null;
        AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
        if (info == null || info.getData() == null) {
            return null;
        }

        AppHealthInfo.DataBean data = info.getData();
        if (TYPE_FRAME_ITEM == mType) {
            list = data.getFps();
        } else if (TYPE_CPU_ITEM == mType) {
            list = data.getCpu();
        } else if (TYPE_MEMORY_ITEM == mType) {
            list = data.getMemory();
        }

        if (list == null) return values;

        for (PerformanceBean b : list) {
            if (b.getPage().equals(mClassName)) {
                List<ValuesBean> tmpList = b.getValues();
                values.addAll(tmpList);
            }
        }

        return values;
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
