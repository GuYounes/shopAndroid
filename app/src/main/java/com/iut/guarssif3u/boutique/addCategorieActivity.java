package com.iut.guarssif3u.boutique;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.modele.metier.Categorie;

public class addCategorieActivity extends AppCompatActivity {

    protected TextView lblCategorie;
    protected Button btnOk;
    protected Button btnRetour;
    protected EditText editNom;
    protected EditText editVisuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categorie);
    }

    @Override
    protected void onStart() {
        super.onStart();

        lblCategorie = (TextView) this.findViewById(R.id.labelCategorie);
        btnOk = (Button) this.findViewById(R.id.btnOkCategorie);
        btnRetour = (Button) this.findViewById(R.id.btnRetour);
        editNom = (EditText) this.findViewById(R.id.editNomCategorie);
        editVisuel = (EditText) this.findViewById(R.id.editNomVisuel);

        Categorie categorie = (Categorie) this.getIntent().getExtras().get("categorie");
        if(categorie != null) {
            lblCategorie.setText("Modification catégorie");
            btnOk.setText("Modifier");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editCategorie();
                }
            });
        }
    }

    public void ajouterCategorie(View view) {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        if(nom.length() != 0 && visuel.length() != 0) {
            // ajout catégorie
            Toast.makeText(this,"Ajout", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrect !", Toast.LENGTH_LONG).show();
        }
    }

    public void editCategorie() {
        String nom = editNom.getText().toString();
        String visuel = editVisuel.getText().toString();

        if(nom.length() != 0 && visuel.length() != 0) {
            // modification catégorie
            Toast.makeText(this,"Modification", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrect !", Toast.LENGTH_LONG).show();
        }
    }

    public void retour(View view) {
        this.finish();
    }
}
