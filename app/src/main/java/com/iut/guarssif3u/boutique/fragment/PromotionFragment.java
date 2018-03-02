package com.iut.guarssif3u.boutique.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.BoutiqueActivity;
import com.iut.guarssif3u.boutique.DAO.PromotionDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.ManagePromotionActivity;
import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.PromotionAdapter;
import com.iut.guarssif3u.boutique.dialog.SuppressionDialog;
import com.iut.guarssif3u.boutique.modele.metier.Promotion;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class PromotionFragment extends Fragment implements ActiviteEnAttenteAvecResultat<Promotion>, View.OnClickListener, ObjetMetier<Promotion>, DialogInterface.OnClickListener {

    protected ArrayList<Promotion> promotions;
    protected Promotion targetPromotion;

    protected ListView listView;
    protected Drawable substitut;
    protected ProgressBar loader;
    protected FloatingActionButton addPromotionBtn;

    BoutiqueActivity activity;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_promotion, null);

        this.promotions = new ArrayList<>();
        this.activity = (BoutiqueActivity)this.getActivity();

        try {
            this.substitut = Drawable.createFromStream(getActivity().getAssets().open("substitut.png"), null);
        } catch (IOException e){}

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPromotionBtn = view.findViewById(R.id.add);
        addPromotionBtn.setOnClickListener(this);

        this.listView = view.findViewById(R.id.liste);
        this.loader = view.findViewById(R.id.loader);

        PromotionAdapter promotionAdapter = new PromotionAdapter(getActivity(), this, promotions, substitut);
        this.listView.setAdapter(promotionAdapter);

        PromotionDAO.getInstance(this).findAll();
        this.afficheLoader();
    }

    @Override
    public void afficheLoader() {
        this.loader.setVisibility(View.VISIBLE);
        this.listView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void cacheLoaderAfficheContenu() {
        this.loader.setVisibility(View.INVISIBLE);
        this.listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyRetourRequete(Promotion resultat, String method, boolean error) {
        if(error){
            Toast.makeText(getContext(), R.string.erreur_serveur, Toast.LENGTH_LONG).show();
            return;
        }
        switch (method){
            case HTTPRequestMethod.DELETE:
                this.promotions.remove(this.targetPromotion);
                ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(getContext(), R.string.supp_ok, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {
        this.promotions.clear();
        this.promotions.addAll(list);

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
        this.cacheLoaderAfficheContenu();

        // Si nous étions sur ce fragment avant de lancer une autre activité, nous reviendrons sur ce fragment, au bon niveau de scroll
        if(this.activity.getCurrentFragment() == this.activity.getViewPagerAdapter().getItemPosition(this)){
            this.listView.setSelectionFromTop(this.activity.getCurrentItemPosition(), this.activity.getCurrentTopPosition());
        }
    }

    @Override
    public void supprimer(Promotion object) {
        this.targetPromotion = object;
        try{
            SuppressionDialog dialog = SuppressionDialog.newInstance(((BoutiqueActivity)getActivity()).getViewPagerAdapter().getItemPosition(this), getActivity().getString(R.string.supp_promotion_message), getActivity().getString(R.string.supp_promotion_message));
            dialog.show(getActivity().getFragmentManager(), "suppression");
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ajouter() {
        this.saveFragmentAndPosition();

        Intent activityLauncher = new Intent(this.getActivity(), ManagePromotionActivity.class);
        activityLauncher.putExtra("method", HTTPRequestMethod.POST);
        startActivity(activityLauncher);
    }

    @Override
    public void modifier(Promotion object) {
        this.saveFragmentAndPosition();

        Intent activityLauncher = new Intent(getContext(), ManagePromotionActivity.class);
        activityLauncher.putExtra("promotion", object);
        activityLauncher.putExtra("method", HTTPRequestMethod.PUT);
        this.getActivity().startActivity(activityLauncher);
    }

    @Override
    public void recuperer(int id) {}

    @Override
    public void onClick(View view) {
        this.ajouter();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            PromotionDAO.getInstance(this).delete(this.targetPromotion);
        } else {
            return;
        }
    }
    /**
     * Sauvegarde le fragment sur lequel nous sommes, et la position du scroll
     */
    protected void saveFragmentAndPosition(){
        int index = this.listView.getFirstVisiblePosition();
        View v = this.listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - this.listView.getPaddingTop());

        this.activity.saveFragmentAndPosition(this.activity.getViewPagerAdapter().getItemPosition(this), index, top);
    }

}
