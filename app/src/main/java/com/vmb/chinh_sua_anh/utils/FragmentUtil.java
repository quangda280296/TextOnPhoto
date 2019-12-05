package com.vmb.chinh_sua_anh.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentUtil {

    private static FragmentUtil fragmentHandler;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    public static FragmentUtil getInstance() {
        if (fragmentHandler == null) {
            synchronized (FragmentUtil.class) {
                fragmentHandler = new FragmentUtil();
            }
        }
        return fragmentHandler;
    }

    public FragmentManager getManager() {
        return manager;
    }

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public FragmentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(FragmentTransaction transaction) {
        this.transaction = transaction;
    }

    public int countBackstack() {
        return manager.getBackStackEntryCount();
    }

    public void popBackstack() {
        manager.popBackStack();
    }

    public Fragment findFragmentByTAG(String TAG) {
        return manager.findFragmentByTag(TAG);
    }

    public void add(Fragment fragment, String name) {
        transaction = manager.beginTransaction();
        transaction.add(fragment, name);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public void add(int id, Fragment fragment, String name) {
        transaction = manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(id, fragment, name);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public void replace(int id, Fragment fragment, String name) {
        transaction = manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(id, fragment, name);
        transaction.addToBackStack(name);
        transaction.commit();
    }
}