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


    // Need to run off main thread
    public void deleteWord(MovieEntry word) {
        new deleteWordAsyncTask(mMovieDao).execute(word);
    }

    // Static inner classes below here to run database interactions
    // in the background.

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



    /**
     *  Delete a single word from the database.
     */
    private static class deleteWordAsyncTask extends AsyncTask<MovieEntry, Void, Void> {
        private MovieDao mAsyncTaskDao;

        deleteWordAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieEntry... params) {
            mAsyncTaskDao.deleteMovie(params[0]);
            return null;
        }
    }
}

