package com.iut.guarssif3u.boutique;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        method = (String) this.getIntent().getExtras().get("method");

        btnOk.setOnClickListener(this);
        if(method.equals(HTTPRequestMethod.PUT)) {
            Categorie categorie = (Categorie) this.getIntent().getExtras().get("categorie");
            if(categorie != null) {
                lblCategorie.setText("Modification catégorie");
                editNom.setText(categorie.getNom());
                editVisuel.setText(categorie.getVisuel());
                btnOk.setText("Modifier");
                newCategorie = categorie;
            }
        }

    }

    public void ajouterCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        if(nom.length() != 0 && visuel.length() != 0) {
            // ajout catégorie
            Categorie newCategorie = new Categorie(nom, visuel);
            CategorieDAO.getInstance(this).insert(newCategorie);
            Toast.makeText(this,"Ajout", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrect !", Toast.LENGTH_LONG).show();
        }
    }

    public void editCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        if(nom.length() != 0 && visuel.length() != 0) {
            if(newCategorie.getNom() != nom || newCategorie.getVisuel() != visuel) {
                // modification catégorie
                Categorie newCategorie = new Categorie(nom, visuel);
                CategorieDAO.getInstance(this).update(newCategorie);
                Toast.makeText(this,"Modification", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrect !", Toast.LENGTH_LONG).show();
        }
    }

    public void retour(View view) {
        this.finish();
    }

    @Override
    public void onClick(View view) {
        if(method == HTTPRequestMethod.POST) {
            ajouterCategorie();
        } else if(method == HTTPRequestMethod.PUT) {
            editCategorie();
        }
    }

    @Override
    public void afficheLoader() {

    }

    @Override
    public void cacheLoaderAfficheContenu() {

    }

    @Override
    public void notifyRetourRequete(Object resultat, String method, String error) {

    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {

    }

}
