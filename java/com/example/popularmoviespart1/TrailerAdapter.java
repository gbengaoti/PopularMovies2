package com.example.popularmoviespart1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.model.Trailer;
import com.example.popularmoviespart1.viewholders.MyTrailerViewHolder;

public class TrailerAdapter extends RecyclerView.Adapter<MyTrailerViewHolder> {

    private Trailer[] mTrailerList;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler{
        void onClick(Trailer currentTrailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler mClickHandler){
        this.mClickHandler = mClickHandler;
    }

    void setTrailerData(Trailer[] trailers){
        this.mTrailerList = trailers;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MyTrailerViewHolder(view, mTrailerList, mClickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrailerViewHolder holder, int position) {
        TextView textView = holder.getmTrailerTextView();
        textView.setText(String.format("Trailer %s", String.valueOf(position+1)));

    }

    @Override
    public int getItemCount() {
        if (null == mTrailerList) return 0;
        return mTrailerList.length;
    }
}
