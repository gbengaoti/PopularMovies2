package com.example.popularmoviespart1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.model.Comment;
import com.example.popularmoviespart1.viewholders.MyCommentViewHolder;

public class CommentAdapter extends RecyclerView.Adapter<MyCommentViewHolder> {

    private Comment[] mCommentList;

    void setCommentData(Comment[] comments){
        this.mCommentList = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.comment_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MyCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentViewHolder holder, int position) {
        TextView authorTextView = holder.getmAuthorTextView();
        TextView contentTextView = holder.getmContentTextView();

        authorTextView.setText(mCommentList[position].getAuthor());
        contentTextView.setText(mCommentList[position].getComment());
    }

    @Override
    public int getItemCount() {
        if (null == mCommentList) return 0;
        return mCommentList.length;
    }


}
