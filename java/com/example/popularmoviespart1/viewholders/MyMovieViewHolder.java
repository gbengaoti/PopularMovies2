package com.example.popularmoviespart1.viewholders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.MovieAdapter;
import com.example.popularmoviespart1.R;
import com.example.popularmoviespart1.model.Movie;

public class MyMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final ImageView mMovieImageView;
    private final MovieAdapter.MovieAdapterOnClickHandler mClickHandler;
    private final Movie[] mMovieList;

    public MyMovieViewHolder(@NonNull View view, Movie[] mMovieList,
                             MovieAdapter.MovieAdapterOnClickHandler mClickHandler) {
        super(view);
        mMovieImageView = view.findViewById(R.id.movie_image_box);
        this.mClickHandler = mClickHandler;
        this.mMovieList = mMovieList;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        Movie myMovie = mMovieList[adapterPosition];
        mClickHandler.onClick(myMovie);
    }

    public ImageView getmMovieImageView(){
        return this.mMovieImageView;
    }
}
