package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */

public class MainActivity extends Activity {

    ArrayAdapter<Livre> adapter;
    ArrayList<Livre> livres;

    @Override
    protected void onResume() {
        super.onResume();
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        livres = db.getAllLivre();
        int nbLivres = livres.size();
        String newTitle = getResources().getString(R.string.nbLivreDansBiblio, String.valueOf(nbLivres));
        TextView text_nbLivre = (TextView) findViewById(R.id.text_nbLivre);
        text_nbLivre.setText(newTitle);
        ListView list_livres = (ListView) findViewById(R.id.list_livre);
        list_livres.setOnItemClickListener(listOnItemClickListener);

        RadioButton rb_sortById = (RadioButton) findViewById(R.id.rb_sortById);
        rb_sortById.setOnCheckedChangeListener(rbCheckedChangeListener);
        RadioButton rb_sortByISBN = (RadioButton) findViewById(R.id.rb_sortByISBN);
        rb_sortByISBN.setOnCheckedChangeListener(rbCheckedChangeListener);
        RadioButton rb_sortByTitre = (RadioButton) findViewById(R.id.rb_sortByTitre);
        rb_sortByTitre.setOnCheckedChangeListener(rbCheckedChangeListener);
        RadioButton rb_sortByAuteurs = (RadioButton) findViewById(R.id.rb_sortByAuteurs);
        rb_sortByAuteurs.setOnCheckedChangeListener(rbCheckedChangeListener);


        adapter = new ArrayAdapter<>(MainActivity.this, R.layout.simple_list, R.id.rowTextView);
        // S'il y a au moins un contact on l'ajoute à la liste
        if (!livres.isEmpty()) {
            for(Livre livre : livres){
                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                livre.setAuteurs(auteurs);
                adapter.add(livre);
            }
        }
        list_livres.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        livres = db.getAllLivre();
        int nbLivres = livres.size();
        String newTitle = getResources().getString(R.string.nbLivreDansBiblio, String.valueOf(nbLivres));
        TextView text_nbLivre = (TextView) findViewById(R.id.text_nbLivre);
        text_nbLivre.setText(newTitle);
        ListView list_livres = (ListView) findViewById(R.id.list_livre);
        adapter = new ArrayAdapter<>(MainActivity.this, R.layout.simple_list, R.id.rowTextView);
        // S'il y a au moins un contact on l'ajoute à la liste
        if (!livres.isEmpty()) {
            for(Livre livre : livres){
                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                livre.setAuteurs(auteurs);
                adapter.add(livre);
            }
        }
        list_livres.setAdapter(adapter);

    }

    /**
     * Création de la menu bar
     * @param menu Le menu a afficher
     * @return boolean
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.action_settings:
                return true;

            case R.id.action_addBook:
                i = new Intent(MainActivity.this, AjouterLivreActivity.class);
                this.startActivity(i);
                return true;

            case R.id.action_editBook:
                return true;

            case R.id.action_deleteBook:
                return true;

            case R.id.action_scanBook:
                i = new Intent(MainActivity.this, ScannerActivity.class);
                this.startActivity(i);
                return true;

            case R.id.action_exit:
                MainActivity.this.finish();
                System.exit(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Livre livre = (Livre)parent.getItemAtPosition(position);
            Intent i = new Intent(MainActivity.this, ModifierLivreActivity.class);
            i.putExtra("idLivre", String.valueOf(livre.getIdLivre()));
            startActivity(i);
        }
    };

    CompoundButton.OnCheckedChangeListener rbCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()){
                case  R.id.rb_sortById:
                    if(isChecked){
                        if(livres != null) {
                            Collections.sort(livres, sortById);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
                            livres = db.getAllLivre();
                            for(Livre livre : livres) {
                                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                                livre.setAuteurs(auteurs);
                            }
                            Collections.sort(livres, sortById);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case R.id.rb_sortByISBN:
                    if(isChecked){
                        if(livres != null) {
                            Collections.sort(livres, sortByISBN);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
                            livres = db.getAllLivre();
                            for(Livre livre : livres) {
                                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                                livre.setAuteurs(auteurs);
                            }
                            Collections.sort(livres, sortByISBN);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case R.id.rb_sortByTitre:
                    if(isChecked){
                        if(livres != null) {
                            Collections.sort(livres, sortByTitre);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
                            livres = db.getAllLivre();
                            for(Livre livre : livres) {
                                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                                livre.setAuteurs(auteurs);
                            }
                            Collections.sort(livres, sortByTitre);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case R.id.rb_sortByAuteurs:
                    if(isChecked){
                        if(livres != null) {
                            Collections.sort(livres, sortByAuteur);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
                            livres = db.getAllLivre();
                            for(Livre livre : livres) {
                                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                                livre.setAuteurs(auteurs);
                            }
                            Collections.sort(livres, sortByAuteur);
                            adapter.clear();
                            for(Livre livre : livres){
                                adapter.add(livre);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };

    Comparator<Livre> sortById = new Comparator<Livre>() {
        @Override
        public int compare(Livre livre1, Livre livre2) {
            if(livre1.getIdLivre() > livre2.getIdLivre()){
                return 1;
            }
            else{
                return -1;
            }
        }
    };

    Comparator<Livre> sortByISBN = new Comparator<Livre>() {
        @Override
        public int compare(Livre livre1, Livre livre2) {
            String livre1ISBN = livre1.getISBN(), livre2ISBN = livre2.getISBN();
            long ISBN1 = Long.parseLong(livre1ISBN), ISBN2 = Long.parseLong(livre2ISBN);
            if(ISBN1 > ISBN2){
                return 1;
            }
            else{
                return -1;
            }
        }
    };

    Comparator<Livre> sortByTitre = new Comparator<Livre>() {
        @Override
        public int compare(Livre livre1, Livre livre2) {
            String titre1 = livre1.getTitre(), titre2 = livre2.getTitre();
            if(titre1.compareTo(titre2) > 1){
                return 1;
            }
            else{
                return -1;
            }
        }
    };

    Comparator<Livre> sortByAuteur = new Comparator<Livre>() {
        @Override
        public int compare(Livre livre1, Livre livre2) {
            ArrayList<Auteur> auteurs1 = livre1.getAuteurs(), auteurs2 = livre2.getAuteurs();
            if(auteurs1.size() < 1){
                return -1;
            }
            else if(auteurs2.size() < 1){
                return 1;
            }
            else {
                String auteur1 = livre1.getAuteurs().get(0).toString(), auteur2 = livre2.getAuteurs().get(0).toString();
                if (auteur1.compareTo(auteur2) > 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    };

}
