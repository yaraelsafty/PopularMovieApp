package com.example.yara.popularmovieapp.Database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yara.popularmovieapp.MovieDetails.MovieDetailsActivity;
import com.example.yara.popularmovieapp.MoviesResult;
import com.example.yara.popularmovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yara on 20-Dec-18.
 */

public class MovieDBAapter extends ArrayAdapter<MovieEntry> {
    String TAG= this.getClass().getSimpleName();
    private List<MovieEntry> movieEntries;

    public MovieDBAapter(@NonNull Context context, List<MovieEntry>moviesList) {
        super(context,0,moviesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MovieEntry movies = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/original"+movies.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(iconView);


        TextView titleView = (TextView) convertView.findViewById(R.id.movie_title);
        titleView.setText(movies.getTitle());

        TextView voteView=convertView.findViewById(R.id.tv_vote);
        String vote= String.valueOf(movies.getVoteAverage());
         voteView.setText( vote);


        TextView dateView=convertView.findViewById(R.id.tv_movie_release_date);
        String currentString = movies.getReleaseDate();
        Log.d(TAG,"date  "+movies.getReleaseDate()+"--rate--"+movies.getVoteAverage());
       String[] separated = currentString.split("-");
        dateView.setText(separated[0]);


        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("id",movies.getMovie_id());
                Log.i(TAG,"--id--"+movies.getMovie_id());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    public List<MovieEntry> getMovieEntries() {
        return movieEntries;
    }

    public void setMovieEntries(List<MovieEntry> movieEntries) {
        this.movieEntries = movieEntries;
        notifyDataSetChanged();
    }
}

