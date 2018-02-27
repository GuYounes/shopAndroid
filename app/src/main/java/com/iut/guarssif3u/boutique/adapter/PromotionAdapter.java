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
import com.iut.guarssif3u.boutique.modele.metier.Categorie;
import com.iut.guarssif3u.boutique.modele.metier.Promotion;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 19/02/2018.
 */

public class PromotionAdapter extends ArrayAdapter<Promotion> {

    protected FragmentActivity activity;
    protected Drawable substitut;
    protected ProgressBar loader;

    public PromotionAdapter(FragmentActivity activity, ArrayList<Promotion> liste, Drawable subsitut){
        super(activity, 0, liste);
        this.activity = activity;
        this.substitut = subsitut;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Promotion promotion = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_promotion, parent, false);
        }

        this.loader = convertView.findViewById(R.id.loader);

        return convertView;
    }
}
