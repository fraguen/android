package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */

public class PickModifLivre extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_modif_livre);
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        ListView list_pickModifLivre = (ListView) findViewById(R.id.list_pickModifLivre);
        ArrayAdapter<Livre> adapter = new ArrayAdapter<>(PickModifLivre.this, R.layout.simple_list, R.id.rowTextView);
        ArrayList<Livre> livres = db.getAllLivre();
        // S'il y a au moins un contact on l'ajoute à la liste
        if (!livres.isEmpty()) {
            for(Livre livre : livres){
                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                livre.setAuteurs(auteurs);
                adapter.add(livre);
            }
        }
        list_pickModifLivre.setAdapter(adapter);
        list_pickModifLivre.setOnItemClickListener(listOnItemClickListener);

    }

    AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Livre livre = (Livre)parent.getItemAtPosition(position);
            Intent i = new Intent(PickModifLivre.this, ModifierLivreActivity.class);
            i.putExtra("idLivre", String.valueOf(livre.getIdLivre()));
            startActivity(i);
            finish();
        }
    };
}
