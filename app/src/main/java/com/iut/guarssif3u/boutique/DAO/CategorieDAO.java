package com.iut.guarssif3u.boutique.DAO;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.async.HTTPRequest;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttente;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by younes on 22/01/2018.
 */

public class CategorieDAO implements DAO<Categorie>{

    protected final String URL_SERVEUR = "https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/api/public/";

    protected ActiviteEnAttenteAvecResultat activite;

    protected static CategorieDAO INSTANCE = null;

    protected CategorieDAO(ActiviteEnAttenteAvecResultat activite){
        this.activite = activite;
    }

    public static CategorieDAO getInstance(ActiviteEnAttenteAvecResultat activite, ProgressBar loader, ListView listView){
        if(INSTANCE == null){
            INSTANCE = new CategorieDAO(activite);
        }
        else {
            INSTANCE.setActivite(activite);
        }

        return INSTANCE;
    }

    @Override
    public void findAll() {
        HTTPRequest<Categorie> req = new HTTPRequest<Categorie>(activite, this, HTTPRequestMethod.GET, null, Categorie.class);
        req.execute(URL_SERVEUR + "categories");
    }

    @Override
    public void insert(Categorie object) {
        HTTPRequest<Categorie> req = new HTTPRequest<Categorie>(activite, this, HTTPRequestMethod.POST, object, Categorie.class);
        req.execute(URL_SERVEUR + "categories");
    }

    @Override
    public void delete(Categorie object) {
        HTTPRequest<Categorie> req = new HTTPRequest<Categorie>(activite, this, HTTPRequestMethod.DELETE, null, Categorie.class);
        req.execute(URL_SERVEUR + "categories/" + object.getId());
    }

    @Override
    public void update(Categorie object) {
        HTTPRequest<Categorie> req = new HTTPRequest<Categorie>(activite, this, HTTPRequestMethod.PUT, object, Categorie.class);
        req.execute(URL_SERVEUR + "categories/" + object.getId());
    }

    protected ActiviteEnAttenteAvecResultat getActivite(){
        return this.activite;
    }

    protected void setActivite(ActiviteEnAttenteAvecResultat activite){
        this.activite = activite;
    }
}
