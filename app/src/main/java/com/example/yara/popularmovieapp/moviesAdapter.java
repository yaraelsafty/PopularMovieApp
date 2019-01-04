package com.example.yara.popularmovieapp;

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
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Yara on 28-Oct-18.
 */

public class moviesAdapter extends ArrayAdapter<MoviesResult> {
    String TAG= this.getClass().getSimpleName();
    public moviesAdapter(@NonNull Context context, List<MoviesResult>moviesList) {
        super(context,0,moviesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MoviesResult movies = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/original"+movies.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(iconView);


        TextView titleView = (TextView) convertView.findViewById(R.id.movie_title);
        titleView.setText(movies.getTitle());

        TextView voteView=convertView.findViewById(R.id.tv_vote);
        voteView.setText(movies.getVoteAverage().toString());

        TextView dateView=convertView.findViewById(R.id.tv_movie_release_date);
        String currentString = movies.getReleaseDate();
        Log.d(TAG,"date  "+movies.getReleaseDate());
        String[] separated = currentString.split("-");
        dateView.setText(separated[0]);


        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("id",movies.getId());
                Log.i(TAG,"--id--"+movies.getId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
