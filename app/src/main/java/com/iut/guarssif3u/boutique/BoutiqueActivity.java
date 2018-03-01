package com.iut.guarssif3u.boutique;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.iut.guarssif3u.boutique.adapter.ViewPagerAdapter;
import com.iut.guarssif3u.boutique.fragment.ArticleFragment;
import com.iut.guarssif3u.boutique.fragment.CategorieFragment;
import com.iut.guarssif3u.boutique.fragment.PromotionFragment;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BoutiqueActivity extends AppCompatActivity {

    protected ViewPager viewPager;
    protected TabLayout tabLayout;
    protected Bundle savedInstanceState;

    protected ViewPagerAdapter adapter;

    protected int currentFragmentPosition = -1;
    protected int currentItemPosition;
    protected int currentTopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.viewPager = this.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        this.savedInstanceState = savedInstanceState;
        this.adapter = new ViewPagerAdapter(getSupportFragmentManager());

        this.getSupportFragmentManager().getFragments();

        if (savedInstanceState == null){
                this.setupViewPager();
            }else {
                Integer count = savedInstanceState.getInt("tabsCount");
                String[] titles = savedInstanceState.getStringArray("titles");
                for (int i = 0; i < count; i++) {
                    this.adapter.addFragment(getFragment(i), titles[i]);
                }
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        viewPager.setAdapter(adapter);

        this.tabLayout = this.findViewById(R.id.tabLayout);
        this.tabLayout.setupWithViewPager(viewPager);

        if(currentFragmentPosition != -1){
            viewPager.setCurrentItem(currentFragmentPosition);
        }
    }

    private Fragment getFragment(int position){
        return savedInstanceState == null ? adapter.getItem(position) : getSupportFragmentManager().findFragmentByTag(getFragmentTag(position));
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.viewPager + ":" + position;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabsCount", adapter.getCount());
        outState.putStringArray("titles", adapter.getFragmentTitles().toArray(new String[0]));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public ViewPager getViewPager(){
        return this.viewPager;
    }

    public ViewPagerAdapter getViewPagerAdapter(){
        return this.adapter;
    }

    public int getCurrentFragment(){
        return this.currentFragmentPosition;
    }

    public int getCurrentItemPosition(){
        return this.currentItemPosition;
    }

    public int getCurrentTopPosition(){
        return this.currentTopPosition;
    }

    public void setCurrentFragment(int fragmentPosition){
        this.currentFragmentPosition = fragmentPosition;
    }

    public void setCurrentItemPosition(int index){
        this.currentItemPosition = index;
    }

    public void setCurrentTopPosition(int top){
        this.currentTopPosition = top;
    }

    public void saveFragmentAndPosition(int fragmentPosition, int index, int top){
        this.setCurrentFragment(fragmentPosition);
        this.setCurrentItemPosition(index);
        this.setCurrentTopPosition(top);
    }

    private void setupViewPager() {
        this.adapter.addFragment(new CategorieFragment(), "Catégories");
        this.adapter.addFragment(new ArticleFragment(), "Articles");
        this.adapter.addFragment(new PromotionFragment(), "Promotions");
    }

}
