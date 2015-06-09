package com.iut.mylibrary;

import java.util.ArrayList;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */

public class Livre {

    private int idLivre;
    private String ISBN;
    private String titre;
    private ArrayList<Auteur> auteurs;
    private boolean selected;

    public Livre(){

    }

    public Livre(String ISBN, String titre, ArrayList<Auteur> auteurs ){
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteurs = auteurs;
        this.selected = false;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        String toString = titre + "\n";
        if(auteurs.size() > 0) {
            if(auteurs.size() == 1){
                toString += auteurs.get(0) + "\n";
            }
            else if (auteurs.size() == 2){
                toString += auteurs.get(0) + ", " + auteurs.get(1) + "\n";
            }
            else if(auteurs.size() > 2) {
                toString += auteurs.get(0) + ", " + auteurs.get(1) + ", ... \n";
            }
        }
        else{
            toString += "Aucun auteur\n";
        }
        toString += "ISBN : " + ISBN;
        return toString;
    }
}
