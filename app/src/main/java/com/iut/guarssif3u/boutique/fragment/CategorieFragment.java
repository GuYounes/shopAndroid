package com.iut.guarssif3u.boutique.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.BoutiqueActivity;
import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
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

    private static final int MODIFICATION = 0;
    private static final int CREATION = 1;

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

        this.categories = new ArrayList<>();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_categorie, null);

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

        if(this.categories.isEmpty()){
            CategorieDAO.getInstance(this).findAll();
            this.afficheLoader();
        }
    }

    @Override
    public void notifyRetourRequete(Categorie result, String method, boolean error){
        try{
            if(error){
                Toast.makeText(getContext(), R.string.erreur_serveur, Toast.LENGTH_LONG).show();
                return;
            }
            switch (method){
                case HTTPRequestMethod.DELETE:
                    this.categories.remove(result);
                    ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
                    Toast.makeText(getContext(), R.string.supp_ok, Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e){}

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
        startActivityForResult(activityLauncher, CREATION);
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
        startActivityForResult(activityLauncher, MODIFICATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == ManageCategorieActivity.OK ){
            ArrayList<Categorie> categories = data.getParcelableArrayListExtra("categories");
            for(Categorie categorie : categories) {
                this.categories.add(categorie);
            }
        }
        if(resultCode == ManageCategorieActivity.MODIFIE){
            Toast.makeText(getActivity().getApplicationContext(), R.string.modif_ok, Toast.LENGTH_LONG).show();
            Categorie categorie = data.getParcelableExtra("categorie");
            int index = this.categories.indexOf(categorie);
            this.categories.remove(index);
            this.categories.add(index, categorie);
        }

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void recuperer(int id) {}

    /**
     * Appelé sur le click des FloatingActionButtons
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh) {
            CategorieDAO.getInstance(this).findAll();
            this.afficheLoader();
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
            Toast.makeText(getActivity().getApplicationContext(), R.string.supp_en_cours, Toast.LENGTH_SHORT).show();
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

    public void filtrerArticleParCategorie(Categorie categorie){
        ArticleFragment fragment = (ArticleFragment)this.activity.getViewPagerAdapter().getItem(1);
        fragment.filtrerParCategorie(categorie);
        this.activity.getViewPager().setCurrentItem(1, true);
    }

}
