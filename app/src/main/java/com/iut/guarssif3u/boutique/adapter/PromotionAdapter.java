package com.iut.guarssif3u.boutique.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.fragment.ObjetMetier;
import com.iut.guarssif3u.boutique.modele.metier.Promotion;
import java.util.ArrayList;

/**
 * Created by younes on 19/02/2018.
 */

public class PromotionAdapter extends ArrayAdapter<Promotion> implements View.OnClickListener {

    protected FragmentActivity activity;
    protected ObjetMetier<Promotion> parent;

    protected Drawable substitut;
    protected ProgressBar loader;
    protected ImageView btnEdit;
    protected ImageView btnDelete;

    protected ArrayList<Promotion> promotions;

    public PromotionAdapter(FragmentActivity activity, ObjetMetier<Promotion> parent,ArrayList<Promotion> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.promotions = liste;
        this.activity = activity;
        this.substitut = subsitut;
        this.parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Promotion promotion = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_promotion, parent, false);
        }

        btnEdit = convertView.findViewById(R.id.modifier);
        btnDelete = convertView.findViewById(R.id.supprimer);

        btnEdit.setTag(position);
        btnDelete.setTag(position);

        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        this.loader = convertView.findViewById(R.id.loader);

        TextView tvNom = convertView.findViewById(R.id.nom);
        tvNom.setText(promotion.getArticle().getNom());

        this.loader = convertView.findViewById(R.id.loader);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Promotion promotion = this.promotions.get((int)v.getTag());

        switch (v.getId()){
            case (R.id.supprimer):
                this.parent.supprimer(promotion);
                break;

            case (R.id.modifier):
                this.parent.modifier(promotion);
                break;
        }
    }
}
