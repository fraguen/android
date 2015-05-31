package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by william on 30/05/15.
 */

public class AjouterLivreActivity extends Activity{

    private EditText edit_ISBNLivre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_livre);
        Button btn_rechercheLivre = (Button) findViewById(R.id.btn_findLivre);
        btn_rechercheLivre.setOnClickListener(btnClickListener);
        edit_ISBNLivre = (EditText) findViewById(R.id.edit_ISBNLivre);
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.btn_findLivre:
                    if(edit_ISBNLivre.getText() != null) {
                        String ISBN = String.valueOf(edit_ISBNLivre.getText());
                        if(ISBN.isEmpty()){
                            Toast.makeText(
                                    AjouterLivreActivity.this,
                                    getResources().getString(R.string.toastErrorNoISBN),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                        else{
                            /*
                            String url = "http://www.worldcat.org/isbn/" + ISBN + "?page=endnote";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            */
                            ISBN = ISBN.trim();
                            String url = "http://www.worldcat.org/isbn/" + ISBN;
                            Intent i = new Intent(AjouterLivreActivity.this, ViewInfoBookActivity.class);
                            i.putExtra("url", url);
                            i.putExtra("isbn", ISBN);
                            startActivity(i);
                        }
                    }

            }
        }
    };
}
