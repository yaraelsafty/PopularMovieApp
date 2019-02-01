package com.example.yara.popularmovieapp.MovieDetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yara.popularmovieapp.Database.AppDatabase;
import com.example.yara.popularmovieapp.Database.AppExecutors;
import com.example.yara.popularmovieapp.Database.MovieDBAapter;
import com.example.yara.popularmovieapp.Database.MovieEntry;
import com.example.yara.popularmovieapp.DatabaseHandler;
import com.example.yara.popularmovieapp.MainActivity;
import com.example.yara.popularmovieapp.MainViewModel;
import com.example.yara.popularmovieapp.MovieDetails.Reviews.ReviewsAdapter;
import com.example.yara.popularmovieapp.MovieDetails.Reviews.ReviewsResponse;
import com.example.yara.popularmovieapp.MovieDetails.Reviews.ReviewsResult;
import com.example.yara.popularmovieapp.MovieDetails.Trailers.TrailersResponse;
import com.example.yara.popularmovieapp.MovieDetails.Trailers.TrailsAdapter;
import com.example.yara.popularmovieapp.MovieDetails.Trailers.TrailsResults;
import com.example.yara.popularmovieapp.MoviesResult;
import com.example.yara.popularmovieapp.R;
import com.example.yara.popularmovieapp.Utils.network.ApiClient;
import com.example.yara.popularmovieapp.Utils.network.ApiServices;
import com.example.yara.popularmovieapp.moviesAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class MovieDetailsActivity extends AppCompatActivity  {
    String TAG=MovieDetailsActivity.this.getClass().getSimpleName();

    ImageView poster;
    TextView tv_movie_date ,tv_movie_time,tv_movie_review,tv_movie_overView;
    CheckBox ch_favorite;
    RecyclerView rv_reviews,rv_trailer;
     public static int id;



    String Title;
    String PosterPath;
    double Rate;
    String Date;

    List<TrailsResults>trails;
    List<ReviewsResult>reviews;
    ApiServices apiServices;
    private AppDatabase mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        poster=findViewById(R.id.iv_movie);
        tv_movie_date=findViewById(R.id.tv_movie_date);
        tv_movie_time=findViewById(R.id.tv_movie_time);
        tv_movie_review=findViewById(R.id.tv_movie_review);
        tv_movie_overView=findViewById(R.id.tv_movie_overView);
        rv_trailer=findViewById(R.id.rv_trailer);
        rv_reviews=findViewById(R.id.rv_reviews);
        ch_favorite=findViewById(R.id.ch_favorite);

        apiServices = ApiClient.getClient().create(ApiServices.class);
        mDB=AppDatabase.getsInstance(getApplicationContext());


        id=getIntent().getIntExtra("id",0);

        IsFavorite();
        //ch_favorite.setChecked(getFromSP("favorite"));

        ch_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_favorite.isChecked())
                {
                    addToFavorite();
                }
                else
                    Log.d(TAG,"deleted");
                deleteFromFavorite();
            }
        });


        fetchData();
        getTrails();
        getReviews();


    }

    private void IsFavorite() {
        ch_favorite.setChecked(false);


        MainViewModel viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getAllMovies().observe(this, new Observer<List<MovieEntry>>() {

            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d(TAG," from ViewModel  favorite list size  "+movieEntries.size() );
                for (int i=0; i<movieEntries.size(); i++){
                    if (movieEntries.get(i).getMovie_id()==id)
                    {
                        Log.d(TAG,""+movieEntries.get(i).getMovie_id()+"***"+id);
                        ch_favorite.setChecked(true);
                    }
                }
            }
        });
    }





    private void fetchData() {
        Call<MovieDetailsModel>call= apiServices.getMovieDetails("https://api.themoviedb.org/3/movie/"+id+"?api_key="+ApiClient.api_key);
        call.enqueue(new Callback<MovieDetailsModel>() {
            @Override
            public void onResponse(Call<MovieDetailsModel> call, Response<MovieDetailsModel> response) {
                Title=response.body().getTitle();
                PosterPath=response.body().getPosterPath();
                Date=response.body().getReleaseDate();
                Rate= response.body().getVoteAverage();

                Picasso.with(MovieDetailsActivity.this).load("https://image.tmdb.org/t/p/original"+response.body().getPosterPath()).placeholder(R.mipmap.ic_launcher).into(poster);
                tv_movie_date.setText(response.body().getReleaseDate());
                tv_movie_time.setText(response.body().getRuntime()+" min");
                tv_movie_review.setText(response.body().getVoteAverage()+"/10");
                tv_movie_overView.setText(response.body().getOverview());


            }

            @Override
            public void onFailure(Call<MovieDetailsModel> call, Throwable t) {
                Log.e(TAG,""+t);

            }
        });
    }

    private void getTrails() {
        Call<TrailersResponse>call= apiServices.getMovieTrailers("https://api.themoviedb.org/3/movie/"+id+"/videos?api_key=ba009f6f38f10de53d888a1a7f5dce6c&language=en-US");
        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                trails=response.body().getResults();
                TrailsAdapter trailsAdapter=new TrailsAdapter(trails,MovieDetailsActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                rv_trailer.setLayoutManager(mLayoutManager);
                rv_trailer.setItemAnimator(new DefaultItemAnimator());
                rv_trailer.setAdapter(trailsAdapter);
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                Log.e(TAG,""+t);

            }
        });

    }
    private void getReviews() {
        Call<ReviewsResponse>call= apiServices.getMovieReviews("https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key=ba009f6f38f10de53d888a1a7f5dce6c&language=en-US");
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                reviews=response.body().getResults();
                ReviewsAdapter reviewsAdapter=new ReviewsAdapter(reviews,MovieDetailsActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_reviews.setLayoutManager(mLayoutManager);
                rv_reviews.setItemAnimator(new DefaultItemAnimator());
                rv_reviews.setAdapter(reviewsAdapter);
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG,""+t);

            }
        });
    }
    private void addToFavorite() {
        Log.d(TAG,"add to favorite"+id+"--"+Title+"--"+Date+"--"+Rate);
       final MovieEntry movieEntry=new MovieEntry(id,Title,PosterPath,Date,Rate);
       MainViewModel viewModel=ViewModelProviders.of(this).get(MainViewModel.class);
       viewModel.insert(movieEntry);

    }
    private void deleteFromFavorite() {

       final MovieEntry movieEntry=new MovieEntry(id,Title,PosterPath,Date,Rate);
        Log.d(TAG,"favorite"+movieEntry.getMovie_id()+"--"+movieEntry.getTitle());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.movieDao().deleteMovie(id);

            }
        });

    }
}

