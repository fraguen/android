package com.iut.mylibrary;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */


public class Auteur {

    private int idAuteur;
    private String nomPrenom;

    public Auteur(){

    }

    public Auteur(String nomPrenom){
        this.nomPrenom = nomPrenom;
    }

    public int getIdAuteur() {
        return idAuteur;
    }

    public void setIdAuteur(int idAuteur) {
        this.idAuteur = idAuteur;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    @Override
    public String toString() {
        return nomPrenom;
    }
}
