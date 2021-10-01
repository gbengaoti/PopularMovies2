package com.example.popularmoviespart1.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.R;
import com.example.popularmoviespart1.TrailerAdapter;
import com.example.popularmoviespart1.model.Trailer;

public class MyTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView mTrailerTextView;
    private final Trailer[] mTrailerList;
    private final TrailerAdapter.TrailerAdapterOnClickHandler mClickHandler;

    public MyTrailerViewHolder(@NonNull View itemView, Trailer[] mTrailerList, TrailerAdapter.TrailerAdapterOnClickHandler mClickHandler) {
        super(itemView);
        ImageView mPlayButton = itemView.findViewById(R.id.trailer_play_button);
        itemView.setOnClickListener(this);
        this.mTrailerList = mTrailerList;
        mTrailerTextView = itemView.findViewById(R.id.trailer_text_view);
        this.mClickHandler = mClickHandler;
    }

    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        Trailer myTrailer = mTrailerList[adapterPosition];
        mClickHandler.onClick(myTrailer);
    }

    public TextView getmTrailerTextView(){
        return this.mTrailerTextView;
    }

}