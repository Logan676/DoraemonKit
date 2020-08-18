package com.didichuxing.doraemonkit.kit.health.chart;

import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class NetworkTrafficXAxisValueFormatter implements XAxisValueFormatter {
    public NetworkTrafficXAxisValueFormatter(String suffix) {
    }


    @Override
    public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        return String.valueOf(index);
    }
}
