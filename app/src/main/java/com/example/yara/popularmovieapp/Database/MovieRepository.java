package com.example.yara.popularmovieapp.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Yara on 10-Jan-19.
 */

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<MovieEntry>> mAllMovies;


    public MovieRepository(Application application) {
        AppDatabase db = AppDatabase.getsInstance(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.loadAllMovies();
    }

  public LiveData<List<MovieEntry>> getAllMovies() {
        return mAllMovies;
    }

    public void insert(MovieEntry word) {
        new insertAsyncTask(mMovieDao).execute(word);
    }



    /**
     * Insert a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<MovieEntry, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieEntry... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }



}

