package com.iut.guarssif3u.boutique.adapter;

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
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieAdapter extends ArrayAdapter<Categorie> implements ActiviteEnAttenteAvecResultat {

    protected FragmentActivity activity;
    protected Drawable substitut;
    protected ProgressBar loader;
    protected ImageView btnEdit;
    protected ImageView btnDelete;
    protected Categorie categorie;

    public CategorieAdapter(FragmentActivity activity, ArrayList<Categorie> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.activity = activity;
        this.substitut = subsitut;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        this.categorie = getItem(position);

        final CategorieDAO categorieDAO = CategorieDAO.getInstance(this);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_categorie, parent, false);

            btnEdit = (ImageView) convertView.findViewById(R.id.modifier);
            btnDelete = (ImageView) convertView.findViewById(R.id.supprimer);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent activityLauncher = new Intent(getContext(), ManageCategorieActivity.class);
                    activityLauncher.putExtra("categorie", getItem(position));
                    activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
                    activity.startActivity(activityLauncher);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categorieDAO.update(categorie);
                    Toast.makeText(getContext(), "Suppression " + position, Toast.LENGTH_LONG).show();
                }
            });
        }

        this.loader = convertView.findViewById(R.id.loader);

        TextView tvNom = convertView.findViewById(R.id.nom);
        tvNom.setText(categorie.getNom());

        ImageView iconeModifier = convertView.findViewById(R.id.modifier);
        ImageView iconeSupprimer = convertView.findViewById(R.id.supprimer);
        ImageView iconeVisuel = convertView.findViewById(R.id.visuel);

        if(iconeVisuel.getDrawable() == null){
            ImageFromURL<Categorie> ifu = new ImageFromURL<>(this, iconeVisuel, substitut, loader);
            ifu.execute("https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/images/categorie/" + categorie.getVisuel());
        }

        if(iconeVisuel.getDrawable() == null){
            try {
                iconeModifier.setImageDrawable(Drawable.createFromStream(activity.getAssets().open("crayon.png"), null));
            } catch (IOException e){}
        }

        if(iconeSupprimer.getDrawable() == null){
            try {
                iconeSupprimer.setImageDrawable(Drawable.createFromStream(activity.getAssets().open("corbeille.png"), null));
            } catch (IOException e){}
        }

        return convertView;
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
