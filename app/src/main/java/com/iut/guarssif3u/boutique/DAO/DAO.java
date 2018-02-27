package com.iut.guarssif3u.boutique.DAO;

import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;

/**
 * Created by younes on 22/01/2018.
 */

public abstract class DAO<T> {
    protected final String URL_SERVEUR = "https://infodb.iutmetz.univ-lorraine.fr/~guarssif3u/ppo/ecommerce/api/public/";
    protected ActiviteEnAttenteAvecResultat activite;

    abstract void findAll();
    abstract void insert(T object);
    abstract void delete(T object);
    abstract void update(T object);

    protected ActiviteEnAttenteAvecResultat getActivite(){
        return this.activite;
    }

    protected void setActivite(ActiviteEnAttenteAvecResultat activite){
        this.activite = activite;
    }
}
