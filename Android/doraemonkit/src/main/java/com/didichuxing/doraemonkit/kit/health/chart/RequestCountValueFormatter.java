package com.didichuxing.doraemonkit.kit.health.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class RequestCountValueFormatter implements ValueFormatter {

    private final DecimalFormat mFormat;
    private String suffix;

    public RequestCountValueFormatter(String suffix) {
        mFormat = new DecimalFormat("###,###,###,##0.0");
        this.suffix = suffix;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return null;
    }

//    @Override
//    public String getFormattedValue(float value) {
//        return mFormat.format(value) + suffix;
//    }
//
//    @Override
//    public String getAxisLabel(float value, AxisBase axis) {
//        if (axis instanceof XAxis) {
//            return mFormat.format(value);
//        } else if (value > 0) {
//            return mFormat.format(value) + suffix;
//        } else {
//            return mFormat.format(value);
//        }
//    }
}
