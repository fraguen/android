package com.iut.mylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 */

public class ViewInfoBookActivity extends Activity{

    private String ISBN;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private ProgressDialog mProgressDialog;

    private final String AUTEUR = "AU  - ";
    private final String TITRE = "T1  - ";
    private File sdcard;

    private String encoding;


    // Progress Dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_info);
        ISBN = getIntent().getStringExtra("isbn");
        TextView text_infoBook = (TextView) findViewById(R.id.text_infoBook);
        String newTitle = getApplication().getApplicationContext().getResources().getString(R.string.text_infoBook, ISBN);
        text_infoBook.setText(newTitle);
        Button btn_addBook = (Button) findViewById(R.id.btn_addBook);
        btn_addBook.setOnClickListener(btnClickListener);
        String url = getIntent().getStringExtra("url");
        WebView web_infoBook = (WebView) findViewById(R.id.web_infoBook);
        WebSettings webSettings = web_infoBook.getSettings();
        webSettings.setJavaScriptEnabled(true);
        final Activity activity = this;
        web_infoBook.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        web_infoBook.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        web_infoBook.loadUrl(url);
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_addBook:
                    String url = "http://www.worldcat.org/isbn/" + ISBN + "?page=endnote";
                    new DownloadFileAsync().execute(url);
                    break;
            }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Récupération des informations en cours..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count = 0;

            try {

                URL url = new URL(aurl[0].trim());
                URLConnection connexion = url.openConnection();
                connexion.connect();
                sdcard = Environment.getExternalStorageDirectory();
                int lenghtOfFile = connexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                String externalPath = sdcard + "/myLibraryFiles/";
                encoding = getEncoding(connexion);
                InputStreamReader input = new InputStreamReader(url.openStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(connexion.getInputStream(), encoding));
                //InputStream input = new BufferedInputStream(url.openStream());
                // create a File object for the parent directory
                File myLibraryDirectory = new File(externalPath);
                // have the object build the directory structure, if needed.
                myLibraryDirectory.mkdirs();
                File outputFile = new File(externalPath, ISBN + ".txt");
                //OutputStream output = new FileOutputStream(outputFile);
                //FileWriter output = new FileWriter(outputFile);
                Writer output = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outputFile), encoding));

                char data[] = new char[1024];
                long total = 0;
                /*while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }*/
                for (String line; (line = in.readLine()) != null; ){
                    total += line.length();
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(line);
                    output.write("\n");
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Toast.makeText(
                        ViewInfoBookActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Toast.makeText(
                    ViewInfoBookActivity.this,
                    "Download Finished",
                    Toast.LENGTH_LONG
            ).show();
            createLivre(sdcard + "/myLibraryFiles/" + ISBN + ".txt");
        }
    }

    public void createLivre(String path){
        //Get the text file
        File file = new File(path);
        try {
            Livre livre = new Livre();
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding));
            String line;
            livre.setISBN(ISBN);
            ArrayList<Auteur> auteurs = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if(line.contains(AUTEUR)){
                    Auteur auteur = new Auteur();
                    String auteurRead = line.split(AUTEUR)[1];
                    String[] split = auteurRead.split(",");
                    String nom  = split[0].trim();
                    try {
                        String prenom = split[1].replace(".", "").trim();
                        auteur.setNomPrenom(prenom + " " + nom);
                    }catch (Exception e){
                        Log.d("creationLivre", "Pas de prénom pour l'auteur");
                        auteur.setNomPrenom(nom);
                    }
                    auteurs.add(auteur);

                }
                else if(line.contains(TITRE)){
                    String titre = line.split(TITRE)[1];
                    livre.setTitre(titre);
                }
            }
            livre.setAuteurs(auteurs);
            MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
            db.addLivre(livre);
            for(Auteur auteur : auteurs){
                if(db.getAuteurByNomPrenom(auteur.getNomPrenom()).getNomPrenom() != null){
                    auteur = db.getAuteurByNomPrenom(auteur.getNomPrenom());
                }
                else {
                    db.addAuteur(auteur);
                }
                db.addAuteurForLivre(auteur, livre);
            }
            br.close();
            finish();
        }
        catch (IOException e) {
            Log.e("CreateLivre", e.getMessage());
            Toast.makeText(
                    ViewInfoBookActivity.this,
                    getResources().getString(R.string.toastErrorAjoutLivre),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    public String getEncoding(URLConnection connection){
        String contentType = connection.getContentType();
        String[] values = contentType.split(";"); //The values.length must be equal to 2...
        String charset = "";

        for (String value : values) {
            value = value.trim();

            if (value.toLowerCase().startsWith("charset=")) {
                charset = value.substring("charset=".length());
            }
        }
        return charset;
    }
}
