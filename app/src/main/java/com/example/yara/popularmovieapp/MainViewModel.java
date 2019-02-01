package com.example.yara.popularmovieapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.yara.popularmovieapp.Database.AppDatabase;
import com.example.yara.popularmovieapp.Database.MovieDao;
import com.example.yara.popularmovieapp.Database.MovieEntry;
import com.example.yara.popularmovieapp.Database.MovieRepository;
import com.example.yara.popularmovieapp.MovieDetails.MovieDetailsActivity;

import org.w3c.dom.Entity;

import java.util.List;

/**
 * Created by Yara on 03-Jan-19.
 */

public class MainViewModel extends AndroidViewModel {
    LiveData<List<MovieEntry>> list;
    LiveData<List<MovieEntry>> favoriteList;
    MovieEntry movieEntry;
    private MovieRepository mRepository;
    String TAG = this.getClass().getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        list = mRepository.getAllMovies();


    }


//    public void deleteWord(MovieEntry movieEntry) {
//        mRepository.deleteWord(movieEntry);
//    }
   public LiveData<List<MovieEntry>> getAllMovies() {
        return list;
    }


    public void insert(MovieEntry movieEntry) {
        mRepository.insert(movieEntry);
    }

}
