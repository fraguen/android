package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
