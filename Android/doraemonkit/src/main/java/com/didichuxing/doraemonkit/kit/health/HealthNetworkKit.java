package com.didichuxing.doraemonkit.kit.health;

import android.content.Context;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

public class HealthNetworkKit extends AbstractKit {
    @Override
    public int getCategory() {
        return Category.HEALTH;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_health_net;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_network_traffic;
    }

    @Override
    public void onClick(Context context) {
        startUniversalActivity(context, FragmentIndex.FRAGMENT_HEALTH_NETWORK_RESULT_LIST);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
