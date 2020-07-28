package com.didichuxing.doraemonkit.kit.health.cpu;

import android.content.Context;
import android.os.Bundle;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.BundleKey;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_TYPE;

public class HealthCPUKit extends AbstractKit {
    @Override
    public int getCategory() {
        return Category.HEALTH;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_health_cpu;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_cpu2;
    }

    @Override
    public void onClick(Context context) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, BundleKey.TYPE_CPU);
        startUniversalActivity(context, bundle, FragmentIndex.FRAGMENT_HEALTH_CPU);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
