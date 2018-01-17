package com.iut.guarssif3u.boutique.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    private Context context;
    protected Drawable substitut;

    public CategorieAdapter(Context context, ArrayList<Categorie> liste){
        super(context, 0, liste);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Categorie categorie = getItem(position);

        try {
            substitut = Drawable.createFromStream(context.getAssets().open("crayon.png", AssetManager.ACCESS_STREAMING),null);
        } catch (IOException e){

        }
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_categorie, parent, false);
        }

        TextView tvNom = (TextView) convertView.findViewById(R.id.nom);
        tvNom.setText(categorie.getNom());

        ImageView iconeVisuel = (ImageView) convertView.findViewById(R.id.visuel);
        if(iconeVisuel.getDrawable() == null){
            ImageFromURL ifu = new ImageFromURL((Activity)context, iconeVisuel, substitut);
            ifu.execute("http://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/" + categorie.getVisuel());
        }

        ImageView iconeModifier = (ImageView) convertView.findViewById(R.id.modifier);
        if(iconeVisuel.getDrawable() == null){
            try {
                iconeModifier.setImageDrawable(Drawable.createFromStream(context.getAssets().open("crayon.png"), null));
            } catch (IOException e){

            }
        }

        ImageView iconeSupprimer = (ImageView) convertView.findViewById(R.id.supprimer);
        if(iconeVisuel.getDrawable() == null){
            try {
                iconeSupprimer.setImageDrawable(Drawable.createFromStream(context.getAssets().open("corbeille.png", AssetManager.ACCESS_STREAMING), null));
            } catch (IOException e){

            }
        }

        return convertView;
    }
}
