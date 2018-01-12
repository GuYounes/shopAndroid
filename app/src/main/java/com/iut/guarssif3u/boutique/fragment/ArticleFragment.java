package com.iut.guarssif3u.boutique.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.modele.metier.Article;

import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class ArticleFragment extends Fragment {

    protected ArrayList<Article> liste;

    public ArticleFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_article, null);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        liste = new ArrayList<Article>();
    }
}
