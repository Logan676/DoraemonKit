package com.didichuxing.doraemonkit.kit.health;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.didichuxing.doraemonkit.BuildConfig;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.config.CrashCaptureConfig;
import com.didichuxing.doraemonkit.constant.CachesKey;
import com.didichuxing.doraemonkit.constant.DokitConstant;
import com.didichuxing.doraemonkit.kit.blockmonitor.core.BlockMonitorManager;
import com.didichuxing.doraemonkit.kit.common.PerformanceDataManager;
import com.didichuxing.doraemonkit.kit.crash.CrashCaptureManager;
import com.didichuxing.doraemonkit.kit.health.model.AppHealthInfo;
import com.didichuxing.doraemonkit.kit.network.NetworkManager;
import com.didichuxing.doraemonkit.okgo.DokitOkGo;
import com.didichuxing.doraemonkit.okgo.callback.StringCallback;
import com.didichuxing.doraemonkit.okgo.model.Response;
import com.didichuxing.doraemonkit.util.CacheUtils;
import com.didichuxing.doraemonkit.util.LogHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020-01-02-16:42
 * 描    述：app 健康体检工具类
 * 修订历史：
 * ================================================
 */
public class AppHealthInfoUtil {
    private static String TAG = "AppHealthInfoUtil";

    private AppHealthInfo mAppHealthInfo = new AppHealthInfo();

    public AppHealthInfo getAppHealthInfo() {
        return mAppHealthInfo;
    }

    /**
     * 静态内部类单例
     */
    private static class Holder {
        private static AppHealthInfoUtil INSTANCE = new AppHealthInfoUtil();
    }

    public static AppHealthInfoUtil getInstance() {
        return AppHealthInfoUtil.Holder.INSTANCE;
    }


    /**
     * 设置基本信息
     *
     * @param caseName   用例名称
     * @param testPerson 测试人员名字
     */
    void setBaseInfo(String caseName, String testPerson) {
        AppHealthInfo.BaseInfoBean baseInfoBean = new AppHealthInfo.BaseInfoBean();
        baseInfoBean.setTestPerson(testPerson);
        baseInfoBean.setCaseName(caseName);
        baseInfoBean.setAppName(AppUtils.getAppName());
        baseInfoBean.setAppVersion(AppUtils.getAppVersionName());
        baseInfoBean.setDokitVersion(BuildConfig.DOKIT_VERSION);
        baseInfoBean.setPlatform("Android");
        baseInfoBean.setPhoneMode(DeviceUtils.getModel());
        baseInfoBean.setTime(TimeUtils.getNowString());
        baseInfoBean.setSystemVersion(DeviceUtils.getSDKVersionName());
        baseInfoBean.setpId("" + DokitConstant.PRODUCT_ID);
        mAppHealthInfo.setBaseInfo(baseInfoBean);
    }

    /**
     * 设置app启动耗时的具体信息
     *
     * @param costTime
     * @param costDetail
     */
    public void setAppStartInfo(long costTime, String costDetail, List<AppHealthInfo.DataBean.AppStartBean.LoadFuncBean> loadFunc) {
        AppHealthInfo.DataBean.AppStartBean appStartBean = new AppHealthInfo.DataBean.AppStartBean();
        appStartBean.setCostTime(costTime);
        appStartBean.setCostDetail(costDetail);
        appStartBean.setLoadFunc(loadFunc);
        getData().setAppStart(appStartBean);
    }

    /**
     * 添加cpu信息
     *
     * @param cpuBean
     */
    public void addCPUInfo(AppHealthInfo.DataBean.PerformanceBean cpuBean) {
        List<AppHealthInfo.DataBean.PerformanceBean> cpus = getData().getCpu();
        if (cpus == null) {
            cpus = new ArrayList<>();
            getData().setCpu(cpus);
        }
        //不过滤最大最小值
        //cpuBean.setValues(sortValue(cpuBean.getValues()));
        cpus.add(cpuBean);
    }


    /**
     * 添加memory信息
     *
     * @param memoryBean
     */
    public void addMemoryInfo(AppHealthInfo.DataBean.PerformanceBean memoryBean) {
        List<AppHealthInfo.DataBean.PerformanceBean> memories = getData().getMemory();
        if (memories == null) {
            memories = new ArrayList<>();
            getData().setMemory(memories);
        }
        //不过滤最大最小值
        //memoryBean.setValues(sortValue(memoryBean.getValues()));
        memories.add(memoryBean);
    }

