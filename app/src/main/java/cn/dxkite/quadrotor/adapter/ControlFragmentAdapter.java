package cn.dxkite.quadrotor.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.dxkite.quadrotor.fragment.ControlFragment;

public class ControlFragmentAdapter extends FragmentPagerAdapter {

    private List<ControlFragment> fragmentList;
    private String[] titles;

    public ControlFragmentAdapter(FragmentManager fm, List<ControlFragment> list) {
        super(fm);
        this.fragmentList = list;
    }

    public ControlFragmentAdapter(FragmentManager fm, List<ControlFragment> list, String[] titles) {
        super(fm);
        this.fragmentList = list;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return fragmentList.size();//有几个页面
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > position && position >= 0)
            return titles[position];
        return "无标题";
    }
}
