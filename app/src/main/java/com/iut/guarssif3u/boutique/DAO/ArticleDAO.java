package com.iut.guarssif3u.boutique.DAO;

import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.async.HTTPRequest;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Article;

/**
 * Created by younes on 19/02/2018.
 */

public class ArticleDAO extends DAO<Article> {

    protected static ArticleDAO INSTANCE = null;

    protected ArticleDAO(ActiviteEnAttenteAvecResultat activite){
        this.activite = activite;
    }

    public static ArticleDAO getInstance(ActiviteEnAttenteAvecResultat activite){
        if(INSTANCE == null){
            INSTANCE = new ArticleDAO(activite);
        }
        else {
            INSTANCE.setActivite(activite);
        }

        return INSTANCE;
    }

    @Override
    public void findAll() {
    HTTPRequest<Article> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.GET, null, Article.class);
        req.execute(URL_SERVEUR + "articles");
    }

    @Override
    public void insert(Article object) {
        HTTPRequest<Article> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.POST, object, Article.class);
        req.execute(URL_SERVEUR + "articles");
    }

    @Override
    public void delete(Article object) {
        HTTPRequest<Article> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.DELETE, object, Article.class);
        req.execute(URL_SERVEUR + "articles/" + object.getId());
    }

    @Override
    public void update(Article object) {
        HTTPRequest<Article> req = new HTTPRequest<>(activite, this, HTTPRequestMethod.PUT, object, Article.class);
        req.execute(URL_SERVEUR + "articles/" + object.getId());
    }
}
