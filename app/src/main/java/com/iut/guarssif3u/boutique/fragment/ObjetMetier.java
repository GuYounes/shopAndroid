package com.iut.guarssif3u.boutique.fragment;

/**
 * Created by younes on 27/02/2018.
 */

public interface ObjetMetier<T> {

    void supprimer(T object);
    void ajouter(T object);
    void modifier(T object);
    void recuperer(int id);
}
