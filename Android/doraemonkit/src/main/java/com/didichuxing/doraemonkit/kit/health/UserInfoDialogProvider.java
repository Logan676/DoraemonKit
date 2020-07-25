package com.didichuxing.doraemonkit.kit.health;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.ui.dialog.DialogListener;
import com.didichuxing.doraemonkit.ui.dialog.DialogProvider;
import com.didichuxing.doraemonkit.util.LogHelper;

import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_TIME;

/**
 * Created by jint on 2019/4/12
 * 完善健康体检用户信息dialog
 * @author jintai
 */
public class UserInfoDialogProvider extends DialogProvider<Object> {
    private TextView mPositive;
    private TextView mNegative;
    private TextView mClose;
    private EditText mCaseName;
    private EditText mUserName;

    UserInfoDialogProvider(Object data, DialogListener listener) {
        super(data, listener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dk_dialog_userinfo;
    }

    @Override
    protected void findViews(View view) {
        mPositive = view.findViewById(R.id.positive);
        mNegative = view.findViewById(R.id.negative);
        mClose = view.findViewById(R.id.close);
        mCaseName = view.findViewById(R.id.edit_case_name);
        mUserName = view.findViewById(R.id.edit_user_name);

        String dateTime = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(), FORMAT_SHOW_TIME | FORMAT_SHOW_DATE);
        String caseName = "性能监控 "+ dateTime;
        mCaseName.setText(caseName);
        mUserName.setText("测试");
    }

    @Override
    protected void bindData(Object data) {

    }

    @Override
    protected View getPositiveView() {
        return mPositive;
    }

    @Override
    protected View getNegativeView() {
        return mNegative;
    }

    @Override
    protected View getCancelView() {
        return mClose;
    }

    /**
     * 上传健康体检数据
     */
    boolean uploadAppHealthInfo(Context context, UploadAppHealthCallback uploadAppHealthCallBack) {
        if (!userInfoCheck()) {
            ToastUtils.showShort("请填写测试用例和测试人");
            return false;
        }
        String caseName = mCaseName.getText().toString();
        String userName = mUserName.getText().toString();

        AppHealthInfoUtil.getInstance().setBaseInfo(caseName, userName);
        //上传数据
        AppHealthInfoUtil.getInstance().post(context, uploadAppHealthCallBack);
        return true;
    }

    /**
     * 检查用户数据
     */
    private boolean userInfoCheck() {
        if (mCaseName == null || TextUtils.isEmpty(mCaseName.getText().toString())) {
            return false;
        }

        if (mUserName == null || TextUtils.isEmpty(mUserName.getText().toString())) {
            return false;
        }

        return true;
    }

}