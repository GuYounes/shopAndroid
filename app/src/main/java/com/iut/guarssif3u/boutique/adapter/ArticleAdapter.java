package com.iut.guarssif3u.boutique.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.ManageArticleActivity;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.async.ImageFromURL;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Article;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 19/02/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    protected FragmentActivity activity;
    protected Drawable substitut;
    protected ProgressBar loader;
    protected ImageView btnEdit;
    protected ImageView btnDelete;
    protected Article article;

    public ArticleAdapter(FragmentActivity activity, ArrayList<Article> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.activity = activity;
        this.substitut = subsitut;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        this.article = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_article, parent, false);

            btnEdit = convertView.findViewById(R.id.modifier);
            btnDelete = convertView.findViewById(R.id.supprimer);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent activityLauncher = new Intent(getContext(), ManageArticleActivity.class);
                    activityLauncher.putExtra("article", getItem(position));
                    activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
                    activity.startActivity(activityLauncher);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Suppression " + position, Toast.LENGTH_LONG).show();
                }
            });
        }

        this.loader = convertView.findViewById(R.id.loader);

        TextView tvNom = convertView.findViewById(R.id.nom);
        tvNom.setText(article.getNom());

        TextView tvReference = convertView.findViewById(R.id.reference);
        tvReference.setText(article.getReference());

        TextView tvTarif = convertView.findViewById(R.id.tarif);
        tvTarif.setText(Float.toString(article.getTarif()) + "€");

        ImageView iconeVisuel = convertView.findViewById(R.id.visuel);
        if(iconeVisuel.getDrawable() == null){
            ImageFromURL<Article> ifu = new ImageFromURL<>(this, iconeVisuel, substitut, loader);
            ifu.execute("https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/images/article/" + article.getVisuel());
        }

        ImageView iconeModifier = convertView.findViewById(R.id.modifier);
        if(iconeVisuel.getDrawable() == null){
            try {
                iconeModifier.setImageDrawable(Drawable.createFromStream(activity.getAssets().open("crayon.png"), null));
            } catch (IOException e){}
        }

        ImageView iconeSupprimer = convertView.findViewById(R.id.supprimer);
        if(iconeVisuel.getDrawable() == null){
            try {
                iconeSupprimer.setImageDrawable(Drawable.createFromStream(activity.getAssets().open("corbeille.png"), null));
            } catch (IOException e){}
        }

        return convertView;
    }
}
