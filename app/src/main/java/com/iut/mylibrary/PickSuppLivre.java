package com.iut.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */


public class PickSuppLivre extends Activity {

    private ListView list_pickSuppLivre;
    private ArrayList<Livre> livresToDelete = new ArrayList<>();
    private int posLivreSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_supp_livre);
        if(getIntent().getExtras() != null){
            posLivreSelected = Integer.parseInt(getIntent().getStringExtra("idLivreSelected"));
        }
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        ArrayList<Livre>livres = db.getAllLivre();
        list_pickSuppLivre = (ListView) findViewById(R.id.list_pickSuppLivre);
        Button btn_suppLivre = (Button) findViewById(R.id.btn_suppLivre);
        btn_suppLivre.setOnClickListener(itemClickListener);
        ArrayAdapter<Livre>adapter = new ArrayAdapter<Livre>(PickSuppLivre.this, R.layout.simple_list_multiple_selection, R.id.rowText){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                View text = row.findViewById(R.id.rowText);
                text.setOnClickListener(itemClickListener);
                text.setTag("text" + position);
                View cb = row.findViewById(R.id.cb);
                cb.setOnClickListener(itemClickListener);
                cb.setTag("cb" + position);
                if(posLivreSelected != -1){
                    MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
                    Livre livreSelected = db.getLivreById(posLivreSelected);
                    Livre livreAtPosition = (Livre)list_pickSuppLivre.getItemAtPosition(position);
                    if(livresToDelete.size() > 0) {
                        if (livreAtPosition.getIdLivre() == livreSelected.getIdLivre() && !livresToDelete.contains(livreAtPosition)) {
                            CheckBox checkBox = (CheckBox)row.findViewWithTag("cb" + position);
                            checkBox.setChecked(true);
                            livresToDelete.add((Livre) list_pickSuppLivre.getItemAtPosition(position));
                        }
                    }
                    else{
                        if (livreAtPosition.getIdLivre() == livreSelected.getIdLivre()) {
                            CheckBox checkBox = (CheckBox)row.findViewWithTag("cb" + position);
                            checkBox.setChecked(true);
                            livresToDelete.add((Livre) list_pickSuppLivre.getItemAtPosition(position));
                        }
                    }
                }
                return row;
            }
        };
        if (!livres.isEmpty()) {
            for(Livre livre : livres){
                ArrayList<Auteur> auteurs = db.getAuteursByLivre(livre);
                livre.setAuteurs(auteurs);
                adapter.add(livre);
            }
        }
        list_pickSuppLivre.setAdapter(adapter);
    }

/*    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CheckBox cb = (CheckBox)view.findViewById(R.id.cb);
            Livre selectedLivre = (Livre) list_pickSuppLivre.getItemAtPosition(position);
            if(livresToDelete.contains(selectedLivre)){
                cb.setChecked(false);
                livresToDelete.remove(selectedLivre);
            }
            else{
                livresToDelete.add(selectedLivre);
                cb.setChecked(true);
            }
        }
    };
    */

    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String positionOnListString;
            int positionOnList;
            CheckBox cb;
            Livre livre;
            switch(v.getId()){
                case R.id.rowText:
                    positionOnListString = String.valueOf(v.getTag()).split("text")[1];
                    positionOnList = Integer.parseInt(positionOnListString);
                    livre = (Livre)list_pickSuppLivre.getItemAtPosition(positionOnList);
                    cb = (CheckBox)list_pickSuppLivre.findViewWithTag("cb" + positionOnList);
                    if(livresToDelete.size() > 0) {
                        if (livresToDelete.contains(livre)) {
                            livresToDelete.remove(livre);
                            cb.setChecked(false);
                        } else {
                            livresToDelete.add(livre);
                            cb.setChecked(true);
                        }
                    }
                    else{
                        livresToDelete.add(livre);
                        cb.setChecked(true);
                    }
                    break;
                case R.id.cb :
                    positionOnListString = String.valueOf(v.getTag()).split("cb")[1];
                    positionOnList = Integer.parseInt(positionOnListString);
                    livre = (Livre)list_pickSuppLivre.getItemAtPosition(positionOnList);
                    cb = (CheckBox)list_pickSuppLivre.findViewWithTag("cb" + positionOnList);
                    if(livresToDelete.size() > 0) {
                        if (livresToDelete.contains(livre)) {
                            livresToDelete.remove(livre);
                            cb.setChecked(false);
                        } else {
                            livresToDelete.add(livre);
                            cb.setChecked(true);
                        }
                    }
                    else{
                        livresToDelete.add(livre);
                        cb.setChecked(true);
                    }
                    break;

                case R.id.btn_suppLivre:
                    if(livresToDelete.size() > 0){
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickSuppLivre.this);
                        alertDialog.setMessage(getApplicationContext().getResources().getString(R.string.messageDeleteLivre));
                        alertDialog.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
                                for(Livre currDeletingLivre : livresToDelete){
                                    ArrayList<Auteur> auteurs = currDeletingLivre.getAuteurs();
                                    for (Auteur auteur : auteurs) {
                                        db.removeAuteurForLivre(auteur, currDeletingLivre);
                                        if (db.getLivresByAuteur(auteur).size() == 1) {
                                            db.deleteAuteur(auteur);
                                        }
                                    }
                                    db.deleteLivre(currDeletingLivre);
                                }
                                dialogInterface.dismiss();
                                finish();
                            }
                        });
                        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.create().show();
                    }
                    else{
                        Toast.makeText(
                                PickSuppLivre.this,
                                getApplicationContext().getResources().getString(R.string.toastErrorNoLivreSelected),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    break;
            }

        }
    };

}
