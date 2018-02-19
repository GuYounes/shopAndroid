package com.iut.guarssif3u.boutique.fragment;

import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.ArticleAdapter;
import com.iut.guarssif3u.boutique.modele.metier.Article;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class ArticleFragment extends Fragment implements ActiviteEnAttenteAvecResultat<Article>, View.OnClickListener {

    protected ArrayList<Article> articles;

    protected ListView listView;
    protected Drawable substitut;
    protected ProgressBar loader;
    protected FloatingActionButton addArticleBtn;

    public ArticleFragment(){};

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        this.articles = new ArrayList<>();

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_article, null);

        addArticleBtn = view.findViewById(R.id.addArticle);
        addArticleBtn.setOnClickListener(this);

        try {
            this.substitut = Drawable.createFromStream(getActivity().getAssets().open("substitut.png"), null);
        } catch (IOException e){}

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.listView = view.findViewById(R.id.liste);
        this.loader = view.findViewById(R.id.loader);

        ArticleAdapter articleAdapter = new ArticleAdapter(getActivity(), articles, substitut);
        this.listView.setAdapter(articleAdapter);

        ArticleDAO.getInstance(this).findAll();
        this.afficheLoader();
    }

    @Override
    public void notifyRetourRequete(Article result){

    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList<Article> liste){
        this.articles.clear();
        this.articles.addAll(liste);

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
        this.cacheLoaderAfficheContenu();
    }

    @Override
    public void afficheLoader(){
        this.loader.setVisibility(View.VISIBLE);
        this.listView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void cacheLoaderAfficheContenu() {
        this.loader.setVisibility(View.INVISIBLE);
        this.listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

    }

}
