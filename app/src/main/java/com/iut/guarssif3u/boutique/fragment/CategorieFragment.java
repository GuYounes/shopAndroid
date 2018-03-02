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

    protected FloatingActionButton btnRefresh;

    BoutiqueActivity activity;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_categorie, null);

        this.categories = new ArrayList<>();
        this.activity = (BoutiqueActivity)this.getActivity();

        try {
            this.substitut = Drawable.createFromStream(getActivity().getAssets().open("cintre.png"), null);
        } catch (IOException e){}

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addCategorieBtn = view.findViewById(R.id.add);
        addCategorieBtn.setOnClickListener(this);

        this.listView = view.findViewById(R.id.liste);
        this.loader = view.findViewById(R.id.loader);

        this.btnRefresh = view.findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(this);

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

        // Si nous étions sur ce fragment avant de lancer une autre activité, nous reviendrons sur ce fragment, au bon niveau de scroll
        if(this.activity.getCurrentFragment() == this.activity.getViewPagerAdapter().getItemPosition(this)){
            this.listView.setSelectionFromTop(this.activity.getCurrentItemPosition(), this.activity.getCurrentTopPosition());
        }
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
    public void afficheLoaderListe() {

    }

    @Override
    public void cacheLoaderAfficheListe() {

    }

    @Override
    public void supprimer(Categorie object) {
        this.targetCategorie = object;
        try{
            SuppressionDialog dialog = SuppressionDialog.newInstance(((BoutiqueActivity)getActivity()).getViewPagerAdapter().getItemPosition(this), getActivity().getString(R.string.supp_categorie_titre), getActivity().getString(R.string.supp_categorie_message));
            dialog.show(getActivity().getFragmentManager(), "suppression");
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Ouvre une nouvelle activité
     */
    @Override
    public void ajouter() {
        this.saveFragmentAndPosition();

        Intent activityLauncher = new Intent(this.getActivity(), ManageCategorieActivity.class);
        activityLauncher.putExtra("method", HTTPRequestMethod.POST);
        startActivity(activityLauncher);
    }

    /**
     * Appelé dans CategorieAdapter
     *
     * @param object
     */
    @Override
    public void modifier(Categorie object) {
        this.saveFragmentAndPosition();

        Intent activityLauncher = new Intent(getContext(), ManageCategorieActivity.class);
        activityLauncher.putExtra("categorie", object);
        activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
        this.getActivity().startActivity(activityLauncher);
    }

    @Override
    public void recuperer(int id) {}

    /**
     * Appelé sur le click du FloatingActionButton
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh) {
            saveFragmentAndPosition();
            activity.recreate();
        } else {
            this.ajouter();
        }
    }

    /**
     * Appelé sur le click de la popup lors de la suppression d'une categorie
     *
     * @param dialog
     * @param i
     */
    @Override
    public void onClick(DialogInterface dialog, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            CategorieDAO.getInstance(this).delete(this.targetCategorie);
        } else {
            return;
        }
    }

    /**
     * Sauvegarde le fragment sur lequel nous sommes, et la position du scroll
     */
    protected void saveFragmentAndPosition(){
        int index = this.listView.getFirstVisiblePosition();
        View v = this.listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - this.listView.getPaddingTop());

        this.activity.saveFragmentAndPosition(this.activity.getViewPagerAdapter().getItemPosition(this), index, top);
    }

}
