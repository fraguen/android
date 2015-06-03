package com.iut.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by william on 23/05/15.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyLibrary.db";
    private static final int DATABASE_VERSION = 1;

    /*
        TABLE LIVRE
     */

    // Définitions pour la table livre

    public static final String TABLE_LIVRE = "livre";

    public static final String COLUMN_ID_LIVRE = "idLivre";
    public static final String COLUMN_ISBN = "isbnLivre";
    public static final String COLUMN_TITIRE = "titreLivre";


    // Books Table Columns names
    public static final String KEY_ID_LIVRE = COLUMN_ID_LIVRE;
    public static final String KEY_ISBN = COLUMN_ISBN;
    public static final String KEY_TITIRE = COLUMN_TITIRE;

    private static final String[] COLUMNS_LIVRE = {
            KEY_ID_LIVRE,
            KEY_ISBN,
            KEY_TITIRE
    };

    // Creation de la table livre

    private static final String DATABASE_CREATE_LIVRE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LIVRE + "("
            + COLUMN_ID_LIVRE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ISBN + " TEXT, "
            + COLUMN_TITIRE + " TEXT NOT NULL"
            + ");";

    /*
        TABLE AUTEUR
     */

    // Définitions pour la table auteur

    public static final String TABLE_AUTEUR = "auteur";

    public static final String COLUMN_ID_AUTEUR = "idAuteur";
    public static final String COLUMN_NOM_AUTEUR = "nomAuteur";
    public static final String COLUMN_PRENOM_AUTEUR = "prenomAuteur";


    // Books Table Columns names
    public static final String KEY_ID_AUTEUR = COLUMN_ID_AUTEUR;
    public static final String KEY_NOM_AUTEUR = COLUMN_NOM_AUTEUR;
    public static final String KEY_PRENOM_AUTEUR = COLUMN_PRENOM_AUTEUR;

    private static final String[] COLUMNS_AUTEUR = {
            KEY_ID_AUTEUR,
            KEY_NOM_AUTEUR,
            KEY_PRENOM_AUTEUR
    };

    // Creation de la table auteur

    private static final String DATABASE_CREATE_AUTEUR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AUTEUR + "("
            + COLUMN_ID_AUTEUR + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOM_AUTEUR + " TEXT NOT NULL, "
            + COLUMN_PRENOM_AUTEUR + " TEXT NOT NULL"
            + ");";


    /*
        TABLE LIVRE ECRIT PAR
     */

    // Définitions pour la table livreEcritPar

    public static final String TABLE_LIVRE_ECRIT_PAR = "livreEcritPar";

    public static final String COLUMN_FK_ID_LIVRE_LIVRE_ECRIT_PAR = "idLivre";
    public static final String COLUMN_FK_ID_AUTEUR_LIVRE_ECRIT_PAR = "idAuteur";


    // Books Table Columns names
    public static final String KEY_FK_ID_LIVRE_LIVRE_ECRIT_PAR = COLUMN_FK_ID_LIVRE_LIVRE_ECRIT_PAR;
    public static final String KEY_FK_ID_AUTEUR_LIVRE_ECRIT_PAR = COLUMN_FK_ID_AUTEUR_LIVRE_ECRIT_PAR;

    private static final String[] COLUMNS_LIVRE_ECRIT_PAR = {
            KEY_FK_ID_LIVRE_LIVRE_ECRIT_PAR,
            KEY_FK_ID_AUTEUR_LIVRE_ECRIT_PAR
    };

    private static final String DATABASE_CREATE_FK_ID_LIVRE_LIVRE_ECRIT_PAR =
            "FOREIGN KEY ("
                    + COLUMN_FK_ID_AUTEUR_LIVRE_ECRIT_PAR
                    + ") REFERENCES "
                    + TABLE_LIVRE
                    + "(" + COLUMN_ID_LIVRE + ") ON DELETE CASCADE";

    private static final String DATABASE_CREATE_FK_ID_AUTEUR_LIVRE_ECRIT_PAR =
            "FOREIGN KEY ("
                    + COLUMN_FK_ID_AUTEUR_LIVRE_ECRIT_PAR
                    + ") REFERENCES "
                    + TABLE_AUTEUR
                    + "(" + COLUMN_ID_AUTEUR + ") ON DELETE CASCADE";

    // Creation de la table livreEcritPar

    private static final String DATABASE_CREATE_LIVRE_ECXRIT_PAR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LIVRE_ECRIT_PAR + "("
            + COLUMN_FK_ID_LIVRE_LIVRE_ECRIT_PAR + " INTEGER NOT NULL, "
            + COLUMN_FK_ID_AUTEUR_LIVRE_ECRIT_PAR+ " INTEGER NOT NULL, "
            + DATABASE_CREATE_FK_ID_AUTEUR_LIVRE_ECRIT_PAR + ", "
            + DATABASE_CREATE_FK_ID_LIVRE_LIVRE_ECRIT_PAR + ", "
            + "PRIMARY KEY (" + COLUMN_FK_ID_LIVRE_LIVRE_ECRIT_PAR + "," + COLUMN_FK_ID_AUTEUR_LIVRE_ECRIT_PAR + ")" // Clef primaire composite
            + ");";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_LIVRE);
        db.execSQL(DATABASE_CREATE_AUTEUR);
        db.execSQL(DATABASE_CREATE_LIVRE_ECXRIT_PAR);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVRE_ECRIT_PAR);
    }

    /*
        METHODES POUR LES LIVRES
     */

    public void addLivre(Livre livre){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ISBN, livre.getISBN());
        values.put(KEY_TITIRE, livre.getTitre());
        db.insert(TABLE_LIVRE, null , values);
        db.close();
        db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID_LIVRE + " FROM " + TABLE_LIVRE;
        Cursor cur = db.rawQuery(query, null);
        if(cur != null){
            cur.moveToLast();
            try {
                livre.setIdLivre(cur.getInt(0));
            }
            catch (Exception e) {
                livre.setIdLivre(1);
            }
            cur.close();
        }
        db.close();
    }

    public ArrayList<Livre> getAllLivre(){
        ArrayList<Livre> livres = new ArrayList<Livre>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_LIVRE, COLUMNS_LIVRE, null, null, null, null, null);
        if(cur.moveToFirst()){
            do{
                Livre livre = new Livre();
                livre.setIdLivre(cur.getInt(0));
                livre.setISBN(cur.getString(1));
                livre.setTitre(cur.getString(2));
                livres.add(livre);
            }while(cur.moveToNext());
        }
        cur.close();
        db.close();
        return livres;

    }

    public void deleteLivre(Livre livre){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LIVRE,
                KEY_ID_LIVRE + "=" + String.valueOf(livre.getIdLivre()) + " AND "
                        + KEY_ISBN + "=\"" + livre.getISBN() + "\"" + "AND "
                        + KEY_TITIRE + "=\"" + livre.getTitre() + "\"",
                null
        );
        db.close();
    }

    /*
        METHODES POUR LES AUTEURS
     */

    public void addAuteur(Auteur auteur){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM_AUTEUR, auteur.getNom());
        values.put(KEY_PRENOM_AUTEUR, auteur.getPrenom());
        db.insert(TABLE_AUTEUR, null, values);
        db.close();
        db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_AUTEUR, COLUMNS_AUTEUR, null, null, null, null, null);
        if(cur != null){
            cur.moveToLast();
            try {
                auteur.setIdAuteur(cur.getInt(0));
            }
            catch (Exception e) {
                auteur.setIdAuteur(1);
            }
            cur.close();
        }
        db.close();
    }

    public ArrayList<Auteur> getAllAuteur(){
        ArrayList<Auteur> auteurs = new ArrayList<Auteur>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_AUTEUR, COLUMNS_AUTEUR, null, null, null, null, null);
        if(cur.moveToFirst()){
            do{
                Auteur auteur = new Auteur();
                auteur.setIdAuteur(cur.getInt(0));
                auteur.setNom(cur.getString(1));
                auteur.setPrenom(cur.getString(2));
                auteurs.add(auteur);
            }while(cur.moveToNext());
        }
        cur.close();
        db.close();
        return auteurs;

    }

    public void deleteAuteur(Auteur auteur){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_AUTEUR,
                KEY_ID_AUTEUR + "=" + String.valueOf(auteur.getIdAuteur()) + " AND "
                        + KEY_NOM_AUTEUR + "=\"" + auteur.getNom() + "\"" + "AND "
                        + KEY_PRENOM_AUTEUR + "=\"" + auteur.getPrenom() + "\"",
                null
        );
        db.close();
    }

    /*
        METHODES POUR LES LIVRES ECRITS PAR
     */

    public void addAuteurForLivre(Auteur auteur, Livre livre){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FK_ID_LIVRE_LIVRE_ECRIT_PAR, livre.getIdLivre());
        values.put(KEY_FK_ID_AUTEUR_LIVRE_ECRIT_PAR, auteur.getIdAuteur());
        db.insert(TABLE_LIVRE_ECRIT_PAR, null, values);
        db.close();
    }

    public ArrayList<Auteur> getAuteursByLivre(Livre livre){
        ArrayList<Auteur> auteurs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> ids = new ArrayList<>();
        Cursor cur = db.query(TABLE_LIVRE_ECRIT_PAR, COLUMNS_LIVRE_ECRIT_PAR, KEY_FK_ID_LIVRE_LIVRE_ECRIT_PAR + "=?" , new String[]{String.valueOf(livre.getIdLivre())}, null, null, null);
        if(cur.moveToFirst()){
            do{
                ids.add(cur.getInt(1));
            }while(cur.moveToNext());
        }
        cur.close();
        for(Integer idAuteur : ids){
            cur = db.query(TABLE_AUTEUR, COLUMNS_AUTEUR, KEY_ID_AUTEUR + "=?", new String[]{String.valueOf(idAuteur)}, null, null, null);
            if(cur.moveToFirst()){
                do{
                    Auteur auteur = new Auteur();
                    auteur.setIdAuteur(cur.getInt(0));
                    auteur.setNom(cur.getString(1));
                    auteur.setPrenom(cur.getString(2));
                    auteurs.add(auteur);
                }while(cur.moveToNext());
            }
            cur.close();
        }
        db.close();
        return auteurs;
    }

    public ArrayList<Livre> getLivresByAuteur(Auteur auteur){
        ArrayList<Livre> livres = new ArrayList<Livre>();
        ArrayList<Integer> ids = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_LIVRE_ECRIT_PAR, COLUMNS_LIVRE_ECRIT_PAR, KEY_FK_ID_AUTEUR_LIVRE_ECRIT_PAR + "=?" , new String[]{String.valueOf(auteur.getIdAuteur())}, null, null, null);
        if(cur.moveToFirst()){
            do{
                ids.add(cur.getInt(0));
            }while(cur.moveToNext());
        }
        cur.close();
        for(Integer idLivre : ids) {
            cur = db.query(TABLE_LIVRE, COLUMNS_LIVRE, KEY_ID_LIVRE+ "=?", new String[]{String.valueOf(idLivre)}, null, null, null);
            if (cur.moveToFirst()) {
                do {
                    Livre livre = new Livre();
                    livre.setIdLivre(cur.getInt(0));
                    livre.setISBN(cur.getString(1));
                    livre.setTitre(cur.getString(2));
                    livres.add(livre);
                } while (cur.moveToNext());
            }
            cur.close();
        }
        db.close();
        return livres;
    }


}
