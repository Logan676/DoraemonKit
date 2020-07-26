package com.didichuxing.doraemonkit.kit.health;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.DokitConstant;
import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.blockmonitor.BlockListFragment;
import com.didichuxing.doraemonkit.kit.blockmonitor.BlockMonitorFragment;
import com.didichuxing.doraemonkit.kit.largepicture.LargeImageListFragment;
import com.didichuxing.doraemonkit.kit.largepicture.LargePictureFragment;
import com.didichuxing.doraemonkit.kit.methodtrace.MethodCostFragment;
import com.didichuxing.doraemonkit.kit.network.ui.NetWorkMainPagerFragment;
import com.didichuxing.doraemonkit.kit.parameter.cpu.CpuMainPageFragment;
import com.didichuxing.doraemonkit.kit.parameter.frameInfo.FrameInfoFragment;
import com.didichuxing.doraemonkit.kit.parameter.ram.RamMainPageFragment;
import com.didichuxing.doraemonkit.kit.timecounter.TimeCounterFragment;
import com.didichuxing.doraemonkit.kit.timecounter.TimeCounterListFragment;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;
import com.didichuxing.doraemonkit.ui.kit.GroupKitAdapter;
import com.didichuxing.doraemonkit.ui.kit.KitItem;
import com.didichuxing.doraemonkit.ui.widget.titlebar.HomeTitleBar;
import com.didichuxing.doraemonkit.view.verticalviewpager.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 健康体检fragment
 */
public class HealthFragment extends BaseFragment {
//    VerticalViewPager mVerticalViewPager;
    HomeTitleBar mHomeTitleBar;
    private RecyclerView mGroupKitContainer;
    private GroupKitAdapter mGroupKitAdapter;

    List<Fragment> mFragments = new ArrayList<>();
    FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_health;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        initView();
    }

    private void initView() {
        mFragments.clear();
        mFragments.add(new HealthFragmentChildNetWorkTraffic());
//        mFragments.add(new BlockListFragment());
//        mFragments.add(new NetWorkMainPagerFragment());
//        mFragments.add(new FrameInfoFragment());
//        mFragments.add(new CpuMainPageFragment());
//        mFragments.add(new RamMainPageFragment());
//        mFragments.add(new TimeCounterListFragment());
//        mFragments.add(new LargeImageListFragment());
//        mFragments.add(new MethodCostFragment());
        mFragments.add(new HealthFragmentChild0());
        mFragments.add(new HealthFragmentChild1());
        mHomeTitleBar = findViewById(R.id.title_bar);
        mHomeTitleBar.setListener(new HomeTitleBar.OnTitleBarClickListener() {
            @Override
            public void onRightClick() {
                finish();
            }
        });

        mGroupKitContainer = findViewById(R.id.group_kit_container);
        mGroupKitContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        mGroupKitAdapter = new GroupKitAdapter(getContext());
        List<List<KitItem>> kitLists = new ArrayList<>();
        //健康体检
        kitLists.add(DokitConstant.getKitItems(Category.HEALTH));
        //性能监控
        kitLists.add(DokitConstant.getKitItems(Category.PERFORMANCE));
        mGroupKitAdapter.setData(kitLists);
        mGroupKitContainer.setAdapter(mGroupKitAdapter);

//        mVerticalViewPager = findViewById(R.id.view_pager);
//        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragments.size();
//            }
//
//            @Nullable
//            @Override
//            public CharSequence getPageTitle(int position) {
//                String title = "";
//                switch (position) {
//                    case 0:
//                        title = "网络流量";
//                        break;
//                    case 1:
//                        title = getString(R.string.dk_kit_block_monitor_list);
//                        break;
//                    case 2:
//                        title = getString(R.string.dk_net_monitor_title_summary);
//                        break;
//                    case 3:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    case 4:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    case 5:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    case 6:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    case 7:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    case 8:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    case 9:
//                        title = getString(R.string.dk_kit_frame_info_desc);
//                        break;
//                    default:
//                        title = "开发中...";
//                        break;
//                }
//                return title;
//            }
//        };
//        mVerticalViewPager.setAdapter(mFragmentPagerAdapter);
//
//
//        mVerticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                CharSequence pageTitle = mFragmentPagerAdapter.getPageTitle(position);
//                mHomeTitleBar.setTitle(String.valueOf(pageTitle));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    /**
     * 滑动到顶部
     */
    protected void scroll2theTop() {
//        if (mVerticalViewPager != null && mFragmentPagerAdapter != null) {
//            mVerticalViewPager.setCurrentItem(0, true);
//        }
    }


}
