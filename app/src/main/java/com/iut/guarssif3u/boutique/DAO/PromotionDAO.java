package com.iut.guarssif3u.boutique.DAO;

import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.async.HTTPRequest;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Promotion;

/**
 * Created by vincent on 28/02/2018.
 */

public class PromotionDAO extends DAO<Promotion> {

    protected static PromotionDAO INSTANCE = null;

    protected PromotionDAO(ActiviteEnAttenteAvecResultat activite){
        this.activite = activite;
    }

    public static PromotionDAO getInstance(ActiviteEnAttenteAvecResultat activite){
        if(INSTANCE == null){
            INSTANCE = new PromotionDAO(activite);
        }
        else {
            INSTANCE.setActivite(activite);
        }

        return INSTANCE;
    }

    @Override
    public void findAll() {
        HTTPRequest<Promotion> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.GET, null, Promotion.class);
        req.execute(URL_SERVEUR + "promotions");
    }

    @Override
    public void insert(Promotion object) {
        HTTPRequest<Promotion> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.POST, object, Promotion.class);
        req.execute(URL_SERVEUR + "promotions");
    }

    @Override
    public void delete(Promotion object) {
        HTTPRequest<Promotion> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.DELETE, null, Promotion.class);
        req.execute(URL_SERVEUR + "promotions");
    }

    @Override
    public void update(Promotion object) {
        HTTPRequest<Promotion> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.PUT, object, Promotion.class);
        req.execute(URL_SERVEUR + "promotions");
    }
}