    /**
     * 添加fps信息
     *
     * @param fpsBean
     */
    public void addFPSInfo(AppHealthInfo.DataBean.PerformanceBean fpsBean) {
        List<AppHealthInfo.DataBean.PerformanceBean> fpsBeans = getData().getFps();
        if (fpsBeans == null) {
            fpsBeans = new ArrayList<>();
            getData().setFps(fpsBeans);
        }
        //不过滤最大最小值
        //fpsBean.setValues(sortValue(fpsBean.getValues()));
        fpsBeans.add(fpsBean);
    }

    /**
     * 获取当前最后一个PerformanceInfo信息
     *
     * @return PerformanceBean
     */
    public AppHealthInfo.DataBean.PerformanceBean getLastPerformanceInfo(int performanceType) {
        List<AppHealthInfo.DataBean.PerformanceBean> performanceBeans = null;
        if (performanceType == PerformanceDataManager.PERFORMANCE_TYPE_CPU) {
            performanceBeans = getData().getCpu();
        } else if (performanceType == PerformanceDataManager.PERFORMANCE_TYPE_MEMORY) {
            performanceBeans = getData().getMemory();
        } else if (performanceType == PerformanceDataManager.PERFORMANCE_TYPE_FPS) {
            performanceBeans = getData().getFps();
        }
        if (performanceBeans == null || performanceBeans.size() == 0) {
            return null;
        }
        return performanceBeans.get(performanceBeans.size() - 1);
    }

    /**
     * 移除满足条件的最后一个PerformanceInfo信息
     *
     * @return PerformanceBean
     */
    public void removeLastPerformanceInfo(int performanceType) {
        List<AppHealthInfo.DataBean.PerformanceBean> performanceBeans = null;
        if (performanceType == PerformanceDataManager.PERFORMANCE_TYPE_CPU) {
            performanceBeans = getData().getCpu();
        } else if (performanceType == PerformanceDataManager.PERFORMANCE_TYPE_MEMORY) {
            performanceBeans = getData().getMemory();
        } else if (performanceType == PerformanceDataManager.PERFORMANCE_TYPE_FPS) {
            performanceBeans = getData().getFps();
        }
        if (performanceBeans != null && performanceBeans.size() > 0) {
            performanceBeans.remove(performanceBeans.size() - 1);
        }
    }


    /**
     * 添加网络信息
     *
     * @param networkBean
     */
    public void addNetWorkInfo(AppHealthInfo.DataBean.NetworkBean networkBean) {
        List<AppHealthInfo.DataBean.NetworkBean> networks = getData().getNetwork();
        if (networks == null) {
            networks = new ArrayList<>();
            getData().setNetwork(networks);
        }
        networks.add(networkBean);
    }

    /**
     * 获取指定的NetworkBean
     *
     * @param activityName
     */
    public AppHealthInfo.DataBean.NetworkBean getNetWorkInfo(String activityName) {
        List<AppHealthInfo.DataBean.NetworkBean> networks = getData().getNetwork();
        if (networks == null || networks.size() == 0) {
            return null;
        }
        AppHealthInfo.DataBean.NetworkBean networkBean = null;

        for (AppHealthInfo.DataBean.NetworkBean traverseNetworkBean : networks) {
            if (traverseNetworkBean.getPage().equals(activityName)) {
                networkBean = traverseNetworkBean;
                break;
            }
        }

        return networkBean;
    }

    /**
     * 添加卡顿信息
     *
     * @param blockBean
     */
    public void addBlockInfo(AppHealthInfo.DataBean.BlockBean blockBean) {
        List<AppHealthInfo.DataBean.BlockBean> blocks = getData().getBlock();
        if (blocks == null) {
            blocks = new ArrayList<>();
            getData().setBlock(blocks);
        }
        blocks.add(blockBean);
    }


    /**
     * 添加页面层级信息
     *
     * @param uiLevelBean
     */
    public void addUiLevelInfo(AppHealthInfo.DataBean.UiLevelBean uiLevelBean) {
        List<AppHealthInfo.DataBean.UiLevelBean> uiLevels = getData().getUiLevel();
        if (uiLevels == null) {
            uiLevels = new ArrayList<>();
            getData().setUiLevel(uiLevels);
        }
        uiLevels.add(uiLevelBean);
    }

