package com.iut.guarssif3u.boutique.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.CategorieAdapter;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieFragment extends Fragment implements ActiviteEnAttenteAvecResultat<Categorie> {

    protected ArrayList<Categorie> categories;

    protected ListView listView;
    protected Drawable substitut;
    protected ProgressBar loader;

    public CategorieFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_categorie, null);

        try {
            this.substitut = Drawable.createFromStream(getActivity().getAssets().open("cintre.png"), null);
        } catch (IOException e){}

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Bundle args = this.getArguments();
        //this.categories = args.getParcelableArrayList("categories");
        this.categories = new ArrayList<>();
    }

    @Override
    public void onStart(){
        super.onStart();

        this.listView = getActivity().findViewById(R.id.liste);
        this.loader = getActivity().findViewById(R.id.loader);

        CategorieAdapter categorieAdapter = new CategorieAdapter(getActivity(), categories, substitut);
        this.listView.setAdapter(categorieAdapter);

        CategorieDAO.getInstance(this, this.loader, this.listView).findAll();
    }

    @Override
    public void notifyRetourRequete(Categorie result){

    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList<Categorie> liste){
        this.categories.clear();
        this.categories.addAll(liste);

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
        //this.cacheLoaderAfficheContenu();
    }

    @Override
    public void afficheLoader(){
        getActivity().findViewById(R.id.loader).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.liste).setVisibility(View.INVISIBLE);
    }

    @Override
    public void cacheLoaderAfficheContenu() {
        getActivity().findViewById(R.id.loader).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.liste).setVisibility(View.VISIBLE);
    }

}
