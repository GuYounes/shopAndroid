package com.iut.guarssif3u.boutique;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    protected TextView lblCategorie;
    protected Button btnOk;
    protected Button btnRetour;
    protected EditText editNom;
    protected EditText editVisuel;
    protected ProgressBar loader;

    private String method;
    private Categorie newCategorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categorie);
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
            Categorie categorie = (Categorie) this.getIntent().getExtras().get("categorie");
            if(categorie != null) {
                lblCategorie.setText(R.string.modif_categorie);
                editNom.setText(categorie.getNom());
                editVisuel.setText(categorie.getVisuel());
                btnOk.setText(R.string.modifier);
                newCategorie = categorie;
            }
        }

    }

    public void ajouterCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        try {
            // ajout catégorie
            Categorie newCategorie = new Categorie(nom, visuel);
            CategorieDAO.getInstance(this).insert(newCategorie);
            this.afficheLoader();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        if(!newCategorie.getNom().equals(nom) || !newCategorie.getVisuel().equals(visuel)) {
            try{
                // modification catégorie
                Categorie newCategorie = new Categorie(this.newCategorie.getId(), nom, visuel);
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
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnOkCategorie):
                if(method.equals(HTTPRequestMethod.POST)) ajouterCategorie();
                if(method.equals(HTTPRequestMethod.PUT)) editCategorie();
                break;
            case (R.id.btnRetour):
                this.finish();
                break;
        }

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
    public void notifyRetourRequete(Object resultat, String method, boolean error) {
        this.cacheLoaderAfficheContenu();
        if(error){
            Toast.makeText(this, R.string.erreur_serveur, Toast.LENGTH_LONG).show();
            return;
        }
        switch (method){
            case HTTPRequestMethod.POST:
                Toast.makeText(this, R.string.ajout_ok, Toast.LENGTH_LONG).show();
                this.editNom.setText("");
                this.editVisuel.setText("");
                break;
            case HTTPRequestMethod.PUT:
                Toast.makeText(this, R.string.modif_ok, Toast.LENGTH_LONG).show();
                this.finish();
                break;
        }
    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {}

}
