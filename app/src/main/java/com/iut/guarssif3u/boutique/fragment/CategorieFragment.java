package com.iut.guarssif3u.boutique.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.iut.guarssif3u.boutique.BoutiqueActivity;
import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.CategorieAdapter;
import com.iut.guarssif3u.boutique.ManageCategorieActivity;
import com.iut.guarssif3u.boutique.dialog.SuppressionDialog;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieFragment extends Fragment implements ActiviteEnAttenteAvecResultat<Categorie>, ObjetMetier<Categorie>, View.OnClickListener, DialogInterface.OnClickListener {

    protected ArrayList<Categorie> categories;
    protected Categorie targetCategorie;

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

        CategorieAdapter categorieAdapter = new CategorieAdapter(getActivity(), this, categories, substitut);
        this.listView.setAdapter(categorieAdapter);
        CategorieDAO.getInstance(this).findAll();
        this.afficheLoader();
    }

    @Override
    public void notifyRetourRequete(Categorie result, String method, boolean error){
        if(error){
            Toast.makeText(getContext(), R.string.erreur_serveur, Toast.LENGTH_LONG).show();
            return;
        }
        switch (method){
            case HTTPRequestMethod.DELETE:
                this.categories.remove(this.targetCategorie);
                ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(getContext(), R.string.supp_ok, Toast.LENGTH_LONG).show();
        }
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
    public void supprimer(Categorie object) {
        this.targetCategorie = object;
        try{
            SuppressionDialog dialog = SuppressionDialog.newInstance(((BoutiqueActivity)getActivity()).adapter.getItemPosition(this));
            dialog.show(getActivity().getFragmentManager(), "suppression");
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ajouter(Categorie object) {
        CategorieDAO.getInstance(this).insert(this.targetCategorie);
    }

    @Override
    public void modifier(Categorie object) {
        Intent activityLauncher = new Intent(getContext(), ManageCategorieActivity.class);
        activityLauncher.putExtra("categorie", object);
        activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
        this.getActivity().startActivity(activityLauncher);
    }

    @Override
    public void recuperer(int id) {

    }

    @Override
    public void onClick(View view) {
        Intent activityLauncher = new Intent(this.getActivity(), ManageCategorieActivity.class);
        activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
        startActivity(activityLauncher);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            CategorieDAO.getInstance(this).delete(this.targetCategorie);
        } else {
            return;
        }
    }

}
