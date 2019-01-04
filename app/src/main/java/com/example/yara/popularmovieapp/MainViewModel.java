package com.example.yara.popularmovieapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.yara.popularmovieapp.Database.AppDatabase;
import com.example.yara.popularmovieapp.Database.MovieEntry;
import com.example.yara.popularmovieapp.MovieDetails.MovieDetailsActivity;

import java.util.List;

/**
 * Created by Yara on 03-Jan-19.
 */

public class MainViewModel extends AndroidViewModel {
    LiveData<List<MovieEntry>> list;
    LiveData<List<MovieEntry>> favoriteList;
    String TAG = this.getClass().getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase=AppDatabase.getsInstance(this.getApplication());
        Log.d(TAG,"Actively retrieve movies from database");
        list=appDatabase.movieDao().loadAllMovies();
        favoriteList=appDatabase.movieDao().IsFavorite(MovieDetailsActivity.id);
    }

    public void setFavoriteList(LiveData<List<MovieEntry>> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public LiveData<List<MovieEntry>> getFavoriteList() {
        return favoriteList;
    }

    public LiveData<List<MovieEntry>> getList() {
        return list;
    }
}
