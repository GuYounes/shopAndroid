package com.iut.guarssif3u.boutique.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by younes on 12/01/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    protected final List<Fragment> fragmentList = new ArrayList<>();
    protected final List<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        for(int i = 0; i < fragmentList.size(); i++){
            if(fragmentList.get(i) == object) return i;
        }

        return -1;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    public List<String> getFragmentTitles(){
        return this.fragmentTitleList;
    }
}
