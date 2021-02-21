package com.didichuxing.doraemonkit.kit.health.chart;

import com.didichuxing.doraemonkit.kit.network.utils.ByteUtil;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class NetworkTrafficValueFormatter implements ValueFormatter, YAxisValueFormatter {
    public NetworkTrafficValueFormatter(String suffix) {
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return ByteUtil.getPrintSize((long) value);
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return ByteUtil.getPrintSize((long) value);
    }
}
