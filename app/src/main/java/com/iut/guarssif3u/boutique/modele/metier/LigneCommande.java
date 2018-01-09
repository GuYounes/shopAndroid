package com.iut.guarssif3u.boutique.modele.metier;

/**
 * Created by younes on 09/01/2018.
 */

/**
 * Represente une ligne d'une commande
 */
public class LigneCommande {

    /**
     * Id de la ligne d'une commande
     */
    protected int id;

    /**
     * Commande dans laquelle se trouve la ligne
     */
    protected Commande commande;

    /**
     * Article auquel la ligne fait reference
     */
    protected Article article;

    /**
     * Quantite de l'article auquel la ligne fait reference
     */
    protected int quantite;

    /**
     * Constructeur
     */
    public LigneCommande(){};

    /**
     * Constructeur
     *
     * @param id
     * Id de la ligne d'une commande
     *
     * @param commande
     * Commande dans laquelle se trouve la ligne
     *
     * @param article
     * Article auquel la ligne fait reference
     *
     * @param quantite
     * Quantite de l'article auquel la ligne fait reference
     */
    public LigneCommande(int id, Commande commande, Article article, int quantite) {
        this.setId(id);
        this.setCommande(commande);
        this.setArticle(article);
        this.setQuantite(quantite);
    }

    /**
     * Récupère l'id de la ligne
     *
     * @return
     * l'id de la ligne
     */
    public int getId() {
        return id;
    }

    /**
     * Donne un id à la ligne
     *
     * @param id
     * id à passer à la ligne
     */
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    /**
     * Récupère la commande dans laquelle se trouve la ligne
     *
     * @return
     * la commande dans laquelle se trouve la ligne
     */
    public Commande getCommande() {
        return commande;
    }

    /**
     * Donne une commande à la ligne
     *
     * @param commande
     * Commande à passer à la ligne
     */
    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    /**
     * Récupère l'article auquel la ligne fait référence
     *
     * @return
     * l'article auquel la ligne fait référence
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Donne un article à la ligne
     *
     * @param article
     * Article à passer à la ligne
     */
    public void setArticle(Article article) {
        this.article = article;
    }

    /**
     * Récupère la quantité de l'article auquel la ligne fait référence
     *
     * @return
     * la quantité de l'article auquel la ligne fait référence
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Donne une quantité à l'article auquel la ligne fait référence
     *
     * @param quantite
     * La quantité de l'article auquel la ligne fait référence
     */
    public void setQuantite(int quantite) {
        if(quantite < 0) throw new IllegalArgumentException("La quantite ne peut pas être négative");
        this.quantite = quantite;
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

        LigneCommande that = (LigneCommande) o;

        if (id != that.id) return false;
        if (quantite != that.quantite) return false;
        if (commande != null ? !commande.equals(that.commande) : that.commande != null)
            return false;
        return article != null ? article.equals(that.article) : that.article == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (commande != null ? commande.hashCode() : 0);
        result = 31 * result + (article != null ? article.hashCode() : 0);
        result = 31 * result + quantite;
        return result;
    }
}
