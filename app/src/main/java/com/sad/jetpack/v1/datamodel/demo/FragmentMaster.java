package com.sad.jetpack.v1.datamodel.demo;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public final class FragmentMaster {
    private FragmentManager fragmentManager=null;
    private ViewPager viewPager;
    private ViewPager2 viewPager2;
    private List<Fragment> fragments=new ArrayList<>();

    interface LoadModeChoice {

        AsDialog dialog(Activity activity);

        AsViewAddToContainerInvoker addAllToContainer(int container);

        AsObjectBindToViewPager bindViewPager(ViewPager viewPager);
    }

    interface AsDialog{

    }

    interface AsViewAddToContainerInvoker{

        AsViewAddToContainerInvoker hideAll(Lifecycle.State maxLifecycle);

        AsViewAddToContainerInvoker hideAll(List<Lifecycle.State> maxLifecycles);

        AsViewAddToContainerInvoker show(int i, Lifecycle.State maxLifecycle);

        AsViewAddToContainerInvoker showByTag(@NonNull String tag,Lifecycle.State maxLifecycle);

        AsViewAddToContainerInvoker hide(int i, Lifecycle.State maxLifecycle);

        AsViewAddToContainerInvoker hideByTag(@NonNull String tag,Lifecycle.State maxLifecycle);
    }

    interface AsObjectBindToViewPager{

        AsObjectBindToViewPager adapter(PagerAdapter adapter);

        AsObjectBindToViewPager show();

    }

    private FragmentMaster(){}
    private FragmentMaster(@NonNull FragmentManager fragmentManager){
        this.fragmentManager=fragmentManager;
    }
    public static FragmentMaster with(@NonNull FragmentManager fragmentManager){
        return new FragmentMaster(fragmentManager);
    }
    public FragmentMaster removeAllFragments(){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (Fragment f:fragments
        ) {
            ft.remove(f);
        }
        ft.commitNowAllowingStateLoss();
        fragments.clear();
        return this;
    }
    public FragmentMaster prePare(@NonNull Fragment... fragments){
        if (fragments!=null){
            for (Fragment fragment:fragments
                 ) {
                this.fragments.add(fragment);
            }
        }
        return this;
    }

    public LoadModeChoice choiceMode(){
        return new DefaultFragmentLoadModeChoice(fragmentManager,fragments);
    }


}
