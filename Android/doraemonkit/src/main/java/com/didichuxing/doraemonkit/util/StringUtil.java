package com.didichuxing.doraemonkit.util;

import android.text.TextUtils;

public class StringUtil {

    public static String getSimpleClassName(String className) {
        if (!TextUtils.isEmpty(className)) {
            int subIndex = className.lastIndexOf(".") + 1;
            if (subIndex < className.length()) {
                String substring = className.substring(subIndex);
                return substring.replaceAll("Activity", "");
            }
        }
        return "";
    }

}
