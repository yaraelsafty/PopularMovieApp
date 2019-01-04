package com.example.yara.popularmovieapp.MovieDetails.Trailers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yara.popularmovieapp.MovieDetails.MovieDetailsActivity;
import com.example.yara.popularmovieapp.MovieDetails.Reviews.ReviewsAdapter;
import com.example.yara.popularmovieapp.MovieDetails.Reviews.ReviewsResult;
import com.example.yara.popularmovieapp.R;

import java.util.List;

/**
 * Created by Yara on 01-Nov-18.
 */

public class TrailsAdapter extends RecyclerView.Adapter<TrailsAdapter.MyViewHolder> {
    String TAG= this.getClass().getSimpleName();
    List<TrailsResults>list;
    Context context;

    public TrailsAdapter(List<TrailsResults> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public TrailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new TrailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailsAdapter.MyViewHolder holder, int position) {
        final TrailsResults trailsResults = list.get(position);
        holder.trailer.setText(trailsResults.getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailsResults.getKey())));
         }
     });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView trailer;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            trailer=itemView.findViewById(R.id.tv_trailer);
            imageView=itemView.findViewById(R.id.ic_play);
        }
    }
}
