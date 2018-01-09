package com.iut.guarssif3u.boutique.modele.metier;

/**
 * Created by Vicek on 09/01/2018.
 */

public class Client {

    /**
     * Id du client
     */
    protected int id;

    /**
     * Nom du client
     */
    protected String nom;

    /**
     * Prenom du client
     */
    protected String prenom;

    /**
     * Ville du client
     */
    protected String ville;

    /**
     * Constructeur vide
     */
    public Client() {};

    /**
     * Constructeur
     *
     * @param nom
     * nom du client
     * @param prenom
     * prenom du client
     * @param ville
     * ville du client
     */
    public Client(String nom, String prenom, String ville) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setVille(ville);
    }

    /**
     * Retourne l'id du client
     *
     * @return
     * id du client
     */
    public int getId() {
        return id;
    }

    /**
     * Change l'id du client
     *
     * @param id
     * nouvel id
     *
     * @throws IllegalArgumentException
     * si l'id est negatif
     */
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    /**
     * Retourne le nom du client
     *
     * @return
     * nom du client
     */
    public String getNom() {
        return nom;
    }

    /**
     * Change le nom du client
     *
     * @param nom
     * nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prenom du client
     *
     * @return
     * prenom du client
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Change le prenom du client
     *
     * @param prenom
     * nouveau prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne la ville du client
     *
     * @return
     * ville du client
     */
    public String getVille() {
        return ville;
    }

    /**
     * Change la ville du client
     *
     * @param ville
     * nouvelle ville
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * Equals id nom prenom ville
     *
     * @param o
     * objet de type Client
     *
     * @return
     * true si this == 0, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != client.id) return false;
        if (!nom.equals(client.nom)) return false;
        if (!prenom.equals(client.prenom)) return false;
        return ville.equals(client.ville);
    }

    /**
     * Hashcode id nom prenom ville
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + ville.hashCode();
        return result;
    }
}
