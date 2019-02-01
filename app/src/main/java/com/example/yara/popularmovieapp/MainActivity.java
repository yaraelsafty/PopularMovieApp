package com.example.yara.popularmovieapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.yara.popularmovieapp.Database.AppDatabase;
import com.example.yara.popularmovieapp.Database.AppExecutors;
import com.example.yara.popularmovieapp.Database.MovieDBAapter;
import com.example.yara.popularmovieapp.Database.MovieEntry;
import com.example.yara.popularmovieapp.Utils.network.ApiClient;
import com.example.yara.popularmovieapp.Utils.network.ApiServices;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.this.getClass().getSimpleName();

    List<MoviesResult> moviesList = new ArrayList<>();
    ApiServices apiServices;
    GridView gridView;
    int index , position ;

   AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.movies_grid);


        apiServices = ApiClient.getClient().create(ApiServices.class);
        mDB=AppDatabase.getsInstance(getApplicationContext());
        getPopularMovies();
        moviesAdapter moviesAdapter = new moviesAdapter(MainActivity.this, moviesList);
        gridView.setAdapter(moviesAdapter);

       if (savedInstanceState != null) {
           position = savedInstanceState.getInt("position");
           Log.d(TAG,"position  "+position);
           gridView.smoothScrollToPosition(position);
       }

 }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                getPopularMovies();
                break;
            case R.id.topRated:
                getTopRatedMovies();
                break;
            case R.id.favorite:
                getFavorite();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        index = gridView.getFirstVisiblePosition();
        savedInstanceState.putInt("position", index);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
         position = savedInstanceState.getInt("position");
    }



    private void getTopRatedMovies() {
        Call<MoviesResponseModel> call = apiServices.getTopRatedMovies(ApiClient.api_key);
        call.enqueue(new Callback<MoviesResponseModel>() {
            @Override
            public void onResponse(Call<MoviesResponseModel> call, Response<MoviesResponseModel> response) {
                moviesList = response.body().getResults();
//                moviesAdapter moviesAdapter = new moviesAdapter(MainActivity.this, moviesList);
//                gridView.setAdapter(moviesAdapter);

            }

            @Override
            public void onFailure(Call<MoviesResponseModel> call, Throwable t) {
                Log.e(TAG, "Failure..." + t);

            }
        });
    }




    private void getPopularMovies() {
        Call<MoviesResponseModel> call = apiServices.getPopularMovies(ApiClient.api_key);
        call.enqueue(new Callback<MoviesResponseModel>() {
            @Override
            public void onResponse(Call<MoviesResponseModel> call, Response<MoviesResponseModel> response) {
                moviesList = response.body().getResults();
                moviesAdapter moviesAdapter = new moviesAdapter(MainActivity.this, moviesList);
                gridView.setAdapter(moviesAdapter);

            }

            @Override
            public void onFailure(Call<MoviesResponseModel> call, Throwable t) {
                Log.e(TAG, "Failure..." + t);

            }
        });
    }

    private void getMovies() {
        Call<MoviesResponseModel> call = apiServices.getMovies(ApiClient.api_key);
        call.enqueue(new Callback<MoviesResponseModel>() {
            @Override
            public void onResponse(Call<MoviesResponseModel> call, Response<MoviesResponseModel> response) {
                moviesList = response.body().getResults();
                moviesAdapter moviesAdapter = new moviesAdapter(MainActivity.this, moviesList);
                gridView.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponseModel> call, Throwable t) {
                Log.e(TAG, "Failure..." + t);
            }
        });
    }
    private void getFavorite() {

          MainViewModel viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
          viewModel.getAllMovies().observe(this, new Observer<List<MovieEntry>>() {

            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d(TAG," updating from ViewModel");
                MovieDBAapter moviesAdapter = new MovieDBAapter(MainActivity.this, movieEntries);
                gridView.setAdapter(moviesAdapter);
            }
        });


    }

}
