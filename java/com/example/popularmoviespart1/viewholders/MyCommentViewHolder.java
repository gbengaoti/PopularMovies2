package com.example.popularmoviespart1.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.R;

public class MyCommentViewHolder extends RecyclerView.ViewHolder {
    private final TextView mAuthorTextView;
    private final TextView mContentTextView;

    public MyCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        mAuthorTextView = itemView.findViewById(R.id.author_text_view);
        mContentTextView = itemView.findViewById(R.id.comment_text_view);
    }

    public TextView getmAuthorTextView(){
        return this.mAuthorTextView;
    }

    public TextView getmContentTextView(){
        return this.mContentTextView;
    }
}
