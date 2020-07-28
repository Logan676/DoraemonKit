package com.didichuxing.doraemonkit.kit.health.uilayer;

import android.content.Context;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

public class HealthUILayerKit extends AbstractKit {
    @Override
    public int getCategory() {
        return Category.HEALTH;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_health_ui_layer;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_ui_layer;
    }

    @Override
    public void onClick(Context context) {
//        startUniversalActivity(context, FragmentIndex.FRAGMENT_FRAME_INFO);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
