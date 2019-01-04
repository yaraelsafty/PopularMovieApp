package com.example.yara.popularmovieapp.Utils.network;

import com.example.yara.popularmovieapp.MovieDetails.MovieDetailsModel;
import com.example.yara.popularmovieapp.MovieDetails.Reviews.ReviewsResponse;
import com.example.yara.popularmovieapp.MovieDetails.Trailers.TrailersResponse;
import com.example.yara.popularmovieapp.MoviesResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Yara on 01-Nov-18.
 */

public interface ApiServices {
    @GET("discover/movie")
    Call<MoviesResponseModel> getMovies(@Query("api_key") String api_key );

    @GET("movie/popular")
    Call<MoviesResponseModel> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<MoviesResponseModel> getTopRatedMovies(@Query("api_key") String api_key);

    @GET
    Call<MovieDetailsModel>getMovieDetails(@Url String url);

    @GET
    Call<TrailersResponse>getMovieTrailers(@Url String url);

    @GET
    Call<ReviewsResponse>getMovieReviews(@Url String url);

}