    /**
     * 添加内存泄漏信息
     *
     * @param leakBean
     */
    public void addLeakInfo(AppHealthInfo.DataBean.LeakBean leakBean) {
        List<AppHealthInfo.DataBean.LeakBean> leaks = getData().getLeak();
        if (leaks == null) {
            leaks = new ArrayList<>();
            getData().setLeak(leaks);
        }
        leaks.add(leakBean);
    }

    /**
     * 添加页面加载耗时信息
     *
     * @param pageLoadBean
     */
    public void addPageLoadInfo(AppHealthInfo.DataBean.PageLoadBean pageLoadBean) {
        List<AppHealthInfo.DataBean.PageLoadBean> pageloads = getData().getPageLoad();
        if (pageloads == null) {
            pageloads = new ArrayList<>();
            getData().setPageLoad(pageloads);
        }
        pageloads.add(pageLoadBean);
    }

    /**
     * 添加页面加载耗时信息
     *
     * @param bigFileBean
     */
    public void addBigFilrInfo(AppHealthInfo.DataBean.BigFileBean bigFileBean) {
        List<AppHealthInfo.DataBean.BigFileBean> bigFiles = getData().getBigFile();
        if (bigFiles == null) {
            bigFiles = new ArrayList<>();
            getData().setBigFile(bigFiles);
        }
        bigFiles.add(bigFileBean);
    }


