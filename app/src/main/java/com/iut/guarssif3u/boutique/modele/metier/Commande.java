package com.iut.guarssif3u.boutique.modele.metier;

import java.util.Date;

/**
 * Created by younes on 09/01/2018.
 */

public class Commande {

    /**
     * Id de la commande
     */
    protected int id;

    /**
     * Date de la commande
     */
    protected Date date;

    /**
     * Client qui à effectué la la commande
     */
    protected Client client;

    /**
     * Constructeur
     */
    public Commande(){};

    /**
     * Constructeur
     *
     * @param id
     * Id de la commande
     *
     * @param date
     * Date de la commande
     *
     * @param client
     * Client qui à effectué la la commande
     */
    public Commande(int id, Date date, Client client){
        this.setId(id);
        this.setDate(date);
        this.setClient(client);
    }

    /**
     * Récupère l'id de la commande
     *
     * @return
     * l'id de la commande
     */
    public int getId() {
        return id;
    }

    /**
     * Donne un id à la commande
     *
     * @param id
     * Id à passer à la commande
     *
     * @throws IllegalArgumentException
     * si l'id est négatif
     */
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    /**
     * Récupère la date de la commande
     *
     * @return
     * la date de la commande
     */
    public Date getDate() {
        return date;
    }

    /**
     * Donne une date à la commande
     *
     * @param date
     * Date à passer à la commande
     */
    public void setDate(Date date) {
        //TODO
        this.date = date;
    }

    /**
     * Récupère le client qui à effectué la la commande
     *
     * @return
     * Le client qui à effectué la la commande
     */
    public Client getClient() {
        return client;
    }

    /**
     * Donne un client à la commande
     *
     * @param client
     * Client à passer à la commande
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Vérifie si un objet o est égale à l'object courant
     *
     * @param o
     * Objet à comparer
     *
     * @return
     * true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Commande commande = (Commande) o;

        if (id != commande.id) return false;
        if (date != null ? !date.equals(commande.date) : commande.date != null) return false;
        return client != null ? client.equals(commande.client) : commande.client == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        return result;
    }
}
