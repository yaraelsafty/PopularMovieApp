package com.example.yara.popularmovieapp.MovieDetails.Reviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yara.popularmovieapp.R;

import java.util.List;

/**
 * Created by Yara on 02-Nov-18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{
    String TAG= this.getClass().getSimpleName();

    List<ReviewsResult>list;
    Context context;

    public ReviewsAdapter(List<ReviewsResult> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_row, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviewsResult reviewsResult = list.get(position);
        holder.author.setText(reviewsResult.getAuthor());
        holder.content.setText(reviewsResult.getContent());
        Log.i(TAG,"---author---"+reviewsResult.getAuthor()+"--content--"+reviewsResult.getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author,content;

        public MyViewHolder(View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.tv_reviews_name);
            content=itemView.findViewById(R.id.tv_review_content);
        }
    }
}
