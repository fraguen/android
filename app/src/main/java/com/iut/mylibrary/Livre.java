package com.iut.mylibrary;

import java.util.ArrayList;

/**
 * Created by william on 23/05/15.
 */

public class Livre {

    private int idLivre;
    private String ISBN;
    private String titre;
    private ArrayList<Auteur> auteurs;

    public Livre(){

    }

    public Livre(String ISBN, String titre, ArrayList<Auteur> auteurs ){
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteurs = auteurs;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public ArrayList<Auteur> getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(ArrayList<Auteur> auteurs) {
        this.auteurs = auteurs;
    }

    public void addAuteur(Auteur auteur){
        this.auteurs.add(auteur);
    }

    public boolean removeAuteur(Auteur auteur){
        if(isAuteur(auteur)){
            this.auteurs.remove(auteur);
            return true;
        }
        return false;
    }

    public boolean isAuteur(Auteur auteur){
        return this.auteurs.contains(auteur);
    }

    @Override
    public String toString() {
        String toString = titre + "\n";
        if(auteurs.size() > 0) {
            if (auteurs.size() > 1){
                toString += auteurs.get(0) + ", " + auteurs.get(1);
            }
            else if(auteurs.size() > 2){
                    toString += ", ... \n";
            }
        }
        else{
            toString += "Aucun auteur\n";
        }
        toString += "ISBN : " + ISBN;
        return toString;
    }
}
