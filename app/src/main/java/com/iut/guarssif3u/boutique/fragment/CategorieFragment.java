package com.iut.guarssif3u.boutique.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.CategorieAdapter;
import com.iut.guarssif3u.boutique.ManageCategorieActivity;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieFragment extends Fragment implements ActiviteEnAttenteAvecResultat<Categorie>, View.OnClickListener {

    protected ArrayList<Categorie> categories;

    protected ListView listView;
    protected Drawable substitut;
    protected ProgressBar loader;
    protected FloatingActionButton addCategorieBtn;

    public CategorieFragment(){};

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        this.categories = new ArrayList<>();

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_categorie, null);

        addCategorieBtn = view.findViewById(R.id.add);
        addCategorieBtn.setOnClickListener(this);

        try {
            this.substitut = Drawable.createFromStream(getActivity().getAssets().open("cintre.png"), null);
        } catch (IOException e){}

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.listView = view.findViewById(R.id.liste);
        this.loader = view.findViewById(R.id.loader);

        CategorieAdapter categorieAdapter = new CategorieAdapter(getActivity(), categories, substitut);
        this.listView.setAdapter(categorieAdapter);
        CategorieDAO.getInstance(this).findAll();
        this.afficheLoader();
    }

    @Override
    public void notifyRetourRequete(Categorie result){

    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList<Categorie> liste){
        this.categories.clear();
        this.categories.addAll(liste);

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
    public void onClick(View view) {
        Intent activityLauncher = new Intent(this.getActivity(), ManageCategorieActivity.class);
        activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
        startActivity(activityLauncher);
    }

}
