package com.iut.guarssif3u.boutique.modele.metier;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vicek on 09/01/2018.
 */

public class Categorie implements Parcelable {

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
     *
     * @param nom
     * nom de la catégorie
     *
     * @param visuel
     * visuel de la catégorie
     */
    public Categorie(String nom, String visuel) {
        this.setNom(nom);
        this.setVisuel(visuel);
    }

    /**
     * Constructeur
     *
     * @param id
     * id de la categorie
     *
     * @param nom
     * nom de la catégorie
     *
     * @param visuel
     * visuel de la catégorie
     */
    public Categorie(int id, String nom, String visuel) {
        this.setId(id);
        this.setNom(nom);
        this.setVisuel(visuel);
    }

    /**
     * Constructeur
     *
     * @param in
     * Parcel à partir duquel reconstruire l'objet
     */
    public Categorie(Parcel in){
        this.id = in.readInt();
        this.nom = in.readString();
        this.visuel = in.readString();
    }

    /**
     * Retourne l'id de la catégorie
     *
     * @return
     * id de la categorie
     */
    public int getId() {
        return id;
    }

    /**
     * Change l'id de la catégorie
     *
     * @param id
     * Nouvel id
     *
     * @throws IllegalArgumentException
     * si l'id est négatif
     */
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("L'id ne peut pas être négatif");
        this.id = id;
    }

    /**
     * Retourne le nom de la catégorie
     *
     * @return
     * nom de la categorie
     */
    public String getNom() {
        return nom;
    }

    /**
     * Change le nom de la catégorie
     *
     * @param nom
     * nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le visuel de la catégorie
     *
     * @return
     * visuel de la categorie
     */
    public String getVisuel() {
        return visuel;
    }

    /**
     * Change le visuel de la catégorie
     *
     * @param visuel
     */
    public void setVisuel(String visuel) {
        this.visuel = visuel;
    }

    /**
     * Equals id nom visuel
     *
     * @param o
     * objet de type Categorie
     *
     * @return
     * true si this == o, sinon false
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
     * Hashcode id nom visuel
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + nom.hashCode();
        result = 31 * result + visuel.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.nom);
        parcel.writeString(this.visuel);
    }

    public static final Parcelable.Creator<Categorie> CREATOR = new Parcelable.Creator<Categorie>(){

        @Override
        public Categorie createFromParcel(Parcel source){
            return new Categorie(source);
        }

        @Override
        public Categorie[] newArray(int size){
            return new Categorie[size];
        }
    };

}
