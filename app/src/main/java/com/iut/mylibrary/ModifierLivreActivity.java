package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */

public class ModifierLivreActivity extends Activity {

    private EditText edit_titreLivre, edit_ISBN, edit_auteurs;
    private Livre livre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        setContentView(R.layout.activity_modifier_livre);
        if(getIntent().getExtras() != null){
            Intent i = getIntent();
            if(i.getExtras().getString("idLivre") != null){
                int idLivre = Integer.parseInt(getIntent().getStringExtra("idLivre"));
                livre = db.getLivreById(idLivre);
                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                livre.setAuteurs(auteurs);
                edit_titreLivre = (EditText) findViewById(R.id.edit_titreLivre);
                edit_titreLivre.setOnFocusChangeListener(editOnFocusChangeListener);
                edit_ISBN = (EditText) findViewById(R.id.edit_ISBN);
                edit_ISBN.setOnFocusChangeListener(editOnFocusChangeListener);
                edit_auteurs = (EditText) findViewById(R.id.edit_auteurs);
                edit_auteurs.setOnFocusChangeListener(editOnFocusChangeListener);
                Button btn_valider = (Button) findViewById(R.id.btn_validModifLivre);
                btn_valider.setOnClickListener(btnClickListener);
                if(livre.getTitre() != null){
                    edit_titreLivre.setText(livre.getTitre());
                }
                if(livre.getISBN() != null){
                    edit_ISBN.setText(livre.getISBN());
                }
                if(livre.getAuteurs().size() > 0){
                    auteurs = livre.getAuteurs();
                    for (int currAuteurIndex = 0; currAuteurIndex < auteurs.size(); currAuteurIndex++) {
                        String editAuteurText = String.valueOf(edit_auteurs.getText());
                        editAuteurText += auteurs.get(currAuteurIndex).toString();
                        if(currAuteurIndex < auteurs.size() -1){
                            editAuteurText += ", ";
                        }
                        edit_auteurs.setText(editAuteurText);
                    }
                }
            }
        }
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
            switch(v.getId()){
                case R.id.btn_validModifLivre:
                    boolean nedUpdate = false;
                    if(edit_titreLivre.getText() != null) {
                        String titreLivre = String.valueOf(edit_titreLivre.getText());
                        if (!titreLivre.equals(livre.getTitre())) {
                            livre.setTitre(titreLivre);
                            nedUpdate = true;
                        }
                    }
                    else{
                        Toast.makeText(
                                ModifierLivreActivity.this,
                                getApplicationContext().getResources().getString(R.string.toastErrorNoTitreLivre),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    }
                    if(edit_ISBN.getText() != null){
                        String ISBN = String.valueOf(edit_ISBN.getText());
                        if(!ISBN.equals(livre.getISBN())){
                            livre.setISBN(ISBN);
                            nedUpdate = true;
                        }
                    }
                    else{
                        Toast.makeText(
                                ModifierLivreActivity.this,
                                getApplicationContext().getResources().getString(R.string.toastErrorNoISBNLivre),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    }
                    if(edit_auteurs.getText() != null) {
                        String auteursString = String.valueOf(edit_auteurs.getText());
                        String[] splitAuteur = auteursString.split(",");
                        ArrayList<Auteur> auteurs = livre.getAuteurs();
                        for (Auteur auteur : auteurs) {
                            db.removeAuteurForLivre(auteur, livre);
                            if (db.getLivresByAuteur(auteur).size() == 1) {
                                db.deleteAuteur(auteur);
                            }
                        }
                        for (String splittedAuteur : splitAuteur) {
                            Auteur newAuteur = new Auteur(splittedAuteur);
                            if (db.getAuteurByNomPrenom(splittedAuteur).getNomPrenom() != null) {
                                newAuteur = db.getAuteurByNomPrenom(splittedAuteur);
                                db.addAuteurForLivre(newAuteur, livre);
                            } else {
                                db.addAuteur(newAuteur);
                                db.addAuteurForLivre(newAuteur, livre);
                            }
                        }
                    }
                    else{
                        for (Auteur auteur : db.getAuteursByLivre(livre)){
                            db.removeAuteurForLivre(auteur, livre);
                            if(db.getLivresByAuteur(auteur).size() == 1){
                                db.deleteAuteur(auteur);
                            }
                        }

                    }
                    if(nedUpdate){
                        db.updateLivre(livre);
                    }
                    break;
            }
        }
    };

    View.OnFocusChangeListener editOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch(v.getId()){
                case R.id.edit_titreLivre:
                    if(hasFocus) {
                        Selection.setSelection(edit_titreLivre.getText(), edit_titreLivre.getText().length());
                    }
                    break;
                case R.id.edit_ISBN:
                    if(hasFocus){
                        Selection.setSelection(edit_ISBN.getText(), edit_ISBN.getText().length());
                    }
                    break;
                case R.id.edit_auteurs:
                    if(hasFocus){
                        Selection.setSelection(edit_auteurs.getText(), edit_auteurs.getText().length());
                    }
                    break;
            }
        }
    };
}
