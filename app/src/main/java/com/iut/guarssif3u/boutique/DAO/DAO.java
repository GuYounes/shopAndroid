package com.iut.guarssif3u.boutique.DAO;

/**
 * Created by younes on 22/01/2018.
 */

public interface DAO<T> {

    public void findAll();
    public void insert(T object);
    public void delete(T object);
    public void update(T object);
}
