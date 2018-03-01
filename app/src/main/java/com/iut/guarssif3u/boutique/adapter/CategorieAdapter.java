package com.iut.guarssif3u.boutique.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.CategorieDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.ManageCategorieActivity;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.async.ImageFromURL;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttente;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.fragment.ObjetMetier;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieAdapter extends ArrayAdapter<Categorie> implements View.OnClickListener{

    protected FragmentActivity activity;
    protected ObjetMetier<Categorie> parent;

    protected Drawable substitut;
    protected ProgressBar loader;
    protected ImageView btnEdit;
    protected ImageView btnDelete;

    protected ArrayList<Categorie> categories;

    public CategorieAdapter(FragmentActivity activity, ObjetMetier<Categorie> parent, ArrayList<Categorie> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.categories = liste;
        this.activity = activity;
        this.substitut = subsitut;
        this.parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Categorie categorie = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_categorie, parent, false);
        }

        btnEdit = convertView.findViewById(R.id.modifier);
        btnDelete = convertView.findViewById(R.id.supprimer);

        btnEdit.setTag(position);
        btnDelete.setTag(position);

        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        this.loader = convertView.findViewById(R.id.loader);

        TextView tvNom = convertView.findViewById(R.id.nom);
        tvNom.setText(categorie.getNom());

        ImageView iconeVisuel = convertView.findViewById(R.id.visuel);

        if(iconeVisuel.getDrawable() == null){
            ImageFromURL<Categorie> ifu = new ImageFromURL<>(this, iconeVisuel, substitut, loader);
            ifu.execute("https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/images/categorie/" + categorie.getVisuel());
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Categorie categorie = this.categories.get((int)v.getTag());

        switch (v.getId()){
            case (R.id.supprimer):
                this.parent.supprimer(categorie);
                break;

            case (R.id.modifier):
                this.parent.modifier(categorie);
                break;
        }

    }

}
