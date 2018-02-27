package com.iut.guarssif3u.boutique;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Article;

import java.util.ArrayList;
import java.util.List;

public class ManageArticleActivity extends AppCompatActivity implements ActiviteEnAttenteAvecResultat, View.OnClickListener {

    protected TextView lblArticle;
    protected Button btnOk;
    protected Button btnRetour;
    protected EditText editReference;
    protected EditText editTarif;
    protected EditText editNom;
    protected EditText editVisuel;
    protected Spinner spinnerCategorie;
    private String method;
    private Article newArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_article);
    }

    @Override
    protected void onStart() {
        super.onStart();

        lblArticle = this.findViewById(R.id.labelArticle);
        btnOk = this.findViewById(R.id.btnOkCategorie);
        btnRetour = this.findViewById(R.id.btnRetour);
        editNom = this.findViewById(R.id.editNomCategorie);
        editVisuel = this.findViewById(R.id.editNomVisuel);
        editTarif = this.findViewById(R.id.editTarif);
        editReference = this.findViewById(R.id.editReferenceArticle);
        spinnerCategorie = this.findViewById(R.id.categorieSpinner);

        CategorieDAO categorieDAO = CategorieDAO.getInstance(this);
        categorieDAO.findAll();

        method = (String) this.getIntent().getExtras().get("method");

        btnOk.setOnClickListener(this);
        if(method.equals(HTTPRequestMethod.PUT)) {
            Article article = (Article) this.getIntent().getExtras().get("article");
            if(article != null) {
                lblArticle.setText("Modification article");
                editNom.setText(article.getNom());
                editTarif.setText(String.valueOf(article.getTarif()));
                editReference.setText(article.getReference());
                editVisuel.setText(article.getVisuel());
                btnOk.setText("Modifier");
                newArticle = article;
            }
        }

    }

    public void ajouterArticle() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();
        Float tarif = Float.valueOf(editTarif.getText().toString());
        String reference = editReference.getText().toString();

        if(nom.length() != 0 && visuel.length() != 0 && tarif > 0 && reference.length() != 0) {
            // ajout article
            Article newArticle = new Article(reference, nom, tarif, visuel);
            ArticleDAO.getInstance(this).insert(newArticle);
            Toast.makeText(this,"Ajout", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrect !", Toast.LENGTH_LONG).show();
        }
    }

    public void editArticle() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();
        Float tarif = Float.valueOf(editTarif.getText().toString());
        String reference = editReference.getText().toString();

        if(nom.length() != 0 && visuel.length() != 0 && tarif > 0 && reference.length() != 0) {
            if(newArticle.getNom() != nom || newArticle.getVisuel() != visuel || newArticle.getTarif() != tarif || newArticle.getReference() != reference) {
                // modification article
                Article newArticle = new Article(reference, nom, tarif, visuel);
                ArticleDAO.getInstance(this).update(newArticle);
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
            ajouterArticle();
        } else if(method == HTTPRequestMethod.PUT) {
            editArticle();
        }
    }

    @Override
    public void afficheLoader() {

    }

    @Override
    public void cacheLoaderAfficheContenu() {

    }

    @Override
    public void notifyRetourRequete(Object resultat) {

    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {

    }

}