package com.sad.jetpack.v1.datamodel.demo;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class DefaultFragmentLoadModeChoice implements FragmentMaster.LoadModeChoice {

    private List<Fragment> fragments=new ArrayList<>();
    private FragmentManager fragmentManager;

    public DefaultFragmentLoadModeChoice(FragmentManager fragmentManager,List<Fragment> fragments) {
        this.fragments = fragments;
        this.fragmentManager=fragmentManager;
    }


    @Override
    public FragmentMaster.AsDialog dialog(Activity activity) {
        return null;
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker addAllToContainer(int container) {
        return new DefaultAsViewAddToContainerInvoker(fragments,container,fragmentManager);
    }


    @Override
    public FragmentMaster.AsObjectBindToViewPager bindViewPager(ViewPager viewPager) {
        return null;
    }
}
