package com.iut.guarssif3u.boutique.DAO;

import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.async.HTTPRequest;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

/**
 * Created by younes on 22/01/2018.
 */

public class CategorieDAO extends DAO<Categorie>{

    protected static CategorieDAO INSTANCE = null;

    protected CategorieDAO(ActiviteEnAttenteAvecResultat activite){
        this.activite = activite;
    }

    public static CategorieDAO getInstance(ActiviteEnAttenteAvecResultat activite){
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
        HTTPRequest<Categorie> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.GET, null, Categorie.class);
        req.execute(URL_SERVEUR + "categories");
    }

    @Override
    public void insert(Categorie object) {
        HTTPRequest<Categorie> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.POST, object, Categorie.class);
        req.execute(URL_SERVEUR + "categories");
    }

    @Override
    public void delete(Categorie object) {
        HTTPRequest<Categorie> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.DELETE, null, Categorie.class);
        req.execute(URL_SERVEUR + "categories/" + object.getId());
    }

    @Override
    public void update(Categorie object) {
        HTTPRequest<Categorie> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.PUT, object, Categorie.class);
        req.execute(URL_SERVEUR + "categories/" + object.getId());
    }
}
