package com.iut.guarssif3u.boutique.fragment;

import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;

import java.util.ArrayList;

/**
 * Created by younes on 22/01/2018.
 */

public interface ActiviteEnAttenteAvecResultat<T> extends ActiviteEnAttente {

    void notifyRetourRequete(T resultat, String method, boolean error);
    void notifyRetourRequeteFindAll(ArrayList<T> list);
}
