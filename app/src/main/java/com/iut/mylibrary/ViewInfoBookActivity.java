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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Random;

/**
 * Created by william on 31/05/15.
 */

public class ViewInfoBookActivity extends Activity{

    private String ISBN;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private ProgressDialog mProgressDialog;

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
                    /*String url = "http://www.worldcat.org/isbn/" + ISBN + "?page=endnote";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                    try {
                        URL url = new URL("file://www.worldcat.org/isbn/" + ISBN + "?page=endnote");
                        URLConnection urlConnection = url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        readStream(in);
                        in.close();
                    }
                    catch (IOException e){
                        Toast.makeText(
                                ViewInfoBookActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }*/
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
            int count;

            try {

                URL url = new URL(aurl[0].trim());
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                String externalPath = Environment.getExternalStorageDirectory() + "/myLibraryFiles/";
                InputStream input = new BufferedInputStream(url.openStream());
                // create a File object for the parent directory
                File myLibraryDirectory = new File(externalPath);
                // have the object build the directory structure, if needed.
                myLibraryDirectory.mkdirs();
                File outputFile = new File(externalPath, ISBN + ".txt");
                OutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
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
        }
    }
}
