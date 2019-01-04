package com.example.yara.popularmovieapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yara on 27-Nov-18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    String TAG =this.getClass().getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "MoviesDB";

    // Contacts table name
    private static final String TABLE_Movies = "movies";

    // Contacts Table Columns names
    private static final String KEY_Movie_ID = "Movie_id";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PATH = "image_path";
    private static final String KEY_DATE = "date";
    private static final String KEY_RATE = "rate";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create table

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_Movies + "("
                + KEY_Movie_ID + " INTEGER PRIMARY KEY," + KEY_ID + " TEXT,"+ KEY_TITLE + " TEXT,"+ KEY_PATH + " TEXT,"+ KEY_RATE + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Movies);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Movie
   public void addMovie(MoviesResult movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_PATH, movie.getPosterPath());
        values.put(KEY_DATE, movie.getReleaseDate());
        values.put(KEY_RATE, movie.getVoteAverage());
        db.insert(TABLE_Movies, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Movie
    public List<MoviesResult> getMovie(String name) {
        List<MoviesResult> contactList = new ArrayList<MoviesResult>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Movies, new String[] { KEY_Movie_ID,KEY_ID,
                        KEY_TITLE, KEY_PATH,KEY_DATE ,KEY_RATE}, KEY_TITLE + "=?",
                new String[] { name }, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null & cursor.getCount()>0){
            do {MoviesResult movies = new MoviesResult(
                    Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),
                    cursor.getDouble(5));
                contactList.add(movies);
            }while (cursor.moveToNext());
            return contactList;}
        else {
            MoviesResult movies=new MoviesResult();
            contactList.add(movies);
            return contactList;
        }

    }

    // Getting All Movies
    public List<MoviesResult> getAllMovies() {
        List<MoviesResult> moviesList = new ArrayList<MoviesResult>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Movies;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MoviesResult movies = new MoviesResult();
                movies.setMovie_id(Integer.parseInt(cursor.getString(0)));
                movies.setId(cursor.getInt(1));
                movies.setTitle(cursor.getString(2));
                movies.setPosterPath(cursor.getString(3));
                movies.setReleaseDate(cursor.getString(4));
                movies.setVoteAverage(cursor.getDouble(5));


                // Adding contact to list
                moviesList.add(movies);
            } while (cursor.moveToNext());
        }
        return moviesList;
    }

    // Updating single Movie
    public int updateMovies(MoviesResult movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_PATH, movie.getPosterPath());
        values.put(KEY_DATE, movie.getReleaseDate());
        values.put(KEY_RATE, movie.getVoteAverage());

        return db.update(TABLE_Movies, values, KEY_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
    }

    // Deleting single movie
    public void deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Movies, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

}
