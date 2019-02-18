package com.example.yara.popularmovieapp.MovieDetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import javax.crypto.spec.IvParameterSpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class MovieDetailsActivity extends AppCompatActivity  {
        private String TAG=MovieDetailsActivity.this.getClass().getSimpleName();

        private ImageView poster;
        private TextView tv_title ,tv_movie_date ,tv_movie_time,tv_movie_review,tv_movie_overView;
        private CheckBox ch_favorite;
        private RecyclerView rv_reviews,rv_trailer;
        private   ImageView expandedImage;
        private View v_summary,v_trailer,v_reviews;
        private TextView tv_summary,tv_reviews,tv_trailer;

        public static int id;

        private   AppBarLayout appBarLayout;
        private TextView titleTv;
        private CollapsingToolbarLayout collapsingToolbarLayout;
        private String Title;
        private String PosterPath;
        private double Rate;
        private String Date;

        private List<TrailsResults>trails;
        private List<ReviewsResult>reviews;
        private ApiServices apiServices;
        private AppDatabase mDB;
        private Menu menu;
        CollapsingToolbarLayout ctl;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
            appBarLayout = findViewById(R.id.appbar);
            titleTv = findViewById(R.id.movie_title);
            expandedImage=findViewById(R.id.expandedImage);
//            Toolbar myToolbar =  findViewById(R.id.z_toolbar);
//            setSupportActionBar(myToolbar);
//             expandedImage=findViewById(R.id.expandedImage);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
 //           ctl = findViewById(R.id.collapsing_toolbar);
//            ctl.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
//            ctl.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
        poster=findViewById(R.id.iv_movie);
        tv_title=findViewById(R.id.tv_movie_title);
        tv_movie_date=findViewById(R.id.tv_movie_date);
        tv_movie_time=findViewById(R.id.tv_movie_time);
        tv_movie_review=findViewById(R.id.tv_movie_review);
        tv_movie_overView=findViewById(R.id.tv_movie_overView);
        rv_trailer=findViewById(R.id.rv_trailer);
        rv_reviews=findViewById(R.id.rv_reviews);
        ch_favorite=findViewById(R.id.ch_favorite);
        v_reviews=findViewById(R.id.v_reviews);
        v_trailer=findViewById(R.id.v_trailers);
        v_summary=findViewById(R.id.v_summary);
        tv_summary=findViewById(R.id.tv_summary);
        tv_reviews=findViewById(R.id.tv_reviews);
        tv_trailer=findViewById(R.id.tv_trailers);


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

            expandedImage.setVisibility(View.VISIBLE);
            titleTv.setVisibility(View.GONE);
            poster.setVisibility(View.GONE);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                    if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                        //  Collapsed
                        expandedImage.setVisibility(View.GONE);
                        titleTv.setVisibility(View.VISIBLE);
                        poster.setVisibility(View.VISIBLE);

                    } else {
                        //Expanded
                        expandedImage.setVisibility(View.VISIBLE);
                        titleTv.setVisibility(View.GONE);
                        poster.setVisibility(View.GONE);

                    }
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
                tv_summary.setVisibility(View.VISIBLE);
                 v_summary.setVisibility(View.VISIBLE);

                Title=response.body().getTitle();
                titleTv.setText(Title);

                tv_title.setText(Title);
                PosterPath=response.body().getPosterPath();
                Date=response.body().getReleaseDate();
                Rate= response.body().getVoteAverage();

                Picasso.with(MovieDetailsActivity.this).load("https://image.tmdb.org/t/p/original"+response.body().getPosterPath()).placeholder(R.drawable.noimage).into(poster);
                Picasso.with(MovieDetailsActivity.this).load("https://image.tmdb.org/t/p/original"+response.body().getPosterPath()).placeholder(R.drawable.noimage).into(expandedImage);

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

                    tv_trailer.setVisibility(View.VISIBLE);
                    v_trailer.setVisibility(View.VISIBLE);
                    TrailsAdapter trailsAdapter = new TrailsAdapter(trails, MovieDetailsActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
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
                if (reviews.size()>0) {
                    tv_reviews.setVisibility(View.VISIBLE);
                    v_reviews.setVisibility(View.VISIBLE);
                    ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews, MovieDetailsActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rv_reviews.setLayoutManager(mLayoutManager);
                    rv_reviews.setItemAnimator(new DefaultItemAnimator());
                    rv_reviews.setAdapter(reviewsAdapter);
                }
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

