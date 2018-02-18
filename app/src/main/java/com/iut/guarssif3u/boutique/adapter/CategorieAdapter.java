package com.iut.guarssif3u.boutique.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.async.ImageFromURL;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieAdapter extends ArrayAdapter<Categorie> {

    protected FragmentActivity activity;
    protected Drawable substitut;
    protected ProgressBar loader;

    public CategorieAdapter(FragmentActivity activity, ArrayList<Categorie> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.activity = activity;
        this.substitut = subsitut;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Categorie categorie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_categorie, parent, false);
        }

        this.loader = convertView.findViewById(R.id.loader);

        TextView tvNom = convertView.findViewById(R.id.nom);
        tvNom.setText(categorie.getNom());

        ImageView iconeVisuel = convertView.findViewById(R.id.visuel);
        if(iconeVisuel.getDrawable() == null){
            ImageFromURL ifu = new ImageFromURL(this, iconeVisuel, substitut, loader);
            ifu.execute("https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/" + categorie.getVisuel());
        }

        ImageView iconeModifier = convertView.findViewById(R.id.modifier);
        if(iconeVisuel.getDrawable() == null){
            try {
                iconeModifier.setImageDrawable(Drawable.createFromStream(activity.getAssets().open("crayon.png"), null));
            } catch (IOException e){

            }
        }

        ImageView iconeSupprimer = convertView.findViewById(R.id.supprimer);
        if(iconeVisuel.getDrawable() == null){
            try {
                iconeSupprimer.setImageDrawable(Drawable.createFromStream(activity.getAssets().open("corbeille.png"), null));
            } catch (IOException e){

            }
        }

        return convertView;
    }

}
