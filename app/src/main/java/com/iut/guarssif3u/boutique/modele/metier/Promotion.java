package com.iut.guarssif3u.boutique.modele.metier;

import java.util.Date;

/**
 * Created by Vicek on 09/01/2018.
 */

public class Promotion {

    /**
     * Article ciblé par la promotion
     */
    protected Article article;

    /**
     * Date de debut de la promotion
     */
    protected Date date_debut;

    /**
     * Date de fin de la promotion
     */
    protected Date date_fin;

    /**
     * Pourcentage de reduction
     */
    protected float pourcentage;

    /**
     * Constructeur vide
     */
    public Promotion(){}

    /**
     * Constructeur
     *
     * @param article
     * article cilbé
     *
     * @param date_debut
     * date de debut de la pormotion
     *
     * @param date_fin
     * date de fin de la promotion
     *
     * @param pourcentage
     * pourcentage de reduction
     */
    public Promotion(Article article, Date date_debut, Date date_fin, float pourcentage) {
        this.setArticle(article);
        this.setDate_debut(date_debut);
        this.setDate_fin(date_fin);
        this.setPourcentage(pourcentage);
    }

    /**
     * Retourne l'article ciblé par la promotion
     *
     * @return
     * article ciblé par la promotion
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Change l'article ciblé par la promotion
     *
     * @param article
     * nouvel article
     */
    public void setArticle(Article article) {
        this.article = article;
    }

    /**
     * Retourne la date de debut la promotion
     *
     * @return
     * date de debut de la promotion
     */
    public Date getDate_debut() {
        return date_debut;
    }

    /**
     * Change la date de debut de la promotion
     *
     * @param date_debut
     * nouvelle date
     */
    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    /**
     * Retourne la date de fin de la promotion
     *
     * @return
     * date de fin de la promotion
     */
    public Date getDate_fin() {
        return date_fin;
    }

    /**
     * Change la date de fin de la promotion
     *
     * @param date_fin
     * nouvelle date
     */
    public void setDate_fin(Date date_fin) {
        if(date_fin.compareTo(this.date_debut) < 0 ) throw new IllegalArgumentException("La date de fin ne peut pas être plus tôt que la date de début");
        this.date_fin = date_fin;
    }

    /**
     * Retourne le pourcentage de reduction de la promotion
     *
     * @return
     * le pourcentage de reduction de la promotion
     */
    public float getPourcentage() {
        return pourcentage;
    }

    /**
     * Change le pourcentage de reduction de la promotion
     *
     * @param pourcentage
     * nouveau pourcentage
     */
    public void setPourcentage(float pourcentage) {
        if(pourcentage < 0 || pourcentage > 1) throw new IllegalArgumentException("Le pourecentage est compris entre 0% et 100%");
        this.pourcentage = pourcentage;
    }

    /**
     * Equals article date_debeut date_fin
     *
     * @param o
     * objet de type Promotion
     *
     * @return
     * true si this == o, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Promotion promotion = (Promotion) o;

        if (Float.compare(promotion.pourcentage, pourcentage) != 0) return false;
        if (!article.equals(promotion.article)) return false;
        if (!date_debut.equals(promotion.date_debut)) return false;
        return date_fin.equals(promotion.date_fin);
    }

    /**
     * Hashcode article date_debut date_fin
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = article.hashCode();
        result = 31 * result + date_debut.hashCode();
        result = 31 * result + date_fin.hashCode();
        result = 31 * result + (pourcentage != +0.0f ? Float.floatToIntBits(pourcentage) : 0);
        return result;
    }
}
