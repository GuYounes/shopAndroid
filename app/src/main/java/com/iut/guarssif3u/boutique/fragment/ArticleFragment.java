package com.iut.guarssif3u.boutique.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.BoutiqueActivity;
import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.ManageArticleActivity;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.ArticleAdapter;
import com.iut.guarssif3u.boutique.dialog.SuppressionDialog;
import com.iut.guarssif3u.boutique.modele.metier.Article;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class ArticleFragment extends Fragment implements ActiviteEnAttenteAvecResultat<Article>, View.OnClickListener, ObjetMetier<Article>, DialogInterface.OnClickListener {

    private static final int MODIFICATION = 0;
    private static final int CREATION = 1;
    private static final int HAUTEUR_BANDEAU = 225;


    protected ArrayList<Article> articles;
    protected ArrayList<Article> filteredArticles;
    protected Article targetArticle;
    protected Categorie categorie;

    protected ListView listView;
    protected Drawable substitut;
    protected ProgressBar loader;
    protected FloatingActionButton addArticleBtn;
    protected FloatingActionButton btnRefresh;
    protected ConstraintLayout bandeau;
    protected TextView filtre;
    protected ImageView croix;

    BoutiqueActivity activity;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        this.filteredArticles = new ArrayList<>();
        this.categorie = null;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_article, null);

        this.articles = new ArrayList<>();

        this.activity = (BoutiqueActivity)this.getActivity();

        try {
            this.substitut = Drawable.createFromStream(getActivity().getAssets().open("substitut.png"), null);
        } catch (IOException e){}

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addArticleBtn = view.findViewById(R.id.add);
        addArticleBtn.setOnClickListener(this);

        this.listView = view.findViewById(R.id.liste);
        this.loader = view.findViewById(R.id.loader);

        this.btnRefresh = view.findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(this);

        this.bandeau = view.findViewById(R.id.bandeau);
        this.filtre = view.findViewById(R.id.filtre);
        this.croix = view.findViewById(R.id.croix);
        this.croix.setOnClickListener(this);

        ArticleAdapter articleAdapter = new ArticleAdapter(getActivity(), this, this.filteredArticles, substitut);
        this.listView.setAdapter(articleAdapter);

        if(this.articles.isEmpty()){
            ArticleDAO.getInstance(this).findAll();
            this.afficheLoader();
        }

        if(this.categorie != null){
            this.afficherBandeau();
        }
    }

    @Override
    public void notifyRetourRequete(Article resultat, String method, boolean error) {
        try{
            if(error){
                Toast.makeText(getContext(), R.string.erreur_serveur, Toast.LENGTH_LONG).show();
                return;
            }
            switch (method){
                case HTTPRequestMethod.DELETE:
                    this.articles.remove(resultat);
                    this.filteredArticles.remove(resultat);
                    ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
                    Toast.makeText(getContext(), R.string.supp_ok, Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e){}
    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList<Article> liste){
        this.articles.clear();
        this.articles.addAll(liste);

        if(this.categorie != null){
            this.filtrerParCategorie(this.categorie);
        } else {
            this.filteredArticles.clear();
            this.filteredArticles.addAll(this.articles);
        }

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

    /**
     * Supprime un objet
     * Appelé dans ArticleAdapter
     *
     * @param object
     */
    @Override
    public void supprimer(Article object) {
        this.targetArticle = object;

        try{
            SuppressionDialog dialog = SuppressionDialog.newInstance(((BoutiqueActivity)getActivity()).getViewPagerAdapter().getItemPosition(this), getActivity().getString(R.string.supp_article_titre), getActivity().getString(R.string.supp_article_message));
            dialog.show(getActivity().getFragmentManager(), "suppression");
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Ouvre une nouvelle activité pour ajouter un nouvel article
     */
    @Override
    public void ajouter() {
        this.saveFragmentAndPosition();

        Intent activityLauncher = new Intent(this.getActivity(), ManageArticleActivity.class);
        activityLauncher.putExtra("method", HTTPRequestMethod.POST);
        startActivityForResult(activityLauncher, CREATION);
    }

    /**
     * Ouvre une nouvelle activité pour modifier un article
     * Appelé dans ArticleAdapter
     *
     * @param object
     */
    @Override
    public void modifier(Article object) {
        this.saveFragmentAndPosition();

        Intent activityLauncher = new Intent(getContext(), ManageArticleActivity.class);
        activityLauncher.putExtra("article", object);
        activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
        startActivityForResult(activityLauncher, MODIFICATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == ManageArticleActivity.OK ){
            ArrayList<Article> articles = data.getParcelableArrayListExtra("articles");
            for(Article article : articles){
                this.articles.add(article);
                if(article.getCategorie() == this.categorie){
                    this.filteredArticles.add(article);
                }
            }
            this.articles.addAll(articles);
            this.filteredArticles.addAll(articles);
        }
        if(resultCode == ManageArticleActivity.MODIFIE){
            Toast.makeText(getActivity().getApplicationContext(), R.string.modif_ok, Toast.LENGTH_LONG).show();
            Article article = data.getParcelableExtra("article");
            int index = this.articles.indexOf(article);
            this.articles.remove(index);
            this.articles.add(index, article);

            if(this.categorie != null) this.filtrerParCategorie(this.categorie);
        }

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void recuperer(int id) {}

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh) {
            ArticleDAO.getInstance(this).findAll();
            this.afficheLoader();
        }
        if(v.getId() == R.id.add){
            this.ajouter();
        }
        if(v.getId() == R.id.croix){
            this.cacherBandeau();
            this.filteredArticles.clear();
            this.filteredArticles.addAll(this.articles);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            ArticleDAO.getInstance(this).delete(this.targetArticle);
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

    public void filtrerParCategorie(Categorie categorie){
        this.filteredArticles.clear();
        this.categorie = categorie;
        this.afficherBandeau();
        for(Article article : this.articles){
            if(article.getCategorie().equals(categorie)) this.filteredArticles.add(article);
        }

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
    }

    protected void afficherBandeau(){
        this.bandeau.setVisibility(View.VISIBLE);
        this.filtre.setText(categorie.getNom());
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) this.listView.getLayoutParams();
        lp.setMargins(0, HAUTEUR_BANDEAU, 0, 0);
        this.listView.setLayoutParams(lp);
    }

    protected void cacherBandeau(){
        this.categorie = null;
        this.bandeau.setVisibility(View.INVISIBLE);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) this.listView.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        this.listView.setLayoutParams(lp);
    }
}
