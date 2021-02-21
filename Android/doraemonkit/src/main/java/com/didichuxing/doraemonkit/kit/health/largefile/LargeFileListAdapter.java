package com.didichuxing.doraemonkit.kit.health.largefile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.network.utils.ByteUtil;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsRecyclerAdapter;
import com.didichuxing.doraemonkit.ui.widget.recyclerview.AbsViewBinder;

import java.util.Collection;

import static com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo.DataBean.BigFileBean;

/**
 * @desc: 大文件
 */
public class LargeFileListAdapter extends AbsRecyclerAdapter<AbsViewBinder<BigFileBean>, BigFileBean> {
    public LargeFileListAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbsViewBinder<BigFileBean> createViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_large_file_item, parent, false);
    }

    private static class ItemViewHolder extends AbsViewBinder<BigFileBean> {
        private TextView fileName;
        private TextView fileSize;
        private TextView filePath;

        public ItemViewHolder(View view) {
            super(view);
        }

        @Override
        protected void getViews() {
            fileName = getView(R.id.large_file_name);
            fileSize = getView(R.id.large_file_size);
            filePath = getView(R.id.large_file_path);
        }

        @Override
        public void bind(final BigFileBean bean) {
            fileName.setText(bean.getFileName());
            long size = Long.parseLong(bean.getFileSize());
            fileSize.setText("文件大小：" + ByteUtil.getPrintSize(size));
            filePath.setText("文件路径：" + bean.getFilePath());
        }
    }

    @Override
    public void setData(Collection<BigFileBean> items) {
        super.setData(items);
    }
}

