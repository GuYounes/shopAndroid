package com.iut.guarssif3u.boutique.modele.metier;

/**
 * Created by younes on 09/01/2018.
 */

public class Article {

    /**
     * Id de l'article
     */
    protected int id;

    /**
     * Categorie de l'article
     */
    protected Categorie categorie;

    /**
     * Reference de l'article
     */
    protected String reference;

    /**
     * Nom de l'article
     */
    protected String nom;

    /**
     * Tarif de l'article
     */
    protected float tarif;

    /**
     * Nom de l'image representant l'article
     */
    protected String visuel;

    /**
     * Constructeur
     */
    public Article(){};

    public Article(int id, String reference, String nom, float tarif, String visuel) {
        this.setId(id);
        this.setReference(reference);
        this.setNom(nom);
        this.setTarif(tarif);
        this.setVisuel(visuel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getTarif() {
        return tarif;
    }

    public void setTarif(float tarif) {
        if(tarif < 0) throw new IllegalArgumentException("Le tarif ne peut pas être négatif");
        this.tarif = tarif;
    }

    public String getVisuel() {
        return visuel;
    }

    public void setVisuel(String visuel) {
        this.visuel = visuel;
    }
}
