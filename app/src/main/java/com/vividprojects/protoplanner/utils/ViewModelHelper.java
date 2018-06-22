package com.vividprojects.protoplanner.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.vividprojects.protoplanner.viewmodel.ViewModelHolder;

public class ViewModelHelper {
    public static <T extends ViewModel> T obtainViewModel(Class<T> viewModelClass, FragmentManager fragmentManager, ViewModelProvider.Factory factory, AppCompatActivity activity) {
        ViewModelHolder<T> viewModelHolder = (ViewModelHolder<T>)fragmentManager.findFragmentByTag(ViewModelHolder.TAG);
        if (viewModelHolder != null && viewModelHolder.getViewModel() != null) {
            return viewModelHolder.getViewModel();
        } else {
            T model = ViewModelProviders.of(activity, factory).get(viewModelClass);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment fff = ViewModelHolder.createContainer(model);
            ft.add(fff,ViewModelHolder.TAG).commit();
            return model;
        }
    }
}
