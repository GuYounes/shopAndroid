package com.iut.guarssif3u.boutique.modele.metier;

/**
 * Created by Vicek on 09/01/2018.
 */

public class Categorie {

    /**
     * Id de la catégorie
     */
    protected int id;

    /**
     * Nom de la catégorie
     */
    protected String nom;

    /**
     * Nom du png
     */
    protected String visuel;

    /**
     * Constructeur vide
     */
    public Categorie() {};

    /**
     * Constructeur
     * @param nom
     * nom de la catégorie
     * @param visuel
     * visuel de la catégorie
     */
    public Categorie(String nom, String visuel) {
        this.setNom(nom);
        this.setVisuel(visuel);
    }

    /**
     * retourne l'id de la catégorie
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * change l'id de la catégorie
     * @param id
     * nouvel id
     */
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    /**
     * retourne le nom de la catégorie
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * change le nom de la catégorie
     * @param nom
     * nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * retourne le visuel de la catégorie
     * @return
     */
    public String getVisuel() {
        return visuel;
    }

    /**
     * change le visuel de la catégorie
     * @param visuel
     */
    public void setVisuel(String visuel) {
        this.visuel = visuel;
    }

    /**
     * equals id nom visuel
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Categorie categorie = (Categorie) o;

        if (id != categorie.id) return false;
        if (!nom.equals(categorie.nom)) return false;
        return visuel.equals(categorie.visuel);
    }

    /**
     * hashcode id nom visuel
     * @return
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + nom.hashCode();
        result = 31 * result + visuel.hashCode();
        return result;
    }
}
