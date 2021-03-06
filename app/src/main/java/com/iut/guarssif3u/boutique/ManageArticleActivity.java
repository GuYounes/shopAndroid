package com.iut.guarssif3u.boutique;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.async.HTTPRequest;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Article;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.util.ArrayList;
import java.util.List;

public class ManageArticleActivity extends AppCompatActivity implements ActiviteEnAttenteAvecResultat, View.OnClickListener {

    public static final int OK = 0;
    public static final int MODIFIE = 1;
    public static final int NON_MODIFIE = 2;

    protected TextView lblArticle;

    protected Button btnOk;
    protected Button btnRetour;

    protected EditText editReference;
    protected EditText editTarif;
    protected EditText editNom;
    protected EditText editVisuel;
    protected ProgressBar loader;
    protected ProgressBar loaderList;
    protected Spinner spinnerCategorie;

    protected ArrayList categories;

    private String method;
    private Article newArticle;
    private Article currentArticle;
    private ArrayList<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            categories = savedInstanceState.getStringArrayList("categoriesArray");
            this.newArticle = (Article)savedInstanceState.getSerializable("article");
        } else {
            categories = new ArrayList();
        }

        this.articles = new ArrayList<>();
        setContentView(R.layout.activity_manage_article);
    }

    @Override
    protected void onStart() {
        super.onStart();

        lblArticle = this.findViewById(R.id.labelArticle);
        btnOk = this.findViewById(R.id.btnOkArticle);
        btnRetour = this.findViewById(R.id.btnRetour);
        editNom = this.findViewById(R.id.editNomArticle);
        editVisuel = this.findViewById(R.id.editNomVisuel);
        editTarif = this.findViewById(R.id.editTarif);
        editReference = this.findViewById(R.id.editReferenceArticle);
        spinnerCategorie = this.findViewById(R.id.categorieSpinner);
        loader = this.findViewById(R.id.loader);
        loaderList = this.findViewById(R.id.loaderlist);

        method = (String) this.getIntent().getExtras().get("method");


        // request to get spinner items
        if(categories.isEmpty()) {
            this.afficheLoaderListe();
            CategorieDAO categorieDAO = CategorieDAO.getInstance(this);
            categorieDAO.findAll();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorie.setAdapter(adapter);
            this.cacheLoaderAfficheListe();
        }

        btnOk.setOnClickListener(this);
        btnRetour.setOnClickListener(this);

        if(method.equals(HTTPRequestMethod.PUT)) {
            this.currentArticle = (Article) this.getIntent().getExtras().get("article");
            if(currentArticle != null) {
                lblArticle.setText(R.string.modif_article);
                editNom.setText(currentArticle.getNom());
                editTarif.setText(String.valueOf(currentArticle.getTarif()));
                editReference.setText(currentArticle.getReference());
                editVisuel.setText(currentArticle.getVisuel());
                btnOk.setText(R.string.modifier);
                newArticle = currentArticle;
            }
        }

    }

    public void ajouterArticle() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();
        Float tarif = null;
        String reference = editReference.getText().toString();
        Categorie categorie = (Categorie) spinnerCategorie.getSelectedItem();

        if(!editTarif.getText().toString().equals("")) {
            tarif = Float.valueOf(editTarif.getText().toString());
        } else {
            tarif = 0.0f;
        }
        try{
            // ajout article
            Article newArticle = new Article(reference, nom, tarif, visuel, categorie);
            ArticleDAO.getInstance(this).insert(newArticle);
            this.btnRetour.setEnabled(false);
            this.afficheLoader();
        } catch (IllegalArgumentException e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void editArticle() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();
        Float tarif = null;
        String reference = editReference.getText().toString();
        Categorie categorie = (Categorie) spinnerCategorie.getSelectedItem();

        if(!editTarif.getText().toString().equals("")) {
            tarif = Float.valueOf(editTarif.getText().toString());
        } else {
            tarif = 0.0f;
        }

        if(newArticle.getNom() != nom || newArticle.getVisuel() != visuel || newArticle.getTarif() != tarif || newArticle.getReference() != reference || newArticle.getCategorie() != categorie) {
            try{
                // modification article
                this.newArticle = new Article(this.newArticle.getId(), reference, nom, tarif, visuel, categorie);
                ArticleDAO.getInstance(this).update(newArticle);
                this.afficheLoader();
            } catch (IllegalArgumentException e) {
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.champs_non_changé, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList("articles", this.articles);
        savedInstanceState.putParcelable("article", this.newArticle);
        savedInstanceState.putStringArrayList("categoriesArray", categories);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.articles = (ArrayList<Article>) savedInstanceState.getSerializable("articles");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnOkArticle):
                if(method.equals(HTTPRequestMethod.POST)) ajouterArticle();
                if(method.equals(HTTPRequestMethod.PUT)) editArticle();
                break;
            case (R.id.btnRetour):
                if(this.method.equals(HTTPRequestMethod.POST)){
                    Intent donnees = new Intent();
                    donnees.putParcelableArrayListExtra("articles", this.articles);
                    this.setResult(OK, donnees);
                } else {
                    this.setResult(NON_MODIFIE);
                }
                this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(this.method.equals(HTTPRequestMethod.POST)){
            Intent donnees = new Intent();
            donnees.putParcelableArrayListExtra("articles", this.articles);
            this.setResult(OK, donnees);
        } else {
            this.setResult(NON_MODIFIE);
        }
        this.finish();
    }

    @Override
    public void afficheLoader() {
        this.btnOk.setVisibility(View.GONE);
        this.loader.setVisibility(View.VISIBLE);
        this.btnRetour.setEnabled(false);
    }

    @Override
    public void cacheLoaderAfficheContenu() {
        this.loader.setVisibility(View.GONE);
        this.btnOk.setVisibility(View.VISIBLE);
        this.btnRetour.setEnabled(true);
    }

    @Override
    public void afficheLoaderListe() {
        this.spinnerCategorie.setVisibility(View.INVISIBLE);
        this.loaderList.setVisibility(View.VISIBLE);
        btnOk.setEnabled(false);
    }

    @Override
    public void cacheLoaderAfficheListe() {
        this.spinnerCategorie.setVisibility(View.VISIBLE);
        this.loaderList.setVisibility(View.INVISIBLE);
        btnOk.setEnabled(true);
    }

    @Override
    public void notifyRetourRequete(Object resultat, String method, boolean error) {
        if(error){
            this.cacheLoaderAfficheContenu();
            Toast.makeText(this, R.string.erreur_serveur, Toast.LENGTH_LONG).show();
            return;
        }
        switch (method){
            case HTTPRequestMethod.POST:
                this.cacheLoaderAfficheContenu();
                Toast.makeText(this, R.string.ajout_ok, Toast.LENGTH_LONG).show();
                this.articles.add((Article)resultat);
                this.editNom.setText("");
                this.editVisuel.setText("");
                this.editReference.setText("");
                this.editTarif.setText("");
                this.spinnerCategorie.setSelection(0, false);
                break;
            case HTTPRequestMethod.PUT:
                Intent donnees = new Intent();
                donnees.putExtra("article", this.newArticle);
                this.setResult(MODIFIE, donnees);
                this.finish();
                break;
        }
        this.btnRetour.setEnabled(true);
    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {
        this.cacheLoaderAfficheListe();
        this.categories.clear();
        this.categories.addAll(list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);

        if(this.method.equals(HTTPRequestMethod.PUT)){
            int spinnerPosition = list.indexOf(newArticle.getCategorie());
            spinnerCategorie.setSelection(spinnerPosition, false);
        }
    }

}
