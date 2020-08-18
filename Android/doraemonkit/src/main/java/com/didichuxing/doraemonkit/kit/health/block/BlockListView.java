package com.didichuxing.doraemonkit.kit.health.block;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.health.AppHealthInfoUtil;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.DividerItemDecoration;

import java.util.Collections;
import java.util.List;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.BlockBean;

public class BlockListView extends LinearLayout {
    private RecyclerView mBlockList;
    private TextView mTextViewDetailLog;
    private BlockListAdapter mBlockListAdapter;

    public BlockListView(Context context) {
        this(context, null);
    }

    public BlockListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dk_fragment_block_list, this);
        initView();
        initData();
    }


    private void initView() {
        findViewById(R.id.title_bar).setVisibility(GONE);

        mBlockList = findViewById(R.id.block_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBlockList.setLayoutManager(layoutManager);
        mBlockListAdapter = new BlockListAdapter(getContext());
        mBlockList.setAdapter(mBlockListAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.dk_divider));
        decoration.showHeaderDivider(true);
        mBlockList.addItemDecoration(decoration);

        mTextViewDetailLog = findViewById(R.id.tx_block_detail);
        mTextViewDetailLog.setVisibility(GONE);

        mBlockListAdapter.setOnItemClickListener(new BlockListAdapter.OnItemClickListener() {
            @Override
            public void onClick(BlockBean info) {
                mTextViewDetailLog.setVisibility(VISIBLE);
                mBlockList.setVisibility(GONE);
                mTextViewDetailLog.setText(info.getDetail());
            }
        });
    }

    public boolean onBackPressed() {
        if (mTextViewDetailLog.getVisibility() == View.VISIBLE) {
            mTextViewDetailLog.setVisibility(View.GONE);
            mBlockList.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


    private void initData() {
        synchronized (this) {

            AppHealthInfo info = AppHealthInfoUtil.getInstance().getAppHealthInfo();
            if (info == null || info.getData() == null || info.getData().getBlock() == null) {
                return;
            }

            List<BlockBean> list = info.getData().getBlock();
            Collections.sort(list);
            mBlockListAdapter.setData(list);
        }
    }

}
