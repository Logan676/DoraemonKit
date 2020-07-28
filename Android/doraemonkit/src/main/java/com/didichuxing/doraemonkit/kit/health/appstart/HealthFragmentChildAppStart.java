package com.didichuxing.doraemonkit.kit.health.appstart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.widget.titlebar.TitleBar;
import com.zzhoujay.richtext.RichText;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.AppStartBean;

public class HealthFragmentChildAppStart extends BaseFragment {
    private TextView mAppStartTimeMs;
    private TextView mAppStartStackTrace;

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_health_child_app_start;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        final TitleBar mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightClick() {

            }
        });

        mAppStartTimeMs = findViewById(R.id.app_start_time_ms);
        mAppStartStackTrace = findViewById(R.id.app_start_stack_trace);
    }


    private void initData() {
        synchronized (this) {
            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null || info.getData().getAppStart() == null) {
                return;
            }

            AppStartBean appStart = info.getData().getAppStart();
            mAppStartTimeMs.setText(appStart.getCostTime() + " ms");
            RichText.from(appStart.getCostDetail()).into(mAppStartStackTrace);
        }
    }
}
