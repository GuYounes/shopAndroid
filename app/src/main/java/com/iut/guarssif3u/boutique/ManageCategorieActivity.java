package com.iut.guarssif3u.boutique;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.DAO.DAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.util.ArrayList;

public class ManageCategorieActivity extends AppCompatActivity implements ActiviteEnAttenteAvecResultat, View.OnClickListener {

    public static final int OK = 0;
    public static final int MODIFIE = 1;
    public static final int NON_MODIFIE = 2;

    protected TextView lblCategorie;
    protected Button btnOk;
    protected Button btnRetour;
    protected EditText editNom;
    protected EditText editVisuel;
    protected ProgressBar loader;

    private String method;
    private Categorie currentCategorie;
    private Categorie newCategorie;
    private ArrayList<Categorie> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categorie);

        this.categories = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        lblCategorie = this.findViewById(R.id.labelCategorie);
        btnOk = this.findViewById(R.id.btnOkCategorie);
        btnRetour = this.findViewById(R.id.btnRetour);
        editNom = this.findViewById(R.id.editNomCategorie);
        editVisuel = this.findViewById(R.id.editNomVisuel);
        loader = this.findViewById(R.id.loader);

        method = (String) this.getIntent().getExtras().get("method");

        btnOk.setOnClickListener(this);
        btnRetour.setOnClickListener(this);

        if(method.equals(HTTPRequestMethod.PUT)) {
            this.currentCategorie= (Categorie) this.getIntent().getExtras().get("categorie");
            if(currentCategorie != null) {
                lblCategorie.setText(R.string.modif_categorie);
                editNom.setText(currentCategorie.getNom());
                editVisuel.setText(currentCategorie.getVisuel());
                btnOk.setText(R.string.modifier);
                newCategorie = currentCategorie;
            }
        }

    }

    public void ajouterCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        try {
            // ajout catégorie
            this.newCategorie = new Categorie(nom, visuel);
            CategorieDAO.getInstance(this).insert(newCategorie);
            this.afficheLoader();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        if(!this.currentCategorie.getNom().equals(nom) || !currentCategorie.getVisuel().equals(visuel)) {
            try{
                // modification catégorie
                this.newCategorie = new Categorie(this.newCategorie.getId(), nom, visuel);
                CategorieDAO.getInstance(this).update(newCategorie);
                this.afficheLoader();
            } catch (IllegalArgumentException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.champs_non_changé, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList("categories", this.categories);
        savedInstanceState.putParcelable("categorie", this.newCategorie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.categories = (ArrayList<Categorie>) savedInstanceState.getSerializable("categories");
        this.newCategorie = (Categorie)savedInstanceState.getSerializable("categorie");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnOkCategorie):
                this.btnRetour.setEnabled(false);
                if(method.equals(HTTPRequestMethod.POST)) ajouterCategorie();
                if(method.equals(HTTPRequestMethod.PUT)) editCategorie();
                break;
            case (R.id.btnRetour):
                if(this.method.equals(HTTPRequestMethod.POST)){
                    Intent donnees = new Intent();
                    donnees.putParcelableArrayListExtra("categories", this.categories);
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
            donnees.putParcelableArrayListExtra("categories", this.categories);
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
    }

    @Override
    public void cacheLoaderAfficheContenu() {
        this.loader.setVisibility(View.GONE);
        this.btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void afficheLoaderListe() {

    }

    @Override
    public void cacheLoaderAfficheListe() {

    }

    @Override
    public void notifyRetourRequete(Object resultat, String method, boolean error) {
        this.btnRetour.setEnabled(true);
        if(error){
            this.cacheLoaderAfficheContenu();
            Toast.makeText(this, R.string.erreur_serveur, Toast.LENGTH_LONG).show();
            return;
        }
        switch (method){
            case HTTPRequestMethod.POST:
                this.cacheLoaderAfficheContenu();
                Toast.makeText(this, R.string.ajout_ok, Toast.LENGTH_LONG).show();
                this.categories.add((Categorie)resultat);
                this.editNom.setText("");
                this.editVisuel.setText("");
                break;
            case HTTPRequestMethod.PUT:
                Intent donnees = new Intent();
                donnees.putExtra("categorie", this.newCategorie);
                this.setResult(MODIFIE, donnees);
                this.finish();
                break;
        }
    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {}

}
