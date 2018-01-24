package com.iut.guarssif3u.boutique;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.iut.guarssif3u.boutique.adapter.ViewPagerAdapter;
import com.iut.guarssif3u.boutique.fragment.ArticleFragment;
import com.iut.guarssif3u.boutique.fragment.CategorieFragment;
import com.iut.guarssif3u.boutique.fragment.PromotionFragment;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.util.ArrayList;

public class BoutiqueActivity extends AppCompatActivity {

    private ArrayList<Categorie> categories;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListView listView;

    private Drawable substitut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_boutique);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.categories = new ArrayList<Categorie>();
        this.categories.add(new Categorie(1, "short", "short.png"));
        this.categories.add(new Categorie(2, "T-Shirt", "short.png"));
    }

    @Override
    public void onStart(){
        super.onStart();

        this.viewPager = this.findViewById(R.id.viewPager);
        this.setupViewPager(viewPager);

        this.tabLayout = this.findViewById(R.id.tabLayout);
        this.tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putParcelableArrayList("categories", this.categories);

        CategorieFragment categorieFragment = new CategorieFragment();
        categorieFragment.setArguments(args);

        adapter.addFragment(categorieFragment, "Catégories");
        adapter.addFragment(new ArticleFragment(), "Articles");
        adapter.addFragment(new PromotionFragment(), "Promotions");
        viewPager.setAdapter(adapter);
    }

}
