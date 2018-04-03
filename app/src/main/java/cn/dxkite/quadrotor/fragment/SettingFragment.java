package cn.dxkite.quadrotor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.dxkite.quadrotor.QuadrotorService;
import cn.dxkite.quadrotor.R;
import cn.dxkite.quadrotor.adapter.ControlFragmentAdapter;
import cn.dxkite.view.NumberEdit;
import cn.dxkite.view.StaticViewPager;

public class SettingFragment extends ControlFragment {

    ControlFragmentAdapter adapter;
    List<ControlFragment> fragmentList = new ArrayList<>();
    TabLayout tabLayout;
    StaticViewPager viewPager;

    @Override
    public View onInitView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null, false);
        tabLayout = view.findViewById(R.id.settingTab);
        viewPager = view.findViewById(R.id.settingViewPager);
        adapter = new ControlFragmentAdapter(getFragmentManager(), fragmentList, new String[]{"PID设置", "姿势设置"});
        fragmentList.add(ControlFragment.createFragment(PidFragment.class, viewPager));
        fragmentList.add(ControlFragment.createFragment(PoseFragment.class, viewPager));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.d(TAG,"fragmentList size ->" + fragmentList.size() + " and binder is " +binder);
        for (ControlFragment fragment : fragmentList) {
            fragment.setBinder(binder);
        }
        return view;
    }

    @Override
    public void setBinder(QuadrotorService.QuadrotorBinder binder) {
        super.setBinder(binder);
    }
}
