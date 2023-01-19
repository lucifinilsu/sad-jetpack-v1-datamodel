package com.sad.jetpack.v1.datamodel.demo;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.List;

public class DefaultAsViewAddToContainerInvoker implements FragmentMaster.AsViewAddToContainerInvoker {

    private List<Fragment> fragments=new ArrayList<>();
    private int container;
    private FragmentManager fragmentManager;

    public DefaultAsViewAddToContainerInvoker(List<Fragment> fragments, int container, FragmentManager fragmentManager) {
        this.fragments = fragments;
        this.container = container;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker hideAll(Lifecycle.State maxLifecycle) {
        List<Lifecycle.State> states=new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            states.add(maxLifecycle);
        }
        return hideAll(states);
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker hideAll(List<Lifecycle.State> maxLifecycles) {
        FragmentTransaction trx = fragmentManager.beginTransaction();
        try {
            for (Fragment fragmentOrg:fragments
                 ) {
                int index=fragments.indexOf(fragmentOrg);
                Fragment fragmentTarget=fragmentOrg;
                String tag=fragmentTarget.getTag();
                Fragment f_cache=fragmentManager.findFragmentByTag(tag);
                if (f_cache!=null){
                    fragmentTarget=f_cache;
                }
                if(fragmentTarget!=null){
                    if (!fragmentTarget.isAdded()){
                        trx.add(container,fragmentTarget);
                    }
                    trx.setMaxLifecycle(fragmentTarget,maxLifecycles.get(index));
                    if (!fragmentTarget.isHidden()){
                        trx.hide(fragmentTarget).commitNowAllowingStateLoss();
                    }

                    fragmentManager.executePendingTransactions();
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker show(int i, Lifecycle.State maxLifecycle) {
        try {
            FragmentTransaction trx = fragmentManager.beginTransaction();
            Fragment fragmentTarget=fragments.get(i);
            String tag=fragmentTarget.getTag();
            Fragment f_cache=fragmentManager.findFragmentByTag(tag);
            if (f_cache!=null){
                fragmentTarget=f_cache;
            }
            if(fragmentTarget!=null ){
                if (!fragmentTarget.isAdded()){
                    trx.add(container,fragmentTarget);
                }
                if (!fragmentTarget.isVisible()){
                    trx.setMaxLifecycle(fragmentTarget,maxLifecycle);
                    trx.show(fragmentTarget).commitNowAllowingStateLoss();
                }
            }
            fragmentManager.executePendingTransactions();

        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker showByTag(String tag, Lifecycle.State maxLifecycle) {

        try {
            FragmentTransaction trx = fragmentManager.beginTransaction();
            Fragment fragmentTarget=null;
            for (Fragment ff:fragments
                 ) {
                if (tag.equals(ff.getTag())){
                    fragmentTarget=ff;
                    break;
                }
            }
            Fragment f_cache=fragmentManager.findFragmentByTag(tag);
            if (f_cache!=null){
                fragmentTarget=f_cache;
            }
            if(fragmentTarget!=null ){
                if (!fragmentTarget.isAdded()){
                    trx.add(container,fragmentTarget);
                }
                if (!fragmentTarget.isVisible()){
                    trx.setMaxLifecycle(fragmentTarget,maxLifecycle);
                    trx.show(fragmentTarget).commitNowAllowingStateLoss();
                }
            }
            fragmentManager.executePendingTransactions();

        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker hide(int i, Lifecycle.State maxLifecycle) {

        try {
            FragmentTransaction trx = fragmentManager.beginTransaction();
            Fragment fragmentTarget=fragments.get(i);
            String tag=fragmentTarget.getTag();
            Fragment f_cache=fragmentManager.findFragmentByTag(tag);
            if (f_cache!=null){
                fragmentTarget=f_cache;
            }
            if(fragmentTarget!=null ){
                if (!fragmentTarget.isAdded()){
                    trx.add(container,fragmentTarget);
                }
                if (!fragmentTarget.isHidden()){
                    trx.setMaxLifecycle(fragmentTarget,maxLifecycle);
                    trx.hide(fragmentTarget).commitNowAllowingStateLoss();
                }
            }
            fragmentManager.executePendingTransactions();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public FragmentMaster.AsViewAddToContainerInvoker hideByTag(String tag, Lifecycle.State maxLifecycle) {
        try {
            FragmentTransaction trx = fragmentManager.beginTransaction();
            Fragment fragmentTarget=null;
            for (Fragment ff:fragments
            ) {
                if (tag.equals(ff.getTag())){
                    fragmentTarget=ff;
                    break;
                }
            }
            Fragment f_cache=fragmentManager.findFragmentByTag(tag);
            if (f_cache!=null){
                fragmentTarget=f_cache;
            }
            if(fragmentTarget!=null ){
                if (!fragmentTarget.isAdded()){
                    trx.add(container,fragmentTarget);
                }
                if (!fragmentTarget.isHidden()){
                    trx.setMaxLifecycle(fragmentTarget,maxLifecycle);
                    trx.hide(fragmentTarget).commitNowAllowingStateLoss();
                }

            }
            fragmentManager.executePendingTransactions();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }
}
