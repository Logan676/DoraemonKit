package com.didichuxing.doraemonkit.kit.health.memory;

import android.content.Context;
import android.os.Bundle;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.BundleKey;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

import static com.didichuxing.doraemonkit.constant.BundleKey.KEY_TYPE;

public class HealthMemoryKit extends AbstractKit {
    @Override
    public int getCategory() {
        return Category.HEALTH;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_health_memory;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_memory;
    }

    @Override
    public void onClick(Context context) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, BundleKey.TYPE_MEMORY);
        startUniversalActivity(context, bundle, FragmentIndex.FRAGMENT_HEALTH_MEMORY);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
