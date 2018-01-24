package com.iut.guarssif3u.boutique.fragment;

import java.util.ArrayList;

/**
 * Created by younes on 22/01/2018.
 */

public interface ActiviteEnAttenteAvecResultat<T> extends ActiviteEnAttente {

    void notifyRetourRequete(T resultat);
    void notifyRetourRequeteFindAll(ArrayList<T> list);
}
