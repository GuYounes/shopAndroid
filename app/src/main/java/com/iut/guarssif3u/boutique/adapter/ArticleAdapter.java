package com.iut.guarssif3u.boutique.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.async.ImageFromURL;
import com.iut.guarssif3u.boutique.fragment.ObjetMetier;
import com.iut.guarssif3u.boutique.modele.metier.Article;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 19/02/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> implements View.OnClickListener {

    protected FragmentActivity activity;
    protected ObjetMetier<Article> parent;

    protected Drawable substitut;
    protected ProgressBar loader;
    protected ImageView btnEdit;
    protected ImageView btnDelete;

    protected ArrayList<Article> articles;

    public ArticleAdapter(FragmentActivity activity, ObjetMetier<Article> parent, ArrayList<Article> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.parent = parent;
        this.articles = liste;
        this.activity = activity;
        this.substitut = subsitut;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Article article = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_article, parent, false);
        }

        btnEdit = convertView.findViewById(R.id.modifier);
        btnDelete = convertView.findViewById(R.id.supprimer);

        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        btnEdit.setTag(position);
        btnDelete.setTag(position);

        this.loader = convertView.findViewById(R.id.loader);

        TextView tvNom = convertView.findViewById(R.id.nom);
        tvNom.setText(article.getNom());

        TextView tvReference = convertView.findViewById(R.id.reference);
        tvReference.setText(article.getReference());

        TextView tvTarif = convertView.findViewById(R.id.tarif);
        tvTarif.setText(String.format("%s %s", Float.toString(article.getTarif()), activity.getResources().getString(R.string.euro)));

        ImageView iconeVisuel = convertView.findViewById(R.id.visuel);
        if(iconeVisuel.getDrawable() == null){
            ImageFromURL<Article> ifu = new ImageFromURL<>(this, iconeVisuel, substitut, loader);
            ifu.execute("https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/images/article/" + article.getVisuel());
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Article article = this.articles.get((int)v.getTag());

        switch (v.getId()){
            case (R.id.supprimer):
                this.parent.supprimer(article);
                break;

            case (R.id.modifier):
                this.parent.modifier(article);
                break;
        }
    }
}
