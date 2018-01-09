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
     * Chemin vers l'image representant l'article
     */
    protected String visuel;

    /**
     * Constructeur
     */
    public Article(){};

    /**
     * Constructeur
     *
     * @param id
     * Id de l'article
     *
     * @param reference
     * Reference de l'article
     *
     * @param nom
     * Nom de l'article
     *
     * @param tarif
     * Tarif de l'article
     *
     * @param visuel
     * Chemin vers l'image de l'article
     */
    public Article(int id, String reference, String nom, float tarif, String visuel) {
        this.setId(id);
        this.setReference(reference);
        this.setNom(nom);
        this.setTarif(tarif);
        this.setVisuel(visuel);
    }

    /**
     * Récupère l'id
     *
     * @return
     * l'id de l'article
     */
    public int getId() {
        return id;
    }

    /**
     * Donne un id à l'article
     *
     * @param id
     * Id à passer à l'article
     *
     * @throws IllegalArgumentException
     * si l'id est négatif
     */
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    /**
     * Récupère la catégorie de l'article
     *
     * @return
     * la catégorie de l'article
     */
    public Categorie getCategorie() {
        return categorie;
    }

    /**
     * Donne une catégorie à l'article
     *
     * @param categorie
     * Catégorie à passer à l'article
     */
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    /**
     * Récupère la référence de l'article
     *
     * @return
     * la référence de l'article
     */
    public String getReference() {
        return reference;
    }

    /**
     * Donne une référence à l'article
     *
     * @param reference
     * Référence à passer à l'article
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Récupère le nom de l'article
     *
     * @return
     * le nom de l'article
     */
    public String getNom() {
        return nom;
    }

    /**
     * Donne un nom à l'article
     *
     * @param nom
     * Nom à passer à l'article
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le tarif de l'article
     *
     * @return
     * le tartif de l'article
     */
    public float getTarif() {
        return tarif;
    }

    /**
     * Donne un tarif à l'article
     *
     * @param tarif
     * Tarif à passer à l'article
     *
     * @throws IllegalArgumentException
     * si le tarif est négatif
     */
    public void setTarif(float tarif) {
        if(tarif < 0) throw new IllegalArgumentException("Le tarif ne peut pas être négatif");
        this.tarif = tarif;
    }

    /**
     * Récupère le visuel de l'article
     *
     * @return
     * le visuel de l'article
     */
    public String getVisuel() {
        return visuel;
    }

    /**
     * Donne un visuel à l'article
     *
     * @param visuel
     * Visuel à passer à l'article
     */
    public void setVisuel(String visuel) {
        this.visuel = visuel;
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

        Article article = (Article) o;

        if (id != article.id) return false;
        if (Float.compare(article.tarif, tarif) != 0) return false;
        if (categorie != null ? !categorie.equals(article.categorie) : article.categorie != null)
            return false;
        if (reference != null ? !reference.equals(article.reference) : article.reference != null)
            return false;
        if (nom != null ? !nom.equals(article.nom) : article.nom != null) return false;
        return visuel != null ? visuel.equals(article.visuel) : article.visuel == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (categorie != null ? categorie.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (tarif != +0.0f ? Float.floatToIntBits(tarif) : 0);
        result = 31 * result + (visuel != null ? visuel.hashCode() : 0);
        return result;
    }
}
