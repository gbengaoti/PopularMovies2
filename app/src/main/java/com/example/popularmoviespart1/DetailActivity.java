package com.example.popularmoviespart1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.database.AppDatabase;
import com.example.popularmoviespart1.database.FavoriteEntry;
import com.example.popularmoviespart1.databinding.ActivityDetailBinding;
import com.example.popularmoviespart1.model.Comment;
import com.example.popularmoviespart1.model.CommentResponsePage;
import com.example.popularmoviespart1.model.Movie;
import com.example.popularmoviespart1.model.Trailer;
import com.example.popularmoviespart1.model.TrailerResponsePage;
import com.example.popularmoviespart1.network.APIMovieClient;
import com.example.popularmoviespart1.network.MovieAPIInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;
import static com.example.popularmoviespart1.network.MovieAPIInterface.API_KEY;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private RecyclerView mTrailerRecyclerReview;
    private RecyclerView mCommentsRecyclerReview;

    private static final String TAG = DetailActivity.class.getSimpleName();
    private final String ERROR_MESSAGE_COMMENTS = "Failure to get comments";
    private final String ERROR_MESSAGE_TRAILERS = "Failure to get trailers";

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private static final String RATING_TOTAL = "/10";

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    private TextView mMovieTitleView;
    private TextView mUserRatingView;
    private TextView mReleaseDateView;
    private TextView mSynopsisView;
    private TrailerAdapter mTrailerAdapter;
    private static CommentAdapter mCommentAdapter;
    private TextView mTrailerErrorTextView;
    private TextView mCommentErrorTextView;
    private Button mFavoriteButton;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mMovieTitleView = binding.detailTopHalf.movieTitle;
        ImageView mImageView = binding.detailTopHalf.imageBox;
        mUserRatingView = binding.detailTopHalf.userRating;
        mReleaseDateView = binding.detailTopHalf.releaseDate;
        mSynopsisView = binding.detailTopHalf.synopsis;

        mFavoriteButton = binding.detailTopHalf.favoriteButton;

        mTrailerErrorTextView = binding.trailerDetails.errorTrailerTextView;
        mCommentErrorTextView = binding.commentDetails.errorCommentsTextView;

        mTrailerRecyclerReview = binding.trailerDetails.trailerRecyclerView;
        mCommentsRecyclerReview = binding.commentDetails.commentsRecyclerView;

        mTrailerRecyclerReview.setHasFixedSize(true);
        mCommentsRecyclerReview.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutTrailerManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        mTrailerRecyclerReview.setLayoutManager(layoutTrailerManager);

        RecyclerView.LayoutManager layoutCommentManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        mCommentsRecyclerReview.setLayoutManager(layoutCommentManager);

        // adapter
        mTrailerAdapter = new TrailerAdapter(this);
        // setadapter
        mTrailerRecyclerReview.setAdapter(mTrailerAdapter);

        DividerItemDecoration trailerDecoration =
                new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mTrailerRecyclerReview.addItemDecoration(trailerDecoration);

        // comment adapter
        mCommentAdapter = new CommentAdapter();
        mCommentsRecyclerReview.setAdapter(mCommentAdapter);

        DividerItemDecoration commentDecoration =
                new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mCommentsRecyclerReview.addItemDecoration(commentDecoration);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
           closeOnError();
        }else{
            final Movie currentMovie =
                    intent.getParcelableExtra(Intent.EXTRA_TEXT);
            assert currentMovie != null;
            populateDetailUI(currentMovie);
            final int id = currentMovie.getId();
            final String title = currentMovie.getTitle();
            final String posterPath = currentMovie.getPosterPath();
            final String synopsis = currentMovie.getOverview();
            final float voteAverage = currentMovie.getVoteAverage();
            final String releaseDate = currentMovie.getReleaseDate();

            FavoriteEntry newFavoriteEntry  = new FavoriteEntry(id, title, posterPath, synopsis, voteAverage, releaseDate);

            setupViewModel(newFavoriteEntry);

            getTrailers(id);

            getComments(id);

            mFavoriteButton.setOnClickListener(v -> {

                final FavoriteEntry favoriteEntry = new FavoriteEntry(id, title, posterPath, synopsis, voteAverage, releaseDate);
                //Log.v(TAG, "OnClick - Favorite");
                AppExecutors.getInstance().diskIO().execute(() -> {
                    if (mFavoriteButton.getText().equals(getResources().getString(R.string.mark_as_favorite))) {
                        // set movie as favorite
                        mDb.favoriteDao().insertFavorite(favoriteEntry);
                        //change button text
                        runOnUiThread(() -> mFavoriteButton.setText(getResources().getString(R.string.unmark_as_favorite)));

                    }else if (mFavoriteButton.getText().equals(getResources().getString(R.string.unmark_as_favorite))){
                        // delete from favorites
                        mDb.favoriteDao().deleteFavorite(favoriteEntry);
                        // change button text
                        runOnUiThread(() -> mFavoriteButton.setText(getResources().getString(R.string.mark_as_favorite)));

                    }
                });

            });

            String getPath = currentMovie.getPosterPath();
            String fullPath = IMAGE_BASE_URL + getPath;
            Picasso.get()
                    .load(fullPath)
                    .placeholder(R.mipmap.placeholder_image)
                    .error(R.mipmap.placeholder_error_image)
                    .resize(WIDTH, HEIGHT)
                    .into(mImageView);

        }
    }



    private void populateDetailUI(Movie movieDetail) {
        mMovieTitleView.setText(movieDetail.getTitle());
        mSynopsisView.setText(movieDetail.getOverview());
        mReleaseDateView.setText(movieDetail.getReleaseDate().substring(0,4));
        mUserRatingView.setText(String.format("%s%s",
                movieDetail.getVoteAverage(),
                RATING_TOTAL));
    }

    private void setupViewModel(final FavoriteEntry newFavoriteEntry) {
        FavoriteListViewModel viewModel =
                ViewModelProviders.of(this).get(FavoriteListViewModel.class);
        viewModel.getFavorites().observe(this, favoriteEntries -> {
            //Log.v(TAG, "OnChanged called");
            if (favoriteEntries != null) {
                if (favoriteEntries.contains(newFavoriteEntry)){
                    runOnUiThread(() -> mFavoriteButton.setText(getResources()
                            .getString(R.string.unmark_as_favorite)));

                }
            }

        });
    }

    @Override
    public void onClick(Trailer currentTrailer) {
        // perform intent service with link thing
        //Log.v(TAG, "inoncclick");
        Uri videoLink = Uri.parse(getVideoLink(currentTrailer));
        Intent intent = new Intent(Intent.ACTION_VIEW, videoLink);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public String getVideoLink(Trailer currentTrailer){
        String baseURL = "";
        String site = currentTrailer.getSite().toLowerCase();
        if (site.equals("youtube")){
            baseURL = "https://www.youtube.com/watch?v=";
        }else if (site.equals("vimeo")){
            baseURL = "https://www.vimeo.com/";
        }

        return baseURL + currentTrailer.getKey();

    }


    private void getTrailers(Integer id) {
        // make retrofit instance
        Retrofit retrofit = APIMovieClient.getRetrofitInstance();
        // get service for trailers
        MovieAPIInterface apiService = retrofit.create(MovieAPIInterface.class);

        Call<TrailerResponsePage> call = apiService.getTrailers(id, API_KEY);

        call.enqueue(new Callback<TrailerResponsePage>() {
            @Override
            public void onResponse(Call<TrailerResponsePage> call, Response<TrailerResponsePage> response) {
                //Log.v(TAG,"Trailer response " + response.code());
                if (response.isSuccessful()){
                    assert response.body() != null;
                    final List<Trailer> trailers = response.body().getResults();
                    if (trailers != null) {
                        mTrailerAdapter.setTrailerData(trailers.toArray(new Trailer[0]));
                    }
                }
            }

            @Override
            public void onFailure(Call<TrailerResponsePage> call, Throwable t) {
                //System.out.println(t.getMessage());
                //Log.v(TAG, ERROR_MESSAGE_TRAILERS);
                showTrailerError();
            }
        });


    }


    public void getComments(Integer id){
        // make retrofit instance
        Retrofit retrofit = APIMovieClient.getRetrofitInstance();
        // get service for comments
        MovieAPIInterface apiService = retrofit.create(MovieAPIInterface.class);

        Call<CommentResponsePage> call = apiService.getComments(id, API_KEY);
        call.enqueue(new Callback<CommentResponsePage>() {
            @Override
            public void onResponse(Call<CommentResponsePage> call, Response<CommentResponsePage> response) {
                //Log.v(TAG, "Comment response " + response.code());
                if (response.isSuccessful()){
                    assert response.body() != null;
                    final List<Comment> comments = response.body().getResults();
                    if (comments != null) {
                        mCommentAdapter.setCommentData(comments.toArray(new Comment[0]));
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentResponsePage> call, Throwable t) {
                //Log.v(TAG, ERROR_MESSAGE_COMMENTS);
                showCommentError();
            }
        });
    }

    private void showTrailerError(){
        mTrailerErrorTextView.setVisibility(View.VISIBLE);
        mTrailerRecyclerReview.setVisibility(View.INVISIBLE);
    }

    private void showCommentError(){
        mCommentErrorTextView.setVisibility(View.VISIBLE);
        mCommentsRecyclerReview.setVisibility(View.INVISIBLE);
    }

    private void closeOnError(){
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}



