    /**
     * 上传健康体检数据到服务器
     */
    public void post(final Context context, final UploadAppHealthCallback uploadAppHealthCallBack) {
        if (mAppHealthInfo == null) {
            return;
        }
        //线上地址：https://www.dokit.cn/healthCheck/addCheckData
        //测试环境地址:http://dokit-test.intra.xiaojukeji.com/healthCheck/addCheckData

        saveToLocal(context);

        String s = GsonUtils.toJson(mAppHealthInfo);
        Log.d("上传健康体检数据到服务器", s);
        DokitOkGo.<String>post(NetworkManager.APP_HEALTH_URL)
                .upJson(s)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (uploadAppHealthCallBack != null) {
                            uploadAppHealthCallBack.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (uploadAppHealthCallBack != null) {
                            uploadAppHealthCallBack.onError(response);
                        }
                    }
                });

    }

    public void saveToLocal(final Context context) {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                LogHelper.d("saveToLocal", "doInBackground: \n" + mAppHealthInfo.toString());
                CacheUtils.saveObject(context, CachesKey.NETWORK_TRAFFIC, mAppHealthInfo);
                return null;
            }

            @Override
            public void onSuccess(Object result) {
                LogHelper.d("saveToLocal", "onSuccess");

            }
        });
    }

    private void readFromLocal(final Context context) {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<AppHealthInfo>() {
            @Override
            public AppHealthInfo doInBackground() throws Throwable {
                LogHelper.d("readFromLocal", "doInBackground: \n" + mAppHealthInfo.toString());
                return (AppHealthInfo) CacheUtils.readObject(context, CachesKey.NETWORK_TRAFFIC);
            }

            @Override
            public void onSuccess(AppHealthInfo result) {
                LogHelper.d("readFromLocal", "onSuccess");
                if (result != null) {
                    if (mAppHealthInfo == null) {
                        mAppHealthInfo = result;
                        return;
                    }

                    if (mAppHealthInfo.getBaseInfo() == null) {
                        mAppHealthInfo.setBaseInfo(result.getBaseInfo());
                    } else {
                        if (result.getBaseInfo() != null) {
                            mAppHealthInfo.setBaseInfo(mAppHealthInfo.getBaseInfo());
                        }
                    }

                    if (mAppHealthInfo.getData() == null) {
                        mAppHealthInfo.setData(result.getData());
                    } else {
                        if (result.getData() != null) {
                            AppHealthInfo.DataBean data = mAppHealthInfo.getData();
                            if (data.getNetwork() == null) {
                                data.setNetwork(result.getData().getNetwork());
                            } else if (getData().getNetwork() != null) {
                                data.getNetwork().addAll(getData().getNetwork());
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取data对象
     *
     * @return
     */
    private AppHealthInfo.DataBean getData() {
        if (mAppHealthInfo.getData() == null) {
            AppHealthInfo.DataBean dataBean = new AppHealthInfo.DataBean();
            dataBean.setCpu(new ArrayList<AppHealthInfo.DataBean.PerformanceBean>());
            dataBean.setMemory(new ArrayList<AppHealthInfo.DataBean.PerformanceBean>());
            dataBean.setFps(new ArrayList<AppHealthInfo.DataBean.PerformanceBean>());
            dataBean.setNetwork(new ArrayList<AppHealthInfo.DataBean.NetworkBean>());
            dataBean.setBlock(new ArrayList<AppHealthInfo.DataBean.BlockBean>());
            dataBean.setUiLevel(new ArrayList<AppHealthInfo.DataBean.UiLevelBean>());
            dataBean.setLeak(new ArrayList<AppHealthInfo.DataBean.LeakBean>());
            dataBean.setPageLoad(new ArrayList<AppHealthInfo.DataBean.PageLoadBean>());
            dataBean.setBigFile(new ArrayList<AppHealthInfo.DataBean.BigFileBean>());
            dataBean.setSubThreadUI(new ArrayList<AppHealthInfo.DataBean.SubThreadUIBean>());
            mAppHealthInfo.setData(dataBean);
        }
        return mAppHealthInfo.getData();
    }

    /**
     * 时候处于app 健康体检状态
     *
     * @return
     */
    public boolean isAppHealthRunning() {
        boolean isRunning = DokitConstant.APP_HEALTH_RUNNING;
        if (isRunning) {
            ToastUtils.showShort("App当前处于健康体检状态,无法进行此操作");
        }
        return isRunning;
    }

    /**
     * 开启健康体检监控
     *
     * @param app
     */
    public void start(Application app) {
        readFromLocal(app);

        PerformanceDataManager.getInstance().init();
        //帧率
        PerformanceDataManager.getInstance().startMonitorFrameInfo();
        //cpu
        PerformanceDataManager.getInstance().startMonitorCPUInfo();
        //内存
        PerformanceDataManager.getInstance().startMonitorMemoryInfo();
        //网络
        PerformanceDataManager.getInstance().startMonitorNetFlowInfo();
        //卡顿
        BlockMonitorManager.getInstance().start();
        //crash 开关
        CrashCaptureConfig.setCrashCaptureOpen(DoraemonKit.APPLICATION, true);
        CrashCaptureManager.getInstance().start();
    }

    /**
     * 结束健康体检监控
     */
    public void stop() {
        //帧率
        PerformanceDataManager.getInstance().stopMonitorFrameInfo();
        //cpu
        PerformanceDataManager.getInstance().stopMonitorCPUInfo();
        //内存
        PerformanceDataManager.getInstance().stopMonitorMemoryInfo();
        //网络
        PerformanceDataManager.getInstance().stopMonitorNetFlowInfo();
        //卡顿
        BlockMonitorManager.getInstance().stop();
        //crash 开关
        CrashCaptureConfig.setCrashCaptureOpen(DoraemonKit.APPLICATION, false);
        CrashCaptureManager.getInstance().stop();

        // TODO: 2020/7/23 持久化 数据

    }

    /**
     * list 去掉最大值和最小值 并重新 排序
     */
    private List<AppHealthInfo.DataBean.PerformanceBean.ValuesBean> sortValue(List<AppHealthInfo.DataBean.PerformanceBean.ValuesBean> valuesBeans) {
        List<AppHealthInfo.DataBean.PerformanceBean.ValuesBean> newValuesBeans = new ArrayList<>(valuesBeans);
        Collections.sort(newValuesBeans, new Comparator<AppHealthInfo.DataBean.PerformanceBean.ValuesBean>() {
            @Override
            public int compare(AppHealthInfo.DataBean.PerformanceBean.ValuesBean pre, AppHealthInfo.DataBean.PerformanceBean.ValuesBean next) {
                float preValue = Float.parseFloat(pre.getValue());
                float nextValue = Float.parseFloat(next.getValue());
                if (preValue < nextValue) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });
        newValuesBeans.remove(0);
        newValuesBeans.remove(newValuesBeans.size() - 1);
        Collections.sort(newValuesBeans, new Comparator<AppHealthInfo.DataBean.PerformanceBean.ValuesBean>() {
            @Override
            public int compare(AppHealthInfo.DataBean.PerformanceBean.ValuesBean pre, AppHealthInfo.DataBean.PerformanceBean.ValuesBean next) {
                long preValue = Long.parseLong(pre.getTime());
                long nextValue = Long.parseLong(next.getTime());
                if (preValue < nextValue) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return newValuesBeans;

    }

    /**
     * 内存释放
     */
    public void release() {
        if (mAppHealthInfo != null) {
            mAppHealthInfo = null;
        }
    }

}
