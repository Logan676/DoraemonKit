package com.didichuxing.doraemonkit.kit.health.largefile;

import android.content.Context;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

public class HealthLargeFileKit extends AbstractKit {
    @Override
    public int getCategory() {
        return Category.HEALTH;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_health_large_file;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_large_file;
    }

    @Override
    public void onClick(Context context) {
//        startUniversalActivity(context, FragmentIndex.FRAGMENT_FRAME_INFO);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
